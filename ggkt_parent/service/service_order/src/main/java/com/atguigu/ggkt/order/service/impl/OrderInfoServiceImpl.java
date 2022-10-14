package com.atguigu.ggkt.order.service.impl;

import com.atguigu.ggkt.activity.client.CouponInfoFeignClient;
import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.order.OrderDetail;
import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.order.mapper.OrderInfoMapper;
import com.atguigu.ggkt.order.service.OrderDetailService;
import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.user.UserInfoFeignClient;
import com.atguigu.ggkt.utils.exception.GgktException;
import com.atguigu.ggkt.utils.utils.AuthContextHolder;
import com.atguigu.ggkt.utils.utils.OrderNoUtils;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.atguigu.ggkt.vo.order.OrderInfoVo;
import com.atguigu.ggkt.vod.course.CourseFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-28
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    //订单详情注入
    @Resource
    private OrderDetailService orderDetailService;
    //课程信息接口
    @Resource
    private CourseFeignClient courseFeignClient;
    //用户信息接口
    @Resource
    private UserInfoFeignClient userInfoFeignClient;
    //优惠券信息接口
    @Resource
    private CouponInfoFeignClient couponInfoFeignClient;

    //    根据订单id获取订单信息
    @Override
    public OrderInfoVo getOrderInfoById(Long id) {
        OrderInfo orderInfoId = baseMapper.selectById(id);
        OrderDetail byId = orderDetailService.getById(id);
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        BeanUtils.copyProperties(orderInfoId, orderInfoVo);
        orderInfoVo.setCourseId(byId.getCourseId());
        orderInfoVo.setCourseName(byId.getCourseName());
        return orderInfoVo;
    }
    //    更新订单支付状态:已支付成功
    @Override
    public void updateOrderStatus(String out_trade_no) {
        //根据订单号查询订单
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOutTradeNo,out_trade_no);
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        //设置订单状态
        //更新订单状态 1 已经支付
        orderInfo.setOrderStatus("1");
        //调用方法更新
        baseMapper.updateById(orderInfo);
    }

    //    新增点播课程订单
    @Override
    public Long subitOrder(OrderFormVo orderFormVo) {
        //1获取生成订单条件值
        Long courseId = orderFormVo.getCourseId();
        Long couponId = orderFormVo.getCouponId();
        Long userId = AuthContextHolder.getUserId();
        //2判断当前用户,针对当前课程是否已经生成订单
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getCourseId, courseId);
        queryWrapper.eq(OrderDetail::getUserId, userId);
        OrderDetail orderDetailExist = orderDetailService.getOne(queryWrapper);
        if(orderDetailExist != null){
            return orderDetailExist.getId(); //如果订单已存在，则直接返回订单id
        }
        //3根据课程id查询课程信息
        Course course = courseFeignClient.getById(courseId);
        if (course == null) {
            throw new GgktException(20001,"课程不存在");
        }
        //4根据用户id查询用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        if (userInfo == null) {
            throw new GgktException(20001,"用户不存在");
        }
        //5根据优惠券id查询优惠券信息
        //优惠券金额
        BigDecimal couponReduce = new BigDecimal(0);
        if(null != couponId) {
            CouponInfo couponInfo = couponInfoFeignClient.getById(couponId);
            couponReduce = couponInfo.getAmount();
        }
        //6封装订单生成需要数据到对象，完成添加订单
        //6.1封装数据到orderInfo对象里面，添加订单基本信息表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setPhone(userInfo.getPhone());
        orderInfo.setProvince(userInfo.getProvince());
        orderInfo.setOriginAmount(course.getPrice());
        orderInfo.setCouponReduce(couponReduce);
//        订单的总价格getOriginAmount-优惠券的价格getCouponReduce
        orderInfo.setFinalAmount(orderInfo.getOriginAmount().subtract(orderInfo.getCouponReduce()));
//        订单号
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo());
        orderInfo.setTradeBody(course.getTitle());
//        订单状态
        orderInfo.setOrderStatus("0");
//        this.save(orderInfo);
        baseMapper.insert(orderInfo);
        //6.2封装数据到orderDetail对象里面，添加订单详情信息表
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderInfo.getId());
        orderDetail.setUserId(userId);
        orderDetail.setCourseId(courseId);
        orderDetail.setCourseName(course.getTitle());
        orderDetail.setCover(course.getCover());
        orderDetail.setOriginAmount(course.getPrice());
        orderDetail.setCouponReduce(new BigDecimal(0));
        orderDetail.setFinalAmount(orderDetail.getOriginAmount().subtract(orderDetail.getCouponReduce()));
        orderDetailService.save(orderDetail);

        //7更新优惠券状态
        if(null != orderFormVo.getCouponUseId()) {
            couponInfoFeignClient.updateCouponInfoUseStatus(orderFormVo.getCouponUseId(), orderInfo.getId());
        }
        //8返回订单id

        return orderInfo.getId();
    }

    @Override
    public Map<String, Object> selectOrderInfoPage(Page<OrderInfo> pages, OrderInfoQueryVo orderInfoQueryVo) {
        // orderInfoQueryVo获取查询条件
        Long userId = orderInfoQueryVo.getUserId();
        //出交易号(订单号)
        String outTradeNo = orderInfoQueryVo.getOutTradeNo();
        String phone = orderInfoQueryVo.getPhone();
        //创建时间结束
        String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();
        //创建时间开始
        String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin();
        //订单状态(已支付和未支付)
        Integer orderStatus = orderInfoQueryVo.getOrderStatus();

        //判断条件值是否为空，不为空，进行条件封装
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(orderStatus)) {
            wrapper.eq("order_status",orderStatus);
        }
        if(!StringUtils.isEmpty(userId)) {
            wrapper.eq("user_id",userId);
        }
        if(!StringUtils.isEmpty(outTradeNo)) {
            wrapper.eq("out_trade_no",outTradeNo);
        }
        if(!StringUtils.isEmpty(phone)) {
            wrapper.eq("phone",phone);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }
        //调用实现条件分页查询
        Page<OrderInfo> orderInfoPage = baseMapper.selectPage(pages, wrapper);
        //总数
        long total = orderInfoPage.getTotal();
        long pagesCount = orderInfoPage.getPages();
//        记录
        List<OrderInfo> records = orderInfoPage.getRecords();
        //吧OrderInfo里的数据进行遍历获取到course_name
        records.stream().forEach(item -> {
            this.getOrderDetail(item);
        });
        Map<String,Object> orderHashMap = new HashMap<>();
        orderHashMap.put("total", total);
        orderHashMap.put("pagesCount", pagesCount);
        orderHashMap.put("records", records);
        return orderHashMap;
    }

    //查询订单详情数据
    private OrderInfo getOrderDetail(OrderInfo item) {
        Long id = item.getId();
        //查询订单信息
        OrderDetail orderById = orderDetailService.getById(id);
        if(orderById != null) {
            String courseName = orderById.getCourseName();
            item.getParam().put("courseName",courseName);
        }
        return item;
    }
}

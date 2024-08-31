package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IVoucherOrderService extends IService<VoucherOrder> {

    /**
     * @description 下单秒杀券
     * @param voucherId
     * @return
     */
    Result seckillVoucher(Long voucherId);

    /**
     * @description 创建订单，使用悲观锁保证线程安全
     * @param voucherId
     * @return
     */
    Result createVoucherOrderByPessimisticLock(Long voucherId);
}

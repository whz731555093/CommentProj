package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @description 生成全局唯一 ID，用于表示商品订单等不重复、寻求安全性的场景
 */
@Component
public class RedisIdWorker {
    /**
     * 开始时间戳
     * 通过 getStartTimeStamp 计算的来
     */
    private static final long BEGIN_TIMESTAMP = 1640995200L;

    /**
     * 序列号的位数
     */
    private static final int SERIAL_NUMBER_BIT_LENGTH = 32;

    private StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * @description 根据某个时间点，计算时间戳
     * @return 计算得到的时间戳
     */
    public static long getStartTimeStamp() {
        LocalDateTime localDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * @description 生成唯一 ID
     * @param keyPrefix 不同业务的 Redis key 的前缀
     * @return 生成的 ID
     */
    public long nextId(String keyPrefix) {
        // 1.生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        // 2.生成序列号
        // 2.1.获取当前日期，精确到天，按年：月：日区分，便于统计年月日的订单量
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 2.2.自增长 拼接 date 区分到不同天的下单量
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        // 3.拼接并返回
        // 格式为：符号位 + 时间错（31 bit） + 序列号（32 bit）
        return timestamp << SERIAL_NUMBER_BIT_LENGTH | count;
    }
}

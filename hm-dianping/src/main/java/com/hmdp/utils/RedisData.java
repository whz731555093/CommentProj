package com.hmdp.utils;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 存储到 Redis 的数据，用于实现逻辑过期
 */
@Data
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}

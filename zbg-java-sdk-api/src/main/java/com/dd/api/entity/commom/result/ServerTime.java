package com.dd.api.entity.commom.result;

import lombok.Data;

/**
 * @author zhangzp
 */
@Data
public class ServerTime {

    /**
     * 毫秒级时间戳
     */
    private Long timestamp;
    /**
     * 标准的ISO-8601的时间格式
     */
    private String iso;
}

package com.dd.api.entity.commom;

import lombok.Data;

import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class Page<T> {
    /**
     * 页码
     */
    private Integer page;
    /**
     * 查询记录大小
     */
    private Integer size;
    /**
     * 返回的总记录数
     */
    private Integer rows;
    /**
     * 返回的数据列表
     */
    private List<T> list;
}

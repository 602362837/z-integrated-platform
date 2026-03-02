package com.platform.usercenter.dto.request;

import lombok.Data;

/**
 * 应用查询请求
 */
@Data
public class AppQuery {

    /**
     * 应用名称(模糊查询)
     */
    private String appName;

    /**
     * 应用代码(模糊查询)
     */
    private String appCode;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}

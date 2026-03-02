package com.platform.usercenter.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用响应VO
 */
@Data
public class AppVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用代码(唯一)
     */
    private String appCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

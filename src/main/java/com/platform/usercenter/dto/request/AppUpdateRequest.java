package com.platform.usercenter.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 应用更新请求
 */
@Data
public class AppUpdateRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    private Long id;

    /**
     * 应用代码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;
}

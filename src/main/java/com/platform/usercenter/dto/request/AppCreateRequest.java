package com.platform.usercenter.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 应用创建请求
 */
@Data
public class AppCreateRequest {

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    private String appName;

    /**
     * 应用代码(唯一)
     */
    @NotBlank(message = "应用代码不能为空")
    private String appCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status = 1;
}

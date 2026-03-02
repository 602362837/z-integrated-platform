package com.platform.usercenter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 平台用户创建请求
 */
@Data
public class PlatformUserCreateRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 所属组织ID
     */
    @NotNull(message = "组织ID不能为空")
    private Long orgId;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status = 1;
}

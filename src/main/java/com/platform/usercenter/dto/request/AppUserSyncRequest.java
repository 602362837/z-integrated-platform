package com.platform.usercenter.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 应用用户同步请求
 */
@Data
public class AppUserSyncRequest {

    /**
     * 应用用户编码
     */
    @NotBlank(message = "用户编码不能为空")
    private String userCode;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;
}

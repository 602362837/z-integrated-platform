package com.platform.usercenter.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 平台用户更新请求
 */
@Data
public class PlatformUserUpdateRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码(如果需要修改密码)
     */
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
    private Long orgId;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;
}

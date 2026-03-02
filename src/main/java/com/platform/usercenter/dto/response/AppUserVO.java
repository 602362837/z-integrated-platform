package com.platform.usercenter.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用用户VO
 */
@Data
public class AppUserVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用用户编码
     */
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

    /**
     * 平台用户ID
     */
    private Long platformUserId;

    /**
     * 平台用户编码
     */
    private String platformUserCode;

    /**
     * 平台用户名
     */
    private String platformUsername;

    /**
     * 同步时间
     */
    private LocalDateTime syncTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

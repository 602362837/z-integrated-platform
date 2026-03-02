package com.platform.usercenter.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 平台用户VO
 */
@Data
public class PlatformUserVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户编码(全局唯一)
     */
    private String code;

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
     * 所属组织ID
     */
    private Long orgId;

    /**
     * 组织名称
     */
    private String orgName;

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

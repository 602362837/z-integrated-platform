package com.platform.usercenter.dto.request;

import lombok.Data;

/**
 * 平台用户查询请求
 */
@Data
public class PlatformUserQuery {

    /**
     * 用户名(模糊查询)
     */
    private String username;

    /**
     * 昵称(模糊查询)
     */
    private String nickname;

    /**
     * 邮箱(模糊查询)
     */
    private String email;

    /**
     * 手机号(模糊查询)
     */
    private String phone;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 当前页码(从1开始)
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}

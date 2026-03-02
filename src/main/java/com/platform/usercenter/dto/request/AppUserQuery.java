package com.platform.usercenter.dto.request;

import lombok.Data;

/**
 * 应用用户查询请求
 */
@Data
public class AppUserQuery {

    /**
     * 应用编码
     */
    private String appCode;

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
     * 是否有平台用户映射
     */
    private Boolean hasPlatformUser;

    /**
     * 当前页码(从1开始)
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}

package com.platform.usercenter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组织创建请求
 */
@Data
public class OrgCreateRequest {

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空")
    private String orgName;

    /**
     * 组织编码
     */
    @NotBlank(message = "组织编码不能为空")
    private String orgCode;

    /**
     * 父级ID: 0-根节点
     */
    @NotNull(message = "父级ID不能为空")
    private Long parentId;

    /**
     * 排序号
     */
    private Integer sortOrder = 0;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status = 1;
}

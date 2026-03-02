package com.platform.usercenter.service;

import com.platform.usercenter.dto.request.OrgCreateRequest;
import com.platform.usercenter.dto.request.OrgUpdateRequest;
import com.platform.usercenter.dto.response.OrgVO;
import com.platform.usercenter.entity.Org;

import java.util.List;

/**
 * 组织服务接口
 */
public interface IOrgService {

    /**
     * 获取组织树形列表
     */
    List<OrgVO> getOrgTree();

    /**
     * 获取组织详情
     */
    OrgVO getById(Long id);

    /**
     * 创建组织
     */
    OrgVO createOrg(OrgCreateRequest request);

    /**
     * 更新组织
     */
    OrgVO updateOrg(Long id, OrgUpdateRequest request);

    /**
     * 删除组织
     */
    void deleteOrg(Long id);
}

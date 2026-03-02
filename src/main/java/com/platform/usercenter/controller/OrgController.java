package com.platform.usercenter.controller;

import com.platform.usercenter.common.Result;
import com.platform.usercenter.dto.request.OrgCreateRequest;
import com.platform.usercenter.dto.request.OrgUpdateRequest;
import com.platform.usercenter.dto.response.OrgVO;
import com.platform.usercenter.service.IOrgService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织管理Controller
 */
@RestController
@RequestMapping("/api/v1/orgs")
public class OrgController {

    private final IOrgService orgService;

    public OrgController(IOrgService orgService) {
        this.orgService = orgService;
    }

    /**
     * 获取组织树形列表
     */
    @GetMapping("/tree")
    public Result<List<OrgVO>> getOrgTree() {
        return Result.success(orgService.getOrgTree());
    }

    /**
     * 获取组织详情
     */
    @GetMapping("/{id}")
    public Result<OrgVO> getById(@PathVariable Long id) {
        return Result.success(orgService.getById(id));
    }

    /**
     * 创建组织
     */
    @PostMapping
    public Result<OrgVO> createOrg(@Valid @RequestBody OrgCreateRequest request) {
        return Result.success(orgService.createOrg(request));
    }

    /**
     * 更新组织
     */
    @PutMapping("/{id}")
    public Result<OrgVO> updateOrg(@PathVariable Long id, @Valid @RequestBody OrgUpdateRequest request) {
        return Result.success(orgService.updateOrg(id, request));
    }

    /**
     * 删除组织
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteOrg(@PathVariable Long id) {
        orgService.deleteOrg(id);
        return Result.success();
    }
}

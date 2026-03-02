package com.platform.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.usercenter.dto.request.OrgCreateRequest;
import com.platform.usercenter.dto.request.OrgUpdateRequest;
import com.platform.usercenter.dto.response.OrgVO;
import com.platform.usercenter.entity.Org;
import com.platform.usercenter.entity.PlatformUser;
import com.platform.usercenter.mapper.OrgMapper;
import com.platform.usercenter.mapper.PlatformUserMapper;
import com.platform.usercenter.service.IOrgService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织服务实现类
 */
@Service
public class OrgServiceImpl implements IOrgService {

    private final OrgMapper orgMapper;
    private final PlatformUserMapper platformUserMapper;

    public OrgServiceImpl(OrgMapper orgMapper, PlatformUserMapper platformUserMapper) {
        this.orgMapper = orgMapper;
        this.platformUserMapper = platformUserMapper;
    }

    @Override
    public List<OrgVO> getOrgTree() {
        // 查询所有未删除的组织
        List<Org> orgList = orgMapper.selectList(null);
        // 构建树形结构
        return buildTree(orgList);
    }

    @Override
    public OrgVO getById(Long id) {
        Org org = orgMapper.selectById(id);
        if (org == null) {
            return null;
        }
        return convertToVO(org);
    }

    @Override
    @Transactional
    public OrgVO createOrg(OrgCreateRequest request) {
        Org org = new Org();
        BeanUtils.copyProperties(request, org);
        if (org.getSortOrder() == null) {
            org.setSortOrder(0);
        }
        if (org.getStatus() == null) {
            org.setStatus(1);
        }
        orgMapper.insert(org);
        return convertToVO(org);
    }

    @Override
    @Transactional
    public OrgVO updateOrg(Long id, OrgUpdateRequest request) {
        Org org = orgMapper.selectById(id);
        if (org == null) {
            throw new RuntimeException("组织不存在");
        }
        // 不能将父级设置为自己或自己的后代
        if (request.getParentId() != null && request.getParentId().equals(id)) {
            throw new RuntimeException("不能将父级设置为自己");
        }
        List<Long> childIds = getAllChildIds(id);
        if (request.getParentId() != null && childIds.contains(request.getParentId())) {
            throw new RuntimeException("不能将父级设置为自己的子组织");
        }

        BeanUtils.copyProperties(request, org);
        orgMapper.updateById(org);
        return convertToVO(org);
    }

    @Override
    @Transactional
    public void deleteOrg(Long id) {
        Org org = orgMapper.selectById(id);
        if (org == null) {
            throw new RuntimeException("组织不存在");
        }
        // 检查是否有子组织
        List<Org> children = orgMapper.selectList(
            new LambdaQueryWrapper<Org>().eq(Org::getParentId, id)
        );
        if (!children.isEmpty()) {
            throw new RuntimeException("该组织下存在子组织，无法删除");
        }
        // 检查是否有用户
        Long userCount = platformUserMapper.selectCount(
            new LambdaQueryWrapper<PlatformUser>().eq(PlatformUser::getOrgId, id)
        );
        if (userCount > 0) {
            throw new RuntimeException("该组织下存在用户，无法删除");
        }
        orgMapper.deleteById(id);
    }

    /**
     * 构建树形结构
     */
    private List<OrgVO> buildTree(List<Org> orgList) {
        // 转换为VO
        List<OrgVO> voList = orgList.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());

        // 构建树形结构
        List<OrgVO> rootList = new ArrayList<>();
        for (OrgVO vo : voList) {
            if (vo.getParentId() == null || vo.getParentId() == 0L) {
                rootList.add(vo);
            } else {
                // 找到父节点并添加子节点
                for (OrgVO parent : voList) {
                    if (parent.getId().equals(vo.getParentId())) {
                        parent.getChildren().add(vo);
                        break;
                    }
                }
            }
        }
        return rootList;
    }

    /**
     * 转换为VO
     */
    private OrgVO convertToVO(Org org) {
        OrgVO vo = new OrgVO();
        BeanUtils.copyProperties(org, vo);
        return vo;
    }

    /**
     * 获取所有子组织ID
     */
    private List<Long> getAllChildIds(Long parentId) {
        List<Long> result = new ArrayList<>();
        List<Org> children = orgMapper.selectList(
            new LambdaQueryWrapper<Org>().eq(Org::getParentId, parentId)
        );
        for (Org child : children) {
            result.add(child.getId());
            result.addAll(getAllChildIds(child.getId()));
        }
        return result;
    }
}

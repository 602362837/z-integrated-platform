package com.platform.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.usercenter.dto.request.PlatformUserCreateRequest;
import com.platform.usercenter.dto.request.PlatformUserQuery;
import com.platform.usercenter.dto.request.PlatformUserUpdateRequest;
import com.platform.usercenter.dto.response.PlatformUserVO;
import com.platform.usercenter.entity.Org;
import com.platform.usercenter.entity.PlatformUser;
import com.platform.usercenter.mapper.OrgMapper;
import com.platform.usercenter.mapper.PlatformUserMapper;
import com.platform.usercenter.service.IPlatformUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 平台用户服务实现类
 */
@Service
public class PlatformUserServiceImpl implements IPlatformUserService {

    private final PlatformUserMapper platformUserMapper;
    private final OrgMapper orgMapper;
    private final PasswordEncoderService passwordEncoderService;

    public PlatformUserServiceImpl(PlatformUserMapper platformUserMapper,
                                   OrgMapper orgMapper,
                                   PasswordEncoderService passwordEncoderService) {
        this.platformUserMapper = platformUserMapper;
        this.orgMapper = orgMapper;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Override
    public IPage<PlatformUserVO> getUserPage(PlatformUserQuery query) {
        LambdaQueryWrapper<PlatformUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUsername()), PlatformUser::getUsername, query.getUsername())
               .like(StringUtils.hasText(query.getNickname()), PlatformUser::getNickname, query.getNickname())
               .like(StringUtils.hasText(query.getEmail()), PlatformUser::getEmail, query.getEmail())
               .like(StringUtils.hasText(query.getPhone()), PlatformUser::getPhone, query.getPhone())
               .eq(query.getOrgId() != null, PlatformUser::getOrgId, query.getOrgId())
               .eq(query.getStatus() != null, PlatformUser::getStatus, query.getStatus())
               .orderByDesc(PlatformUser::getCreateTime);

        Page<PlatformUser> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<PlatformUser> userPage = platformUserMapper.selectPage(page, wrapper);

        // 转换为VO并填充组织名称
        return userPage.convert(this::convertToVO);
    }

    @Override
    public PlatformUserVO getById(Long id) {
        PlatformUser user = platformUserMapper.selectById(id);
        if (user == null) {
            return null;
        }
        return convertToVO(user);
    }

    @Override
    @Transactional
    public PlatformUserVO createUser(PlatformUserCreateRequest request) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<PlatformUser> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(PlatformUser::getUsername, request.getUsername());
        Long count = platformUserMapper.selectCount(checkWrapper);
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        PlatformUser user = new PlatformUser();
        BeanUtils.copyProperties(request, user);

        // 加密密码
        user.setPassword(passwordEncoderService.encodePassword(request.getPassword()));

        // 生成用户编码: PLAT_ + UUID
        user.setCode("PLAT_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());

        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(1);
        }

        platformUserMapper.insert(user);
        return convertToVO(user);
    }

    @Override
    @Transactional
    public PlatformUserVO updateUser(Long id, PlatformUserUpdateRequest request) {
        PlatformUser user = platformUserMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 如果更新用户名，检查是否与其他用户冲突
        if (StringUtils.hasText(request.getUsername()) && !request.getUsername().equals(user.getUsername())) {
            LambdaQueryWrapper<PlatformUser> checkWrapper = new LambdaQueryWrapper<>();
            checkWrapper.eq(PlatformUser::getUsername, request.getUsername());
            Long count = platformUserMapper.selectCount(checkWrapper);
            if (count > 0) {
                throw new RuntimeException("用户名已存在");
            }
        }

        // 更新密码
        if (StringUtils.hasText(request.getPassword())) {
            request.setPassword(passwordEncoderService.encodePassword(request.getPassword()));
        }

        BeanUtils.copyProperties(request, user);
        platformUserMapper.updateById(user);
        return convertToVO(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        PlatformUser user = platformUserMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        platformUserMapper.deleteById(id);
    }

    /**
     * 转换为VO并填充组织名称
     */
    private PlatformUserVO convertToVO(PlatformUser user) {
        PlatformUserVO vo = new PlatformUserVO();
        BeanUtils.copyProperties(user, vo);

        // 填充组织名称
        if (user.getOrgId() != null) {
            Org org = orgMapper.selectById(user.getOrgId());
            if (org != null) {
                vo.setOrgName(org.getOrgName());
            }
        }

        return vo;
    }
}

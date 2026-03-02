package com.platform.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.usercenter.dto.request.AppCreateRequest;
import com.platform.usercenter.dto.request.AppQuery;
import com.platform.usercenter.dto.request.AppUpdateRequest;
import com.platform.usercenter.dto.response.AppVO;
import com.platform.usercenter.entity.App;
import com.platform.usercenter.exception.BusinessException;
import com.platform.usercenter.mapper.AppMapper;
import com.platform.usercenter.service.IAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 应用服务实现类
 */
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements IAppService {

    private final AppMapper appMapper;

    @Override
    public IPage<AppVO> getAppPage(AppQuery query) {
        LambdaQueryWrapper<App> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getAppName()), App::getAppName, query.getAppName())
                .like(StringUtils.hasText(query.getAppCode()), App::getAppCode, query.getAppCode())
                .eq(query.getStatus() != null, App::getStatus, query.getStatus())
                .orderByDesc(App::getCreateTime);

        Page<App> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<App> result = appMapper.selectPage(page, wrapper);

        return result.convert(this::convertToVO);
    }

    @Override
    public AppVO getById(Long id) {
        App app = appMapper.selectById(id);
        if (app == null) {
            throw new BusinessException("应用不存在");
        }
        return convertToVO(app);
    }

    @Override
    public AppVO createApp(AppCreateRequest request) {
        // 检查appCode是否已存在
        LambdaQueryWrapper<App> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(App::getAppCode, request.getAppCode());
        Long count = appMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("应用代码已存在");
        }

        App app = new App();
        BeanUtils.copyProperties(request, app);
        appMapper.insert(app);

        return convertToVO(app);
    }

    @Override
    public AppVO updateApp(AppUpdateRequest request) {
        App app = appMapper.selectById(request.getId());
        if (app == null) {
            throw new BusinessException("应用不存在");
        }

        // 如果修改了appCode，检查是否已存在
        if (StringUtils.hasText(request.getAppCode()) && !request.getAppCode().equals(app.getAppCode())) {
            LambdaQueryWrapper<App> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(App::getAppCode, request.getAppCode())
                    .ne(App::getId, request.getId());
            Long count = appMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException("应用代码已存在");
            }
        }

        BeanUtils.copyProperties(request, app);
        appMapper.updateById(app);

        return convertToVO(app);
    }

    @Override
    public void deleteApp(Long id) {
        App app = appMapper.selectById(id);
        if (app == null) {
            throw new BusinessException("应用不存在");
        }
        appMapper.deleteById(id);
    }

    private AppVO convertToVO(App app) {
        AppVO vo = new AppVO();
        BeanUtils.copyProperties(app, vo);
        return vo;
    }
}

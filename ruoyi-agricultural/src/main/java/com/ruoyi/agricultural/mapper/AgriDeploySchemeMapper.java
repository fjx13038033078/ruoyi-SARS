package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriDeployScheme;
import java.util.List;

/**
 * 部署方案主表访问，SQL 定义于 {@code AgriDeploySchemeMapper.xml}。
 */
public interface AgriDeploySchemeMapper {
    List<AgriDeployScheme> selectSchemeList(AgriDeployScheme scheme);
    AgriDeployScheme selectSchemeById(Long schemeId);
    /** 插入后通常需回填 {@code schemeId}（依赖 XML useGeneratedKeys） */
    int insertScheme(AgriDeployScheme scheme);
    int updateScheme(AgriDeployScheme scheme);
    int deleteSchemeById(Long schemeId);
    int deleteSchemeByIds(Long[] schemeIds);
}
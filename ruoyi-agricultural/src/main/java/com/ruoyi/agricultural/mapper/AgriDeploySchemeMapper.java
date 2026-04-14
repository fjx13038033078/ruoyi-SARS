package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriDeployScheme;
import java.util.List;

public interface AgriDeploySchemeMapper {
    List<AgriDeployScheme> selectSchemeList(AgriDeployScheme scheme);
    AgriDeployScheme selectSchemeById(Long schemeId);
    int insertScheme(AgriDeployScheme scheme);
    int updateScheme(AgriDeployScheme scheme);
    int deleteSchemeById(Long schemeId);
    int deleteSchemeByIds(Long[] schemeIds);
}
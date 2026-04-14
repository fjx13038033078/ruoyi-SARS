package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriDeployNode;
import com.ruoyi.agricultural.domain.AgriDeployScheme;
import java.util.List;

public interface IAgriDeploySchemeService {
    List<AgriDeployScheme> selectSchemeList(AgriDeployScheme scheme);
    AgriDeployScheme selectSchemeById(Long schemeId);
    List<AgriDeployNode> selectNodesBySchemeId(Long schemeId);
    Long generateScheme(Long farmlandId, String algorithmType, String createBy, Long userId);
    int updateScheme(AgriDeployScheme scheme);
    int deleteSchemeByIds(Long[] schemeIds);
}
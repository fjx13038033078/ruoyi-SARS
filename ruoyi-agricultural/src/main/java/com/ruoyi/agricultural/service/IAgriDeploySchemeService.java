package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriDeployNode;
import com.ruoyi.agricultural.domain.AgriDeployScheme;
import java.util.List;

/**
 * 部署方案服务：方案与节点查询、按算法生成方案（事务）、更新与级联删除节点。
 */
public interface IAgriDeploySchemeService {
    List<AgriDeployScheme> selectSchemeList(AgriDeployScheme scheme);
    AgriDeployScheme selectSchemeById(Long schemeId);
    List<AgriDeployNode> selectNodesBySchemeId(Long schemeId);
    /**
     * 根据农田栅格与算法类型生成部署方案并落库节点明细。
     * @param algorithmType {@code coverage} 覆盖贪心；{@code cluster} K-Means 聚类
     * @return 新建方案主键
     */
    Long generateScheme(Long farmlandId, String algorithmType, String createBy, Long userId);
    int updateScheme(AgriDeployScheme scheme);
    int deleteSchemeByIds(Long[] schemeIds);
}
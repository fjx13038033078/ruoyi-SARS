package com.ruoyi.agricultural.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.agricultural.algorithm.*;
import com.ruoyi.agricultural.domain.*;
import com.ruoyi.agricultural.mapper.*;
import com.ruoyi.agricultural.service.IAgriDeploySchemeService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 部署方案实现：{@link #generateScheme} 在单事务内插入方案主表与节点批量表；
 * 覆盖算法写真实覆盖率与固定能量均衡占位；聚类算法当前将能量均衡得分同时写入覆盖率与能量均衡字段（与现有业务字段约定一致，便于列表展示）。
 */
@Service
public class AgriDeploySchemeServiceImpl implements IAgriDeploySchemeService {

    @Autowired
    private AgriDeploySchemeMapper schemeMapper;
    @Autowired
    private AgriDeployNodeMapper nodeMapper;
    @Autowired
    private AgriFarmlandMapper farmlandMapper;
    @Autowired
    private CoverageOptimizer coverageOptimizer;
    @Autowired
    private ClusterOptimizer clusterOptimizer;

    @Override
    public List<AgriDeployScheme> selectSchemeList(AgriDeployScheme scheme) {
        return schemeMapper.selectSchemeList(scheme);
    }
    @Override
    public AgriDeployScheme selectSchemeById(Long schemeId) {
        return schemeMapper.selectSchemeById(schemeId);
    }
    @Override
    public List<AgriDeployNode> selectNodesBySchemeId(Long schemeId) {
        return nodeMapper.selectNodesBySchemeId(schemeId);
    }

    /**
     * 流程：加载农田栅格 → 组装 {@link AlgorithmParam} → 分支调用 {@link CoverageOptimizer} 或 {@link ClusterOptimizer}
     * → 持久化 {@link AgriDeployScheme}（含 JSON 结果）→ {@link AgriDeployNodeMapper#batchInsertNodes}。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long generateScheme(Long farmlandId, String algorithmType, String createBy, Long userId) {
        AgriFarmland farmland = farmlandMapper.selectFarmlandById(farmlandId);
        if (farmland == null) {
            throw new ServiceException("Farmland not found, ID: " + farmlandId);
        }
        int rows = farmland.getGridRows() != null ? farmland.getGridRows() : 20;
        int cols = farmland.getGridCols() != null ? farmland.getGridCols() : 20;

        AlgorithmParam param = new AlgorithmParam();
        param.setGridRows(rows);
        param.setGridCols(cols);

        List<NodeResult> nodeResults;
        double coverageRate;
        double energyBalance;

        if ("coverage".equals(algorithmType)) {
            param.setSensorRadius(3.0);
            param.setCoverageTarget(0.90);
            nodeResults = coverageOptimizer.optimize(param);
            coverageRate = coverageOptimizer.calcCoverageRate(nodeResults, rows, cols);
            energyBalance = 100.0;
        } else if ("cluster".equals(algorithmType)) {
            param.setClusterCount(5);
            param.setMaxIterations(100);
            nodeResults = clusterOptimizer.optimize(param);
            coverageRate = clusterOptimizer.calcEnergyBalance(nodeResults);
            energyBalance = clusterOptimizer.calcEnergyBalance(nodeResults);
        } else {
            throw new ServiceException("Unsupported algorithm type: " + algorithmType);
        }

        AgriDeployScheme scheme = new AgriDeployScheme();
        scheme.setSchemeName(farmland.getFarmlandName() + "-"
                + ("coverage".equals(algorithmType) ? "CoverageOpt" : "ClusterOpt")
                + "-" + DateUtils.dateTimeNow());
        scheme.setFarmlandId(farmlandId);
        scheme.setAlgorithmType(algorithmType);
        scheme.setNodeCount(nodeResults.size());
        scheme.setCoverageRate(BigDecimal.valueOf(coverageRate).setScale(2, RoundingMode.HALF_UP));
        scheme.setEnergyBalance(BigDecimal.valueOf(energyBalance).setScale(2, RoundingMode.HALF_UP));
        scheme.setResultJson(JSON.toJSONString(nodeResults));
        scheme.setUserId(userId);
        scheme.setStatus("0");
        scheme.setCreateBy(createBy);
        scheme.setCreateTime(DateUtils.getNowDate());
        schemeMapper.insertScheme(scheme);

        List<AgriDeployNode> nodes = new ArrayList<>();
        for (NodeResult nr : nodeResults) {
            AgriDeployNode node = new AgriDeployNode();
            node.setSchemeId(scheme.getSchemeId());
            node.setNodeCode(nr.getNodeCode());
            node.setNodeType(nr.getNodeType());
            node.setPosX(BigDecimal.valueOf(nr.getPosX()).setScale(4, RoundingMode.HALF_UP));
            node.setPosY(BigDecimal.valueOf(nr.getPosY()).setScale(4, RoundingMode.HALF_UP));
            node.setCoverageRadius(BigDecimal.valueOf(nr.getCoverageRadius()).setScale(2, RoundingMode.HALF_UP));
            node.setEnergyLevel(BigDecimal.valueOf(nr.getEnergyLevel()).setScale(2, RoundingMode.HALF_UP));
            node.setClusterId(nr.getClusterId());
            node.setIsClusterHead(nr.isClusterHead() ? "Y" : "N");
            nodes.add(node);
        }
        if (!nodes.isEmpty()) {
            nodeMapper.batchInsertNodes(nodes);
        }
        return scheme.getSchemeId();
    }

    @Override
    public int updateScheme(AgriDeployScheme scheme) {
        scheme.setUpdateTime(DateUtils.getNowDate());
        return schemeMapper.updateScheme(scheme);
    }

    /** 先按方案 ID 删除节点明细，再批量删除方案主表，保证外键/数据一致性。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSchemeByIds(Long[] schemeIds) {
        for (Long id : schemeIds) {
            nodeMapper.deleteNodesBySchemeId(id);
        }
        return schemeMapper.deleteSchemeByIds(schemeIds);
    }
}
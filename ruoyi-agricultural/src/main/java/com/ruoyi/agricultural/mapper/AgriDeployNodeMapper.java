package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriDeployNode;
import java.util.List;

public interface AgriDeployNodeMapper {
    List<AgriDeployNode> selectNodesBySchemeId(Long schemeId);
    int batchInsertNodes(List<AgriDeployNode> nodes);
    int deleteNodesBySchemeId(Long schemeId);
}
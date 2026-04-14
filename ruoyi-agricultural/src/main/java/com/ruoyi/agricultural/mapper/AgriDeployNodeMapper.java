package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriDeployNode;
import java.util.List;

/**
 * 部署方案节点明细表访问，SQL 定义于 {@code AgriDeployNodeMapper.xml}。
 */
public interface AgriDeployNodeMapper {
    /** 按方案 ID 查询全部节点 */
    List<AgriDeployNode> selectNodesBySchemeId(Long schemeId);
    /** 批量插入节点（生成方案后调用） */
    int batchInsertNodes(List<AgriDeployNode> nodes);
    /** 删除某方案下全部节点（删除方案前清理） */
    int deleteNodesBySchemeId(Long schemeId);
}
package com.ruoyi.agricultural.domain;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 部署方案中的单个节点，对应表 {@code agri_deploy_node}。
 * <p>坐标与栅格算法一致：{@code posX}/{@code posY} 为网格平面坐标（列/行或自定义约定）；{@code nodeType} 区分 sensor、sink 等。</p>
 */
@Data
public class AgriDeployNode implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 节点主键 */
    private Long nodeId;
    /** 所属方案 ID */
    private Long schemeId;
    /** 节点编码，如 S001、CH01 */
    private String nodeCode;
    /** 节点类型：sensor、sink 等 */
    private String nodeType;
    /** X 坐标（栅格列方向） */
    private BigDecimal posX;
    /** Y 坐标（栅格行方向） */
    private BigDecimal posY;
    /** 覆盖半径（与算法圆盘覆盖模型一致） */
    private BigDecimal coverageRadius;
    /** 能量水平（演示或均衡指标用） */
    private BigDecimal energyLevel;
    /** 聚类簇编号（非聚类算法可为 0） */
    private Integer clusterId;
    /** 是否簇头：Y/N */
    private String isClusterHead;
}
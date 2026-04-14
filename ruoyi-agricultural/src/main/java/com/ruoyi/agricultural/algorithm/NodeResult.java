package com.ruoyi.agricultural.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 算法输出的单节点结果，序列化进方案 {@code resultJson} 并映射为 {@link com.ruoyi.agricultural.domain.AgriDeployNode}。
 * <p>坐标系：{@code posX} 对应列方向、{@code posY} 对应行方向，与 {@link CoverageOptimizer} 中网格索引一致（传感器放在整数格点，sink 可为半格中心）。</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeResult {
    /** 展示用编码，如 S001、CH01、SINK001 */
    private String nodeCode;
    /** sensor / sink 等，覆盖率重算时仅统计 sensor */
    private String nodeType;
    private double posX;
    private double posY;
    /** 覆盖半径，与算法圆盘模型一致 */
    private double coverageRadius;
    /** 能量水平：覆盖算法固定 100；聚类算法簇头 100、普通节点随机 80–100 */
    private double energyLevel;
    /** 簇编号：覆盖算法为 0；聚类为 1..K */
    private int clusterId;
    /** 是否为簇头（覆盖算法仅 sink 为 true） */
    private boolean clusterHead;
}
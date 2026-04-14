package com.ruoyi.agricultural.algorithm;

import lombok.Data;

/**
 * 部署算法超参数 DTO，由 {@link com.ruoyi.agricultural.service.impl.AgriDeploySchemeServiceImpl} 按农田与算法类型填充。
 * <p>覆盖算法主要使用栅格规模、{@code sensorRadius}（欧氏圆盘半径，与栅格单元同量纲）、{@code coverageTarget}（0–1，停止条件为已覆盖格数占比）。</p>
 * <p>聚类算法使用 {@code clusterCount}、{@code maxIterations}（K-Means 上限迭代）；节点规模在 {@link ClusterOptimizer} 内按面积与半径估算。</p>
 */
@Data
public class AlgorithmParam {
    /** 栅格行数 R，与农田 {@code gridRows} 对齐 */
    private int gridRows = 20;
    /** 栅格列数 C */
    private int gridCols = 20;
    /** 传感器覆盖半径（圆盘模型，距离阈值平方为 radius²） */
    private double sensorRadius = 3.0;
    /** 目标覆盖率，例如 0.90 表示至少90% 栅格被覆盖则停止贪心 */
    private double coverageTarget = 0.90;
    /** K-Means 簇个数 K */
    private int clusterCount = 5;
    /** K-Means 最大迭代轮数，提前收敛（分配向量不变）时提前结束 */
    private int maxIterations = 100;
}
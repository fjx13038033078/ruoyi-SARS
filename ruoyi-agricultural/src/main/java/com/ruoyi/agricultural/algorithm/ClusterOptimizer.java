package com.ruoyi.agricultural.algorithm;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 聚类部署（LEACH 风格 + K-Means）。
 * <p><b>步骤：</b>（1）用农田面积与固定半径估算节点数 N，并保证 N ≥ 3K；（2）在 [0,C)×[0,R) 上均匀随机布点（种子42 可复现）；
 * （3）K 个初始质心按 sqrt(K) 网格铺在场上；（4）标准 K-Means：分配 → 更新质心，质心裁剪在场内；（5）每簇取离质心最近的点为簇头，编码 CH**，其余为 S**，
 * 簇头节点类型记为 sink、{@code clusterHead=true}，能量略高于普通节点。</p>
 * <p><b>注意：</b>{@link #optimize} 内部半径写死为 3.0，与 {@link AlgorithmParam#getSensorRadius()} 解耦（演示用）。</p>
 * <p><b>复杂度：</b>每轮 K-Means O(N·K)，最多 {@code maxIterations} 轮；总体线性于 N、K 的乘积。</p>
 */
@Component
public class ClusterOptimizer {

    /** 预留：按簇比例选头（当前实现为每簇最近质心一个头） */
    private static final double CLUSTER_HEAD_RATIO = 0.1;

    /**
     * 生成 N 个节点并聚类，返回列表顺序与随机布点顺序一致。
     */
    public List<NodeResult> optimize(AlgorithmParam param) {
        int rows = param.getGridRows();
        int cols = param.getGridCols();
        int clusterCount = param.getClusterCount();
        int maxIter = param.getMaxIterations();

        double radius = 3.0;
        int totalNodes = (int) Math.ceil(rows * cols / (Math.PI * radius * radius) * 1.5);
        totalNodes = Math.max(totalNodes, clusterCount * 3);

        Random random = new Random(42);
        double[][] positions = new double[totalNodes][2];
        for (int i = 0; i < totalNodes; i++) {
            positions[i][0] = random.nextDouble() * cols;
            positions[i][1] = random.nextDouble() * rows;
        }

        // Initialize cluster centers in a grid pattern
        double[][] centers = initCenters(rows, cols, clusterCount, random);

        // K-Means clustering
        int[] assignment = new int[totalNodes];
        for (int iter = 0; iter < maxIter; iter++) {
            boolean changed = false;
            for (int i = 0; i < totalNodes; i++) {
                int best = nearestCenter(positions[i], centers);
                if (best != assignment[i]) { assignment[i] = best; changed = true; }
            }
            if (!changed) break;
            centers = updateCenters(positions, assignment, clusterCount, rows, cols);
        }

        // Build result nodes
        List<NodeResult> nodes = new ArrayList<>();
        // Find cluster head: nearest node to each cluster center
        int[] clusterHeadIdx = new int[clusterCount];
        Arrays.fill(clusterHeadIdx, -1);
        double[] minDist = new double[clusterCount];
        Arrays.fill(minDist, Double.MAX_VALUE);

        for (int i = 0; i < totalNodes; i++) {
            int cid = assignment[i];
            double d = dist(positions[i], centers[cid]);
            if (d < minDist[cid]) { minDist[cid] = d; clusterHeadIdx[cid] = i; }
        }

        Set<Integer> headSet = new HashSet<>();
        for (int idx : clusterHeadIdx) if (idx >= 0) headSet.add(idx);

        int sensorIdx = 1, headIdx = 1;
        for (int i = 0; i < totalNodes; i++) {
            boolean isHead = headSet.contains(i);
            NodeResult node = new NodeResult();
            node.setNodeCode(isHead ? "CH" + String.format("%02d", headIdx++) : "S" + String.format("%03d", sensorIdx++));
            node.setNodeType(isHead ? "sink" : "sensor");
            node.setPosX(Math.round(positions[i][0] * 100.0) / 100.0);
            node.setPosY(Math.round(positions[i][1] * 100.0) / 100.0);
            node.setCoverageRadius(radius);
            node.setEnergyLevel(isHead ? 100.0 : 80.0 + random.nextDouble() * 20.0);
            node.setClusterId(assignment[i] + 1);
            node.setClusterHead(isHead);
            nodes.add(node);
        }

        return nodes;
    }

    /** 在 sqrt(K)×sqrt(K) 网格上均匀铺开 K 个初始质心（确定性布局，{@code random} 未使用）。 */
    private double[][] initCenters(int rows, int cols, int k, Random random) {
        double[][] centers = new double[k][2];
        int sqrtK = (int) Math.ceil(Math.sqrt(k));
        int idx = 0;
        for (int r = 0; r < sqrtK && idx < k; r++) {
            for (int c = 0; c < sqrtK && idx < k; c++) {
                centers[idx][0] = (c + 0.5) * cols / sqrtK;
                centers[idx][1] = (r + 0.5) * rows / sqrtK;
                idx++;
            }
        }
        return centers;
    }

    /** 欧氏距离下最近质心下标。 */
    private int nearestCenter(double[] pos, double[][] centers) {
        int best = 0;
        double minD = Double.MAX_VALUE;
        for (int i = 0; i < centers.length; i++) {
            double d = dist(pos, centers[i]);
            if (d < minD) { minD = d; best = i; }
        }
        return best;
    }

    /** 按簇内点坐标求均值；空簇回退到场中心附近，并把质心限制在 [0,cols)、[0,rows) 内。 */
    private double[][] updateCenters(double[][] positions, int[] assignment, int k, int rows, int cols) {
        double[][] sum = new double[k][2];
        int[] cnt = new int[k];
        for (int i = 0; i < positions.length; i++) {
            sum[assignment[i]][0] += positions[i][0];
            sum[assignment[i]][1] += positions[i][1];
            cnt[assignment[i]]++;
        }
        double[][] centers = new double[k][2];
        for (int i = 0; i < k; i++) {
            centers[i][0] = cnt[i] > 0 ? Math.min(sum[i][0] / cnt[i], cols - 0.01) : (double) cols / 2;
            centers[i][1] = cnt[i] > 0 ? Math.min(sum[i][1] / cnt[i], rows - 0.01) : (double) rows / 2;
        }
        return centers;
    }

    private double dist(double[] a, double[] b) {
        double dx = a[0] - b[0], dy = a[1] - b[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 能量均衡得分：先算能量均值，再算总体标准差 std，返回 max(0, 100 - std)。
     * <p>所有节点（含簇头）参与统计；std 越小得分越接近 100。</p>
     */
    public double calcEnergyBalance(List<NodeResult> nodes) {
        if (nodes.isEmpty()) return 100.0;
        double sum = 0, sum2 = 0;
        int n = nodes.size();
        for (NodeResult node : nodes) { sum += node.getEnergyLevel(); }
        double avg = sum / n;
        for (NodeResult node : nodes) { double d = node.getEnergyLevel() - avg; sum2 += d * d; }
        double std = Math.sqrt(sum2 / n);
        return Math.max(0, 100.0 - std);
    }
}

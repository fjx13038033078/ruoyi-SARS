package com.ruoyi.agricultural.algorithm;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 基于栅格的覆盖优化（贪心）。
 * <p><b>思路：</b>维护 {@code covered[r][c]}；每轮在全体格点上枚举候选位置，选择「新覆盖未覆盖格」数量最大的位置布设传感器；
 * 重复直至覆盖率 ≥ {@link AlgorithmParam#getCoverageTarget()} 或无正增益。最后在农田几何中心追加一个 sink（较大半径，标记为簇头便于展示）。</p>
 * <p><b>覆盖模型：</b>以欧氏距离平方 ≤ radius² 的圆盘内格子为可覆盖集合（与 {@link #calcGain} / {@link #markCovered} 一致）。</p>
 * <p><b>复杂度直觉：</b>每轮 O(R·C·r²)（r 为半径上取整），轮数上界与目标覆盖率及增益分布有关；最坏可接近 O(节点数·R·C·r²)。</p>
 */
@Component
public class CoverageOptimizer {

    /**
     * 执行覆盖贪心，返回传感器列表 + 末尾一个 sink。
     * @param param 栅格规模、半径、目标覆盖率等
     * @return 有序节点列表（算法输出顺序即布设顺序）
     */
    public List<NodeResult> optimize(AlgorithmParam param) {
        int rows = param.getGridRows();
        int cols = param.getGridCols();
        double radius = param.getSensorRadius();
        double target = param.getCoverageTarget();

        boolean[][] covered = new boolean[rows][cols];
        List<NodeResult> nodes = new ArrayList<>();
        int totalCells = rows * cols;
        int coveredCount = 0;
        int nodeIndex = 1;

        // Greedy: find the grid cell with max coverage gain each iteration
        while ((double) coveredCount / totalCells < target) {
            int bestRow = -1, bestCol = -1, bestGain = -1;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    int gain = calcGain(covered, rows, cols, r, c, radius);
                    if (gain > bestGain) {
                        bestGain = gain;
                        bestRow = r;
                        bestCol = c;
                    }
                }
            }
            if (bestRow == -1 || bestGain == 0) break;

            markCovered(covered, rows, cols, bestRow, bestCol, radius);
            coveredCount += bestGain;

            NodeResult node = new NodeResult();
            node.setNodeCode("S" + String.format("%03d", nodeIndex++));
            node.setNodeType("sensor");
            node.setPosX(bestCol);
            node.setPosY(bestRow);
            node.setCoverageRadius(radius);
            node.setEnergyLevel(100.0);
            node.setClusterId(0);
            node.setClusterHead(false);
            nodes.add(node);
        }

        // Add a sink node at the center
        NodeResult sink = new NodeResult();
        sink.setNodeCode("SINK001");
        sink.setNodeType("sink");
        sink.setPosX(cols / 2.0);
        sink.setPosY(rows / 2.0);
        sink.setCoverageRadius(Math.max(rows, cols) / 2.0);
        sink.setEnergyLevel(100.0);
        sink.setClusterId(0);
        sink.setClusterHead(true);
        nodes.add(sink);

        return nodes;
    }

    /**
     * 若在 (r,c) 放置圆盘，能把多少当前仍为 false 的格子新标为已覆盖（只计数增量）。
     */
    private int calcGain(boolean[][] covered, int rows, int cols, int r, int c, double radius) {
        int gain = 0;
        int ri = (int) Math.ceil(radius);
        for (int dr = -ri; dr <= ri; dr++) {
            for (int dc = -ri; dc <= ri; dc++) {
                if (dr * dr + dc * dc <= radius * radius) {
                    int nr = r + dr, nc = c + dc;
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !covered[nr][nc]) {
                        gain++;
                    }
                }
            }
        }
        return gain;
    }

    /** 将圆盘内所有格子置为已覆盖（不区分此前状态）。 */
    private void markCovered(boolean[][] covered, int rows, int cols, int r, int c, double radius) {
        int ri = (int) Math.ceil(radius);
        for (int dr = -ri; dr <= ri; dr++) {
            for (int dc = -ri; dc <= ri; dc++) {
                if (dr * dr + dc * dc <= radius * radius) {
                    int nr = r + dr, nc = c + dc;
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        covered[nr][nc] = true;
                    }
                }
            }
        }
    }

    /**
     * 按当前节点列表重算栅格覆盖率（百分比0–100）。
     * <p>仅对 {@code nodeType=sensor} 执行 {@link #markCovered}，sink 不参与覆盖统计，与优化阶段「传感器圆盘 +单独 sink」语义一致。</p>
     */
    public double calcCoverageRate(List<NodeResult> nodes, int rows, int cols) {
        boolean[][] covered = new boolean[rows][cols];
        for (NodeResult node : nodes) {
            if ("sensor".equals(node.getNodeType())) {
                int r = (int) Math.round(node.getPosY());
                int c = (int) Math.round(node.getPosX());
                markCovered(covered, rows, cols, r, c, node.getCoverageRadius());
            }
        }
        int cnt = 0;
        for (boolean[] row : covered) for (boolean v : row) if (v) cnt++;
        return (double) cnt / (rows * cols) * 100.0;
    }
}

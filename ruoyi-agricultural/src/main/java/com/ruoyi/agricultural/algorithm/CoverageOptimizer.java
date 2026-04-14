package com.ruoyi.agricultural.algorithm;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Coverage Optimization Algorithm (Greedy Grid-based).
 * Uses a greedy strategy: iteratively places sensor nodes at grid positions
 * that maximize new coverage gain until the target coverage rate is reached.
 * Finally adds a sink node at the center of the field.
 */
@Component
public class CoverageOptimizer {

    /**
     * Run coverage optimization
     * @param param algorithm parameters
     * @return list of deployed nodes
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

    /** Calculate overall coverage rate percentage */
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

package com.ruoyi.agricultural.algorithm;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Cluster Optimization Algorithm (LEACH-inspired + K-Means).
 * 1. Estimates total sensor count based on area and coverage radius.
 * 2. Randomly distributes sensor positions across the grid.
 * 3. Uses K-Means to partition sensors into clusters.
 * 4. Selects the sensor nearest to each cluster center as cluster head.
 */
@Component
public class ClusterOptimizer {

    private static final double CLUSTER_HEAD_RATIO = 0.1;

    /**
     * Run cluster optimization
     * @param param algorithm parameters
     * @return list of deployed nodes
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

    private int nearestCenter(double[] pos, double[][] centers) {
        int best = 0;
        double minD = Double.MAX_VALUE;
        for (int i = 0; i < centers.length; i++) {
            double d = dist(pos, centers[i]);
            if (d < minD) { minD = d; best = i; }
        }
        return best;
    }

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

    /** Calculate energy balance score (100 = perfectly balanced, lower = more variance) */
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

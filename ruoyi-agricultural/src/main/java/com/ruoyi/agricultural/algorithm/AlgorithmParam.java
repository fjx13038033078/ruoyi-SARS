package com.ruoyi.agricultural.algorithm;

import lombok.Data;

@Data
public class AlgorithmParam {
    private int gridRows = 20;
    private int gridCols = 20;
    private double sensorRadius = 3.0;
    private double coverageTarget = 0.90;
    private int clusterCount = 5;
    private int maxIterations = 100;
}
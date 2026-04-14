package com.ruoyi.agricultural.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeResult {
    private String nodeCode;
    private String nodeType;
    private double posX;
    private double posY;
    private double coverageRadius;
    private double energyLevel;
    private int clusterId;
    private boolean clusterHead;
}
package com.ruoyi.agricultural.domain;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AgriDeployNode implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long nodeId;
    private Long schemeId;
    private String nodeCode;
    private String nodeType;
    private BigDecimal posX;
    private BigDecimal posY;
    private BigDecimal coverageRadius;
    private BigDecimal energyLevel;
    private Integer clusterId;
    private String isClusterHead;
}
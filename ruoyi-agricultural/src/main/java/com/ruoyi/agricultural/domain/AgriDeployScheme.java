package com.ruoyi.agricultural.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AgriDeployScheme extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long schemeId;
    @Excel(name = "scheme_name")
    private String schemeName;
    private Long farmlandId;
    private String farmlandName;
    @Excel(name = "algorithm_type", readConverterExp = "coverage=Coverage,cluster=Cluster")
    private String algorithmType;
    @Excel(name = "node_count")
    private Integer nodeCount;
    @Excel(name = "coverage_rate")
    private java.math.BigDecimal coverageRate;
    @Excel(name = "energy_balance")
    private java.math.BigDecimal energyBalance;
    private String resultJson;
    private Long userId;
    @Excel(name = "status", readConverterExp = "0=pending,1=confirmed,2=deprecated")
    private String status;
}
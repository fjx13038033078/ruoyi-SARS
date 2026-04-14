package com.ruoyi.agricultural.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AgriFarmland extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long farmlandId;

    @Excel(name = "farmland_name")
    private String farmlandName;

    private String boundaryData;

    @Excel(name = "area")
    private java.math.BigDecimal area;

    @Excel(name = "crop_type")
    private String cropType;

    @Excel(name = "grid_rows")
    private Integer gridRows;

    @Excel(name = "grid_cols")
    private Integer gridCols;

    private Long userId;

    @Excel(name = "status", readConverterExp = "0=normal,1=disabled")
    private String status;
}
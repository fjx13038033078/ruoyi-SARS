package com.ruoyi.agricultural.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 农田主数据实体，对应表 {@code agri_farmland}。
 * <p>用于描述农田名称、边界、面积、作物及部署算法使用的栅格规模等；继承 {@link com.ruoyi.common.core.domain.BaseEntity} 以复用创建/更新审计字段。</p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AgriFarmland extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 农田主键 ID */
    private Long farmlandId;

    /** 农田名称（展示与导出） */
    @Excel(name = "farmland_name")
    private String farmlandName;

    /** 边界数据（如 GeoJSON 或自定义序列化字符串，供前端地图展示） */
    private String boundaryData;

    /** 农田面积 */
    @Excel(name = "area")
    private java.math.BigDecimal area;

    /** 作物类型 */
    @Excel(name = "crop_type")
    private String cropType;

    /** 部署栅格行数，供覆盖/聚类算法离散化农田 */
    @Excel(name = "grid_rows")
    private Integer gridRows;

    /** 部署栅格列数 */
    @Excel(name = "grid_cols")
    private Integer gridCols;

    /** 归属用户 ID（与业务用户关联） */
    private Long userId;

    /** 状态：0 正常 1 停用（与字典/导出 readConverterExp 一致） */
    @Excel(name = "status", readConverterExp = "0=normal,1=disabled")
    private String status;
}
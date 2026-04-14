package com.ruoyi.agricultural.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 农田环境监测采样数据，对应表 {@code agri_env_data}。
 * <p>单条记录表示某时刻在某块农田上的温湿度、光照、土壤湿度、风速等指标；支持列表查询与模拟批量生成。</p>
 */
@Data
public class AgriEnvData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 数据主键 */
    private Long dataId;
    /** 关联农田 ID */
    private Long farmlandId;
    /** 农田名称（列表联查冗余字段，便于展示） */
    private String farmlandName;

    /** 气温（℃） */
    @Excel(name = "temperature")
    private BigDecimal temperature;

    /** 空气湿度（%） */
    @Excel(name = "humidity")
    private BigDecimal humidity;

    /** 光照强度（lux 等业务约定单位） */
    @Excel(name = "light_intensity")
    private BigDecimal lightIntensity;

    /** 土壤湿度（%） */
    @Excel(name = "soil_moisture")
    private BigDecimal soilMoisture;

    /** 风速（m/s 等业务约定单位） */
    @Excel(name = "wind_speed")
    private BigDecimal windSpeed;

    /** 采样时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "record_time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    /** 创建者 */
    private String createBy;

    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
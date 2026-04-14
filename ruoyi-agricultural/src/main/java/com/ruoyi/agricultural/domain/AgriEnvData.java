package com.ruoyi.agricultural.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AgriEnvData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dataId;
    private Long farmlandId;
    private String farmlandName;

    @Excel(name = "temperature")
    private BigDecimal temperature;

    @Excel(name = "humidity")
    private BigDecimal humidity;

    @Excel(name = "light_intensity")
    private BigDecimal lightIntensity;

    @Excel(name = "soil_moisture")
    private BigDecimal soilMoisture;

    @Excel(name = "wind_speed")
    private BigDecimal windSpeed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "record_time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
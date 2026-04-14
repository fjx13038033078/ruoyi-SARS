package com.ruoyi.agricultural.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class AgriAlgorithmConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long configId;
    private String algorithmType;
    private String algorithmName;
    private String paramJson;
    private String description;
    private String status;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
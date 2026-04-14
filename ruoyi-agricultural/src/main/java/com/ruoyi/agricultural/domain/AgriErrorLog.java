package com.ruoyi.agricultural.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class AgriErrorLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long errorId;
    private String errorType;
    private String errorMsg;
    private String stackTrace;
    private String requestUrl;
    private String requestParam;
    private Long userId;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Map<String, Object> params = new HashMap<>();
}
package com.ruoyi.agricultural.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class AgriComment implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long commentId;
    private String content;
    private String imageUrls;
    private Integer rating;
    private String targetType;
    private Long targetId;
    private String isAnonymous;
    private Long userId;
    private String userName;
    private String status;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
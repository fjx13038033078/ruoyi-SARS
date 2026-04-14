package com.ruoyi.agricultural.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 农业模块异常日志，对应表 {@code agri_error_log}。
 * <p>由切面或业务异步写入；{@code params} 为 MyBatis 查询条件扩展用，非持久化列。</p>
 */
@Data
public class AgriErrorLog implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 日志主键 */
    private Long errorId;
    /** 错误分类（异常类名或业务编码） */
    private String errorType;
    /** 错误摘要（入库前可能截断） */
    private String errorMsg;
    /** 堆栈（可能截断） */
    private String stackTrace;
    /** 请求 URL */
    private String requestUrl;
    /** 请求参数快照 */
    private String requestParam;
    private Long userId;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 列表查询扩展参数（不落库） */
    private Map<String, Object> params = new HashMap<>();
}
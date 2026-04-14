package com.ruoyi.agricultural.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 部署算法配置，对应表 {@code agri_algorithm_config}（若库表名不同以实际 DDL 为准）。
 * <p>按 {@code algorithmType} 区分覆盖优化、聚类优化等；{@code paramJson} 可存放默认超参数 JSON，供前端或调度读取。</p>
 */
@Data
public class AgriAlgorithmConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 配置主键 */
    private Long configId;
    /** 算法类型标识，如 coverage、cluster，与业务代码分支一致 */
    private String algorithmType;
    /** 算法显示名称 */
    private String algorithmName;
    /** 默认参数 JSON 字符串 */
    private String paramJson;
    /** 说明文案 */
    private String description;
    /** 启用状态等（与字典约定一致） */
    private String status;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
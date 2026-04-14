package com.ruoyi.agricultural.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 传感器/节点部署方案，对应表 {@code agri_deploy_scheme}。
 * <p>一次生成记录包含算法类型、节点数量、覆盖率与能量均衡等指标；{@code resultJson} 存算法输出的节点列表序列化结果，明细行见 {@link AgriDeployNode}。</p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AgriDeployScheme extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 方案主键 */
    private Long schemeId;
    /** 方案名称 */
    @Excel(name = "scheme_name")
    private String schemeName;
    /** 关联农田 ID */
    private Long farmlandId;
    /** 农田名称（联查冗余） */
    private String farmlandName;
    /** 使用的算法：coverage 覆盖贪心、cluster 聚类 */
    @Excel(name = "algorithm_type", readConverterExp = "coverage=Coverage,cluster=Cluster")
    private String algorithmType;
    /** 部署节点总数 */
    @Excel(name = "node_count")
    private Integer nodeCount;
    /** 覆盖率（百分比或0–100，与算法实现一致） */
    @Excel(name = "coverage_rate")
    private java.math.BigDecimal coverageRate;
    /** 能量均衡得分（越高越均衡，具体定义见聚类算法） */
    @Excel(name = "energy_balance")
    private java.math.BigDecimal energyBalance;
    /** 算法原始输出 JSON（节点列表等） */
    private String resultJson;
    /** 生成该方案的用户 ID */
    private Long userId;
    /** 业务状态：0 待确认 1 已确认 2 废弃 */
    @Excel(name = "status", readConverterExp = "0=pending,1=confirmed,2=deprecated")
    private String status;
}
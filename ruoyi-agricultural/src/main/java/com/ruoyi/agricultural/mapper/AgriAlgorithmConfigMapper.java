package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriAlgorithmConfig;
import java.util.List;

/**
 * 算法配置表访问，SQL 定义于 {@code AgriAlgorithmConfigMapper.xml}。
 */
public interface AgriAlgorithmConfigMapper {
    /** 条件列表查询 */
    List<AgriAlgorithmConfig> selectConfigList(AgriAlgorithmConfig config);
    /** 按算法类型唯一键查询默认参数等 */
    AgriAlgorithmConfig selectConfigByType(String algorithmType);
}
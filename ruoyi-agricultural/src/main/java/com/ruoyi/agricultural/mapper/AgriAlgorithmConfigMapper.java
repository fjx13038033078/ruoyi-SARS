package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriAlgorithmConfig;
import java.util.List;

public interface AgriAlgorithmConfigMapper {
    List<AgriAlgorithmConfig> selectConfigList(AgriAlgorithmConfig config);
    AgriAlgorithmConfig selectConfigByType(String algorithmType);
}
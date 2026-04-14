package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriEnvData;
import java.util.List;

public interface AgriEnvDataMapper {
    List<AgriEnvData> selectEnvDataList(AgriEnvData envData);
    AgriEnvData selectEnvDataById(Long dataId);
    int insertEnvData(AgriEnvData envData);
    int batchInsertEnvData(List<AgriEnvData> list);
    int deleteEnvDataById(Long dataId);
    int deleteEnvDataByIds(Long[] dataIds);
    int deleteEnvDataByFarmlandId(Long farmlandId);
}
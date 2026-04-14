package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriEnvData;
import java.util.List;

public interface IAgriEnvDataService {
    List<AgriEnvData> selectEnvDataList(AgriEnvData envData);
    AgriEnvData selectEnvDataById(Long dataId);
    int insertEnvData(AgriEnvData envData);
    int generateSimulatedData(Long farmlandId, int count, String createBy);
    int deleteEnvDataByIds(Long[] dataIds);
    int deleteEnvDataById(Long dataId);
}
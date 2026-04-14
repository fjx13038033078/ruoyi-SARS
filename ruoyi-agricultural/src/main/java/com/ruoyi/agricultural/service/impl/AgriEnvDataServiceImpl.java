package com.ruoyi.agricultural.service.impl;

import com.ruoyi.agricultural.domain.AgriEnvData;
import com.ruoyi.agricultural.mapper.AgriEnvDataMapper;
import com.ruoyi.agricultural.service.IAgriEnvDataService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 环境数据服务实现：单条插入写创建时间；{@link #generateSimulatedData} 在近 7 天时间窗内构造均匀步长并叠加随机抖动，各指标在预设物理区间内随机。
 */
@Service
public class AgriEnvDataServiceImpl implements IAgriEnvDataService {

    @Autowired
    private AgriEnvDataMapper envDataMapper;

    @Override
    public List<AgriEnvData> selectEnvDataList(AgriEnvData envData) {
        return envDataMapper.selectEnvDataList(envData);
    }
    @Override
    public AgriEnvData selectEnvDataById(Long dataId) {
        return envDataMapper.selectEnvDataById(dataId);
    }
    @Override
    public int insertEnvData(AgriEnvData envData) {
        envData.setCreateTime(DateUtils.getNowDate());
        return envDataMapper.insertEnvData(envData);
    }
    @Override
    public int generateSimulatedData(Long farmlandId, int count, String createBy) {
        Random random = new Random();
        List<AgriEnvData> dataList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        long startMs = cal.getTimeInMillis();
        long endMs = System.currentTimeMillis();
        long step = count > 1 ? (endMs - startMs) / (count - 1) : 0;
        for (int i = 0; i < count; i++) {
            AgriEnvData data = new AgriEnvData();
            data.setFarmlandId(farmlandId);
            data.setTemperature(rand(random, 15.0, 35.0));
            data.setHumidity(rand(random, 40.0, 90.0));
            data.setLightIntensity(rand(random, 2000.0, 80000.0));
            data.setSoilMoisture(rand(random, 20.0, 70.0));
            data.setWindSpeed(rand(random, 0.0, 10.0));
            data.setRecordTime(new Date(startMs + step * i + (long)(random.nextDouble() * 3600000)));
            data.setCreateBy(createBy);
            data.setCreateTime(DateUtils.getNowDate());
            dataList.add(data);
        }
        return envDataMapper.batchInsertEnvData(dataList);
    }

    /** 在 [{@code min},{@code max}] 上均匀采样并保留两位小数 */
    private BigDecimal rand(Random random, double min, double max) {
        double val = min + random.nextDouble() * (max - min);
        return BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_UP);
    }
    @Override
    public int deleteEnvDataByIds(Long[] dataIds) {
        return envDataMapper.deleteEnvDataByIds(dataIds);
    }
    @Override
    public int deleteEnvDataById(Long dataId) {
        return envDataMapper.deleteEnvDataById(dataId);
    }
}
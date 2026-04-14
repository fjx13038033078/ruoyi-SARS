package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriEnvData;
import java.util.List;

/**
 * 环境监测数据服务：查询、单条入库、按农田批量生成模拟数据、删除。
 */
public interface IAgriEnvDataService {
    List<AgriEnvData> selectEnvDataList(AgriEnvData envData);
    AgriEnvData selectEnvDataById(Long dataId);
    int insertEnvData(AgriEnvData envData);
    /**
     * 在指定农田下生成 {@code count} 条模拟环境数据（时间轴约 7 天内均匀加抖动，指标在业务区间内随机）。
     * @return 实际插入条数（批量插入返回值）
     */
    int generateSimulatedData(Long farmlandId, int count, String createBy);
    int deleteEnvDataByIds(Long[] dataIds);
    int deleteEnvDataById(Long dataId);
}
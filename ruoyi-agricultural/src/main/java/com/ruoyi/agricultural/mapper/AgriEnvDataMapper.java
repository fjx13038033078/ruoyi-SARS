package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriEnvData;
import java.util.List;

/**
 * 环境监测数据表访问，SQL 定义于 {@code AgriEnvDataMapper.xml}。
 */
public interface AgriEnvDataMapper {
    /** 条件查询环境数据列表 */
    List<AgriEnvData> selectEnvDataList(AgriEnvData envData);
    /** 按主键查询 */
    AgriEnvData selectEnvDataById(Long dataId);
    /** 单条插入 */
    int insertEnvData(AgriEnvData envData);
    /** 批量插入（模拟数据生成等场景） */
    int batchInsertEnvData(List<AgriEnvData> list);
    int deleteEnvDataById(Long dataId);
    int deleteEnvDataByIds(Long[] dataIds);
    /** 按农田清空/删除该农田下全部采样（以 XML 实现为准） */
    int deleteEnvDataByFarmlandId(Long farmlandId);
}
package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriFarmland;
import java.util.List;

/**
 * 农田表数据访问，SQL 定义于 {@code AgriFarmlandMapper.xml}。
 */
public interface AgriFarmlandMapper {
    /** 条件分页查询农田列表（非空字段参与动态 WHERE） */
    List<AgriFarmland> selectFarmlandList(AgriFarmland farmland);
    /** 按主键查询单条农田 */
    AgriFarmland selectFarmlandById(Long farmlandId);
    /** 插入农田 */
    int insertFarmland(AgriFarmland farmland);
    /** 按主键更新农田 */
    int updateFarmland(AgriFarmland farmland);
    /** 按主键物理/逻辑删除（以 XML 为准） */
    int deleteFarmlandById(Long farmlandId);
    /** 批量删除 */
    int deleteFarmlandByIds(Long[] farmlandIds);
}
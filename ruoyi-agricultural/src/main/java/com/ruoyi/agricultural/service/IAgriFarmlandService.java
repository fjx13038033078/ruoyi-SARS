package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriFarmland;
import java.util.List;

/**
 * 农田业务服务：列表、详情、增删改；写入时由实现类填充创建/更新时间。
 */
public interface IAgriFarmlandService {
    List<AgriFarmland> selectFarmlandList(AgriFarmland farmland);
    AgriFarmland selectFarmlandById(Long farmlandId);
    int insertFarmland(AgriFarmland farmland);
    int updateFarmland(AgriFarmland farmland);
    int deleteFarmlandByIds(Long[] farmlandIds);
    int deleteFarmlandById(Long farmlandId);
}
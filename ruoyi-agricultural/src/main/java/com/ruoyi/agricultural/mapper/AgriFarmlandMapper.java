package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriFarmland;
import java.util.List;

public interface AgriFarmlandMapper {
    List<AgriFarmland> selectFarmlandList(AgriFarmland farmland);
    AgriFarmland selectFarmlandById(Long farmlandId);
    int insertFarmland(AgriFarmland farmland);
    int updateFarmland(AgriFarmland farmland);
    int deleteFarmlandById(Long farmlandId);
    int deleteFarmlandByIds(Long[] farmlandIds);
}
package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriFarmland;
import java.util.List;

public interface IAgriFarmlandService {
    List<AgriFarmland> selectFarmlandList(AgriFarmland farmland);
    AgriFarmland selectFarmlandById(Long farmlandId);
    int insertFarmland(AgriFarmland farmland);
    int updateFarmland(AgriFarmland farmland);
    int deleteFarmlandByIds(Long[] farmlandIds);
    int deleteFarmlandById(Long farmlandId);
}
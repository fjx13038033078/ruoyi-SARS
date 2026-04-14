package com.ruoyi.agricultural.service.impl;

import com.ruoyi.agricultural.domain.AgriFarmland;
import com.ruoyi.agricultural.mapper.AgriFarmlandMapper;
import com.ruoyi.agricultural.service.IAgriFarmlandService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AgriFarmlandServiceImpl implements IAgriFarmlandService {

    @Autowired
    private AgriFarmlandMapper farmlandMapper;

    @Override
    public List<AgriFarmland> selectFarmlandList(AgriFarmland farmland) {
        return farmlandMapper.selectFarmlandList(farmland);
    }
    @Override
    public AgriFarmland selectFarmlandById(Long farmlandId) {
        return farmlandMapper.selectFarmlandById(farmlandId);
    }
    @Override
    public int insertFarmland(AgriFarmland farmland) {
        farmland.setCreateTime(DateUtils.getNowDate());
        return farmlandMapper.insertFarmland(farmland);
    }
    @Override
    public int updateFarmland(AgriFarmland farmland) {
        farmland.setUpdateTime(DateUtils.getNowDate());
        return farmlandMapper.updateFarmland(farmland);
    }
    @Override
    public int deleteFarmlandByIds(Long[] farmlandIds) {
        return farmlandMapper.deleteFarmlandByIds(farmlandIds);
    }
    @Override
    public int deleteFarmlandById(Long farmlandId) {
        return farmlandMapper.deleteFarmlandById(farmlandId);
    }
}
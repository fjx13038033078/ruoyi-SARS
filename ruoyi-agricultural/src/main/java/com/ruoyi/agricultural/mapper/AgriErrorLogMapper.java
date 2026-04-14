package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriErrorLog;
import java.util.List;

public interface AgriErrorLogMapper {
    List<AgriErrorLog> selectErrorLogList(AgriErrorLog errorLog);
    AgriErrorLog selectErrorLogById(Long errorId);
    int insertErrorLog(AgriErrorLog errorLog);
    int deleteErrorLogByIds(Long[] errorIds);
    int cleanErrorLog();
}
package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriErrorLog;
import java.util.List;

public interface IAgriErrorLogService {
    List<AgriErrorLog> selectErrorLogList(AgriErrorLog errorLog);
    AgriErrorLog selectErrorLogById(Long errorId);
    void recordError(String errorType, String errorMsg, String stackTrace,
                     String requestUrl, String requestParam, Long userId, String userName);
    int deleteErrorLogByIds(Long[] errorIds);
    int cleanErrorLog();
}
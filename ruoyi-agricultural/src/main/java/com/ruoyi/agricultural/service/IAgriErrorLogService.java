package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriErrorLog;
import java.util.List;

/**
 * 异常日志服务：查询、异步落库（实现类通常带 {@code @Async}）、批量删除与清空。
 */
public interface IAgriErrorLogService {
    List<AgriErrorLog> selectErrorLogList(AgriErrorLog errorLog);
    AgriErrorLog selectErrorLogById(Long errorId);
    /**
     * 记录异常；具体为同步还是异步由实现类决定（当前实现为异步且吞掉持久化异常以免反噬调用方）。
     */
    void recordError(String errorType, String errorMsg, String stackTrace,
                     String requestUrl, String requestParam, Long userId, String userName);
    int deleteErrorLogByIds(Long[] errorIds);
    int cleanErrorLog();
}
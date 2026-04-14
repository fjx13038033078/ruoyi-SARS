package com.ruoyi.agricultural.service.impl;

import com.ruoyi.agricultural.domain.AgriErrorLog;
import com.ruoyi.agricultural.mapper.AgriErrorLogMapper;
import com.ruoyi.agricultural.service.IAgriErrorLogService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 异常日志实现：{@link #recordError} 使用 {@code @Async} 异步入库，并对消息/堆栈/请求参数做长度截断，避免超大文本撑爆库表；持久化失败静默忽略。
 */
@Service
public class AgriErrorLogServiceImpl implements IAgriErrorLogService {
    @Autowired
    private AgriErrorLogMapper errorLogMapper;
    @Override
    public List<AgriErrorLog> selectErrorLogList(AgriErrorLog errorLog) { return errorLogMapper.selectErrorLogList(errorLog); }
    @Override
    public AgriErrorLog selectErrorLogById(Long errorId) { return errorLogMapper.selectErrorLogById(errorId); }
    @Override
    @Async
    public void recordError(String errorType, String errorMsg, String stackTrace,
                            String requestUrl, String requestParam, Long userId, String userName) {
        try {
            AgriErrorLog log = new AgriErrorLog();
            log.setErrorType(errorType);
            log.setErrorMsg(errorMsg != null && errorMsg.length() > 500 ? errorMsg.substring(0, 500) : errorMsg);
            log.setStackTrace(stackTrace != null && stackTrace.length() > 5000 ? stackTrace.substring(0, 5000) : stackTrace);
            log.setRequestUrl(requestUrl);
            log.setRequestParam(requestParam != null && requestParam.length() > 2000 ? requestParam.substring(0, 2000) : requestParam);
            log.setUserId(userId);
            log.setUserName(userName);
            log.setCreateTime(DateUtils.getNowDate());
            errorLogMapper.insertErrorLog(log);
        } catch (Exception ignored) {}
    }
    @Override
    public int deleteErrorLogByIds(Long[] errorIds) { return errorLogMapper.deleteErrorLogByIds(errorIds); }
    @Override
    public int cleanErrorLog() { return errorLogMapper.cleanErrorLog(); }
}
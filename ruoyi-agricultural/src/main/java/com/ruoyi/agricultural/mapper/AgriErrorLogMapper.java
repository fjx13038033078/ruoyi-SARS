package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriErrorLog;
import java.util.List;

/**
 * 农业模块异常日志表访问，SQL 定义于 {@code AgriErrorLogMapper.xml}。
 */
public interface AgriErrorLogMapper {
    List<AgriErrorLog> selectErrorLogList(AgriErrorLog errorLog);
    AgriErrorLog selectErrorLogById(Long errorId);
    int insertErrorLog(AgriErrorLog errorLog);
    int deleteErrorLogByIds(Long[] errorIds);
    /** 清空全部日志（运维用，具体语义见 XML） */
    int cleanErrorLog();
}
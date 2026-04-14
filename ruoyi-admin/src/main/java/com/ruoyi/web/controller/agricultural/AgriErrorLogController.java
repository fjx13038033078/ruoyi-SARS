package com.ruoyi.web.controller.agricultural;

import com.ruoyi.agricultural.domain.AgriErrorLog;
import com.ruoyi.agricultural.service.IAgriErrorLogService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Error Log Controller
 */
@RestController
@RequestMapping("/agricultural/errorlog")
public class AgriErrorLogController extends BaseController {

    @Autowired
    private IAgriErrorLogService errorLogService;

    @PreAuthorize("@ss.hasPermi('agricultural:errorlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriErrorLog errorLog) {
        startPage();
        List<AgriErrorLog> list = errorLogService.selectErrorLogList(errorLog);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('agricultural:errorlog:query')")
    @GetMapping("/{errorId}")
    public AjaxResult getInfo(@PathVariable Long errorId) {
        return success(errorLogService.selectErrorLogById(errorId));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:errorlog:query')")
    @Log(title = "ErrorLog", businessType = BusinessType.DELETE)
    @DeleteMapping("/{errorIds}")
    public AjaxResult remove(@PathVariable Long[] errorIds) {
        return toAjax(errorLogService.deleteErrorLogByIds(errorIds));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:errorlog:query')")
    @Log(title = "ErrorLog", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        errorLogService.cleanErrorLog();
        return success();
    }
}

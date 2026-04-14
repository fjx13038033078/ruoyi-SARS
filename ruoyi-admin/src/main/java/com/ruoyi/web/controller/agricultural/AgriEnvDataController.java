package com.ruoyi.web.controller.agricultural;

import com.ruoyi.agricultural.domain.AgriEnvData;
import com.ruoyi.agricultural.service.IAgriEnvDataService;
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
 * Environment Data Controller
 */
@RestController
@RequestMapping("/agricultural/envdata")
public class AgriEnvDataController extends BaseController {

    @Autowired
    private IAgriEnvDataService envDataService;

    @PreAuthorize("@ss.hasPermi('agricultural:envdata:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriEnvData envData) {
        startPage();
        List<AgriEnvData> list = envDataService.selectEnvDataList(envData);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('agricultural:envdata:query')")
    @GetMapping("/{dataId}")
    public AjaxResult getInfo(@PathVariable Long dataId) {
        return success(envDataService.selectEnvDataById(dataId));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:envdata:add')")
    @Log(title = "EnvData", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgriEnvData envData) {
        envData.setCreateBy(getUsername());
        return toAjax(envDataService.insertEnvData(envData));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:envdata:remove')")
    @Log(title = "EnvData", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dataIds}")
    public AjaxResult remove(@PathVariable Long[] dataIds) {
        return toAjax(envDataService.deleteEnvDataByIds(dataIds));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:envdata:generate')")
    @Log(title = "EnvData Generate", businessType = BusinessType.INSERT)
    @PostMapping("/generate")
    public AjaxResult generate(@RequestParam Long farmlandId,
                               @RequestParam(defaultValue = "50") int count) {
        if (count < 1 || count > 500) {
            return error("Count must be between 1 and 500");
        }
        int rows = envDataService.generateSimulatedData(farmlandId, count, getUsername());
        return success("Successfully generated " + rows + " records");
    }
}

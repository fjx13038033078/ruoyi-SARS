package com.ruoyi.web.controller.agricultural;

import com.ruoyi.agricultural.domain.AgriFarmland;
import com.ruoyi.agricultural.service.IAgriFarmlandService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Farmland Management Controller
 */
@RestController
@RequestMapping("/agricultural/farmland")
public class AgriFarmlandController extends BaseController {

    @Autowired
    private IAgriFarmlandService farmlandService;

    @PreAuthorize("@ss.hasPermi('agricultural:farmland:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriFarmland farmland) {
        startPage();
        List<AgriFarmland> list = farmlandService.selectFarmlandList(farmland);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('agricultural:farmland:query')")
    @GetMapping("/{farmlandId}")
    public AjaxResult getInfo(@PathVariable Long farmlandId) {
        return success(farmlandService.selectFarmlandById(farmlandId));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:farmland:add')")
    @Log(title = "Farmland", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgriFarmland farmland) {
        farmland.setCreateBy(getUsername());
        farmland.setUserId(SecurityUtils.getUserId());
        return toAjax(farmlandService.insertFarmland(farmland));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:farmland:edit')")
    @Log(title = "Farmland", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgriFarmland farmland) {
        farmland.setUpdateBy(getUsername());
        return toAjax(farmlandService.updateFarmland(farmland));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:farmland:remove')")
    @Log(title = "Farmland", businessType = BusinessType.DELETE)
    @DeleteMapping("/{farmlandIds}")
    public AjaxResult remove(@PathVariable Long[] farmlandIds) {
        return toAjax(farmlandService.deleteFarmlandByIds(farmlandIds));
    }

    /** Get farmland select list for current user */
    @GetMapping("/selectList")
    public AjaxResult selectList() {
        AgriFarmland farmland = new AgriFarmland();
        farmland.setUserId(SecurityUtils.getUserId());
        return success(farmlandService.selectFarmlandList(farmland));
    }
}

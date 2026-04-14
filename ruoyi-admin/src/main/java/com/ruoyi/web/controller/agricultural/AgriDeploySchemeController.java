package com.ruoyi.web.controller.agricultural;

import com.ruoyi.agricultural.domain.AgriDeployScheme;
import com.ruoyi.agricultural.service.IAgriDeploySchemeService;
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
 * Deploy Scheme Controller
 */
@RestController
@RequestMapping("/agricultural/deploy")
public class AgriDeploySchemeController extends BaseController {

    @Autowired
    private IAgriDeploySchemeService schemeService;

    @PreAuthorize("@ss.hasPermi('agricultural:deploy:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriDeployScheme scheme) {
        startPage();
        List<AgriDeployScheme> list = schemeService.selectSchemeList(scheme);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('agricultural:deploy:query')")
    @GetMapping("/{schemeId}")
    public AjaxResult getInfo(@PathVariable Long schemeId) {
        return success(schemeService.selectSchemeById(schemeId));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:deploy:query')")
    @GetMapping("/{schemeId}/nodes")
    public AjaxResult getNodes(@PathVariable Long schemeId) {
        return success(schemeService.selectNodesBySchemeId(schemeId));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:deploy:add')")
    @Log(title = "DeployScheme Generate", businessType = BusinessType.INSERT)
    @PostMapping("/generate")
    public AjaxResult generate(@RequestParam Long farmlandId,
                               @RequestParam String algorithmType) {
        Long schemeId = schemeService.generateScheme(farmlandId, algorithmType, getUsername(), SecurityUtils.getUserId());
        return AjaxResult.success("Scheme generated successfully", schemeId);
    }

    @PreAuthorize("@ss.hasPermi('agricultural:deploy:edit')")
    @Log(title = "DeployScheme", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgriDeployScheme scheme) {
        scheme.setUpdateBy(getUsername());
        return toAjax(schemeService.updateScheme(scheme));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:deploy:remove')")
    @Log(title = "DeployScheme", businessType = BusinessType.DELETE)
    @DeleteMapping("/{schemeIds}")
    public AjaxResult remove(@PathVariable Long[] schemeIds) {
        return toAjax(schemeService.deleteSchemeByIds(schemeIds));
    }
}

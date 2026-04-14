package com.ruoyi.web.controller.agricultural;

import com.ruoyi.agricultural.domain.AgriAlgorithmConfig;
import com.ruoyi.agricultural.mapper.AgriAlgorithmConfigMapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Algorithm Config Controller - read-only, returns enabled configs
 */
@RestController
@RequestMapping("/agricultural/algorithm")
public class AgriAlgorithmConfigController extends BaseController {

    @Autowired
    private AgriAlgorithmConfigMapper algorithmConfigMapper;

    @GetMapping("/list")
    public AjaxResult list() {
        AgriAlgorithmConfig config = new AgriAlgorithmConfig();
        config.setStatus("0");
        List<AgriAlgorithmConfig> list = algorithmConfigMapper.selectConfigList(config);
        return success(list);
    }
}

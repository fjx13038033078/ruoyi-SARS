package com.ruoyi.web.controller.agricultural;

import com.ruoyi.agricultural.domain.AgriComment;
import com.ruoyi.agricultural.service.IAgriCommentService;
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
 * User Comment Controller
 */
@RestController
@RequestMapping("/agricultural/comment")
public class AgriCommentController extends BaseController {

    @Autowired
    private IAgriCommentService commentService;

    @PreAuthorize("@ss.hasPermi('agricultural:comment:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriComment comment) {
        startPage();
        List<AgriComment> list = commentService.selectCommentList(comment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('agricultural:comment:query')")
    @GetMapping("/{commentId}")
    public AjaxResult getInfo(@PathVariable Long commentId) {
        return success(commentService.selectCommentById(commentId));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:comment:add')")
    @Log(title = "Comment", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgriComment comment) {
        comment.setUserId(SecurityUtils.getUserId());
        comment.setCreateBy(getUsername());
        if ("Y".equals(comment.getIsAnonymous())) {
            comment.setUserName("Anonymous");
        } else {
            if (comment.getUserName() == null || comment.getUserName().isEmpty()) {
                comment.setUserName(getUsername());
            }
        }
        return toAjax(commentService.insertComment(comment));
    }

    /** Admin: update comment visibility status */
    @PreAuthorize("@ss.hasPermi('agricultural:comment:edit')")
    @PutMapping("/{commentId}/status/{status}")
    public AjaxResult updateStatus(@PathVariable Long commentId, @PathVariable String status) {
        return toAjax(commentService.updateCommentStatus(commentId, status));
    }

    @PreAuthorize("@ss.hasPermi('agricultural:comment:remove')")
    @Log(title = "Comment", businessType = BusinessType.DELETE)
    @DeleteMapping("/{commentIds}")
    public AjaxResult remove(@PathVariable Long[] commentIds) {
        return toAjax(commentService.deleteCommentByIds(commentIds));
    }
}

package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriComment;
import java.util.List;

/**
 * 评论服务：列表、详情、新增（默认待审状态）、审核、批量删除。
 */
public interface IAgriCommentService {
    List<AgriComment> selectCommentList(AgriComment comment);
    AgriComment selectCommentById(Long commentId);
    int insertComment(AgriComment comment);
    int updateCommentStatus(Long commentId, String status);
    int deleteCommentByIds(Long[] commentIds);
}
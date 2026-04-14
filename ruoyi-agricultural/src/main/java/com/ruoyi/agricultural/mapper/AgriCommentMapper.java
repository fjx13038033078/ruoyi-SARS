package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriComment;
import java.util.List;

/**
 * 评论表访问，SQL 定义于 {@code AgriCommentMapper.xml}。
 */
public interface AgriCommentMapper {
    List<AgriComment> selectCommentList(AgriComment comment);
    AgriComment selectCommentById(Long commentId);
    int insertComment(AgriComment comment);
    /** 审核通过/驳回等状态更新 */
    int updateCommentStatus(Long commentId, String status);
    int deleteCommentById(Long commentId);
    int deleteCommentByIds(Long[] commentIds);
}
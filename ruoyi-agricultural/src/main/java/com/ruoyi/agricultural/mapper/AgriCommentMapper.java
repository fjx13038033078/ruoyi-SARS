package com.ruoyi.agricultural.mapper;

import com.ruoyi.agricultural.domain.AgriComment;
import java.util.List;

public interface AgriCommentMapper {
    List<AgriComment> selectCommentList(AgriComment comment);
    AgriComment selectCommentById(Long commentId);
    int insertComment(AgriComment comment);
    int updateCommentStatus(Long commentId, String status);
    int deleteCommentById(Long commentId);
    int deleteCommentByIds(Long[] commentIds);
}
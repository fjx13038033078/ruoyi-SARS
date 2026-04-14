package com.ruoyi.agricultural.service;

import com.ruoyi.agricultural.domain.AgriComment;
import java.util.List;

public interface IAgriCommentService {
    List<AgriComment> selectCommentList(AgriComment comment);
    AgriComment selectCommentById(Long commentId);
    int insertComment(AgriComment comment);
    int updateCommentStatus(Long commentId, String status);
    int deleteCommentByIds(Long[] commentIds);
}
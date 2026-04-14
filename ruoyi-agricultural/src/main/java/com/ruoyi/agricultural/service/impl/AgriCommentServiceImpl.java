package com.ruoyi.agricultural.service.impl;

import com.ruoyi.agricultural.domain.AgriComment;
import com.ruoyi.agricultural.mapper.AgriCommentMapper;
import com.ruoyi.agricultural.service.IAgriCommentService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 评论实现：新增时默认 {@code status=0}（待审）并写创建时间。
 */
@Service
public class AgriCommentServiceImpl implements IAgriCommentService {
    @Autowired
    private AgriCommentMapper commentMapper;
    @Override
    public List<AgriComment> selectCommentList(AgriComment comment) { return commentMapper.selectCommentList(comment); }
    @Override
    public AgriComment selectCommentById(Long commentId) { return commentMapper.selectCommentById(commentId); }
    @Override
    public int insertComment(AgriComment comment) {
        comment.setCreateTime(DateUtils.getNowDate());
        comment.setStatus("0");
        return commentMapper.insertComment(comment);
    }
    @Override
    public int updateCommentStatus(Long commentId, String status) { return commentMapper.updateCommentStatus(commentId, status); }
    @Override
    public int deleteCommentByIds(Long[] commentIds) { return commentMapper.deleteCommentByIds(commentIds); }
}
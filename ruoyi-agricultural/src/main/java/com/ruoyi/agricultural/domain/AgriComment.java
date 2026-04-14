package com.ruoyi.agricultural.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 通用评论实体，对应表 {@code agri_comment}。
 * <p>通过 {@code targetType} + {@code targetId} 关联农田、方案等业务对象；支持评分、匿名与审核状态。</p>
 */
@Data
public class AgriComment implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 评论主键 */
    private Long commentId;
    /** 评论正文 */
    private String content;
    /** 图片 URL 列表（多图可用分隔符拼接，与前端约定一致） */
    private String imageUrls;
    /** 评分1–5 等 */
    private Integer rating;
    /** 被评论对象类型（如 farmland、scheme） */
    private String targetType;
    /** 被评论对象主键 */
    private Long targetId;
    /** 是否匿名：Y/N */
    private String isAnonymous;
    private Long userId;
    private String userName;
    /** 审核状态等（如 0 待审 1 通过） */
    private String status;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
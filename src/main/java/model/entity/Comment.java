package model.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Comment extends BaseEntity {

    //这个类对应数据库中的comment表，为评论实体类

    //字段
    private Integer id;            // 主键ID
    private Integer contentId;    // 所属文档ID
    private Integer userId;        // 评论用户ID
    private String content;     // 评论内容
    private Integer parentId;      // 父评论ID，若为顶级评论则为0
}

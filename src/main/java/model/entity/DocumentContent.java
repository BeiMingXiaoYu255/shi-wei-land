package model.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DocumentContent extends BaseEntity {

    //这个类对应数据库中的document_content表，为文档内容实体类

    //字段
    private Long id;            // 主键ID
    private Long chapterId;     // 所属章节ID
    private Long title;         // 内容标题
    private String content;     // 章节内容
    private Integer sort;       // 排序字段
}

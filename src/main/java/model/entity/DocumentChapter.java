package model.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DocumentChapter extends BaseEntity {

    //这个类对应数据库中的document_chapter表，为文档章节实体类

    //字段
    private Integer id;            // 主键ID
    private Integer categoryId;          //所属分类ID
    private String title;       // 章节标题
    private String content;     // 章节简介
    private Integer sort;       // 排序字段
}

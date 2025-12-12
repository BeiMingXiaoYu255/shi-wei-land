package model.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DocumentCategory extends BaseEntity {

    //这个类对应数据库中的document_category表，为文档分类实体类

    //字段
    private Integer id;            // 主键ID
    private String name;        // 分类名称
    private String description; // 分类描述
    private Integer sort;       // 排序字段
}

package model.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BaseEntity {
    //以下为通用字段

    private Long createTime; // 创建时间
    private Long updateTime; // 更新时间
    private Integer status;  // 状态，0-禁用，1-启用
}

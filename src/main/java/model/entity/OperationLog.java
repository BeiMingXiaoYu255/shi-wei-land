package model.entity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class OperationLog extends BaseEntity {

    //这个类对应数据库中的operation_log表，为操作日志实体类

    //字段
    private Integer id;             // 主键ID
    private Integer userId;         // 操作用户ID
    private Integer operation;      // 操作内容
    private String content;         // 具体内容
    private String ip;              // 操作IP地址
    private String model;           // 操作模块
    private String operTime;        // 操作时间
}

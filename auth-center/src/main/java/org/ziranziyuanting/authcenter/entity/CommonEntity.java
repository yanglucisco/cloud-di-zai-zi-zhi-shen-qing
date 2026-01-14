package org.ziranziyuanting.authcenter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 *     通用基础字段实体：创建时间、创建人、修改时间、修改人，需要此通用字段的实体可继承此类，
 *     继承此类要求数据表有对应的字段
 * </p>
 *
 */
@Getter
@Setter
public abstract class CommonEntity {
    private Long id;
    /** 删除标志 */
    @JsonIgnore
    private String deleteFlag = "NOT_DELETED";

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 创建人 */
    private Long createUser;

    /** 更新时间 */
   private LocalDateTime updateTime;

    /** 更新人 */
    private Long updateUser;
}


package org.ziranziyuanting.common.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

/**
 * <p>
 *     通用基础字段实体：创建时间、创建人、修改时间、修改人，需要此通用字段的实体可继承此类，
 *     继承此类要求数据表有对应的字段
 * </p>
 *
 */
@Getter
@Setter
public abstract class CommonEntity implements Persistable<Long> {
    @Id
    private Long id;

    @Transient
    private boolean isNew = true;
    /** 删除标志 */
    private String deleteFlag = "NOT_DELETED";

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 创建人 */
    private Long createUser;

    /** 更新时间 */
   private LocalDateTime updateTime;

    /** 更新人 */
    private Long updateUser;

    @Override
    @Nullable
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}



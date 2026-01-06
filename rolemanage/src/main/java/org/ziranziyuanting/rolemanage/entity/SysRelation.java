package org.ziranziyuanting.rolemanage.entity;

import org.ziranziyuanting.common.entity.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class SysRelation extends CommonEntity {
    private Long objectId;
    private Long targetId;
    private String category;
    private String extJson;
}

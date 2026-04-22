package org.ziranziyuanting.account.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysDictVO {
    private Long id;
    private Long parentId;
    private String dictValue;
    private String dictLabel;
    private List<SysDictVO> children;
}

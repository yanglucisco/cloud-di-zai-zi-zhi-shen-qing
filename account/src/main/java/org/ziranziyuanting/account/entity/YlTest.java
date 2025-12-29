package org.ziranziyuanting.account.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("yl_test")
public class YlTest {
    @Id
    private String id;
    private String name;
}

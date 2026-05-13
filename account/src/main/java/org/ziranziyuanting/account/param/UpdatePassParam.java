package org.ziranziyuanting.account.param;

import lombok.Data;

@Data
public class UpdatePassParam {
    private Long userId;
    private String password;
}

package org.ziranziyuanting.account.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUserPageVO {
    private String id;
    private String avatar;
    private String account;
    private String name;
    private String gender;
    private String nickname;
    private String phone;
    private String orgId;
    private String orgName;
    private String userStatus;
}

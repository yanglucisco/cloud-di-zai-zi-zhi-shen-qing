package org.ziranziyuanting.account.param;

import lombok.Data;

@Data
public class SysUserUpdateParam {
    private Long id;
    private String gender;
    private String name;
    private String nickName;
    private String email;
    private String mobil;
    private String birthday;
    private String avatar;
}
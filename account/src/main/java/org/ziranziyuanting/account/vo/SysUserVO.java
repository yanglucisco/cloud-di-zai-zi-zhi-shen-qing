package org.ziranziyuanting.account.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUserVO {
    private String account;
    private String name;
    private String mobil;
    private String nickName;
    private String gender;
    private String birthday;
    private String email;
}

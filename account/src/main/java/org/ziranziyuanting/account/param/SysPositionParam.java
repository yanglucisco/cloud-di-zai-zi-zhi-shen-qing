package org.ziranziyuanting.account.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysPositionParam {
    /* 
    {
  "createTime": null,
  "createUser": null,
  "createUserName": null,
  "updateTime": null,
  "updateUser": null,
  "updateUserName": null,
  "id": "1543899639134019583",
  "orgId": "1543842934270394368",
  "name": "董事长",
  "code": null,
  "category": "HIGH",
  "sortCode": 1,
  "extJson": null
} */
    @NotNull(message = "orgId不能为空")
    private Long orgId;
    @NotBlank(message = "名称不能为空")
    private String name;
    private String code;
    @NotBlank(message = "分类不能为空")
    private String category;
    @NotNull(message = "排序码不能为空")
    private Integer sortCode;
    private String extJson;
}

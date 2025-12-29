package org.ziranziyuanting.account.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddOrgParam {
    /*
    {
  "sortCode": 99,
  "parentId": 0,
  "name": "测试组织",
  "category": "COMPANY"
}
    */
   @NotNull(message =  "sortCode不能为空")
   private int sortCode;
   @NotNull(message =  "parentId不能为空")
   private int parentId;
   @NotBlank(message =  "name不能为空")
   private String name;
   @NotBlank(message =  "类别不能为空")
   private String category;
}

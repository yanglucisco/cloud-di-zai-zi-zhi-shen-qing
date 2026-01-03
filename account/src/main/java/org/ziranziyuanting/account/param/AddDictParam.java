package org.ziranziyuanting.account.param;

import lombok.Data;

@Data
public class AddDictParam {
    /*
    {
  "type": "颜色",
  "code": "RED",
  "name": "红色",
  "sortCode": 1,
  "parentId": 0
}
    */
   private Long id;
   private String type;
   private String code;
   private String name;
   private int sortCode;
   private Long parentId;
}

package org.ziranziyuanting.account.param;

import java.util.List;

import lombok.Data;

@Data
public class DeleteUserParam {
    private List<Long> ids;
}

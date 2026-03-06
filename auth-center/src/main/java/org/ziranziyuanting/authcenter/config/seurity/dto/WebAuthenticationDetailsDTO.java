package org.ziranziyuanting.authcenter.config.seurity.dto;

import lombok.Data;

@Data
public class WebAuthenticationDetailsDTO {
    private String remoteAddress;
    private String sessionId;
}

package org.ziranziyuanting.authcenter.service;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.ziranziyuanting.authcenter.entity.RegisteredClientEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RegisteredClientService extends IService<RegisteredClientEntity> {

    org.springframework.security.oauth2.server.authorization.client.RegisteredClient findByClientId(String clientId);

    RegisteredClient findById(String id);
    
}

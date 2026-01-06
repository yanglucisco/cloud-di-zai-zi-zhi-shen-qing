package org.ziranziyuanting.rolemanage.service;

import java.util.List;

import org.ziranziyuanting.common.service.CommonService;
import org.ziranziyuanting.rolemanage.entity.SysRelation;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.param.SysRoleShouQuanCaiDanParam;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysRelationService extends CommonService<SysRelation>
{
    Mono<String> shouQuanCaiDan(SysRoleShouQuanCaiDanParam param);
}

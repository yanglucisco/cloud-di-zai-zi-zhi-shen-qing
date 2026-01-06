package org.ziranziyuanting.rolemanage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;
import org.ziranziyuanting.rolemanage.entity.SysRelation;
import org.ziranziyuanting.rolemanage.param.SysRoleShouQuanCaiDan;
import org.ziranziyuanting.rolemanage.param.SysRoleShouQuanCaiDanParam;
import org.ziranziyuanting.rolemanage.respository.SysRelationRepository;
import org.ziranziyuanting.rolemanage.service.SysRelationService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysRelationServiceImpl extends CommonServiceImpl<SysRelation> implements SysRelationService {
    public SysRelationServiceImpl(SysRelationRepository repository){
        super(repository);
    }

    @Override
    public Mono<String> shouQuanCaiDan(SysRoleShouQuanCaiDanParam param) {
        List<SysRelation> sysRelations = new ArrayList<>();
        for (SysRoleShouQuanCaiDan sysRoleShouQuanCaiDan : param.getSysRoleShouQuanCaiDans()) {
            SysRelation menuRelation = SysRelation.builder().objectId(param.getRoleId())
            .targetId(sysRoleShouQuanCaiDan.getId())
            .build();
            sysRelations.add(menuRelation);
            for (Long btnId : sysRoleShouQuanCaiDan.getBtnIds()) {
                SysRelation btnRelation = SysRelation.builder().objectId(param.getRoleId())
                .targetId(btnId).build();
                sysRelations.add(btnRelation);
            }
        }
        var r = saveOrUpdateAll(sysRelations).map(s -> "").collect(Collectors.joining(","));
        return r;
    }
}

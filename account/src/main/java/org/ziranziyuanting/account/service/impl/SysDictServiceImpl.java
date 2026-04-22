package org.ziranziyuanting.account.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.SysDict;
import org.ziranziyuanting.account.repository.SysDictRepository;
import org.ziranziyuanting.account.service.SysDictService;
import org.ziranziyuanting.account.vo.SysDictVO;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysDictServiceImpl extends CommonServiceImpl<SysDict> implements SysDictService {
    public SysDictServiceImpl(SysDictRepository repository) {
        super(repository);
    }

    @Override
    public Mono<SysDictVO> findAllVO() {
        Mono<SysDictVO> r = repository.findAll().collectList().flatMap(dos -> {
            var vos = dos.stream().map(doItem -> BeanUtil.toBean(doItem, SysDictVO.class)).toList();
            var root = SysDictVO.builder().id(0l).dictLabel("root").dictValue("root").build();
            buildTreeDataDO(root, vos);
            return Mono.just(root);
        });
        return r;
    }
    private void buildTreeDataDO(SysDictVO dictDataDO, List<SysDictVO> all) {
        var children = getChildren(dictDataDO, all);
        // SysDictVO vo = SysDictVO.builder().value(dictDataDO.getValue()).build();
        dictDataDO.setChildren(children);
        for(var item : children){
            buildTreeDataDO(item, all);
        }
    }
    private List<SysDictVO> getChildren(SysDictVO parent, List<SysDictVO> all){
        List<SysDictVO> children = all.stream().filter(child -> {
                if(child.getParentId() == null){
                    return false;
                }
                return child.getParentId().equals(parent.getId());
            }).toList();
        return children;
    }
}

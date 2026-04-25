package org.ziranziyuanting.account.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.param.PageParam;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.service.SysOrgService;
import org.ziranziyuanting.account.vo.SysOrgVO;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@CacheConfig(cacheNames = "userCache")
public class SysOrgServiceImpl extends CommonServiceImpl<SysOrg> implements SysOrgService {
    private final SysOrgRepository sysOrgRepository;
    public SysOrgServiceImpl(SysOrgRepository repository, SysOrgRepository sysOrgRepository) {
        super(repository);
        this.sysOrgRepository = sysOrgRepository;
    }

    @Override
    public Mono<SysOrg> save(AddOrgParam parm) {
        SysOrg org = SysOrg.of(parm.getSortCode(), parm.getParentId(), parm.getName(), parm.getCategory());
        return saveOrUpdate(org);
    }

    @Override
    public Mono<SysOrg> update(AddOrgParam parm) {
        return findById(parm.getId()).flatMap(item -> {
            item.setSortCode(parm.getSortCode());
            item.setParentId(parm.getParentId());
            item.setName(parm.getName());
            item.setCategory(parm.getCategory());
            return saveOrUpdate(item);
        });
    }

    @Override
    public void test() {
        SysOrg org = SysOrg.of(1, 0L, "测试组织" + LocalDateTime.now(), "测试类别");
        saveOrUpdate(org).flatMap(item -> {
            Mono<SysOrg> r = findById(item.getId());
            return r;
        }).flatMap(item -> {
            item.setUpdateTime(LocalDateTime.now());
            return saveOrUpdate(item);
        }).subscribe(item -> {
            log.info("测试完成: {}", item);
        });
    }

    @Override
    public Flux<SysOrgVO> orgTree() {
        return repository.findAll().collectList().flatMapMany(orgs -> {
            List<SysOrgVO> vos = orgs.stream().map(doItem -> SysOrgVO.builder().title(doItem.getName())
                    .key(doItem.getCode())
                    .label(doItem.getName())
                    .value(doItem.getId().toString())
                    .id(doItem.getId() + "")
                    .parentId(doItem.getParentId().toString())
                    .build()).toList();
            SysOrgVO root = SysOrgVO.builder().id("0").parentId("-1")
                    .title("root").key("root")
                    .label("root").value("root").build();
            buildTreeDataDO(root, vos);
            return Flux.fromIterable(root.getChildren());
        });
    }

    private void buildTreeDataDO(SysOrgVO dictDataDO, List<SysOrgVO> all) {
        var children = getChildren(dictDataDO, all);
        // SysDictVO vo = SysDictVO.builder().value(dictDataDO.getValue()).build();
        dictDataDO.setChildren(children);
        for (var item : children) {
            buildTreeDataDO(item, all);
        }
    }

    private List<SysOrgVO> getChildren(SysOrgVO parent, List<SysOrgVO> all) {
        List<SysOrgVO> children = all.stream().filter(child -> {
            if (child.getParentId() == null) {
                return false;
            }
            return child.getParentId().equals(parent.getId());
        }).toList();
        return children;
    }

    @Override
    public Flux<SysOrg> findOrgsByPage(PageParam pageParam) {
        PageRequest pageRequest = PageRequest.of(pageParam.getPage(), pageParam.getSize());
        
        // Use the custom method defined in the repository
        return sysOrgRepository.findAllBy(pageRequest);
    }

    @Override
    public Mono<Long> countOrgs() {
        return repository.count();
    }
}
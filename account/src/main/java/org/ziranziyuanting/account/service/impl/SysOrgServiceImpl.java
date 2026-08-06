package org.ziranziyuanting.account.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.SysDict;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.param.PageParam;
import org.ziranziyuanting.account.repository.SysDictRepository;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.service.SysOrgService;
import org.ziranziyuanting.account.vo.SysOrgTreeNodeVO;
import org.ziranziyuanting.account.vo.SysOrgVO;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@CacheConfig(cacheNames = "userCache")
public class SysOrgServiceImpl extends CommonServiceImpl<SysOrg> implements SysOrgService {
    private final SysOrgRepository sysOrgRepository;
    private final SysDictRepository sysDictRepository;

    public SysOrgServiceImpl(SysOrgRepository repository, SysOrgRepository sysOrgRepository, SysDictRepository sysDictRepository) {
        super(repository);
        this.sysOrgRepository = sysOrgRepository;
        this.sysDictRepository = sysDictRepository;
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
            if(ObjUtil.isNotNull(parm.getParentId())){
                item.setParentId(parm.getParentId());
            }
            else
            {
                item.setParentId(null);
            }
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
    public Flux<SysOrgTreeNodeVO> orgTree() {
        return sysOrgRepository.findAllNoDelete().collectList().flatMapMany(orgs -> {
            List<SysOrgTreeNodeVO> vos = orgs.stream().map(doItem -> {
                return SysOrgTreeNodeVO.builder().title(doItem.getName())
                    .key(doItem.getId().toString())
                    .label(doItem.getName())
                    .value(doItem.getId().toString())
                    .id(doItem.getId() + "")
                    .parentId(getOrgParentId(doItem))
                    .build();}).toList();
            SysOrgTreeNodeVO root = SysOrgTreeNodeVO.builder().id("0").parentId("-1")
                    .title("root").key("root")
                    .label("root").value("root").build();
            buildTreeDataDO(root, vos);
            return Flux.fromIterable(root.getChildren());
        });
    }
    private String getOrgParentId(SysOrg org){
        var r = ObjUtil.isNull(org.getParentId())?"":org.getParentId().toString();
        return r;
    }
    private void buildTreeDataDO(SysOrgTreeNodeVO dictDataDO, List<SysOrgTreeNodeVO> all) {
        var children = getChildren(dictDataDO, all);
        // SysDictVO vo = SysDictVO.builder().value(dictDataDO.getValue()).build();
        dictDataDO.setChildren(children);
        for (var item : children) {
            buildTreeDataDO(item, all);
        }
    }

    private List<SysOrgTreeNodeVO> getChildren(SysOrgTreeNodeVO parent, List<SysOrgTreeNodeVO> all) {
        List<SysOrgTreeNodeVO> children = all.stream().filter(child -> {
            if (child.getParentId() == null) {
                return false;
            }
            return child.getParentId().equals(parent.getId());
        }).toList();
        return children;
    }

        @Override
    public Flux<SysOrgVO> findOrgsByPage(PageParam pageParam) {
        PageRequest pageRequest = PageRequest.of(pageParam.getPage(), pageParam.getPageSize());
        Mono<Map<String, String>> dictMono = sysDictRepository.findByType("ORG_TYPE")
        .collectMap(dict -> dict.getDictValue(), dict -> dict.getDictLabel())
        .cache();

        Mono<List<SysOrg>> orgMono = sysOrgRepository.findByNameContainingAndPage(pageParam.getName(), pageParam.getParentId() ,pageRequest)
        .collectList();

        Mono<List<SysOrgVO>> resultMono = Mono.zip(dictMono, orgMono)
        .map(tuple -> {
            Map<String, String> dictMap = tuple.getT1();
            List<SysOrg> orgs = tuple.getT2();
            List<SysOrgVO> orgVos = new ArrayList<SysOrgVO>();
            orgs.forEach(org -> {
                String label = dictMap.get(org.getCategory());
                org.setCategory(label);
                var vo = SysOrgVO.builder()
                        .id(org.getId().toString())  
                        .key(org.getId().toString())     // Convert Long to String
                        .parentId(getOrgParentId(org)) // Convert Long to String
                        .name(org.getName())
                        .code(org.getCode())
                        .sortCode(org.getSortCode())
                        .category(org.getCategory())
                        .createTime(org.getCreateTime())
                        .updateTime(org.getUpdateTime())
                        .build();
                orgVos.add(vo);
            });
            return orgVos;
        });
        var r = resultMono.flatMapMany(Flux::fromIterable);
        return r;
    }

    @Override
    public Mono<Long> countOrgs() {
        // For general count, pass null or empty string to get all
        return sysOrgRepository.countByName(null, -1L);
    }
    @Override
    // Helper method if you want specific count by name in controller
    public Mono<Long> countOrgsByName(String name, Long parentId) {
        return sysOrgRepository.countByName(name, parentId);
    }
    @Override
     /**
     * Batch logically delete organizations.
     * @param ids The list of organization IDs to delete.
     * @return A Mono indicating success.
     */
    public Mono<String> logicalDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Mono.just("没有需要删除的机构");
        }
        
        return sysOrgRepository.logicalDeleteByIds(ids)
                .map(updatedRows -> "成功删除 " + updatedRows + " 个机构");
    }
}
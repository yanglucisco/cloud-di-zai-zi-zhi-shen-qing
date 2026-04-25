package org.ziranziyuanting.account.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.common.repository.CommonReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysOrgRepository extends CommonReactiveCrudRepository<SysOrg>
//  extends ReactiveCrudRepository<SysOrg, Long>
 {
    /**
     * Find all organizations with pagination.
     * @param pageable Contains page number and size information.
     * @return Flux of SysOrg entities.
     */
    @Query("SELECT * FROM sys_org ORDER BY id LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<SysOrg> findAllBy(Pageable pageable);

    @Query("SELECT COUNT(*) FROM sys_org WHERE (:name IS NULL OR :name = '' OR name LIKE CONCAT('%', :name, '%')) AND (:parentId IS NULL OR parent_id = :parentId)")
    Mono<Long> countByName(String name, Long parentId);

    /**
     * Find organizations by name with pagination.
     * @param name The name to search for (can be null or empty for all).
     * @param pageable Pagination information.
     * @return Flux of SysOrg entities.
     */
    @Query("SELECT * FROM sys_org WHERE (:name IS NULL OR :name = '' OR name LIKE CONCAT('%', :name, '%')) AND (:parentId IS NULL OR parent_id = :parentId) ORDER BY id LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<SysOrg> findByNameContainingAndPage(String name, Long parentId, Pageable pageable);
}

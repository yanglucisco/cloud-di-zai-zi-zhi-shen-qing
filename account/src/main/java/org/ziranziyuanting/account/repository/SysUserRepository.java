package org.ziranziyuanting.account.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.ziranziyuanting.account.entity.SysUser;
import org.ziranziyuanting.common.repository.CommonReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysUserRepository extends CommonReactiveCrudRepository<SysUser>
{
    Mono<SysUser> findByAccount(String account);

    /**
     * 分页查询用户，支持关键词模糊搜索、状态筛选、组织筛选
     */
    @Query("SELECT * FROM sys_user WHERE delete_flag = 'NOT_DELETED' AND (:keyword IS NULL OR :keyword = '' OR account LIKE CONCAT('%', :keyword, '%') OR name LIKE CONCAT('%', :keyword, '%')) AND (:status IS NULL OR :status = '' OR user_status = :status) AND (:orgId IS NULL OR org_id = :orgId) ORDER BY id LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<SysUser> findUsersByPage(String keyword, String status, Long orgId, Pageable pageable);

    /**
     * 统计用户数量
     */
    @Query("SELECT COUNT(*) FROM sys_user WHERE delete_flag = 'NOT_DELETED' AND (:keyword IS NULL OR :keyword = '' OR account LIKE CONCAT('%', :keyword, '%') OR name LIKE CONCAT('%', :keyword, '%')) AND (:status IS NULL OR :status = '' OR user_status = :status) AND (:orgId IS NULL OR org_id = :orgId)")
    Mono<Long> countUsers(String keyword, String status, Long orgId);
}

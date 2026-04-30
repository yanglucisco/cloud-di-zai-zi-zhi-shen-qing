package org.ziranziyuanting.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.param.DeleteOrgParam;
import org.ziranziyuanting.account.param.PageParam;
import org.ziranziyuanting.account.service.SysOrgService;
import org.ziranziyuanting.account.vo.SysOrgTreeNodeVO;
import org.ziranziyuanting.common.commonminio.MinioService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("org")
@Validated
public class SysOrgController {
    private final SysOrgService service;
    private final MinioService minioService;
    public SysOrgController(SysOrgService sysOrgService, MinioService minioService)
    {
        this.service = sysOrgService;
        this.minioService = minioService;
    }

    @GetMapping("all")
    public ResponseEntity<Flux<SysOrg>> all() {
        return ResponseEntity.ok(service.findAll());
    }
    @PostMapping("add")
    public ResponseEntity<Mono<String>> add(@Valid @RequestBody AddOrgParam addOrgParam, Authentication authentication) {
        return ResponseEntity.ok(service.save(addOrgParam).map(s -> "新增机构成功"));
    }
    /**
     * Update organization
     */
    @PostMapping("update")
    public ResponseEntity<Mono<String>> update(@Valid @RequestBody AddOrgParam addOrgParam) {
        return ResponseEntity.ok(service.update(addOrgParam).map(s -> "更新机构成功"));
    }
    @GetMapping("test")
    public String test() {
        service.test();
        return LocalDateTime.now().toString();
    }
    @GetMapping("orgTree")
    public ResponseEntity<Flux<SysOrgTreeNodeVO>> orgTree() {
        return ResponseEntity.ok(service.orgTree());
    }
    @GetMapping("page")
    public ResponseEntity<Mono<Map<String, Object>>> getOrgsByPage(@Valid PageParam pageParam) {
        // Adjust page index for database (0-based)
        int dbPage = pageParam.getPage() - 1;
        pageParam.setPage(dbPage < 0 ? 0 : dbPage);
        String name = pageParam.getName();
        Mono<Map<String, Object>> result = Mono.zip(
            service.findOrgsByPage(pageParam).collectList(),
            service.countOrgsByName(name, pageParam.getParentId()) // Use filtered count
        ).map(tuple -> {
            Map<String, Object> map = new HashMap<>();
            map.put("list", tuple.getT1());
            map.put("total", tuple.getT2());
            // Return original 1-based page number to frontend
            map.put("page", pageParam.getPage() + 1); 
            map.put("size", pageParam.getPageSize());
            return map;
        });

        return ResponseEntity.ok(result);
    }
     @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Define bucket and object name
            String bucketName = "di-zai-user-manage";
            // Generate a unique object name to avoid conflicts, e.g., using UUID or timestamp
            String originalFilename = file.getOriginalFilename();
            String objectName = System.currentTimeMillis() + "_" + originalFilename;
            
            // 2. Get Content Type
            String contentType = file.getContentType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 3. Get InputStream from MultipartFile
            InputStream inputStream = file.getInputStream();

            // 4. Call the uploadFile method
            minioService.uploadFile(bucketName, objectName, inputStream, contentType);

            return "File uploaded successfully: " + objectName;
        } catch (Exception e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }
    /**
     * Logically delete an organization.
     * @param id The ID of the organization to delete.
     * @return Result message.
     */
    @PostMapping("delete")
    public ResponseEntity<Mono<String>> delete(@RequestBody DeleteOrgParam ids) {
        return ResponseEntity.ok(service.logicalDelete(ids.getIds()));
    }
}

package org.ziranziyuanting.account.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.ziranziyuanting.account.AccountApp;
import org.ziranziyuanting.account.entity.SysOrg;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(classes = AccountApp.class, webEnvironment = WebEnvironment.NONE)
public class SysOrgRepositoryTest {
    @Autowired
    private SysOrgRepository sysOrgRepository;
    // @Test
    public void testSave(){
        // SysOrg sysOrg = SysOrg.of(1, 0, "测试组织", "测试类别");
        // sysOrg.setId("2");
        // sysOrgRepository.save(sysOrg);
    }
}

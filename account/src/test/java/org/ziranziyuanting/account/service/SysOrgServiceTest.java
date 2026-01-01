package org.ziranziyuanting.account.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.ziranziyuanting.account.AccountApp;

@SpringBootTest(classes = AccountApp.class, webEnvironment = WebEnvironment.NONE)
public class SysOrgServiceTest {
    // @Autowired
    // private SysOrgService sysOrgService;
    // @Test
    // public void saveTest(){
    //     SysOrg sysOrg = SysOrg.of(1, 0, "测试组织", "测试类别");
    //     sysOrgService.save(null);
    // }
}

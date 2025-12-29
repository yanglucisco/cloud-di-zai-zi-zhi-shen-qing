package org.ziranziyuanting.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.service.SysOrgService;

@WebFluxTest(SysOrgController.class)
public class SysOrgControllerTest {
    @Autowired
	private WebTestClient webClient;

	@MockBean
	private SysOrgService sysOrgService;

    // @Test
	void whenBookNotAvailableThenRejectOrder() {
		webClient
				.get()
				.uri("/org/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SysOrg.class)
                .value(response -> {
                    // assertThat(response.getId()).isEqualTo(1L);
                    // assertThat(response.getUsername()).isEqualTo("testuser");
            });
	}
}

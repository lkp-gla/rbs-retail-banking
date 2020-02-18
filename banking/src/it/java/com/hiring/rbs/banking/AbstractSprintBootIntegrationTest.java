package com.hiring.rbs.banking;

import com.jayway.restassured.RestAssured;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = BankingServiceCucumber.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                BankingApplication.class
        }
)
@ActiveProfiles(profiles = {"integration"})
@Ignore
public class AbstractSprintBootIntegrationTest {
    @LocalServerPort
    private void setServerPort(final int serverPort) {
        RestAssured.port = serverPort;
    }
}

package org.identifiers.cloud.hq.validatorregistry.configurations;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;


@TestConfiguration
@SpringBootConfiguration
@ComponentScan("org.identifiers.cloud.hq.validatorregistry")
@EnableAutoConfiguration
class TestApplicationConfiguration {

}
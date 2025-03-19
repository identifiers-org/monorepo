package org.identifiers.cloud.hq.validatorregistry;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.hq.validatorregistry.curation.CurationEngine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@SpringBootApplication
@RequiredArgsConstructor
public class RegistryValidatorCommandLineRunner implements CommandLineRunner {
    private final CurationEngine curationEngine;
    private final ConfigurableApplicationContext ctx;

    @Override
    public void run(String... args) throws Exception {
        curationEngine.execute();
        ctx.close();
    }
}

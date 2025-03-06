package org.identifiers.cloud.hq.validatorregistry;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.hq.validatorregistry.curation.CurationEngine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class RegistryValidatorCommandLineRunner implements CommandLineRunner {
    private final CurationEngine curationEngine;

    @Override
    public void run(String... args) throws Exception {
        curationEngine.execute();
    }
}

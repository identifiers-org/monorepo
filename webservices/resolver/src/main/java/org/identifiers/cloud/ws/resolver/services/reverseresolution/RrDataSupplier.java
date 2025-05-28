package org.identifiers.cloud.ws.resolver.services.reverseresolution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import org.identifiers.cloud.ws.resolver.data.repositories.NamespaceRespository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RrDataSupplier implements Supplier<List<RrResourceData>> {
    private final NamespaceRespository namespaceRepository;

    @Override
    public List<RrResourceData> get() {
        log.debug("Retrieving namespaces from repository for reverse resolution");
        return StreamSupport.stream(namespaceRepository.findAll().spliterator(), false)
                .flatMap(n -> RrResourceData.from(n).stream())
                .toList();
    }
}

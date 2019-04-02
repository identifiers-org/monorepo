package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.data.models.Namespace;
import org.identifiers.cloud.ws.resolver.data.models.Resource;
import org.identifiers.cloud.ws.resolver.data.repositories.NamespaceRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-01-27 9:02
 * ---
 */
@Component
@Scope("prototype")
public class ResolverDataFetcherFromDataBackend implements ResolverDataFetcher {
    private static Logger logger = LoggerFactory.getLogger(ResolverDataFetcherFromDataBackend.class);

    @Autowired
    private NamespaceRespository namespaceRespository;

    @Override
    public List<Resource> findResourcesByPrefix(String prefix) {
        logger.info("Find resources by prefix for '{}'", prefix);
        if (prefix != null) {
            Namespace namespace = namespaceRespository.findByPrefix(prefix);
            if (namespace != null) {
                return namespace.getResources();
            } else {
                logger.warn("NO PID entry for prefix '{}'", prefix);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public Iterable<Namespace> findAllNamespaces() {
        return namespaceRespository.findAll();
    }
}

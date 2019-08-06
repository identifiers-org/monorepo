package org.identifiers.cloud.ws.resourcerecommender.api.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.ws.resourcerecommender.api.ApiCentral;
import org.identifiers.cloud.ws.resourcerecommender.api.requests.ServiceRequestRecommend;
import org.identifiers.cloud.ws.resourcerecommender.api.responses.ResponseRecommendPayload;
import org.identifiers.cloud.ws.resourcerecommender.api.responses.ServiceResponseRecommend;
import org.identifiers.cloud.ws.resourcerecommender.models.RecommendationStrategy;
import org.identifiers.cloud.ws.resourcerecommender.api.data.models.ResolvedResource;
import org.identifiers.cloud.ws.resourcerecommender.models.ResourceRecommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:04
 * ---
 */
@Component
@Scope("prototype")
public class ResourceRecommenderApiModel {
    private static Logger logger = LoggerFactory.getLogger(ResourceRecommenderApiModel.class);

    @Autowired
    private RecommendationStrategy recommendationStrategy;

    private List<ResourceRecommendation> evaluateRecommendations(List<ResolvedResource> resolvedResources) {
        try {
            return recommendationStrategy.getRecommendations(resolvedResources);
        } catch (RuntimeException e) {
            ObjectMapper mapper = new ObjectMapper();
            String resolvedResourcesJsonDump = "---";
            try {
                 resolvedResourcesJsonDump = mapper.writeValueAsString(resolvedResources);
            } catch (JsonProcessingException ex) {
                // Ignore
            }
            logger.error("Recommendation ERROR '{}' " +
                            "when evaluating the following Resolved Resources '{}'",
                    e.getMessage(),
                    resolvedResourcesJsonDump);
            return new ArrayList<>();
        }
    }

    public ServiceResponseRecommend getRecommendations(ServiceRequestRecommend request) {
        // NOTE - I know, I should not use try-catch as an if-else block, but in this case, this logic is sooo simple...
        // TODO - This is where we check the API version?
        // TODO - check payload as well?
        ServiceResponseRecommend response = new ServiceResponseRecommend();
        response.setApiVersion(ApiCentral.apiVersion);
        // Set default payload
        response.setPayload(new ResponseRecommendPayload().setResourceRecommendations(new ArrayList<>()));
        try {
            response.getPayload()
                    .setResourceRecommendations(evaluateRecommendations(request.getPayload().getResolvedResources()));
        } catch (RuntimeException e) {
            logger.error("The following ERROR occurred while trying to evaluate resolved resources recommendations: " +
                    "'{}'", e.getMessage());
            response.setErrorMessage("An error occurred while trying to evaluate the recommendations " +
                    "for the given resolved resources").setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}

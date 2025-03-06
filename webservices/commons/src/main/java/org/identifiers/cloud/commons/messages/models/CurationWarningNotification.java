package org.identifiers.cloud.commons.messages.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CurationWarningNotification implements Serializable {
    @Serial
    private static final long serialVersionUID = 8233497873563092422L;

    /**
     * One of the registry entities (Namespace, resource, or Institution).
     */
    String targetType;

    /**
     * The identifier of the entity that the notification targets.
     */
    long targetId;

    /**
     * A unique identifier among all warnings but equal to correlated warnings.
     */
    String globalId;

    /**
     *
     * An identifier for the type of warning this is. It identifies the data contained in the details.
     */
    String type;

    /**
     * Further data on the warning.
     */
    Map<String, String> details;
}

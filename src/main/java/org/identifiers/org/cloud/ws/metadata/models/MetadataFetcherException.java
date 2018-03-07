package org.identifiers.org.cloud.ws.metadata.models;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-08 11:59
 * ---
 */
public class MetadataFetcherException extends RuntimeException {
    public enum ErrorCode {
        METADATA_NOT_FOUND(0, "Metadata NOT FOUND");

        private final int errorCode;
        private final String errorDescription;

        ErrorCode(int errorCode, String errorDescription) {
            this.errorCode = errorCode;
            this.errorDescription = errorDescription;
        }
    }

    private ErrorCode errorCode;

    public MetadataFetcherException(String message) {
        super(message);
    }

    public MetadataFetcherException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

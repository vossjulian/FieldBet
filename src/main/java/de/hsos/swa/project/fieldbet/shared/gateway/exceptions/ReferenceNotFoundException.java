package de.hsos.swa.project.fieldbet.shared.gateway.exceptions;

/**
 * ReferenceNotFoundException
 * 
 * @author Patrick Felschen
 */
public class ReferenceNotFoundException extends RuntimeException {
    private String entityName;
    private String entityId;
    private String message;

    public ReferenceNotFoundException(String entityName, String entityId, String message) {
        this.entityName = entityName;
        this.entityId = entityId;
        this.message = message;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

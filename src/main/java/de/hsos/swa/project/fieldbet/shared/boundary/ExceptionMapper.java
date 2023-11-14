package de.hsos.swa.project.fieldbet.shared.boundary;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import de.hsos.swa.project.fieldbet.shared.control.ErrorDTO;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityCreationException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityDeleteException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityQueryException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityReadException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityUpdateException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.OperationNotAllowedException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.ReferenceNotFoundException;

/**
 * ExceptionMapper
 * 
 * - Es werden auftretende Exceptions in strukturierte ErrorDTO Objekte
 * umgewandelt
 * 
 * @author Patrick Felschen, Julian Voss
 */
public class ExceptionMapper {
    @ServerExceptionMapper
    public Response mapException(EntityCreationException e) {
        ErrorDTO errorDTO = new ErrorDTO(
                "error",
                Status.BAD_REQUEST.getStatusCode(),
                e.getEntityName(),
                "EntityCreationException",
                e.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(errorDTO).build();
    }

    @ServerExceptionMapper
    public Response mapException(EntityUpdateException e) {
        ErrorDTO errorDTO = new ErrorDTO(
                "error",
                Status.BAD_REQUEST.getStatusCode(),
                e.getEntityName() + " id: " + e.getEntityId(),
                "EntityUpdateException",
                e.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(errorDTO).build();
    }

    @ServerExceptionMapper
    public Response mapException(EntityDeleteException e) {
        ErrorDTO errorDTO = new ErrorDTO(
                "error",
                Status.BAD_REQUEST.getStatusCode(),
                e.getEntityName() + " id: " + e.getEntityId(),
                "EntityDeleteException",
                e.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(errorDTO).build();
    }

    @ServerExceptionMapper
    public Response mapException(EntityReadException e) {
        ErrorDTO errorDTO = new ErrorDTO(
                "error",
                Status.BAD_REQUEST.getStatusCode(),
                e.getEntityName() + " id: " + e.getEntityId(),
                "EntityReadException",
                e.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(errorDTO).build();
    }

    @ServerExceptionMapper
    public Response mapException(EntityQueryException e) {
        ErrorDTO errorDTO = new ErrorDTO(
                "error",
                Status.BAD_REQUEST.getStatusCode(),
                e.getEntityName(),
                "EntityQueryException",
                e.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(errorDTO).build();
    }

    @ServerExceptionMapper
    public Response mapException(ReferenceNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO(
                "error",
                Status.BAD_REQUEST.getStatusCode(),
                e.getEntityName() + " id: " + e.getEntityId(),
                "ReferenceNotFoundException",
                e.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(errorDTO).build();
    }

    @ServerExceptionMapper
    public Response mapException(OperationNotAllowedException e) {
        ErrorDTO errorDTO = new ErrorDTO(
                "error",
                Status.BAD_REQUEST.getStatusCode(),
                e.getEntityName() + " id: " + e.getEntityId(),
                "OperationNotAllowedException",
                e.getMessage());
        return Response.status(Status.BAD_REQUEST).entity(errorDTO).build();
    }
}

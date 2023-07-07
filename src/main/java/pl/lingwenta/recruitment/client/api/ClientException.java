package pl.lingwenta.recruitment.client.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ClientException extends ResponseStatusException {

    public ClientException(String message, HttpStatusCode httpStatus) {
        super(httpStatus, message);
    }

    public static ClientException repositoryNotFound(String owner, String repo) {
        return new ClientException("Repository with name " + repo + " for owner " + owner + " was not found",
                HttpStatus.NOT_FOUND);
    }

    public static ClientException invalidToken() {
        return new ClientException("Authorization token is not valid", HttpStatus.FORBIDDEN);
    }

    public static ClientException tooManyRequests() {
        return new ClientException("Too many requests, try later", HttpStatus.TOO_MANY_REQUESTS);
    }

    public static ClientException unknownClientError(HttpStatusCode status) {
        return new ClientException("Unknown client error with status " + status.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

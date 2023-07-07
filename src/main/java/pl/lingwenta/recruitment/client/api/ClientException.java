package pl.lingwenta.recruitment.client.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ClientException extends ResponseStatusException {

    public ClientException(String message, HttpStatusCode httpStatus) {
        super(httpStatus, message);
    }

    public static ClientException clientError(HttpStatusCode status) {
        return new ClientException("Unknown client error with status " + status.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

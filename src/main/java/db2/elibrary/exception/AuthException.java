package db2.elibrary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuthException extends RuntimeException {
    private static final long serialVersionUID = 5396833917140801098L;

    public AuthException(String message) {
        super(message);
    }
}

package pl.pacinho.MasterBet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pacinho.MasterBet.utils.MediaResponseEntityUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiErrorController implements ErrorController {

    @Autowired
    private MediaResponseEntityUtils mediaResponseEntityUtils;

    @RequestMapping("/error")
    public ResponseEntity handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            String fileName = statusCode == HttpStatus.NOT_FOUND.value() ? "404.gif"
                    : statusCode == HttpStatus.FORBIDDEN.value() ? "403.gif"
                    : statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value() ? "500.gif" : null;
            if (fileName != null) {
                return mediaResponseEntityUtils.get(fileName, MediaType.IMAGE_GIF);
            }
        }
        return null;
    }
}
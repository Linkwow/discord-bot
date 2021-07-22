package ua.projects.discordbot.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
class GlobalDefaultExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ValidationException.class)
    public ModelAndView checkValidation(HttpServletRequest req, Exception e) {
        return getModelAndView(req, e, 400);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ModelAndView checkContains(HttpServletRequest req, Exception e) {
        return getModelAndView(req, e, 404);
    }

    private ModelAndView getModelAndView(HttpServletRequest req, Exception e, int code) {
        ModelAndView errorView = new ModelAndView();
        errorView.addObject("exception", e.getMessage());
        errorView.addObject("url", req.getRequestURL());
        errorView.addObject("status", "Bad Request");
        errorView.addObject("status-code", code);
        errorView.setViewName("error");
        return errorView;
    }
}

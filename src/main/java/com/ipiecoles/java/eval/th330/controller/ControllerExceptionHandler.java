package com.ipiecoles.java.eval.th330.controller;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private Integer CODE_NOT_FOUND = 404;
    private Integer CODE_CONFLICT = 409;
    private Integer CODE_BAD_REQUEST = 400;

    // Entité non trouvé
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", CODE_NOT_FOUND);
        modelAndView.addObject("errorMessage", e.getMessage());
        return modelAndView;
    }

    // Entité déjà existante
    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleEntityExistsException(EntityExistsException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", CODE_CONFLICT);
        modelAndView.addObject("errorMessage", e.getMessage());
        return modelAndView;
    }

    // Type incorrect
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", CODE_BAD_REQUEST);
        modelAndView.addObject("errorMessage", "La valeur du paramètre " + e.getName() + " est incorrecte");
        return modelAndView;
    }

    // Argument incorrect
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", CODE_BAD_REQUEST);
        modelAndView.addObject("errorMessage", e.getMessage());
        return modelAndView;
    }

    // Paramètre manquant
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", CODE_BAD_REQUEST);
        modelAndView.addObject("errorMessage", "Le paramètre " + e.getParameterName() + " n'est pas présent et est obligatoire.");
        return modelAndView;
    }

    // Propriété d'un model non trouvée
    @ExceptionHandler(PropertyReferenceException.class)
    public ModelAndView handlePropertyReferenceException(PropertyReferenceException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", CODE_BAD_REQUEST);
        modelAndView.addObject("errorMessage", e.getMessage() );
        return modelAndView;
    }
}
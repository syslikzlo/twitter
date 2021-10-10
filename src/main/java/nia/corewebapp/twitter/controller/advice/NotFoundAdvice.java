package nia.corewebapp.twitter.controller.advice;

import nia.corewebapp.twitter.controller.PostController;
import nia.corewebapp.twitter.controller.UserController;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;


@ControllerAdvice(assignableTypes = {PostController.class,
                                     UserController.class})
public class NotFoundAdvice {


    @ExceptionHandler({EntityNotFoundException.class,
                        NoSuchElementException.class,
                        UsernameNotFoundException.class})
    public String handleEntityNotFoundException(){
        return "not-found";
    }
}

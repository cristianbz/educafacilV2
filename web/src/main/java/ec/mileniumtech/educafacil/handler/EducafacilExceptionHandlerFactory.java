package ec.mileniumtech.educafacil.handler;

import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;

/**
 * Factoría para el manejador global de excepciones Educafacil.
 * 
 * @author [ Christian Baez ] christian
 */
public class EducafacilExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory parent;

    public EducafacilExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new EducafacilExceptionHandler(parent.getExceptionHandler());
    }
}

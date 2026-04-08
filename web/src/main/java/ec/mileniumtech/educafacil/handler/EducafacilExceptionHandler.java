package ec.mileniumtech.educafacil.handler;

import java.util.Iterator;
import java.util.Map;

import ec.mileniumtech.educafacil.dao.excepciones.BusinessException;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.utilitario.Mensaje;
import jakarta.faces.FacesException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Manejador global de excepciones para JSF.
 * Captura excepciones no manejadas, las loguea y muestra un mensaje al usuario.
 * 
 * @author [ Christian Baez ] christian
 */
@Slf4j
public class EducafacilExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public EducafacilExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();

        while (events.hasNext()) {
            ExceptionQueuedEvent event = events.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();

            FacesContext fc = FacesContext.getCurrentInstance();

            try {
                // Obtener la causa raíz o la excepción de negocio envuelta
                Throwable cause = findCause(t);

                if (cause instanceof BusinessException) {
                    BusinessException be = (BusinessException) cause;
                    handleBusinessException(fc, be);
                } else if (cause instanceof DaoException) {
                    handleDaoException(fc, (DaoException) cause);
                } else {
                    handleGenericException(fc, t);
                }

                fc.renderResponse();
            } finally {
                events.remove();
            }
        }
        getWrapped().handle();
    }

    private Throwable findCause(Throwable t) {
        if (t instanceof BusinessException || t instanceof DaoException) {
            return t;
        }
        if (t.getCause() != null && t.getCause() != t) {
            return findCause(t.getCause());
        }
        return t;
    }

    private void handleBusinessException(FacesContext fc, BusinessException be) {
        String summary = "Aviso";
        String detail = be.getMessage();
        
        // Si tiene una clave de mensaje, se podría usar resources bundles aquí.
        Mensaje.verMensaje(FacesMessage.SEVERITY_WARN, summary, detail);
        log.warn("BusinessException: {}", detail);
    }

    private void handleDaoException(FacesContext fc, DaoException de) {
        Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "Error de Persistencia", "Ha ocurrido un error técnico. Por favor, intente más tarde.");
        log.error("DaoException: ", de);
    }

    private void handleGenericException(FacesContext fc, Throwable t) {
        Mensaje.verMensaje(FacesMessage.SEVERITY_FATAL, "Error Crítico", "Error no esperado en el sistema.");
        log.error("Generic Error: ", t);
    }
}

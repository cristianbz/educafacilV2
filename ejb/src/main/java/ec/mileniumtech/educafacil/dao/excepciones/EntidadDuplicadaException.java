package ec.mileniumtech.educafacil.dao.excepciones;

/**
 * @author [ Christian Baez ]cbaez
 *
 */
public class EntidadDuplicadaException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public EntidadDuplicadaException(String message) {
		super(message);
	}

	public EntidadDuplicadaException(Throwable cause) {
		super("La entidad ya existe en el sistema.", cause);
	}

	public EntidadDuplicadaException(String message, Throwable cause) {
		super(message, cause);
	}
}


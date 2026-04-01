/**
 * Este software esta protegido por derechos de autor CEIMSCAP
 */
package ec.mileniumtech.educafacil.dao.excepciones;

import lombok.Getter;

/**
 * Excepción base para errores de lógica de negocio o validaciones.
 * Es una RuntimeException para facilitar el manejo global y evitar ruidos en la firma de métodos.
 * 
 * @author [ Christian Baez ] christian
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	@Getter
	private final String messageKey;

	/**
	 * Constructor con mensaje directo.
	 * @param message
	 */
	public BusinessException(String message) {
		super(message);
		this.messageKey = null;
	}

	/**
	 * Constructor con clave de mensaje para traducción y causa.
	 * @param messageKey Clave para el archivo de propiedades de mensajes.
	 * @param cause Causa original de la excepción.
	 */
	public BusinessException(String messageKey, Throwable cause) {
		super(cause);
		this.messageKey = messageKey;
	}
	
	/**
	 * Constructor con solo clave de mensaje.
	 * @param messageKey Clave para el archivo de propiedades de mensajes.
	 * @param isKey Indica si es una clave de mensaje para lookup.
	 */
	public BusinessException(String messageKey, boolean isKey) {
		super(messageKey);
		this.messageKey = isKey ? messageKey : null;
	}
	
	/**
	 * Constructor con mensaje y causa.
	 * @param message
	 * @param cause
	 */
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		this.messageKey = null;
	}
}

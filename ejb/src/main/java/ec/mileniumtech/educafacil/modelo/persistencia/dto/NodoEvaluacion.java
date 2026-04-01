package ec.mileniumtech.educafacil.modelo.persistencia.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Christian Baez
 *
 */
public class NodoEvaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Getter
    @Setter
    private String nombre;
    @Getter
    @Setter
    private String curso;
    @Getter
    @Setter
    private String evaluacionCursoId;
    @Getter
    @Setter
    private String tipoEncuestaId;
    @Getter
    @Setter
    private String matriculaId;
    @Getter
    @Setter
    private String evaRealizadas;
    @Getter
    @Setter
    private String ofertaCursoId;
}

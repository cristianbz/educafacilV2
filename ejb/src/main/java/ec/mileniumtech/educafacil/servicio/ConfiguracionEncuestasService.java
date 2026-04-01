package ec.mileniumtech.educafacil.servicio;

import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.dao.CategoriaRespuestaDao;
import ec.mileniumtech.educafacil.dao.ObjetoEvaluacionDao;
import ec.mileniumtech.educafacil.dao.PreguntaDao;
import ec.mileniumtech.educafacil.dao.RespuestasDao;
import ec.mileniumtech.educafacil.dao.TipoEncuestaDao;
import ec.mileniumtech.educafacil.dao.TipoEncuestaPreguntaDao;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.CategoriaRespuesta;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.ObjetoEvaluacion;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Pregunta;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Respuestas;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.TipoEncuesta;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.TipoEncuestaPregunta;

/**
 * Servicio para encapsular la logica de negocio y transaccionalidad de las Encuestas y Configuración.
 */
@Stateless
public class ConfiguracionEncuestasService {

    @EJB
    private ObjetoEvaluacionDao objetoEvaluacionDao;
    @EJB
    private TipoEncuestaDao tipoEncuestaDao;
    @EJB
    private CategoriaRespuestaDao categoriaRespuestaDao;
    @EJB
    private RespuestasDao respuestasDao;
    @EJB
    private PreguntaDao preguntaDao;
    @EJB
    private TipoEncuestaPreguntaDao tipoEncuestaPreguntaDao;

    /**
     * Guarda un Objeto de Evaluacion verificando duplicados
     */
    public void guardarObjetoEvaluacion(ObjetoEvaluacion objeto, List<ObjetoEvaluacion> existentes, String nombreOriginal) throws EntidadDuplicadaException, DaoException, Exception {
        boolean existe = false;
        for (ObjetoEvaluacion oe : existentes) {
            if (oe.getObjeNombre().toUpperCase().equals(objeto.getObjeNombre().toUpperCase())) {
                existe = true;
                break;
            }
        }
        
        if (objeto.getObjeId() == null && existe) {
            throw new EntidadDuplicadaException("El elemento ya existe.");
        } else if (objeto.getObjeId() != null && existe && !objeto.getObjeNombre().equalsIgnoreCase(nombreOriginal)) {
            throw new EntidadDuplicadaException("El elemento ya existe.");
        } else {
            objeto.setObjeNombre(objeto.getObjeNombre().toUpperCase());
            objetoEvaluacionDao.actualizarObjetoEvaluacion(objeto);
        }
    }

    /**
     * Guarda un Tipo de Encuesta verificando duplicados
     */
    public void guardarTipoEncuesta(TipoEncuesta tipoEncuesta, int idObjeto, List<TipoEncuesta> existentes, String nombreOriginal) throws EntidadDuplicadaException, DaoException, Exception {
        ObjetoEvaluacion objEv = new ObjetoEvaluacion();
        objEv.setObjeId(idObjeto);
        tipoEncuesta.setObjetoEvaluacion(objEv);

        boolean existe = false;
        for (TipoEncuesta te : existentes) {
            if (te.getTipeDescripcion().toUpperCase().equals(tipoEncuesta.getTipeDescripcion().toUpperCase()) 
                && idObjeto == te.getObjetoEvaluacion().getObjeId()) {
                existe = true;
                break;
            }
        }
        
        if (tipoEncuesta.getTipeId() == null && existe) {
            throw new EntidadDuplicadaException("El tipo de encuesta ya existe.");
        } else if (tipoEncuesta.getTipeId() != null && existe && !tipoEncuesta.getTipeDescripcion().equalsIgnoreCase(nombreOriginal)) {
            throw new EntidadDuplicadaException("El tipo de encuesta ya existe.");
        } else {
            tipoEncuesta.setTipeDescripcion(tipoEncuesta.getTipeDescripcion().toUpperCase());
            tipoEncuestaDao.actualizarTipoEncuesta(tipoEncuesta);
        }
    }

    /**
     * Guarda una Categoría de Respuesta y todas sus Respuestas en una sola transacción
     */
    public void grabarRespuestas(CategoriaRespuesta categoria, List<Respuestas> listaRespuestas, List<CategoriaRespuesta> existentes, String nombreOriginal) throws EntidadDuplicadaException, DaoException, Exception {
        if (listaRespuestas == null || listaRespuestas.isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos una respuesta.");
        }

        boolean existe = false;
        for (CategoriaRespuesta cr : existentes) {
            if (cr.getCatrNombre().toUpperCase().equals(categoria.getCatrNombre().toUpperCase())) {
                existe = true;
                break;
            }
        }
        
        if (categoria.getCatrId() == null && existe) {
            throw new EntidadDuplicadaException("La categoría ya existe.");
        } else if (categoria.getCatrId() != null && existe && !categoria.getCatrNombre().equalsIgnoreCase(nombreOriginal)) {
            throw new EntidadDuplicadaException("La categoría ya existe.");
        }

        categoria.setCatrNombre(categoria.getCatrNombre().toUpperCase());
        categoriaRespuestaDao.actualizarCategoriaRespuesta(categoria);
        
        for (Respuestas respuesta : listaRespuestas) {
            if (respuesta.getRespId() == null) {
                respuesta.setCategoriaRespuesta(categoria);
            }
            respuestasDao.agregActualizarRespuestas(respuesta);
        }
    }

    /**
     * Vincula Preguntas seleccionadas a un Tipo de Encuesta, verificando duplicados.
     * Retorna null si es exitoso, o las preguntas duplicadas en caso de haberlas.
     */
    public String guardarTipoEncuestaPregunta(TipoEncuesta tipoEncuesta, List<Pregunta> preguntasSelec) throws DaoException, EntidadDuplicadaException, Exception {
        List<TipoEncuestaPregunta> listatemp = tipoEncuestaPreguntaDao.listaPorTipoDeEncuestas(tipoEncuesta.getTipeId());
        boolean pregExistente = false;
        StringBuilder preguntasDuplicadas = new StringBuilder();

        for (TipoEncuestaPregunta tep : listatemp) {
            for (Pregunta pregunta : preguntasSelec) {
                if (pregunta.getPregId().equals(tep.getPregunta().getPregId())) {
                    pregExistente = true;
                    preguntasDuplicadas.append(" ").append(pregunta.getPregDescripcion());
                    break;
                }
            }
        }
        
        if (!pregExistente) {
            for (Pregunta pregunta : preguntasSelec) {
                TipoEncuestaPregunta tpe = new TipoEncuestaPregunta();
                tpe.setPregunta(pregunta);
                tpe.setTipoEncuesta(tipoEncuesta);
                tpe.setTeprEstado(true);
                tipoEncuestaPreguntaDao.agregarActualizarTipoEncuestaPregunta(tpe);
            }
            return null;
        } else {
            return preguntasDuplicadas.toString().trim();
        }
    }

    /**
     * Elimina el enlace entre Tipo Encuesta y Pregunta (baja lógica).
     */
    public void eliminarTipoEncuestaPregunta(TipoEncuestaPregunta tep) throws DaoException, EntidadDuplicadaException, Exception {
        tep.setTeprEstado(false);
        tipoEncuestaPreguntaDao.agregarActualizarTipoEncuestaPregunta(tep);
    }

    /**
     * Elimina una Respuesta (baja lógica).
     */
    public void eliminarRespuesta(Respuestas respuesta) throws DaoException, Exception {
        respuesta.setRespEstado(false);
        respuestasDao.agregActualizarRespuestas(respuesta);
    }
}

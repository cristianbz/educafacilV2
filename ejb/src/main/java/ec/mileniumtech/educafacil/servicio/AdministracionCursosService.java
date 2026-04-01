package ec.mileniumtech.educafacil.servicio;

import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.dao.EvaluacionCursoDao;
import ec.mileniumtech.educafacil.dao.OfertaCapacitacionDao;
import ec.mileniumtech.educafacil.dao.OfertaCursosDao;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.EvaluacionCurso;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Instructor;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCapacitacion;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCursos;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumEstadosOfertaCurso;

/**
 * Servicio para encapsular la logica transaccional de Administracion de Cursos
 */
@Stateless
public class AdministracionCursosService {

    @EJB
    private OfertaCursosDao ofertaCursosDao;
    @EJB
    private OfertaCapacitacionDao ofertaCapacitacionDao;
    @EJB
    private EvaluacionCursoDao evaluacionCursoDao;

    /**
     * Prepara el grafo de dominios y graba la Oferta del Curso
     */
    public void grabarCurso(OfertaCursos ofertaCursos, int codigoArea, int codigoEspecialidad, int codigoCurso, int codigoInstructor, String codigoTipoCurso, boolean isAnularCurso) throws DaoException, EntidadDuplicadaException {
        OfertaCapacitacion ofertaCapacitacion = ofertaCapacitacionDao.buscarOfertaCapacitacion(codigoArea, codigoEspecialidad, codigoCurso);
        
        Instructor instructor = new Instructor();
        instructor.setInstId(codigoInstructor);
        
        ofertaCursos.setInstructor(instructor);
        ofertaCursos.setOcurTipo(codigoTipoCurso);
        ofertaCursos.setOfertaCapacitacion(ofertaCapacitacion);
        
        if (isAnularCurso) {
            ofertaCursos.setOcurEstado(EnumEstadosOfertaCurso.ANULADO.getCodigo());
        } else {
            ofertaCursos.setOcurEstado(EnumEstadosOfertaCurso.INICIADO.getCodigo());
        }
        
        if (ofertaCursos.getOcurId() > 0) {
            ofertaCursosDao.editarOfertaCursos(ofertaCursos);
        } else {
            ofertaCursosDao.agregarOfertaCursos(ofertaCursos);
        }
    }

    /**
     * Guarda multiples evaluaciones en una sola transaccion
     */
    public void guardarEvaluacionesCurso(List<EvaluacionCurso> evaluaciones) throws Exception {
        if (evaluaciones != null && !evaluaciones.isEmpty()) {
            for (EvaluacionCurso evaluacionCurso : evaluaciones) {
                evaluacionCursoDao.agregarEvaluacionCurso(evaluacionCurso);
            }
        }
    }

    /**
     * Aplica la baja logica de la evaluacion
     */
    public void eliminarEvaluacionCurso(EvaluacionCurso evaluacionCurso) throws Exception {
        evaluacionCurso.setEvcuEstado(false);
        evaluacionCursoDao.agregarEvaluacionCurso(evaluacionCurso);
    }
}

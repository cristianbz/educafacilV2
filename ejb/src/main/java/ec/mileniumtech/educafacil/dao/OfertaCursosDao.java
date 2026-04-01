package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Matricula;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCursos;

public interface OfertaCursosDao extends GenericoDao<OfertaCursos, Long> {
    List<OfertaCursos> listaCursosDisponibles(int ofertaCapacitacion) throws DaoException;
    void agregarOfertaCursos(OfertaCursos ofertaCursos) throws DaoException, EntidadDuplicadaException;
    OfertaCursos editarOfertaCursos(OfertaCursos ofertaCursos) throws DaoException, EntidadDuplicadaException;
    List<OfertaCursos> listaOfertaCursosActivos() throws DaoException;
    List<OfertaCursos> listaOfertaCursosActivosCerrados() throws DaoException;
    void finalizarCursoActivo(OfertaCursos ofertaCurso, List<Matricula> matriculas) throws DaoException, EntidadDuplicadaException;
    List<OfertaCursos> listaOfertaCursosPorDefecto() throws DaoException;
    List<OfertaCursos> listaOfertaCursosPorCurso(int codigoCurso) throws DaoException;
    List<OfertaCursos> listaOfertaCursosPorCursoAnio(int codigoCurso, int anio) throws DaoException;
}

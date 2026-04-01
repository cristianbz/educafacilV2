package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Curso;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Especialidad;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCapacitacion;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCursos;

public interface OfertaCapacitacionDao extends GenericoDao<OfertaCapacitacion, Long> {
    OfertaCapacitacion buscarOfertaCapacitacion(int area, int especialidad, int curso) throws DaoException;
    List<Especialidad> listaEspecialidadPorArea(int area) throws DaoException;
    List<Curso> listaCursosPorAreaEspecilidad(int area, int especialidad) throws DaoException;
    OfertaCapacitacion buscarPorCurso(int codigoCurso) throws DaoException;
    List<OfertaCapacitacion> listarOfertasCapacitacion() throws DaoException;
    void agregarOfertaCapacitacion(OfertaCapacitacion ofertaCapacitacion, OfertaCursos ofertaCursos) throws DaoException, EntidadDuplicadaException;
}

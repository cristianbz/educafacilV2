package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Matricula;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCursos;

public interface OfertaCursosDao extends GenericoDao<OfertaCursos, Long> {
    List<OfertaCursos> listaCursosDisponibles(int ofertaCapacitacion);
    void agregarOfertaCursos(OfertaCursos ofertaCursos);
    OfertaCursos editarOfertaCursos(OfertaCursos ofertaCursos);
    List<OfertaCursos> listaOfertaCursosActivos();
    List<OfertaCursos> listaOfertaCursosActivosCerrados();
    void finalizarCursoActivo(OfertaCursos ofertaCurso, List<Matricula> matriculas);
    List<OfertaCursos> listaOfertaCursosPorDefecto();
    List<OfertaCursos> listaOfertaCursosPorCurso(int codigoCurso);
    List<OfertaCursos> listaOfertaCursosPorCursoAnio(int codigoCurso, int anio);
}


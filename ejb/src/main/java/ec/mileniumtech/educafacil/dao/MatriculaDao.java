package ec.mileniumtech.educafacil.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.dto.DtoMatriculasCurso;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Matricula;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Persona;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Usuario;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.UsuarioRol;

public interface MatriculaDao extends GenericoDao<Matricula, Long> {
    void agregarMatriculaInscripcion(Persona persona, Matricula matricula, Usuario usuario, UsuarioRol usuarioRol) throws DaoException, EntidadDuplicadaException;
    List<Matricula> listaMatriculasAlumno(int codigoPersona, String codigoEstado) throws DaoException;
    List<Matricula> listaMatriculasInscripcion(String estado, Date fechaInicio, Date fechaFin) throws DaoException;
    List<Matricula> listaMatriculasCurso(String estado, int codigoCurso) throws DaoException;
    List<Matricula> listaMatriculadosOEnCursoPorOferta(int codigoOferta) throws DaoException;
    List<Matricula> listaOportunidades() throws DaoException;
    List<Matricula> listaMatriculasEstudiante(int codigoEstudiante) throws DaoException;
    void actualizaMatricula(Matricula matricula) throws DaoException;
    void actualizaMatriculaUsuario(Matricula matricula, Usuario usuario) throws DaoException;
    Matricula existeMatricula(int oferta, int estudiante) throws DaoException;
    List<Matricula> listaMatriculadosPorOfertaCurso(int codigoOferta) throws DaoException;
    List<Matricula> listaMatriculasEstudianteActivas(int codigoEstudiante) throws DaoException;
    BigDecimal totalDatosMatricula(int estado) throws DaoException;
    List<DtoMatriculasCurso> listaMatriculasCurso(int tipo) throws DaoException;
}

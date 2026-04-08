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
    void agregarMatriculaInscripcion(Persona persona, Matricula matricula, Usuario usuario, UsuarioRol usuarioRol);
    List<Matricula> listaMatriculasAlumno(int codigoPersona, String codigoEstado);
    List<Matricula> listaMatriculasInscripcion(String estado, Date fechaInicio, Date fechaFin);
    List<Matricula> listaMatriculasCurso(String estado, int codigoCurso);
    List<Matricula> listaMatriculadosOEnCursoPorOferta(int codigoOferta);
    List<Matricula> listaOportunidades();
    List<Matricula> listaMatriculasEstudiante(int codigoEstudiante);
    void actualizaMatricula(Matricula matricula);
    void actualizaMatriculaUsuario(Matricula matricula, Usuario usuario);
    Matricula existeMatricula(int oferta, int estudiante);
    List<Matricula> listaMatriculadosPorOfertaCurso(int codigoOferta);
    List<Matricula> listaMatriculasEstudianteActivas(int codigoEstudiante);
    BigDecimal totalDatosMatricula(int estado);
    List<DtoMatriculasCurso> listaMatriculasCurso(int tipo);
}


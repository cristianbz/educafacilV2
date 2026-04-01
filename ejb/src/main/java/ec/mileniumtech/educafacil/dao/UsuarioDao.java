package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.dto.ObjetosMenuDto;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Usuario;

public interface UsuarioDao extends GenericoDao<Usuario, Long> {
    Usuario actualizaUsuario(Usuario usuario) throws DaoException;
    Usuario agregarUsuario(Usuario usuario) throws DaoException, EntidadDuplicadaException;
    Usuario consultarUsuario(String usuario) throws DaoException;
    List<ObjetosMenuDto> buscarAccesosUsuario(String correo) throws DaoException;
    Usuario consultarUsuarioPorDocumento(String documento) throws DaoException;
    List<Usuario> consultarUsuariosPorIdRol(int idRol) throws DaoException;
}

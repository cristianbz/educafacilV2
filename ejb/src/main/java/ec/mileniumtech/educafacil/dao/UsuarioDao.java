package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.dto.ObjetosMenuDto;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Usuario;

public interface UsuarioDao extends GenericoDao<Usuario, Long> {
    Usuario actualizaUsuario(Usuario usuario);
    Usuario agregarUsuario(Usuario usuario);
    Usuario consultarUsuario(String usuario);
    List<ObjetosMenuDto> buscarAccesosUsuario(String correo);
    Usuario consultarUsuarioPorDocumento(String documento);
    List<Usuario> consultarUsuariosPorIdRol(int idRol);
}


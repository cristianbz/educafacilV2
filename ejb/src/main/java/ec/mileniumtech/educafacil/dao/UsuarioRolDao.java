package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.UsuarioRol;

public interface UsuarioRolDao extends GenericoDao<UsuarioRol, Long> {
    List<UsuarioRol> listaDeUsuarioRol();
    List<UsuarioRol> listaUsuarioRolPorUsuario(int idUsuario);
    UsuarioRol agregarUsuarioRol(UsuarioRol usuarioRol);
}


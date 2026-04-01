package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.CategoriaRespuesta;

public interface CategoriaRespuestaDao extends GenericoDao<CategoriaRespuesta, Long> {
    List<CategoriaRespuesta> listaDeCategorias() throws DaoException;
    CategoriaRespuesta actualizarCategoriaRespuesta(CategoriaRespuesta categoriaRespuesta) throws DaoException, EntidadDuplicadaException;
    CategoriaRespuesta buscaCategoria(int codigoCategoria) throws DaoException;
}

package ec.mileniumtech.educafacil.dao;

import java.math.BigDecimal;
import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Campania;

public interface CampaniaDao extends GenericoDao<Campania, Long> {
    List<Campania> listaCampanias() throws DaoException;
    List<Campania> listaCampaniasporCurso() throws DaoException;
    void agregarActualizarCampania(Campania campania) throws DaoException, EntidadDuplicadaException;
    List<Campania> listaTodasCampanias() throws DaoException;
    Campania campaniaCurso(int curso) throws DaoException;
    BigDecimal totalGastoCampanias() throws DaoException;
}

package ec.mileniumtech.educafacil.dao;

import java.math.BigDecimal;
import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Campania;

public interface CampaniaDao extends GenericoDao<Campania, Long> {
    List<Campania> listaCampanias();
    List<Campania> listaCampaniasporCurso();
    void agregarActualizarCampania(Campania campania);
    List<Campania> listaTodasCampanias();
    Campania campaniaCurso(int curso);
    BigDecimal totalGastoCampanias();
}


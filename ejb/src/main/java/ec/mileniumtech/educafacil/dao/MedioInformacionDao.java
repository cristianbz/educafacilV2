package ec.mileniumtech.educafacil.dao;

import java.util.List;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.MedioInformacion;

public interface MedioInformacionDao extends GenericoDao<MedioInformacion, Long> {
    List<MedioInformacion> listaMediosInformacion() throws DaoException;
}

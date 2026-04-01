/**
 * Este software es propiedad de CEIMSCAP Cia.Ltda, el mismo que esta protegido por derechos de autor
 */
package ec.mileniumtech.educafacil.backing.encuestas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



import org.apache.log4j.Logger;

import ec.mileniumtech.educafacil.backing.MensajesBacking;
import ec.mileniumtech.educafacil.bean.encuestas.BeanConfPreguntas;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.dao.CategoriaRespuestaDao;
import ec.mileniumtech.educafacil.dao.ObjetoEvaluacionDao;
import ec.mileniumtech.educafacil.dao.PreguntaDao;
import ec.mileniumtech.educafacil.dao.RespuestasDao;
import ec.mileniumtech.educafacil.dao.TipoEncuestaDao;
import ec.mileniumtech.educafacil.dao.TipoEncuestaPreguntaDao;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.CategoriaRespuesta;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.ObjetoEvaluacion;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Pregunta;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Respuestas;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.TipoEncuesta;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.TipoEncuestaPregunta;
import ec.mileniumtech.educafacil.servicio.ConfiguracionEncuestasService;
import ec.mileniumtech.educafacil.utilitario.Mensaje;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * @author [Christian Báez] Dec 22, 2023
 *
 */

@Named
@ViewScoped
public class BackingConfPreguntas  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BackingConfPreguntas.class);

	@Getter
	@Inject
	private BeanConfPreguntas beanConfPreguntas;

	@Inject
	@Getter
	private MensajesBacking mensajesBacking;

	@EJB
	@Getter
	private ObjetoEvaluacionDao objetoEvaluacionServicioImpl;


	@EJB
	@Getter
	private CategoriaRespuestaDao categoriaRespuestaServicioImpl;

	@EJB
	@Getter
	private RespuestasDao respuestasServicioImpl;

	@EJB
	@Getter
	private TipoEncuestaDao tipoEncuestaServicioImpl;

	@EJB
	@Getter
	private PreguntaDao preguntaServicioImpl;

	@EJB
	@Getter
	private TipoEncuestaPreguntaDao tipoEncuestaPreguntaServicioImpl;

	@EJB
	@Getter
	private ConfiguracionEncuestasService configuracionEncuestasService;


	private String evaluacionActual;
	private boolean existe;

	@PostConstruct
	public void init () {
		try {
			getBeanConfPreguntas().setListaObjetoEvaluacion(new ArrayList<>());
			getBeanConfPreguntas().setListaTipoEncuesta(new ArrayList<>());
			getBeanConfPreguntas().setListaCategoriaRespuesta(new ArrayList<>());
			getBeanConfPreguntas().setListaObjetoEvaluacion(getObjetoEvaluacionServicioImpl().listaDeObjetosDeEvaluacion());
			getBeanConfPreguntas().setListaTipoEncuesta(getTipoEncuestaServicioImpl().listaDeTiposDeEncuestas());
			getBeanConfPreguntas().setListaCategoriaRespuesta(getCategoriaRespuestaServicioImpl().listaDeCategorias());
			getBeanConfPreguntas().setListaRespuestas(new ArrayList<>());
			getBeanConfPreguntas().setTabActivo(0);
			getBeanConfPreguntas().setListaPreguntas(new ArrayList<>());
			getBeanConfPreguntas().setListaPreguntas(getPreguntaServicioImpl().listaDePreguntas());
			getBeanConfPreguntas().setCategoriaRespuestaSeleccionada(new CategoriaRespuesta());


		}catch(Exception e) {
			e.printStackTrace();
		}

	}



	public void nuevoObjeto() {

		getBeanConfPreguntas().setObjetoEvaluacion(new ObjetoEvaluacion());
		getBeanConfPreguntas().getObjetoEvaluacion().setObjeEstado(true);
		Mensaje.verDialogo("dlgObjeto");

	}
	public void editarObjeto() {
		try {
			getBeanConfPreguntas().setObjetoEvaluacion(new ObjetoEvaluacion());
			getBeanConfPreguntas().getObjetoEvaluacion().setObjeId(getBeanConfPreguntas().getObjetoEvaluacionEditar().getObjeId());
			getBeanConfPreguntas().getObjetoEvaluacion().setObjeNombre(getBeanConfPreguntas().getObjetoEvaluacionEditar().getObjeNombre());
			getBeanConfPreguntas().getObjetoEvaluacion().setObjeEstado(getBeanConfPreguntas().getObjetoEvaluacionEditar().getObjeEstado());
			Mensaje.verDialogo("dlgObjeto");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void guardarObjeto() {
		try {
            getConfiguracionEncuestasService().guardarObjetoEvaluacion(
                getBeanConfPreguntas().getObjetoEvaluacion(), 
                getBeanConfPreguntas().getListaObjetoEvaluacion(), 
                getBeanConfPreguntas().getObjetoEvaluacionEditar() != null ? getBeanConfPreguntas().getObjetoEvaluacionEditar().getObjeNombre() : null
            );
            
            getBeanConfPreguntas().setListaObjetoEvaluacion(getObjetoEvaluacionServicioImpl().listaDeObjetosDeEvaluacion());
            Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregar"));
            Mensaje.ocultarDialogo("dlgObjeto");
            getBeanConfPreguntas().setTabActivo(0);

		} catch (EntidadDuplicadaException duplicateEx) {
            Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.elementoDuplicado"));
        } catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void nuevoTipoEncuesta() {
		if(getBeanConfPreguntas().getListaObjetoEvaluacion().isEmpty()) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregarObj"));
		}else {
			getBeanConfPreguntas().setTipoEncuesta(new TipoEncuesta());
			getBeanConfPreguntas().getTipoEncuesta().setTipeEstado(true);
			getBeanConfPreguntas().setListaPreguntas(new ArrayList<Pregunta>());
			Mensaje.verDialogo("dlgTipoEncuesta");
		}

	}
	public void guardarTipo() {
		try {
            getConfiguracionEncuestasService().guardarTipoEncuesta(
                getBeanConfPreguntas().getTipoEncuesta(),
                getBeanConfPreguntas().getObjetoEvaluacionSeleccionado(),
                getBeanConfPreguntas().getListaTipoEncuesta(),
                getBeanConfPreguntas().getTipoEncuestaEditar() != null ? getBeanConfPreguntas().getTipoEncuestaEditar().getTipeDescripcion() : null
            );

            getBeanConfPreguntas().setListaTipoEncuesta(getTipoEncuestaServicioImpl().listaDeTiposDeEncuestas());
            Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregar"));
            Mensaje.ocultarDialogo("dlgTipoEncuesta");
            getBeanConfPreguntas().setTabActivo(1);

		} catch (EntidadDuplicadaException duplicateEx) {
            Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.elementoDuplicado"));
        } catch(Exception e) {
			e.printStackTrace();
		}
	}


	public void editarTipo() {
		try {
			getBeanConfPreguntas().setObjetoEvaluacionSeleccionado(getBeanConfPreguntas().getTipoEncuestaEditar().getObjetoEvaluacion().getObjeId());
			getBeanConfPreguntas().setTipoEncuesta(new TipoEncuesta());
			getBeanConfPreguntas().getTipoEncuesta().setTipeId(getBeanConfPreguntas().getTipoEncuestaEditar().getTipeId());
			getBeanConfPreguntas().getTipoEncuesta().setTipeDescripcion(getBeanConfPreguntas().getTipoEncuestaEditar().getTipeDescripcion());
			getBeanConfPreguntas().getTipoEncuesta().setTipeEstado(getBeanConfPreguntas().getTipoEncuestaEditar().getTipeEstado());
			Mensaje.verDialogo("dlgTipoEncuesta");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void nuevaCategoria() {
		getBeanConfPreguntas().setCategoriaRespuesta(new CategoriaRespuesta());
		getBeanConfPreguntas().setListaRespuestas(new ArrayList<>());
		getBeanConfPreguntas().getCategoriaRespuesta().setCatrEstado(true);
		Mensaje.verDialogo("dlgCategoria");
	}

	public void agregarRespuestas() {
		Respuestas respuesta = new Respuestas();
		respuesta.setRespEstado(true);
		getBeanConfPreguntas().getListaRespuestas().add(respuesta);
	}

	public void grabarRespuestas() {
		try {
            getConfiguracionEncuestasService().grabarRespuestas(
                getBeanConfPreguntas().getCategoriaRespuesta(),
                getBeanConfPreguntas().getListaRespuestas(),
                getBeanConfPreguntas().getListaCategoriaRespuesta(),
                getBeanConfPreguntas().getCategoriaRespuestaEditar() != null ? getBeanConfPreguntas().getCategoriaRespuestaEditar().getCatrNombre() : null
            );

            getBeanConfPreguntas().setListaCategoriaRespuesta(getCategoriaRespuestaServicioImpl().listaDeCategorias());
            getBeanConfPreguntas().setTabActivo(2);
            Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregar"));
            Mensaje.ocultarDialogo("dlgCategoria");

		} catch (IllegalArgumentException paramEx) {
            Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.informacion"));
        } catch (EntidadDuplicadaException duplicateEx) {
            Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.elementoDuplicado"));
        } catch(Exception e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.generico"));	
			e.printStackTrace();
		}		
	}

	public void editarCategoriaRespuesta() {
		try {
			getBeanConfPreguntas().setListaRespuestas(new ArrayList<>());
			getBeanConfPreguntas().setCategoriaRespuesta(new CategoriaRespuesta());
			getBeanConfPreguntas().getCategoriaRespuesta().setCatrId(getBeanConfPreguntas().getCategoriaRespuestaEditar().getCatrId());
			getBeanConfPreguntas().getCategoriaRespuesta().setCatrNombre(getBeanConfPreguntas().getCategoriaRespuestaEditar().getCatrNombre());
			getBeanConfPreguntas().getCategoriaRespuesta().setCatrEstado(getBeanConfPreguntas().getCategoriaRespuestaEditar().getCatrEstado());
			getBeanConfPreguntas().setCategoriaRespuesta(getCategoriaRespuestaServicioImpl().buscaCategoria(getBeanConfPreguntas().getCategoriaRespuesta().getCatrId()));
			for (Respuestas res : getBeanConfPreguntas().getCategoriaRespuesta().getRespuestas()) {
				if(res.isRespEstado())
					getBeanConfPreguntas().getListaRespuestas().add(res);
			}
			Mensaje.verDialogo("dlgCategoria");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void agregarPreguntas() {
		Pregunta preguntas = new Pregunta();
		getBeanConfPreguntas().getListaPreguntas().add(preguntas);
	}

	public void nuevaPregunta() {
		try {
			getBeanConfPreguntas().setPregunta(new Pregunta());
			getBeanConfPreguntas().setListaCategoriaRespuesta(getCategoriaRespuestaServicioImpl().listaDeCategorias());
			Mensaje.verDialogo("dlgPregunta");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void grabarPregunta() {
		try {
			getBeanConfPreguntas().getPregunta().setCategoriaRespuesta(getBeanConfPreguntas().getCategoriaRespuestaSeleccionada());
			getBeanConfPreguntas().getPregunta().setPregEstado(true);
			getPreguntaServicioImpl().agregarActualizarPregunta(getBeanConfPreguntas().getPregunta());
			getBeanConfPreguntas().setListaPreguntas(getPreguntaServicioImpl().listaDePreguntas());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregar"));
			Mensaje.ocultarDialogo("dlgPregunta");
			getBeanConfPreguntas().setTabActivo(3);
		}catch(DaoException e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.grabarPregunta"));
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "grabarPregunta" + ": ").append(e.getMessage()));
		} catch (EntidadDuplicadaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void editarPregunta() {
		try {
			getBeanConfPreguntas().setCategoriaRespuestaSeleccionada(getBeanConfPreguntas().getPregunta().getCategoriaRespuesta());			
			getBeanConfPreguntas().setListaRespuestas(getRespuestasServicioImpl().listaRespuestasPorCategoria(getBeanConfPreguntas().getCategoriaRespuestaSeleccionada().getCatrId()));
			Mensaje.verDialogo("dlgPregunta");
		}catch(Exception e) {
			e.printStackTrace();
		}
	} 
	public void cargarRespuestasCategoria() {
		try {
			getBeanConfPreguntas().setListaRespuestas(new ArrayList<>());
			getBeanConfPreguntas().setListaRespuestas(getRespuestasServicioImpl().listaRespuestasPorCategoria(getBeanConfPreguntas().getCategoriaRespuestaSeleccionada().getCatrId()));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void nuevoTipoEncuestaPregunta() {
		try {
			getBeanConfPreguntas().setListaTipoEncuesta(new ArrayList<>());
			getBeanConfPreguntas().setListaTipoEncuesta(getTipoEncuestaServicioImpl().listaDeTiposDeEncuestas());
			getBeanConfPreguntas().setListaPreguntasTE(new ArrayList<>());
			getBeanConfPreguntas().setCategoriaRespuestaSeleccionada(new CategoriaRespuesta());
			getBeanConfPreguntas().setListaPreguntasSelec(new ArrayList<>());
			Mensaje.verDialogo("dlgPreguntaAsociadas");

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void cargaPreguntasPorCategorias() {
		try {

			getBeanConfPreguntas().setListaPreguntasTE(new ArrayList<>());
			getBeanConfPreguntas().setListaPreguntasTE(getPreguntaServicioImpl().listaPreguntasPorCategoria(getBeanConfPreguntas().getCategoriaRespuestaSeleccionada().getCatrId()));
			if(getBeanConfPreguntas().getListaPreguntasTE().size()==0)
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("error.noHayDatos"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void guardarTipoEncuestaPregunta(){
		try {
            String duplicadas = getConfiguracionEncuestasService().guardarTipoEncuestaPregunta(
                getBeanConfPreguntas().getTipoEncuesta(),
                getBeanConfPreguntas().getListaPreguntasSelec()
            );

			if (duplicadas == null) {
				getBeanConfPreguntas().setListaTipoEncuesta(getTipoEncuestaServicioImpl().listaDeTiposDeEncuestas());
				getBeanConfPreguntas().setListaPreguntasSelec(new ArrayList<>());
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregar"));
				Mensaje.ocultarDialogo("dlgPreguntaAsociadas");
			} else {
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.elementoDuplicado").concat(" [").concat(duplicadas).concat("]"));
			}
		}catch(Exception e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.generico"));
			e.printStackTrace();
		}
	}

	public void eliminarTipoEncuestaPregunta() {
		try {
            getConfiguracionEncuestasService().eliminarTipoEncuestaPregunta(getBeanConfPreguntas().getTipoEncuestaPreguntaSeleccionada());
			
            getBeanConfPreguntas().setListaTipoEncuesta(getTipoEncuestaServicioImpl().listaDeTiposDeEncuestas());
			getBeanConfPreguntas().setTabActivo(1);
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.eliminar"));

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void eliminarRespuesta() {
		try {
			if(getBeanConfPreguntas().getRespuestaSeleccionada().getRespId()==null) 
				getBeanConfPreguntas().getListaRespuestas().remove(getBeanConfPreguntas().getRespuestaSeleccionada());
			else {
                getConfiguracionEncuestasService().eliminarRespuesta(getBeanConfPreguntas().getRespuestaSeleccionada());
				getBeanConfPreguntas().setListaCategoriaRespuesta(getCategoriaRespuestaServicioImpl().listaDeCategorias());
				getBeanConfPreguntas().getListaRespuestas().remove(getBeanConfPreguntas().getRespuestaSeleccionada());
				getBeanConfPreguntas().setTabActivo(2);
			}
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.eliminar"));	

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}



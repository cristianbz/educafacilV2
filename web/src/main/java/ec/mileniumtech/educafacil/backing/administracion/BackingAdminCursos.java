/**
 * Este software esta protegido por derechos de autor CEIMSCAP
 */
package ec.mileniumtech.educafacil.backing.administracion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import ec.mileniumtech.educafacil.backing.MensajesBacking;
import ec.mileniumtech.educafacil.bean.administracion.BeanAdminCursos;
import ec.mileniumtech.educafacil.dao.excepciones.BusinessException;
import ec.mileniumtech.educafacil.dao.AreaDao;
import ec.mileniumtech.educafacil.dao.CatalogoDao;
import ec.mileniumtech.educafacil.dao.EvaluacionCursoDao;
import ec.mileniumtech.educafacil.dao.InstructorDao;
import ec.mileniumtech.educafacil.dao.ObjetoEvaluacionDao;
import ec.mileniumtech.educafacil.dao.OfertaCapacitacionDao;
import ec.mileniumtech.educafacil.dao.OfertaCursosDao;
import ec.mileniumtech.educafacil.dao.TipoEncuestaDao;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.EvaluacionCurso;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.ObjetoEvaluacion;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCursos;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.TipoEncuesta;
import ec.mileniumtech.educafacil.servicio.AdministracionCursosService;
import ec.mileniumtech.educafacil.utilitario.Mensaje;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumEstadosOfertaCurso;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumTipoCatalogo;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
*@author christian  Jul 13, 2024
*
*/
@Named("backingAdminCursos")
@ViewScoped
public class BackingAdminCursos implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BackingAdminCursos.class);
	
	@Inject
	@Getter
	private MensajesBacking mensajesBacking;
	
	@Inject
	@Getter
	private BeanAdminCursos beanAdminCursos;
	
	@EJB
	@Getter
	private OfertaCursosDao ofertaCursosServicioImpl;
	
	@EJB
	@Getter
	private InstructorDao instructorServicioImpl;
	
	@EJB
	@Getter
	private AreaDao areaServicioImpl;
	
	@EJB
	@Getter
	private OfertaCapacitacionDao ofertaCapacitacionServicioImpl;
	
	@EJB
	@Getter
	private ObjetoEvaluacionDao objetoEvaluacionServicioImpl;
	
	@EJB
	@Getter
	private TipoEncuestaDao tipoEncuestaServicioImpl;
	
	@EJB
	@Getter
	private CatalogoDao catalogoServicioImpl;
	
	@EJB
	@Getter
	private EvaluacionCursoDao evaluacionCursoServicioImpl;

	@EJB
	@Getter
	private AdministracionCursosService administracionCursosService;
	
	@PostConstruct
	public void init() {
		cargarOfertaCursosActivos();
		cargarArea();
		cargaTipoCapacitacion();
		cargarInstructor();
		getBeanAdminCursos().setNuevaOfertaCurso(false);
		getBeanAdminCursos().setObjetoEvaluacion(new ObjetoEvaluacion());
		cargaObjetosEvaluacion();
		getBeanAdminCursos().setListaEvaluacionCursoAsig(new ArrayList<>());;


	}
	/**
	 * Carga los objetos de Evaluacion
	 */
	public void cargaObjetosEvaluacion() {
		try {
			getBeanAdminCursos().setListaObjetoEvaluacion(new ArrayList<>());
			getBeanAdminCursos().setListaObjetoEvaluacion(objetoEvaluacionServicioImpl.listaDeObjetosDeEvaluacion());
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Carga los tipos de encuestas
	 */
	public void cargaTiposEncuestas() {
		try {
			if(getBeanAdminCursos().getObjetoEvaluacion().getObjeId()!=null) {
				getBeanAdminCursos().setListaTipoEncuestas(new ArrayList<>());
				getBeanAdminCursos().setListaEvaluacionCursoAsig(new ArrayList<>());
//				System.out.println(getBeanAdminCursos().getOfertaCursos().getOcurId());
				getBeanAdminCursos().setListaEvaluacionCursoAsig(getEvaluacionCursoServicioImpl().listaDeEvaluacionesPorCurso(getBeanAdminCursos().getOfertaCursos().getOcurId(),getBeanAdminCursos().getObjetoEvaluacion().getObjeId()));
				getBeanAdminCursos().setListaTipoEncuestas(getTipoEncuestaServicioImpl().listaDeTiposDeEncuestasPorOe(getBeanAdminCursos().getObjetoEvaluacion().getObjeId()));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Carga la oferta de cursos activos
	 */
	public void cargarOfertaCursosActivos() {
		getBeanAdminCursos().setListaOfertaCursos(new ArrayList<>());
		getBeanAdminCursos().setListaOfertaCursos(getOfertaCursosServicioImpl().listaOfertaCursosActivos());
		getBeanAdminCursos().setListaOfertaCursos(getBeanAdminCursos().getListaOfertaCursos().stream().sorted((a1,a2) -> a1.getOfertaCapacitacion().getArea().getAreaNombre().compareTo(a2.getOfertaCapacitacion().getArea().getAreaNombre())).collect(Collectors.toList()));		
	}
	/**
	 * Carga las areas
	 */
	public void cargarArea() {
		getBeanAdminCursos().setListaAreas(new ArrayList<>());
		getBeanAdminCursos().setListaAreas(getAreaServicioImpl().listaDeAreas());
		getBeanAdminCursos().setListaAreas(getBeanAdminCursos().getListaAreas().stream().sorted((a1,a2)->a1.getAreaNombre().compareTo(a2.getAreaNombre())).collect(Collectors.toList()));
	}
	/**
	 * Carga las Especialidades
	 */
	public void cargaEspecialidades() {
		getBeanAdminCursos().setListaEspecialidad(new ArrayList<>());
		getBeanAdminCursos().setListaCurso(new ArrayList<>());
		getBeanAdminCursos().setListaEspecialidad(getOfertaCapacitacionServicioImpl().listaEspecialidadPorArea(getBeanAdminCursos().getCodigoArea()));
		getBeanAdminCursos().setListaEspecialidad(getBeanAdminCursos().getListaEspecialidad().stream().sorted((e1,e2)->e1.getEspeNombre().compareTo(e2.getEspeNombre())).collect(Collectors.toList()));
	}
	/**
	 * Carga la lista de instructores
	 */
	public void cargarInstructor() {
		getBeanAdminCursos().setListaInstructores(new ArrayList<>());
		getBeanAdminCursos().setListaInstructores(getInstructorServicioImpl().listaInstructores());
		getBeanAdminCursos().setListaInstructores(getBeanAdminCursos().getListaInstructores().stream().sorted((i1,i2)->i1.getPersona().getPersApellidos().compareTo(i2.getPersona().getPersApellidos())).collect(Collectors.toList()));
	}
	/**
	 * Carga los cursos
	 */
	public void cargarCursos() {
		getBeanAdminCursos().setListaCurso(new ArrayList<>());
		getBeanAdminCursos().setListaCurso(getOfertaCapacitacionServicioImpl().listaCursosPorAreaEspecilidad(getBeanAdminCursos().getCodigoArea(), getBeanAdminCursos().getCodigoEspecialidad()));
		getBeanAdminCursos().setListaCurso(getBeanAdminCursos().getListaCurso().stream().sorted((c1,c2)->c1.getCursNombre().compareTo(c2.getCursNombre())).collect(Collectors.toList()));
	}
	/**
	 * Carga los tipos de capacitacion
	 */
	public void cargaTipoCapacitacion() {
		getBeanAdminCursos().setListaCatalogo(new ArrayList<>());
		getBeanAdminCursos().setListaCatalogo(getCatalogoServicioImpl().catalogosPorTipo(EnumTipoCatalogo.TIPOCAPACITACION.getNemotecnico()));
		getBeanAdminCursos().setListaCatalogo(getBeanAdminCursos().getListaCatalogo().stream().sorted((t1,t2)->t1.getCataDescripcion().compareTo(t2.getCataDescripcion())).collect(Collectors.toList()));
	}
	/**
	 * Permite editar una oferta de curso
	 */
	public void edicionOfertaCurso() {
		getBeanAdminCursos().setNuevaOfertaCurso(false);
		getBeanAdminCursos().setCodigoArea(getBeanAdminCursos().getOfertaCursos().getOfertaCapacitacion().getArea().getAreaId());
		cargaEspecialidades();
		Mensaje.actualizarComponente("comboCurso");
		getBeanAdminCursos().setCodigoEspecialidad(getBeanAdminCursos().getOfertaCursos().getOfertaCapacitacion().getEspecialidad().getEspeId());
		cargarCursos();
		getBeanAdminCursos().setCodigoCurso(getBeanAdminCursos().getOfertaCursos().getOfertaCapacitacion().getCurso().getCursId());
		getBeanAdminCursos().setCodigoTipoCurso(getBeanAdminCursos().getOfertaCursos().getOcurTipo());
		getBeanAdminCursos().setCodigoInstructor(getBeanAdminCursos().getOfertaCursos().getInstructor().getInstId());
		if (! getBeanAdminCursos().getOfertaCursos().getOcurEstado().equals(EnumEstadosOfertaCurso.ANULADO.getCodigo()))
			getBeanAdminCursos().setAnularCurso(false);

		Mensaje.verDialogo("dlgOfertaCursos");
	}
	/**
	 * Graba una oferta de curso
	 */
	public void grabarCurso() {
		getAdministracionCursosService().grabarCurso(
			getBeanAdminCursos().getOfertaCursos(),
			getBeanAdminCursos().getCodigoArea(),
			getBeanAdminCursos().getCodigoEspecialidad(),
			getBeanAdminCursos().getCodigoCurso(),
			getBeanAdminCursos().getCodigoInstructor(),
			getBeanAdminCursos().getCodigoTipoCurso(),
			getBeanAdminCursos().isAnularCurso()
		);

		getBeanAdminCursos().setEditarOfertaCurso(false);
		getBeanAdminCursos().setOfertaCursos(new OfertaCursos());
		cargarOfertaCursosActivos();
		Mensaje.ocultarDialogo("dlgOfertaCursos");
		Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregar"));	
	}
	
	/**
	 * Nueva oferta de curso
	 */
	public void nuevoOfertaCurso() {
		getBeanAdminCursos().setAnularCurso(false);
		getBeanAdminCursos().setNuevaOfertaCurso(true);
		getBeanAdminCursos().setOfertaCursos(new OfertaCursos());
		getBeanAdminCursos().setCodigoArea(0);
		getBeanAdminCursos().setCodigoCurso(0);
		getBeanAdminCursos().setCodigoEspecialidad(0);
		getBeanAdminCursos().setCodigoInstructor(0);
		getBeanAdminCursos().setCodigoTipoCurso("");
		getBeanAdminCursos().setListaEspecialidad(new ArrayList<>());
		getBeanAdminCursos().setListaCurso(new ArrayList<>());
		Mensaje.verDialogo("dlgOfertaCursos");
	}
	
	/**
	 * Abre el dialogo de asignacion de encuestas y vacia las listas
	 */
	public void asignarEncuestas() {
		try {
			getBeanAdminCursos().setObjetoEvaluacion(new ObjetoEvaluacion());
			getBeanAdminCursos().setListaTipoEncuestas(new ArrayList<>());
			getBeanAdminCursos().setListaEncuestasSelect(new ArrayList<>());
			getBeanAdminCursos().setListaEvaluacionCursoAsig(new ArrayList<>());
			if(getBeanAdminCursos().getListaObjetoEvaluacion().size()>0) {
				getBeanAdminCursos().setListaTipoEncuestas(getTipoEncuestaServicioImpl().listaDeTiposDeEncuestasPorOe(getBeanAdminCursos().getListaObjetoEvaluacion().get(0).getObjeId()));
				getBeanAdminCursos().setListaEvaluacionCursoAsig(getEvaluacionCursoServicioImpl().listaDeEvaluacionesPorCurso(getBeanAdminCursos().getOfertaCursos().getOcurId(), getBeanAdminCursos().getListaObjetoEvaluacion().get(0).getObjeId()));
				Mensaje.verDialogo("dlgAsignacionEncuestasCurso");
			}else {
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregarDatosEncu"));

			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * Guarda los datos de las encuestas asignadas
	 */
	public void asignarEvaluacionesCurso() {
		if(getBeanAdminCursos().getListaEncuestasSelect().size()==0) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.nodatos"));
		}else {


			for (TipoEncuesta tipoEncuesta : getBeanAdminCursos().getListaEncuestasSelect()) {
				EvaluacionCurso evcu = new EvaluacionCurso();
				evcu.setTipoEncuesta(tipoEncuesta);
				evcu.setOfertacursos(getBeanAdminCursos().getOfertaCursos());
				evcu.setEvcuEstado(true);

				// Verificar si el tipo de encuesta ya está asignado
				boolean encuestaYaAsignada = false;
				for (EvaluacionCurso evaluacionCurso : getBeanAdminCursos().getListaEvaluacionCursoAsig()) {
					if (evaluacionCurso.getOfertacursos().getOcurId()==evcu.getOfertacursos().getOcurId()&& evaluacionCurso.getTipoEncuesta().getTipeId()==evcu.getTipoEncuesta().getTipeId()) {
						encuestaYaAsignada = true;
						break;
					}
				}

				if (encuestaYaAsignada) {
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.elementoDuplicado"));			

				} else {
					getBeanAdminCursos().getListaEvaluacionCursoAsig().add(evcu);
				}
			}
		}
	}

	/**
	 * Se encarga de guardar la evaluacion Curso
	 */
	public void guardarEvaluacionCurso() {
		try {
            getAdministracionCursosService().guardarEvaluacionesCurso(getBeanAdminCursos().getListaEvaluacionCursoAsig());
				
			cargarOfertaCursosActivos();
			Mensaje.ocultarDialogo("dlgAsignacionEncuestasCurso");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.grabar"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 	Se encarga de eliminar la encuesta seleccionada
	 */
	public void eliminarEvaluacionCurso() {
		try {
			getBeanAdminCursos().getListaEvaluacionCursoAsig().remove(getBeanAdminCursos().getEvaluacionCurso());
            getAdministracionCursosService().eliminarEvaluacionCurso(getBeanAdminCursos().getEvaluacionCurso());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.eliminar"));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}



/**
Este software es propiedad de CEIMSCAP Cia.Ltda, el mismo que esta protegido por derechos de autor
*/

package ec.mileniumtech.educafacil.backing.estudiantes;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.apache.log4j.Logger;

import ec.mileniumtech.educafacil.backing.MensajesBacking;
import ec.mileniumtech.educafacil.bean.estudiantes.BeanMatriculaOnline;
import ec.mileniumtech.educafacil.bean.usuarios.BeanLogin;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.dao.CampaniaDao;
import ec.mileniumtech.educafacil.dao.CatalogoDao;
import ec.mileniumtech.educafacil.dao.ConfiguracionesDao;
import ec.mileniumtech.educafacil.dao.CursoDao;
import ec.mileniumtech.educafacil.dao.EmpresaDao;
import ec.mileniumtech.educafacil.servicio.MatriculaService;
import ec.mileniumtech.educafacil.dao.OfertaCursosDao;
import ec.mileniumtech.educafacil.dao.PersonaDao;
import ec.mileniumtech.educafacil.dao.UsuarioDao;
import ec.mileniumtech.educafacil.dao.UsuarioRolDao;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Campania;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Catalogo;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Empresa;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Estudiante;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Matricula;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCursos;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Persona;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Rol;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Usuario;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.UsuarioRol;
import ec.mileniumtech.educafacil.utilitario.Mensaje;
import ec.mileniumtech.educafacil.utilitarios.correo.Correo;
import ec.mileniumtech.educafacil.utilitarios.encriptacion.Encriptar;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumEstadosMatricula;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumRol;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;

/**
 * @author [ Christian Baez ] christian 24 feb. 2022
 *
 */
@ViewScoped
@Named
public class BackingMatriculaOnline implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BackingMatriculaOnline.class);

	@Inject
	@Getter	
	private BeanLogin beanLogin;

	@EJB
	@Getter	
	private ConfiguracionesDao configuracionesServicioImpl;
	
	@EJB
	@Getter	
	private CampaniaDao campaniaServicioImpl;
	
	@EJB
	@Getter	
	private CatalogoDao catalogoServicioImpl;
	
	@Inject
	@Getter
	private BeanMatriculaOnline beanMatricula;
	@Inject
	@Getter
	private MensajesBacking mensajesBacking;
	@Getter
	@EJB
	private EmpresaDao empresaServicioImpl; 
	@EJB
	@Getter
	private CursoDao cursoServicioImpl;
	
	@EJB
	@Getter
	private OfertaCursosDao ofertaServicios;
	
	@EJB
	@Getter
	private PersonaDao personaServicioImpl;
	
	@EJB
	@Getter
	private MatriculaService matriculaServicio;
	
	@EJB
	@Getter
	private UsuarioDao usuarioServicioImpl;
	
	@EJB
	@Getter
	private UsuarioRolDao usuarioRolServicioImpl;
	
	@Getter
	@Setter
	private String password;
	
	@PostConstruct
	public void init() {
		getBeanMatricula().setPersona(new Persona());
		getBeanMatricula().setEstudiante(new Estudiante());
		getBeanMatricula().setMatricula(new Matricula());
		cargarOfertaCursosActivos();
		getBeanMatricula().setMostrarDatosPersona(true);
		getBeanMatricula().setCodigoCargo(null);
		getBeanMatricula().setCodigoMedioInformacion(null);
		getBeanMatricula().setCodigoModalidadCurso(null);
		getBeanMatricula().setCursoSeleccionado(new OfertaCursos());
		getBeanMatricula().setDeshabilitaMatricula(false);
		cargaProvincias();
	}
	
	public void cargaProvincias() {
		try {
			getBeanMatricula().setListaProvincias(new ArrayList<>());
			getBeanMatricula().setListaProvincias(getCatalogoServicioImpl().catalogosPorTipo("TPROV"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void cargaCantones() {
		try {
			getBeanMatricula().setListaCantones(new ArrayList<>());
			getBeanMatricula().setListaCantones(getCatalogoServicioImpl().catalogosPorPadre(getBeanMatricula().getProvincia()));

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Busca una persona por su cedula
	 * @return
	 */
	public void buscaPersonaPorCedula() {
		Persona persona=null;
		try {			
			persona=getPersonaServicioImpl().buscarPersonaPorCedula(getBeanMatricula().getPersona().getPersDocumentoIdentidad());
			datosMatriculaAlBuscarPersona(persona);
		}catch(DaoException e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.buscaCedula"));			
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "buscaPersonaPorCedula " + ": ").append(e.getMessage()));
		} 	
	}
	/**
     * Obtiene los datos de matricula de la persona
     * @param persona
     */
    public void datosMatriculaAlBuscarPersona(Persona persona) {
    	try {
	    	if(persona!=null) {
	    		persona=getPersonaServicioImpl().buscarPersonaPorId(persona.getPersId());
		    	getBeanMatricula().setPersona(persona);
		    	for (Catalogo cat : getBeanMatricula().getListaProvincias()) {
					if(cat.getCataId() == persona.getPersProvincia()) {
						getBeanMatricula().setProvincia(cat);
						cargaCantones();
						break;
					}
				}
		    	getBeanMatricula().setCodigoCiudad(persona.getPersCiudad());
		    	getBeanMatricula().setCodigoSector(persona.getPersSector());
		    	if(!getBeanMatricula().getPersona().getEstudiantes().isEmpty()) {
			    	getBeanMatricula().setEstudiante(persona.getEstudiantes().get(0));
			    	getBeanMatricula().setCodigoCargo(persona.getEstudiantes().get(0).getEstuCargoOcupa());
			    	getBeanMatricula().setCodigoNivelEstudio(persona.getEstudiantes().get(0).getEstuNivelEstudio());
			    	getBeanMatricula().setCodigoIngresosMensuales(persona.getEstudiantes().get(0).getEstuIngresosMensuales());
		    	}

	    	}
    	} catch (DaoException e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.buscaApellidos"));			
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "datosMatriculaAlBuscarPersona " + ": ").append(e.getMessage()));
		}	
    }
	/**
	 * Carga la oferta de cursos activos
	 */
	public void cargarOfertaCursosActivos() {
		try {
			
			getBeanMatricula().setListaOfertaCursos(new ArrayList<>());
			getBeanMatricula().setListaOfertaCursos(getOfertaServicios().listaOfertaCursosActivos());
			getBeanMatricula().setListaOfertaCursos(getBeanMatricula().getListaOfertaCursos().stream().sorted((a1,a2) -> a1.getOfertaCapacitacion().getCurso().getCursNombre().compareTo(a2.getOfertaCapacitacion().getCurso().getCursNombre())).collect(Collectors.toList()));
		} catch (DaoException e) { 
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.cargarcursos"));			
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarOfertaCursosActivos" + ": ").append(e.getMessage()));
		}
	}
	public void matricular() {
		try {
            password = getBeanMatricula().getPersona().getPersTelefonoMobil();

            getMatriculaServicio().grabarMatriculaOnline(
                getBeanMatricula().getPersona(), 
                getBeanMatricula().getMatricula(), 
                getBeanMatricula().getEstudiante(), 
                getBeanMatricula().getCursoSeleccionado(), 
                getBeanMatricula().getCodigoCargo(), 
                getBeanMatricula().getCodigoNivelEstudio(), 
                getBeanMatricula().getCodigoIngresosMensuales(), 
                getBeanMatricula().getCodigoMedioInformacion(), 
                getBeanMatricula().getCampania(), 
                password,
                getBeanMatricula().getCodigoCiudad(),
                getBeanMatricula().getCodigoSector(),
                getBeanMatricula().getProvincia().getCataId()
            );

			getBeanMatricula().setMostrarDatosPersona(false);
			enviarCorreo();
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.grabar"));
		}catch(DaoException e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.grabar"));
			e.printStackTrace();
		}catch(EntidadDuplicadaException e) {
			e.printStackTrace();
		}
	}
	
	public void redireciona() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("https://www.capacitaciontecnica.ec/sitect/blog/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	}
	public void mostrarDialogoMatricular() {
		Mensaje.verDialogo("dlgCerrar");
	}
	
	public void enviarCorreo() {
		try {
			getBeanLogin().setConfiguraciones(getConfiguracionesServicioImpl().listaConfiguraciones().get(0));
            getMatriculaServicio().enviarCorreo(
                false, 
                getBeanLogin().getConfiguraciones().getConfEmpresa(), 
                getBeanLogin().getConfiguraciones().getConfServidorSmtp(), 
                getBeanLogin().getConfiguraciones().getConfUsuarioCorreo(), 
                getBeanLogin().getConfiguraciones().getConfClaveCorreo(), 
                getBeanLogin().getConfiguraciones().getConfEnviadoMailDesde(), 
                getBeanMatricula().getPersona(), 
                getBeanMatricula().getMatricula()
            );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void validaMatricula() {
		try {			
			if(getBeanMatricula().getEstudiante()!= null) {
				List<Campania> listaCampanias=new ArrayList<>();

				int oferta = getBeanMatricula().getCursoSeleccionado().getOcurId();
				Matricula matricula = getMatriculaServicio().existeMatricula(oferta, getBeanMatricula().getEstudiante().getEstuId() );
				if(matricula == null) {
					getBeanMatricula().setDeshabilitaMatricula(false);
					listaCampanias=getCampaniaServicioImpl().listaCampanias();
					for (Campania campania : listaCampanias) {
						if(campania.getCampId()>0) {
							if(campania.getCurso().getCursId()== getBeanMatricula().getCursoSeleccionado().getOfertaCapacitacion().getCurso().getCursId()) {
								getBeanMatricula().setCampania(campania);
								break;
							}
						}
					}
				}else {
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.matriculaExiste"));
					getBeanMatricula().setDeshabilitaMatricula(true);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

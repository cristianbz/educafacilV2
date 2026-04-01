/**
Este software es propiedad de CEIMSCAP Cia.Ltda, el mismo que esta protegido por derechos de autor
 */

package ec.mileniumtech.educafacil.backing.estudiantes;

import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.file.UploadedFile;

import ec.mileniumtech.educafacil.backing.MensajesBacking;
import ec.mileniumtech.educafacil.bean.estudiantes.BeanSeguimientoClientes;
import ec.mileniumtech.educafacil.bean.usuarios.BeanLogin;
import ec.mileniumtech.educafacil.dao.CampaniaDao;
import ec.mileniumtech.educafacil.dao.CursoDao;
import ec.mileniumtech.educafacil.dao.DetalleSeguimientoDao;
import ec.mileniumtech.educafacil.dao.SeguimientoClientesDao;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Campania;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Curso;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DetalleSeguimiento;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.SeguimientoClientes;
import ec.mileniumtech.educafacil.utilitario.Cadenas;
import ec.mileniumtech.educafacil.utilitario.Mensaje;
import ec.mileniumtech.educafacil.utilitarios.dto.registrodatos.FormFacebookAdsRecord;
import ec.mileniumtech.educafacil.utilitarios.dto.registrodatos.PreguntasFormFacesbookRecord;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumEstadosContactoCliente;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumMedioContacto;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumMedioInformacion;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumUbicacionDomicilio;
import ec.mileniumtech.educafacil.utilitarios.fechas.FechaFormato;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import ec.mileniumtech.educafacil.servicio.SeguimientoClientesService;

/**
 * @author [ Christian Baez ] christian 30 dic. 2021
 *
 */
@ViewScoped
@Named
public class BackingSeguimientoClientes implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BackingSeguimientoClientes.class);
//	public static final APIContext context = new APIContext(
//            "{access-token}",
//            "{appsecret}"
//    );

	@Inject
	@Getter
	private BeanLogin beanLogin;
	
	@Inject
	@Getter
	private MensajesBacking mensajesBacking;
	
	@EJB
	@Getter
	private SeguimientoClientesDao seguimientoDao;

	@EJB
	@Getter
	private CursoDao cursosServicio;

	@EJB
	@Getter
	private SeguimientoClientesDao seguimientoClientesServicioImpl;

	@EJB
	@Getter
	private SeguimientoClientesService seguimientoClientesService;

	@EJB
	@Getter
	private DetalleSeguimientoDao detalleServicio;

	@Inject
	@Getter
	private BeanSeguimientoClientes beanSeguimiento;
	
	@EJB
	@Getter
	private CampaniaDao campaniaDao;
	
	private Integer idCampania;

	@PostConstruct
	public void init() {
		try {
			getBeanSeguimiento().setSeguirIngresandoClientes(false);	
			getBeanSeguimiento().setSeguimientoSeleccionado(new SeguimientoClientes());

			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			getBeanSeguimiento().setListaCursos(new ArrayList<>());
			getBeanSeguimiento().setListaCursos(getCursosServicio().listaCursos());
			getBeanSeguimiento().setMedioContactoLlamada(false);
			getBeanSeguimiento().setMedioContactoVisita(false);
			getBeanSeguimiento().setTipoCargaInformacion(2);
			getBeanSeguimiento().setMotivosNoInteres(new ArrayList<String>());
			getBeanSeguimiento().getMotivosNoInteres().add("Precio alto");
			getBeanSeguimiento().getMotivosNoInteres().add("Distancia");
			getBeanSeguimiento().getMotivosNoInteres().add("Horarios");
			getBeanSeguimiento().getMotivosNoInteres().add("Certificación");
			getBeanSeguimiento().getMotivosNoInteres().add("Equivocado");
			getBeanSeguimiento().getMotivosNoInteres().add("No tiene interés");
			getBeanSeguimiento().setEsCampania(false);
			getBeanSeguimiento().setListaCampanias(new ArrayList<Campania>());
			getBeanSeguimiento().setListaCampanias(getCampaniaDao().listaCampanias());
			getBeanSeguimiento().setListaCampaniasTodas(new ArrayList<Campania>());
			getBeanSeguimiento().setListaCampaniasTodas(getCampaniaDao().listaCampanias());
			getBeanSeguimiento().getListaCampaniasTodas().forEach(f->{				
				String f1 = simpleDateFormat.format(f.getCampFechaDesde());
				String f2 =simpleDateFormat.format(f.getCampFechaHasta());
				f.setFechasCampania(f1.concat(" al ").concat(f2));
			});
			getBeanSeguimiento().setCampaniaSeleccionada(new Campania());
			getBeanSeguimiento().setListaPorLlamar(new ArrayList<SeguimientoClientes>());
			getBeanSeguimiento().setListaPorLlamar(getSeguimientoClientesServicioImpl().listaPendientesLlamada());
			
			

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void vaciarCodigos() {
		getBeanSeguimiento().setCodigoCurso(null);
		getBeanSeguimiento().setCodigoMedioInformacion(null);
		getBeanSeguimiento().setCodigoEstadoContacto(null);
		getBeanSeguimiento().setCodigoMedioContacto(null);
		getBeanSeguimiento().setCodigoDeseoCurso(null);
		getBeanSeguimiento().setCodigoUbicacionLlamada(null);
	}
	
	public void nuevoSeguimiento() {
		getBeanSeguimiento().setSeguirIngresandoClientes(true);
		getBeanSeguimiento().setProximaLlamada(null);
		getBeanSeguimiento().setMedioContactoLlamada(false);
		getBeanSeguimiento().setMedioContactoVisita(false);
		getBeanSeguimiento().setSeguimientoClientes(new SeguimientoClientes());
		getBeanSeguimiento().setDetalleSeguimiento(new DetalleSeguimiento());
		getBeanSeguimiento().setListaDetalle(new ArrayList<DetalleSeguimiento>());
		getBeanSeguimiento().setSeguimientoInicial(true);
		getBeanSeguimiento().getSeguimientoClientes().setSegcSolicitud("Informacion del curso");
		getBeanSeguimiento().setMotivosNoInteresSeleccionados(null);
		getBeanSeguimiento().setCampaniaSeleccionada(null);
		getBeanSeguimiento().setEsCampania(false);
		getBeanSeguimiento().setNohabilitaGrabar(false);
		vaciarCodigos();
		getBeanSeguimiento().getSeguimientoClientes().setSegcCorreo("sincorreo@ec.com");
		Mensaje.verDialogo("dlgNuevoSeguimiento");



	}
	
	public void filtrarPendientesLlamar() {
		try {
			Date fecha = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
			for(SeguimientoClientes sc: getBeanSeguimiento().getListaPorLlamar()) {    			    		
				Date date1 = dateFormat.parse(sc.getSegcProximaLlamada().toString());
				if(date1.before(fecha) || date1.equals(fecha))
					getBeanSeguimiento().getListaLlamarAhora().add(sc);				
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	public void grabarSeguimiento() {
		try {
			if(getBeanSeguimiento().getCodigoEstadoContacto().equals(EnumEstadosContactoCliente.ABANDONADO.getCodigo()) && getBeanSeguimiento().getSeguimientoClientes().getSegcMotivosNoMatricula().isEmpty()) {
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.noInteres"));
			}else {
				
				if(getBeanSeguimiento().getSeguimientoClientes().getSegcId() == null) {					
					Curso curso =new Curso();
					curso.setCursId(getBeanSeguimiento().getCodigoCurso());
					getBeanSeguimiento().getSeguimientoClientes().setCurso(curso);
					getBeanSeguimiento().getSeguimientoClientes().setSegcFechaSolicitud(new Date());
					getBeanSeguimiento().getSeguimientoClientes().setSegcFechaSeguimiento(new Date());

					getBeanSeguimiento().getSeguimientoClientes().setSegcMotivoCurso(getBeanSeguimiento().getCodigoDeseoCurso());
					getBeanSeguimiento().getSeguimientoClientes().setSegcMedioInformacion(getBeanSeguimiento().getCodigoMedioInformacion());
					getBeanSeguimiento().getSeguimientoClientes().setSegcUbicacionLlamada(getBeanSeguimiento().getCodigoUbicacionLlamada());
					if(getBeanSeguimiento().getSeguimientoClientes().getCampania()== null) {
						Campania camp = new Campania();
						camp.setCampId(0);
						getBeanSeguimiento().setCampaniaSeleccionada(camp);
						getBeanSeguimiento().getSeguimientoClientes().setCampania(camp);
					}
					DetalleSeguimiento dts = getBeanSeguimiento().getListaDetalle().get(getBeanSeguimiento().getListaDetalle().size()-1);
					getBeanSeguimiento().getSeguimientoClientes().setSegcUltimoSeguimiento(dts.getDsegObservacion());
					getBeanSeguimiento().getSeguimientoClientes().setSegcProximaLlamada(getBeanSeguimiento().getProximaLlamada());
					getSeguimientoClientesServicioImpl().agregarSeguimiento(getBeanSeguimiento().getSeguimientoClientes(), getBeanSeguimiento().getListaDetalle());
					
				}else{
					getBeanSeguimiento().getSeguimientoClientes().setSegcMotivoCurso(getBeanSeguimiento().getCodigoDeseoCurso());
					getBeanSeguimiento().getSeguimientoClientes().setSegcProximaLlamada(getBeanSeguimiento().getProximaLlamada());
					DetalleSeguimiento dts = getBeanSeguimiento().getListaDetalle().get(getBeanSeguimiento().getListaDetalle().size()-1);
					getBeanSeguimiento().getSeguimientoClientes().setSegcUltimoSeguimiento(dts.getDsegObservacion());
					getBeanSeguimiento().getSeguimientoClientes().setSegcFechaSeguimiento(new Date());
					getSeguimientoClientesServicioImpl().agregarSeguimiento(getBeanSeguimiento().getSeguimientoClientes(), getBeanSeguimiento().getListaDetalle());
					
				}
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregar"));
				cargarSeguimientoCampania();
				vaciarCodigos();				
				Mensaje.ocultarDialogo("dlggrabar");
				
				if(!getBeanSeguimiento().isSeguirIngresandoClientes())
					Mensaje.ocultarDialogo("dlgNuevoSeguimiento");
				else {
			    	getBeanSeguimiento().setSeguimientoClientes(new SeguimientoClientes());
			    	getBeanSeguimiento().setListaDetalle(new ArrayList<>());
			    	getBeanSeguimiento().setSeguimientoInicial(true);
			    	getBeanSeguimiento().setCampaniaSeleccionada(null);
			    	getBeanSeguimiento().setSeguimientoInicial(true);
			    	getBeanSeguimiento().getSeguimientoClientes().setSegcCorreo("sincorreo@ec.com");
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarDialogoGrabar() {
		boolean existeDetalleNuevo=false;
		for(DetalleSeguimiento ds:getBeanSeguimiento().getListaDetalle()) {
			if(ds.getDsegId() == null) {
				existeDetalleNuevo = true;
			}
		}
		if(existeDetalleNuevo)
			Mensaje.verDialogo("dlggrabar");
		else
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.agregarSeguimiento"));
	}
	public void cancelarIngresoSeguimiento() {
		getBeanSeguimiento().setDetalleSeguimiento(new DetalleSeguimiento());
		getBeanSeguimiento().setListaDetalle(new ArrayList<DetalleSeguimiento>());
		getBeanSeguimiento().setSeguimientoClientes(new SeguimientoClientes());
		getBeanSeguimiento().setCodigoCurso(null);
		getBeanSeguimiento().setCodigoMedioContacto(null);
		getBeanSeguimiento().setCodigoMedioInformacion(null);
		Mensaje.ocultarDialogo("dlgNuevoSeguimiento");
	}
	public void removerDetalle() {
		
		if(getBeanSeguimiento().getDetalleSeguimiento().getDsegId() == null) {
			getBeanSeguimiento().getListaDetalle().remove(getBeanSeguimiento().getDetalleSeguimiento());
		}else {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.borraDetalle"));
		}
	}
	public void agregarDetalle() {		
		if(getBeanSeguimiento().getCodigoEstadoContacto().equals(EnumEstadosContactoCliente.ABANDONADO.getCodigo()) && 
				(getBeanSeguimiento().getSeguimientoClientes().getSegcMotivosNoMatricula() == null || getBeanSeguimiento().getSeguimientoClientes().getSegcMotivosNoMatricula().isEmpty() )) {			
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.noInteres"));
		}else {
			getBeanSeguimiento().getDetalleSeguimiento().setSeguimientoClientes(getBeanSeguimiento().getSeguimientoClientes());
			getBeanSeguimiento().getDetalleSeguimiento().setDsegFecha(new Date());
			getBeanSeguimiento().getDetalleSeguimiento().setDsegMedioContacto(getBeanSeguimiento().getCodigoMedioContacto());
			getBeanSeguimiento().getDetalleSeguimiento().setDsegEstado(getBeanSeguimiento().getCodigoEstadoContacto());
			getBeanSeguimiento().getSeguimientoClientes().setSegcEstado(getBeanSeguimiento().getCodigoEstadoContacto());
			getBeanSeguimiento().getSeguimientoClientes().setSegcFechaSeguimiento(new Date());
			getBeanSeguimiento().getDetalleSeguimiento().setDsegProximaLlamada(getBeanSeguimiento().getProximaLlamada());
			getBeanSeguimiento().getListaDetalle().add(getBeanSeguimiento().getDetalleSeguimiento());

			getBeanSeguimiento().setMotivosNoInteresSeleccionados(null);
			getBeanSeguimiento().setDetalleSeguimiento(new DetalleSeguimiento());
		}
		
	}
	
	public void cargarSeguimientoCampania() {
		try {
			getBeanSeguimiento().setListadoSeguimiento(new ArrayList<SeguimientoClientes>());
			getBeanSeguimiento().setListadoSeguimiento(getSeguimientoClientesServicioImpl().listaSeguimientoCampania(getBeanSeguimiento().getCampaniaSeleccionada().getCampId()));				
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void cargarSeguimiento() {
		try {
			getBeanSeguimiento().setListadoSeguimiento(new ArrayList<SeguimientoClientes>());
			getBeanSeguimiento().setListadoSeguimiento(getSeguimientoClientesServicioImpl().listaSeguimiento());				
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void cargaDetalleSeguimiento() {
		try {
			getBeanSeguimiento().setListaDetalle(new ArrayList<DetalleSeguimiento>());
			getBeanSeguimiento().setListaDetalle(getDetalleServicio().listaDetalle(getBeanSeguimiento().getSeguimientoClientes().getSegcId()));
			
			getBeanSeguimiento().setTrazabilidadObj(new ArrayList<DetalleSeguimiento>());
			for(DetalleSeguimiento dt:getBeanSeguimiento().getListaDetalle()) {
				StringBuilder datos= new StringBuilder();
				datos.append(dt.getDsegFecha()).append(" ").append(dt.getDsegObservacion());
				
				getBeanSeguimiento().getTrazabilidadObj().add(dt);
			}
			Mensaje.verDialogo("dlgDetalleSeguimiento");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void agregarNuevoSeguimiento() {
		try {
			getBeanSeguimiento().setSeguirIngresandoClientes(false);
			getBeanSeguimiento().setProximaLlamada(null);
			getBeanSeguimiento().setMedioContactoLlamada(false);
			getBeanSeguimiento().setMedioContactoVisita(false);
			getBeanSeguimiento().setCodigoEstadoContacto("");
			getBeanSeguimiento().setCampaniaSeleccionada(null);
			getBeanSeguimiento().getSeguimientoClientes().setSegcProximaLlamada(null);
			if(getBeanSeguimiento().getSeguimientoClientes().getSegcEstado().equals(EnumEstadosContactoCliente.ABANDONADO.getCodigo())) {
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.noSeguimiento"));
			}else {
				getBeanSeguimiento().setDetalleSeguimiento(new DetalleSeguimiento());
				getBeanSeguimiento().setListaDetalle(getDetalleServicio().listaDetalle(getBeanSeguimiento().getSeguimientoClientes().getSegcId()));
				getBeanSeguimiento().setSeguimientoInicial(false);
				getBeanSeguimiento().setCodigoCurso(getBeanSeguimiento().getSeguimientoClientes().getCurso().getCursId());
				getBeanSeguimiento().setCodigoMedioInformacion(getBeanSeguimiento().getSeguimientoClientes().getSegcMedioInformacion());
				getBeanSeguimiento().setCodigoDeseoCurso(getBeanSeguimiento().getSeguimientoClientes().getSegcMotivoCurso());
				getBeanSeguimiento().setCodigoUbicacionLlamada(getBeanSeguimiento().getSeguimientoClientes().getSegcUbicacionLlamada());
				
				if(getBeanSeguimiento().getSeguimientoClientes().getCampania()!=null) {
					getBeanSeguimiento().setEsCampania(true);
					getBeanSeguimiento().setCampaniaSeleccionada(getBeanSeguimiento().getSeguimientoClientes().getCampania());
				}else {
					getBeanSeguimiento().setEsCampania(false);
					getBeanSeguimiento().setCampaniaSeleccionada(new Campania());
				}
				

					
					
				Mensaje.verDialogo("dlgNuevoSeguimiento");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarDialogoMotivos() {
		if(getBeanSeguimiento().getCodigoEstadoContacto().equals(EnumEstadosContactoCliente.ABANDONADO.getCodigo())) {
			getBeanSeguimiento().setMotivosNoInteresSeleccionados(null);
			Mensaje.verDialogo("dlgmotivos");
		}
	}
	
	public void registrarMotivosNoInteres() {
		getBeanSeguimiento().getSeguimientoClientes().setSegcMotivosNoMatricula(String.join(",", getBeanSeguimiento().getMotivosNoInteresSeleccionados()));
		Mensaje.ocultarDialogo("dlgmotivos");
	}
	
	public void mostrarDialogoCampania() {
		getBeanSeguimiento().setCampaniaSeleccionada(new Campania());
		
		if(getBeanSeguimiento().isEsCampania()) 			
			Mensaje.verDialogo("dlgcampanias");
		
	}
	
	public void cerrarDialogoCampania() {		
		getBeanSeguimiento().getSeguimientoClientes().setCampania(getBeanSeguimiento().getCampaniaSeleccionada());
		Mensaje.ocultarDialogo("dlgcampanias");
	}
	public void mostrarDialogoDatosCampania() {
		Mensaje.verDialogo("dlgdatoscampania");
	}

	public void mostrarDialogoCargaInfo() {
		getBeanSeguimiento().setTipoCargaInformacion(2);
		getBeanSeguimiento().setCampaniaSeleccionada(new Campania());
		getBeanSeguimiento().setCursoSeleccionado(new Curso());
		Mensaje.verDialogo("dlgCargarInfo");
		if(getBeanSeguimiento().getListaPorLlamar().size()>0) {			
			getBeanSeguimiento().setListaLlamarAhora(new ArrayList<SeguimientoClientes>());
			filtrarPendientesLlamar();
			Mensaje.verDialogo("dlgLlamar");
		}
	}
	public void cargarInformacion() {
		try {
			getBeanSeguimiento().setListadoSeguimiento(new ArrayList<SeguimientoClientes>());
			if(getBeanSeguimiento().getTipoCargaInformacion() ==2) {				
				getBeanSeguimiento().setListadoSeguimiento(new ArrayList<SeguimientoClientes>());
				getBeanSeguimiento().setListadoSeguimiento(getSeguimientoClientesServicioImpl().listaSeguimientoCampania(getBeanSeguimiento().getCampaniaSeleccionada().getCampId()));
				idCampania = getBeanSeguimiento().getCampaniaSeleccionada().getCampId();
			}else if(getBeanSeguimiento().getTipoCargaInformacion() == 3){
				getBeanSeguimiento().setListadoSeguimiento(new ArrayList<SeguimientoClientes>());
				getBeanSeguimiento().setListadoSeguimiento(getSeguimientoClientesServicioImpl().listaSeguimientoCampaniaCurso(getBeanSeguimiento().getCursoSeleccionado().getCursId()));
			}else if(getBeanSeguimiento().getTipoCargaInformacion() == 4){
				getBeanSeguimiento().setListadoSeguimiento(new ArrayList<SeguimientoClientes>());
				getBeanSeguimiento().setListadoSeguimiento(getSeguimientoClientesServicioImpl().listaSeguimientoCampaniaFechas(getBeanSeguimiento().getFechaInicio(), getBeanSeguimiento().getFechaFin()));
			}
			
			Mensaje.ocultarDialogo("dlgCargarInfo");
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.cargarInfo"));	
		}catch(Exception e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.generico"));	
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarInformacion " + ": ").append(e.getMessage()));
		}
	}
	
	public void localizaCampaniaCurso() {
		try {
			Campania campania = new Campania();
			campania = getCampaniaDao().campaniaCurso(getBeanSeguimiento().getCodigoCurso());
			int codigocampania=0;
			if(campania!=null) {
				codigocampania= campania.getCampId();
				getBeanSeguimiento().setCampaniaSeleccionada(new Campania());
				getBeanSeguimiento().setCampaniaSeleccionada(campania);
				getBeanSeguimiento().getSeguimientoClientes().setCampania(getBeanSeguimiento().getCampaniaSeleccionada());
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.cursoCampania").concat(" ") + getBeanSeguimiento().getCampaniaSeleccionada().getCampDescripcion());	
				Mensaje.actualizarComponente("growl");
			}else {
				getBeanSeguimiento().getSeguimientoClientes().setCampania(null);
				getBeanSeguimiento().setCampaniaSeleccionada(null);
			}
			if(validaNumeroTelefono()!=null) {
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.telefono"));	

			}else
				getBeanSeguimiento().setNohabilitaGrabar(false);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void actualizarCliente() {
		try {
			getSeguimientoClientesServicioImpl().actualizarSeguimiento(getBeanSeguimiento().getSeguimientoClientes());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.actualizar"));	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarDlgSeguimiento() {
		beanSeguimiento.setListadoSeguimientoExcel(new ArrayList<SeguimientoClientes>());
		beanSeguimiento.setArchivoExcel(null);
		Mensaje.verDialogo("dlgExcel");
	}
	public void mostrarDlgFormulario() {
		getBeanSeguimiento().setCampaniaSeleccionada(null);
		getBeanSeguimiento().setHabilitaCargaFormFaces(true);
		getBeanSeguimiento().setNumeroPreguntas(null);
		getBeanSeguimiento().setListadoLeadsForm(new ArrayList<FormFacebookAdsRecord>());
		Mensaje.verDialogo("dlgActualizaDesdeExcel");
		Mensaje.actualizarComponente("pnlActualizaDesdeExcel");
	}
	public void habilitaCargaFormFacebook() {
		getBeanSeguimiento().setHabilitaCargaFormFaces(false);
	}
	
	public void activaUploadFormulario() {
		if(getBeanSeguimiento().getCampaniaSeleccionada().getCampId()>0)
			getBeanSeguimiento().setActivaUploadFormulario(true);
		else
			getBeanSeguimiento().setActivaUploadFormulario(false);
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		beanSeguimiento.setArchivoExcel(null);
        beanSeguimiento.setArchivoExcel(event.getFile());
        try {
        	InputStream inp = beanSeguimiento.getArchivoExcel().getInputStream();
        	if (beanSeguimiento.getArchivoExcel() != null && beanSeguimiento.getArchivoExcel().getContent() != null && beanSeguimiento.getArchivoExcel().getContent().length > 0 && beanSeguimiento.getArchivoExcel().getFileName() != null) {
            	beanSeguimiento.setListadoSeguimientoExcel(seguimientoClientesService.parseArchivoExcelSeguimiento(inp));
            	Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.archivoCargado"));
        	}
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
	
	public void handleFileUploadFormulario(FileUploadEvent event) {
		beanSeguimiento.setArchivoExcel(null);
        beanSeguimiento.setArchivoExcel(event.getFile());
        try {
        	InputStream inp = beanSeguimiento.getArchivoExcel().getInputStream();
        	if (beanSeguimiento.getArchivoExcel() != null && beanSeguimiento.getArchivoExcel().getContent() != null && beanSeguimiento.getArchivoExcel().getContent().length > 0 && beanSeguimiento.getArchivoExcel().getFileName() != null) {
            	beanSeguimiento.setListadoLeadsForm(seguimientoClientesService.parseArchivoFormularioFacebook(inp, getBeanSeguimiento().getNumeroPreguntas(), null));
            	Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.archivoCargado"));
        	}
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
	
	
	public void procesarSubidaFormulario() {
		try {
			if (getBeanSeguimiento().getListadoSeguimientoExcel().size() > 0) {
				List<SeguimientoClientes> listaAux = getSeguimientoClientesServicioImpl().listaSeguimientoCampania(getBeanSeguimiento().getCampaniaSeleccionada().getCampId());
				seguimientoClientesService.procesaListaFormulario(listaAux, getBeanSeguimiento().getListadoSeguimientoExcel(), getBeanSeguimiento().getCampaniaSeleccionada(), getBeanSeguimiento().isCamposAdicionales());
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.actualizar"));
				Mensaje.ocultarDialogo("dlgFormulario");
			} else {
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.nodatos"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void procesarSubidaExcel() {
		try {
			if (getBeanSeguimiento().getListadoSeguimientoExcel().size() > 0) {
				seguimientoClientesService.procesarSubidaExcel(getBeanSeguimiento().getListadoSeguimientoExcel());
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.actualizar"));
				Mensaje.ocultarDialogo("dlgExcel");
			} else {
				Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.nodatos"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void procesarFormularioFacebook() {
		try {
			seguimientoClientesService.procesarFormularioFacebook(getBeanSeguimiento().getListadoLeadsForm(), getBeanSeguimiento().getCampaniaSeleccionada());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.agregar"));
			Mensaje.ocultarDialogo("dlgActualizaDesdeExcel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SeguimientoClientes validaNumeroTelefono() {
		SeguimientoClientes seg=null;
		String numero=getBeanSeguimiento().getSeguimientoClientes().getSegcTelefono();
		int curso=0;
		int campania=0;
		try {
			if(getBeanSeguimiento().getCodigoCurso()==null)
				curso=0;
			else
				curso=getBeanSeguimiento().getCodigoCurso();
			if(getBeanSeguimiento().getCampaniaSeleccionada()==null)
				campania=0;
			else
				campania=getBeanSeguimiento().getCampaniaSeleccionada().getCampId();
			if(curso>0)
				seg= getSeguimientoClientesServicioImpl().validaNumero(numero, curso, campania);
			if(seg!=null && getBeanSeguimiento().getSeguimientoClientes().getSegcId()==null)
				getBeanSeguimiento().setNohabilitaGrabar(true);
			else
				getBeanSeguimiento().setNohabilitaGrabar(false);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return seg;
	}
	
	public void habilitaMedioMotivoSol() {
		if(getBeanSeguimiento().getDetalleSeguimiento().isDsegContestoLlamada())  
			getBeanSeguimiento().setContestoLlamada(true);			
		else
			getBeanSeguimiento().setContestoLlamada(false);		
	}
	public void habilitaContestoLlamada() {
		if(getBeanSeguimiento().getCodigoMedioContacto().equals(EnumMedioContacto.LLAMADATELEFONICA.getCodigo())) { 
			getBeanSeguimiento().setMedioContactoLlamada(true);
			getBeanSeguimiento().setMedioContactoVisita(false);
		}else if(getBeanSeguimiento().getCodigoMedioContacto().equals(EnumMedioContacto.VISITAINSITU.getCodigo())) {
			getBeanSeguimiento().setMedioContactoLlamada(false);
			getBeanSeguimiento().setMedioContactoVisita(true);
			getBeanSeguimiento().setContestoLlamada(false);		
			getBeanSeguimiento().getDetalleSeguimiento().setDsegContestoLlamada(false);
		}else {
			getBeanSeguimiento().setMedioContactoLlamada(false);
			getBeanSeguimiento().setMedioContactoVisita(false);
			getBeanSeguimiento().setContestoLlamada(false);		
		}
	}
	
	public void mostrarDialogoEnvioMensaje() {
		Mensaje.verDialogo("dlgMensajeMasivo");
	}
	
//	public void enviarMensajeMasivo() {
//		
//		
//		try {
//			EnviarWhastapp whatsapp = new EnviarWhastapp();
////			whatsapp.enviarMensaje(getBeanLogin());
//			whatsapp.enviaMensajeAsync();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	/**
	 * Permite anular la proxima llamada
	 */
	public void anularPendienteLlamada() {
		try {
			getBeanSeguimiento().getSeguimientoClientes().setSegcProximaLlamada(null);
			getBeanSeguimiento().getListaPorLlamar().remove(getBeanSeguimiento().getSeguimientoClientes());
			getSeguimientoClientesServicioImpl().actualizarSeguimiento(getBeanSeguimiento().getSeguimientoClientes());
			Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, getMensajesBacking().getPropiedad("info"), getMensajesBacking().getPropiedad("info.procesoexito"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void onRowSelect(SelectEvent<SeguimientoClientes> event) {
		
		getBeanSeguimiento().setSeguimientoClientes(event.getObject());
		agregarNuevoSeguimiento();
    }
 
    public void onRowUnselect(UnselectEvent<SeguimientoClientes> event) {

    }
    
}

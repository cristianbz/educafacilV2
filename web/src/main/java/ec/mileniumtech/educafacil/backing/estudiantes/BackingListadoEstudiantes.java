/**
Este software es propiedad de CEIMSCAP Cia.Ltda, el mismo que esta protegido por derechos de autor
*/

package ec.mileniumtech.educafacil.backing.estudiantes;


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.mileniumtech.educafacil.servicio.ReportesEstudiantesService;
import ec.mileniumtech.educafacil.backing.MensajesBacking;
import ec.mileniumtech.educafacil.bean.estudiantes.BeanListadoEstudiantes;
import ec.mileniumtech.educafacil.bean.usuarios.BeanLogin;
import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.impl.CursoDaoImpl;
import ec.mileniumtech.educafacil.dao.impl.MatriculaDaoImpl;
import ec.mileniumtech.educafacil.dao.impl.OfertaCursosDaoImpl;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Curso;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Matricula;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCursos;
import ec.mileniumtech.educafacil.utilitario.Mensaje;
import ec.mileniumtech.educafacil.utilitario.Utilitario;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumEstadosMatricula;
import ec.mileniumtech.educafacil.utilitarios.fechas.FechaFormato;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * @author [ Christian Baez ] cbaez Jan 29, 2020
 *
 */
@ViewScoped
@Named
public class BackingListadoEstudiantes implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BackingListadoEstudiantes.class);

	@Getter
	private StreamedContent fileDownload;
	@EJB
	@Getter
	private MatriculaDaoImpl matriculaServicioImpl;
	
	@Inject
	@Getter
	private BeanListadoEstudiantes beanListadoEstudiantes;
	@Getter
	@Inject
	private MensajesBacking mensajesBacking;

	@Inject
	@Getter	
	private BeanLogin beanLogin;
	
	@EJB
	@Getter
	private CursoDaoImpl cursosServicio;
	
	@EJB
	@Getter
	private OfertaCursosDaoImpl ofertaCursosServicio;

	@EJB
	@Getter
	private ReportesEstudiantesService reportesEstudiantesService;

	
	@PostConstruct
	public void iniciar() {
		nuevaBusqueda();
		try {
		getBeanListadoEstudiantes().setListaCursos(new ArrayList<>());
		getBeanListadoEstudiantes().setListaCursos(getCursosServicio().listaCursos());
		getBeanListadoEstudiantes().setOfertaSeleccionada(new OfertaCursos());
		getBeanListadoEstudiantes().getOfertaSeleccionada().setOcurFechaFin(new Date());
		getBeanListadoEstudiantes().getOfertaSeleccionada().setOcurFechaInicio(new Date());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Vacia los datos de la tabla
	 */
	public void vaciarTablaDatos() {
		getBeanListadoEstudiantes().setListaMatriculas(new ArrayList<>());
	}
	/**
	 * Permite buscar las matriculas/inscripciones/culimaciones existentes
	 */
	public void buscarMatriculas() {
		try {
			getBeanListadoEstudiantes().setListaMatriculas(new ArrayList<>());
//			getBeanListadoEstudiantes().setListaMatriculas(getMatriculaServicioImpl().listaMatriculasCurso(getBeanListadoEstudiantes().getCodigoEstadoMatricula(), getBeanListadoEstudiantes().getCursoSeleccionado().getCursId()));			
			getBeanListadoEstudiantes().setListaMatriculas(getMatriculaServicioImpl().listaMatriculadosPorOfertaCurso(getBeanListadoEstudiantes().getOfertaSeleccionada().getOcurId()));
			getBeanListadoEstudiantes().setListaMatriculas(getBeanListadoEstudiantes().getListaMatriculas().stream().sorted((m1,m2)-> m1.getEstudiante().getPersona().getPersApellidos().compareTo(m2.getEstudiante().getPersona().getPersApellidos())).collect(Collectors.toList()));
			if(getBeanListadoEstudiantes().getListaMatriculas()==null || getBeanListadoEstudiantes().getListaMatriculas().isEmpty()) {
				Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.noHayDatos"));
				Mensaje.actualizarComponente("growl");
			}
			Mensaje.ocultarDialogo("dlgBuscar");	
			
		}catch(DaoException e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.cargarMatriculas"));			
			log.error(new StringBuilder().append(this.getClass().getName() + "." + "buscarMatriculas" + ": ").append(e.getMessage()));			
		}
	}
	/**
	 * Realiza una nueva busqueda
	 */
	public void nuevaBusqueda() {

		getBeanListadoEstudiantes().setListaMatriculas(new ArrayList<>());
		getBeanListadoEstudiantes().setCodigoEstadoMatricula(null);
		getBeanListadoEstudiantes().setCodigoCurso(null);
		getBeanListadoEstudiantes().setAnioBusqueda(0);
		getBeanListadoEstudiantes().setCursoSeleccionado(new Curso());
		getBeanListadoEstudiantes().setListaOfertaCursos(new ArrayList<>());
	}
	/**
	 * Permite generar el documento pdf
	 */
	public void generarPdf() {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String logoQR = ctx.getRealPath("/") + File.separator + "imagenes" + File.separator + "logotipo" + File.separator + "logoQR.png";
		String logotipoURL = ctx.getRealPath("/")  + File.separator + "imagenes" + File.separator + "logotipo" + File.separator +"logoCEIMSCAP.png";
		try {
            byte[] pdfBytes = getReportesEstudiantesService().generarRegistroMatriculaPdf(
                getBeanListadoEstudiantes().getMatriculaSeleccionada(),
                getBeanLogin().getConfiguraciones().getConfEmpresa(),
                logotipoURL,
                logoQR
            );

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=" + getBeanListadoEstudiantes().getMatriculaSeleccionada().getEstudiante().getPersona().getPersApellidos() + "-" + getBeanListadoEstudiantes().getMatriculaSeleccionada().getMatrId() + ".pdf");
			response.getOutputStream().write(pdfBytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();

		} catch (Exception e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.crearPdf"));			
			log.error(this.getClass().getName() + ".generarPdf: " + e.getMessage(), e);			
		}
	}

	public void generaCertificadoPdf() {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String logoQR = ctx.getRealPath("/") + File.separator + "imagenes" + File.separator + "logotipo" + File.separator + "logoQR.png";
		String certificadoBg = ctx.getRealPath("/")  + File.separator + "imagenes" + File.separator + "certificado" + File.separator +"certificadoDigital.png";
		try {
            byte[] pdfBytes = getReportesEstudiantesService().generarCertificadoSoporteDigitalPdf(
                getBeanListadoEstudiantes().getMatriculaSeleccionada(),
                certificadoBg,
                logoQR
            );

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=CERT-" + getBeanListadoEstudiantes().getMatriculaSeleccionada().getEstudiante().getPersona().getPersApellidos() + ".pdf");
			response.getOutputStream().write(pdfBytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();

		} catch (Exception e) {
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, getMensajesBacking().getPropiedad("error"), getMensajesBacking().getPropiedad("error.crearPdf"));			
			log.error(this.getClass().getName() + ".generaCertificadoPdf: " + e.getMessage(), e);			
		}
	}

	public void mostrarDialogoBuscar() {
		
		nuevaBusqueda();
		Mensaje.verDialogo("dlgBuscar");
	}
	
	public void cargarOfertaCursos() {
		try {
			getBeanListadoEstudiantes().setListaOfertaCursos(new ArrayList<>());
			getBeanListadoEstudiantes().setListaOfertaCursos(getOfertaCursosServicio().listaOfertaCursosPorCursoAnio(getBeanListadoEstudiantes().getCursoSeleccionado().getCursId(),getBeanListadoEstudiantes().getAnioBusqueda()));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public String formatDate(Date fecha, String pattern) {
        return (new SimpleDateFormat(pattern)).format(fecha);
    }
	
	public void csvMoodle() {
		try {
            byte[] csvBytes = getReportesEstudiantesService().generarCsvUsuariosMoodle(getBeanListadoEstudiantes().getListaMatriculas());
			
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			response.setContentType("application/csv");
			response.setHeader("Content-Disposition", "attachment; filename=usuariosMoodle.csv");
			response.getOutputStream().write(csvBytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void generarReporte() {
		try {
			String infoCurso = getBeanListadoEstudiantes().getCursoSeleccionado().getCursNombre() + " / " + 
							   formatDate(getBeanListadoEstudiantes().getOfertaSeleccionada().getOcurFechaInicio(), "yyyy-MM-dd") + " al " + 
							   formatDate(getBeanListadoEstudiantes().getOfertaSeleccionada().getOcurFechaFin(), "yyyy-MM-dd") + " " + 
							   getBeanListadoEstudiantes().getOfertaSeleccionada().getOcurHorario();

			InputStream reportStream = getClass().getResourceAsStream("/reports/reporteMatriculadosCurso.jasper");
			
			byte[] pdfBytes = getReportesEstudiantesService().generarReporteJasper(
				getBeanListadoEstudiantes().getListaMatriculas(), 
				infoCurso, 
				reportStream
			);
			
			DefaultStreamedContent.Builder builder = DefaultStreamedContent.builder()
					.stream(() -> new ByteArrayInputStream(pdfBytes))
					.contentType("application/pdf")
					.name("reporteMatriculadosCurso.pdf");
			fileDownload = builder.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

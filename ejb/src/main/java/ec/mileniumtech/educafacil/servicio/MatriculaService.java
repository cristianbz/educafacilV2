package ec.mileniumtech.educafacil.servicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import ec.mileniumtech.educafacil.dao.excepciones.DaoException;
import ec.mileniumtech.educafacil.dao.excepciones.EntidadDuplicadaException;
import ec.mileniumtech.educafacil.dao.MatriculaDao;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Empresa;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Estudiante;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Matricula;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.OfertaCursos;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Persona;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Campania;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Usuario;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Rol;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.UsuarioRol;
import ec.mileniumtech.educafacil.utilitarios.encriptacion.Encriptar;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumRol;
import ec.mileniumtech.educafacil.utilitarios.correo.Correo;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumEstadosMatricula;

@Stateless
public class MatriculaService {

	@EJB
	private MatriculaDao matriculaServicioImpl;

	public void grabarMatricula(Persona persona, Matricula matricula, Estudiante estudiante, OfertaCursos ofertaCursosSeleccionado, boolean isEsInscripcion, String codigoCargo, String codigoNivelEstudio, String codigoIngresosMensuales, String codigoMedioInformacion, Empresa empresaSeleccionada) throws EntidadDuplicadaException, DaoException {
		List<Estudiante> listaEstudiante = new ArrayList<>();
		
		matricula.setOfertaCursos(ofertaCursosSeleccionado);
		matricula.setMatrFechaRegistro(new Date());
		Empresa empresa = new Empresa();
		empresa.setEmprId(1);

		if (persona.getPersId() > 0 && persona.getEstudiantes() != null && !persona.getEstudiantes().isEmpty()) {
			estudiante = persona.getEstudiantes().get(0);
		}

		if (isEsInscripcion) {
			matricula.setMatrFechaInscripcion(new Date());
			matricula.setMatrFacturacionEmpresa(false);
			matricula.setMatrEstado(EnumEstadosMatricula.INSCRITO.getCodigo());
			matricula.setEmpresa(empresa);
			persona.setPersFechaNacimiento(null);
		} else {
			matricula.setMatrFechaMatricula(new Date());
			matricula.setMatrEstado(EnumEstadosMatricula.MATRICULADO.getCodigo());
			estudiante.setEstuCargoOcupa(codigoCargo);
			estudiante.setEstuNivelEstudio(codigoNivelEstudio);
			matricula.setMatrMedioInformacion(codigoMedioInformacion);
			
			if (matricula.isMatrFacturacionEmpresa() && empresaSeleccionada != null) {
				matricula.setEmpresa(empresaSeleccionada);
			} else {
				matricula.setEmpresa(empresa);
			}
		}

		matricula.setEstudiante(estudiante);
		estudiante.setPersona(persona);
		estudiante.setEstuIngresosMensuales(codigoIngresosMensuales);
		matriculaServicioImpl.agregarMatriculaInscripcion(persona, matricula, null, null);
	}

	public Matricula existeMatricula(int ocurId, int estuId) throws DaoException {
		return matriculaServicioImpl.existeMatricula(ocurId, estuId);
	}

	public void grabarMatriculaOnline(Persona persona, Matricula matricula, Estudiante estudiante, OfertaCursos ofertaCursosSeleccionado, String codigoCargo, String codigoNivelEstudio, String codigoIngresosMensuales, String codigoMedioInformacion, Campania campania, String password, int ciudad, String sector, int provincia) throws EntidadDuplicadaException, DaoException {
		Empresa empresa = new Empresa();
		Usuario usuarioE = new Usuario();
		Rol rol = new Rol();
		empresa.setEmprId(1);
		
		persona.setPersProvincia(provincia);
		persona.setPersCiudad(ciudad);
		persona.setPersSector(sector);
		
		estudiante.setPersona(persona);
		estudiante.setEstuCargoOcupa(codigoCargo);
		estudiante.setEstuNivelEstudio(codigoNivelEstudio);
		estudiante.setEstuIngresosMensuales(codigoIngresosMensuales);
		
		matricula.setEstudiante(estudiante);
		matricula.setMatrFechaMatricula(new Date());
		matricula.setMatrFechaRegistro(new Date());
		matricula.setEmpresa(empresa);
		matricula.setMatrEstado(EnumEstadosMatricula.MATRICULADO.getCodigo());
		matricula.setOfertaCursos(ofertaCursosSeleccionado);
		matricula.setMatrMedioInformacion(codigoMedioInformacion);
		matricula.setCampania(campania);
		
		String cedula = persona.getPersDocumentoIdentidad();
		usuarioE.setUsuaUsuario(cedula);
		try {
            usuarioE.setUsuaClave(Encriptar.encriptarSHA512(password));
        } catch (Exception e) {
            throw new DaoException(e);
        }
		usuarioE.setUsuaFechaRegistro(matricula.getMatrFechaRegistro());
		usuarioE.setUsuaFechaInicio(ofertaCursosSeleccionado.getOcurFechaInicio());
		usuarioE.setUsuaFechaCaducidad(ofertaCursosSeleccionado.getOcurFechaFin());
		usuarioE.setPersona(persona);
		usuarioE.setUsuaEstado(true);
		
		rol.setRolId(EnumRol.ESTUDIANTE.getCodigo());
		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setRol(rol);
		usuarioRol.setUsuario(usuarioE);
		usuarioRol.setUrolEstado(true);
		
		matriculaServicioImpl.agregarMatriculaInscripcion(persona, matricula, usuarioE, usuarioRol);
	}

	public void enviarCorreo(boolean isEsInscripcion, String confEmpresa, String confServidorSmtp, String confUsuarioCorreo, String confClaveCorreo, String confEnviadoMailDesde, Persona persona, Matricula matricula) {
		StringBuilder contenido = new StringBuilder();
		contenido.append("<h1>").append(confEmpresa).append("</h1>");
		
		if (isEsInscripcion) {
			contenido.append("<h4>Estimado cliente, usted se ha inscrito en uno de nuestros cursos</h4>");
		} else {
			contenido.append("<h4>Estimado cliente, usted se ha matriculado en uno de nuestros cursos</h4>");
		}
		
		contenido.append("<table>");
		contenido.append("<tr><td><h4>NOMBRES:</h4></td><td>").append(persona.getPersNombres()).append("</td></tr>");
		contenido.append("<tr><td><h4>APELLIDOS:</h4></td><td>").append(persona.getPersApellidos()).append("</td></tr>");
		contenido.append("<tr><td><h4>CURSO:</h4></td><td>").append(matricula.getOfertaCursos().getOfertaCapacitacion().getCurso().getCursNombre()).append("</td></tr>");
		contenido.append("<tr><td><h4>FECHA INICIO:</h4></td><td>").append(matricula.getOfertaCursos().getOcurFechaInicio()).append("</td></tr>");
		contenido.append("<tr><td><h4>FECHA FIN:</h4></td><td>").append(matricula.getOfertaCursos().getOcurFechaFin()).append("</td></tr>");
		
		if (matricula.getOfertaCursos().getInstructor() != null && matricula.getOfertaCursos().getInstructor().getPersona() != null) {
			contenido.append("<tr><td><h4>INSTRUCTOR:</h4></td><td>")
				.append(matricula.getOfertaCursos().getInstructor().getPersona().getPersNombres()).append(" ")
				.append(matricula.getOfertaCursos().getInstructor().getPersona().getPersApellidos())
				.append("</td></tr>");
		}
		contenido.append("</table>");
		
		Correo correo = new Correo("Registro de inscripción en uno de nuestros cursos", contenido.toString(), true, persona.getPersCorreoElectronico(), null, confServidorSmtp, confUsuarioCorreo, confClaveCorreo, confEnviadoMailDesde);
		correo.start();
	}
}

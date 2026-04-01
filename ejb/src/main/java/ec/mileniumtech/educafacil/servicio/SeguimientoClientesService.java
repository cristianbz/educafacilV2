package ec.mileniumtech.educafacil.servicio;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ec.mileniumtech.educafacil.dao.SeguimientoClientesDao;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.Campania;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.DetalleSeguimiento;
import ec.mileniumtech.educafacil.modelo.persistencia.entity.SeguimientoClientes;
import ec.mileniumtech.educafacil.utilitarios.dto.registrodatos.FormFacebookAdsRecord;
import ec.mileniumtech.educafacil.utilitarios.dto.registrodatos.PreguntasFormFacesbookRecord;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumEstadosContactoCliente;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumMedioContacto;
import ec.mileniumtech.educafacil.utilitarios.enumeraciones.EnumMedioInformacion;
import ec.mileniumtech.educafacil.utilitarios.fechas.FechaFormato;

@Stateless
public class SeguimientoClientesService {

	@EJB
	private SeguimientoClientesDao seguimientoClientesServicioImpl;

	public List<SeguimientoClientes> parseArchivoExcelSeguimiento(InputStream inp) throws Exception {
		List<SeguimientoClientes> listadoSeguimientoExcel = new ArrayList<>();
		XSSFWorkbook libro = new XSSFWorkbook(inp);
		XSSFSheet sheet = libro.getSheetAt(0);

		int numerofilas = sheet.getLastRowNum();
		Row fila;
		Cell celda;

		for (int f = 0; f <= numerofilas; f++) {
			SeguimientoClientes seguimiento = new SeguimientoClientes();
			fila = sheet.getRow(f);
			celda = fila.getCell(0);
			seguimiento.setSegcId((int) celda.getNumericCellValue());
			if (seguimiento.getSegcId() == 0)
				break;
			celda = fila.getCell(1);
			seguimiento.setSegcCliente(celda.getStringCellValue());
			celda = fila.getCell(6);
			seguimiento.setSegcUltimoSeguimiento(celda.getStringCellValue());
			celda = fila.getCell(3);
			seguimiento.setSegcEstado(celda.getStringCellValue());
			listadoSeguimientoExcel.add(seguimiento);
		}
		return listadoSeguimientoExcel;
	}

	public List<FormFacebookAdsRecord> parseArchivoFormularioFacebook(InputStream inp, int numeroPreguntas, List<SeguimientoClientes> listadoSeguimientoExcel) throws Exception {
		String pregunta1 = "";
		String pregunta2 = "";
		String pregunta3 = "";
		XSSFWorkbook libro = new XSSFWorkbook(inp);
		XSSFSheet sheet = libro.getSheetAt(0);
		
		int numerofilas = sheet.getLastRowNum();
		Row fila = sheet.getRow(0);
		Cell celda;
		List<FormFacebookAdsRecord> listadoLeadsForm = new ArrayList<>();

		if (numeroPreguntas > 0) {
			if (numeroPreguntas == 1) {
				celda = fila.getCell(1);
				pregunta1 = celda.getStringCellValue();
			} else if (numeroPreguntas == 2) {
				celda = fila.getCell(1);
				pregunta1 = celda.getStringCellValue();
				celda = fila.getCell(2);
				pregunta2 = celda.getStringCellValue();
			} else if (numeroPreguntas == 3) {
				celda = fila.getCell(1);
				pregunta1 = celda.getStringCellValue();
				celda = fila.getCell(2);
				pregunta2 = celda.getStringCellValue();
				celda = fila.getCell(3);
				pregunta3 = celda.getStringCellValue();
			}
		}

		for (int f = 1; f <= numerofilas; f++) {
			String respuesta1 = "";
			String respuesta2 = "";
			String respuesta3 = "";
			JsonObject pregResp1;
			JsonObject pregResp2;
			JsonObject pregResp3;
			fila = sheet.getRow(f);

			if (numeroPreguntas > 0) {
				if (numeroPreguntas == 1) {
					celda = fila.getCell(1);
					if (celda != null) {
						ByteBuffer buffer;
						respuesta1 = celda.getStringCellValue();
						buffer = StandardCharsets.ISO_8859_1.encode(respuesta1);
						respuesta1 = StandardCharsets.ISO_8859_1.decode(buffer).toString();
					} else
						respuesta1 = "";

					pregResp1 = Json.createObjectBuilder().add(pregunta1, respuesta1).build();
					listadoLeadsForm.add(new FormFacebookAdsRecord(pregResp1.toString(), devuelveValores(fila.getCell(4)), devuelveValores(fila.getCell(3)), devuelveValores(fila.getCell(2)), devuelveValores(fila.getCell(5)), devuelveValores(fila.getCell(6)), devuelveFechaProcesada(fila.getCell(0))));

				} else if (numeroPreguntas == 2) {
					celda = fila.getCell(1);
					if (celda != null) {
						respuesta1 = celda.getStringCellValue();
						ByteBuffer buffer = StandardCharsets.ISO_8859_1.encode(respuesta1);
						respuesta1 = StandardCharsets.ISO_8859_1.decode(buffer).toString();
					} else
						respuesta1 = "";

					celda = fila.getCell(2);
					if (celda != null) {
						respuesta2 = celda.getStringCellValue();
						ByteBuffer buffer2 = StandardCharsets.ISO_8859_1.encode(respuesta2);
						respuesta2 = StandardCharsets.ISO_8859_1.decode(buffer2).toString();
					} else
						respuesta2 = "";

					pregResp2 = Json.createObjectBuilder().add(pregunta1, respuesta1).add(pregunta2, respuesta2).build();
					listadoLeadsForm.add(new FormFacebookAdsRecord(pregResp2.toString(), devuelveValores(fila.getCell(5)), devuelveValores(fila.getCell(4)), devuelveValores(fila.getCell(3)), devuelveValores(fila.getCell(6)), devuelveValores(fila.getCell(7)), devuelveFechaProcesada(fila.getCell(0))));

				} else if (numeroPreguntas == 3) {
					celda = fila.getCell(1);
					if (celda != null) {
						respuesta1 = celda.getStringCellValue();
						ByteBuffer buffer = StandardCharsets.ISO_8859_1.encode(respuesta1);
						respuesta1 = StandardCharsets.ISO_8859_1.decode(buffer).toString();
					} else
						respuesta1 = "";

					celda = fila.getCell(2);
					if (celda != null) {
						respuesta2 = celda.getStringCellValue();
						ByteBuffer buffer2 = StandardCharsets.ISO_8859_1.encode(respuesta2);
						respuesta2 = StandardCharsets.ISO_8859_1.decode(buffer2).toString();
					} else
						respuesta2 = "";

					celda = fila.getCell(3);
					if (celda != null) {
						respuesta3 = celda.getStringCellValue();
						ByteBuffer buffer3 = StandardCharsets.ISO_8859_1.encode(respuesta3);
						respuesta3 = StandardCharsets.ISO_8859_1.decode(buffer3).toString();
					} else
						respuesta3 = "";

					pregResp3 = Json.createObjectBuilder().add(pregunta1, respuesta1).add(pregunta2, respuesta2).add(pregunta3, respuesta3).build();
					listadoLeadsForm.add(new FormFacebookAdsRecord(pregResp3.toString(), devuelveValores(fila.getCell(6)), devuelveValores(fila.getCell(5)), devuelveValores(fila.getCell(4)), devuelveValores(fila.getCell(7)), devuelveValores(fila.getCell(8)), devuelveFechaProcesada(fila.getCell(0))));
				}
			} else if (numeroPreguntas == 0) {
				listadoLeadsForm.add(new FormFacebookAdsRecord("", devuelveValores(fila.getCell(3)), devuelveValores(fila.getCell(2)), devuelveValores(fila.getCell(1)), devuelveValores(fila.getCell(4)), devuelveValores(fila.getCell(5)), devuelveFechaProcesada(fila.getCell(0))));
			}
		}
		return listadoLeadsForm;
	}

	public void procesarSubidaExcel(List<SeguimientoClientes> listadoSeguimientoExcel) throws Exception {
		if (listadoSeguimientoExcel.size() > 0) {
			SeguimientoClientes seguimiento = seguimientoClientesServicioImpl.seguimiento(listadoSeguimientoExcel.get(0).getSegcId());
			List<SeguimientoClientes> listaAux = new ArrayList<>();
			if (seguimiento.getCampania().getCampId() == 0)
				listaAux = seguimientoClientesServicioImpl.listaSeguimientoCampaniaCurso(seguimiento.getCurso().getCursId());
			else if (seguimiento.getCampania().getCampId() > 0)
				listaAux = seguimientoClientesServicioImpl.listaSeguimientoCampania(seguimiento.getCampania().getCampId());
			procesaListas(listaAux, listadoSeguimientoExcel);
		}
	}

	public void procesaListas(List<SeguimientoClientes> listaBase, List<SeguimientoClientes> listaExcel) throws Exception {
		for (SeguimientoClientes segC : listaBase) {
			for (SeguimientoClientes segE : listaExcel) {
				if (segC.getSegcId().equals(segE.getSegcId()) && !segC.getSegcUltimoSeguimiento().trim().equals(segE.getSegcUltimoSeguimiento().trim())) {
					SeguimientoClientes seguimiento = segC;
					DetalleSeguimiento detalle = new DetalleSeguimiento();
					List<DetalleSeguimiento> listaTemp = new ArrayList<>();
					String estado = "";
					
					if (segE.getSegcEstado().trim().equals(EnumEstadosContactoCliente.ABANDONADO.getLabel()))
						estado = EnumEstadosContactoCliente.ABANDONADO.getCodigo();
					else if (segE.getSegcEstado().trim().equals(EnumEstadosContactoCliente.CANDIDATO.getLabel()))
						estado = EnumEstadosContactoCliente.CANDIDATO.getCodigo();
					else if (segE.getSegcEstado().trim().equals(EnumEstadosContactoCliente.ENSEGUIMIENTO.getLabel()))
						estado = EnumEstadosContactoCliente.ENSEGUIMIENTO.getCodigo();
					else if (segE.getSegcEstado().trim().equals(EnumEstadosContactoCliente.MATRICULADO.getLabel()))
						estado = EnumEstadosContactoCliente.MATRICULADO.getCodigo();
					else if (segE.getSegcEstado().trim().equals(EnumEstadosContactoCliente.PROXIMAOCASION.getLabel()))
						estado = EnumEstadosContactoCliente.PROXIMAOCASION.getCodigo();
					
					seguimiento.setSegcEstado(estado);
					seguimiento.setSegcUltimoSeguimiento(segE.getSegcUltimoSeguimiento());
					seguimiento.setSegcFechaSeguimiento(new Date());

					detalle.setDsegId(0);
					detalle.setSeguimientoClientes(seguimiento);
					detalle.setDsegEstado(estado);
					detalle.setDsegFecha(new Date());
					detalle.setDsegObservacion(segE.getSegcUltimoSeguimiento());
					detalle.setDsegMedioContacto(EnumMedioContacto.LLAMADATELEFONICA.getCodigo());
					listaTemp.add(detalle);
					seguimientoClientesServicioImpl.agregarSeguimiento(seguimiento, listaTemp);
				}
			}
		}
	}

	public void procesarFormularioFacebook(List<FormFacebookAdsRecord> listadoLeadsForm, Campania campaniaSeleccionada) throws Exception {
		for (FormFacebookAdsRecord formulario : listadoLeadsForm) {
			SeguimientoClientes seguimiento = new SeguimientoClientes();
			DetalleSeguimiento detalle = new DetalleSeguimiento();
			List<DetalleSeguimiento> listaTemp = new ArrayList<>();
			String estado = "";

			switch (formulario.estado()) {
			case "EN SEGUIMIENTO":
				estado = EnumEstadosContactoCliente.ENSEGUIMIENTO.getCodigo();
				break;
			case "ABANDONADO":
				estado = EnumEstadosContactoCliente.ABANDONADO.getCodigo();
				break;
			case "CANDIDATO":
				estado = EnumEstadosContactoCliente.CANDIDATO.getCodigo();
				break;
			case "PROXIMAOCASION":
				estado = EnumEstadosContactoCliente.PROXIMAOCASION.getCodigo();
				break;
			case "MATRICULADO":
				estado = EnumEstadosContactoCliente.MATRICULADO.getCodigo();
				break;
			}

			seguimiento.setCampania(campaniaSeleccionada);
			seguimiento.setCurso(campaniaSeleccionada.getCurso());
			seguimiento.setSegcFechaSolicitud(FechaFormato.cambiarStringaDate(formulario.fecharegistro().toString(), "yyyy-MM-dd"));
			seguimiento.setSegcId(null);
			seguimiento.setSegcEstado(estado);
			seguimiento.setSegcUltimoSeguimiento(formulario.observacion());
			seguimiento.setSegcFechaSeguimiento(FechaFormato.cambiarStringaDate(formulario.fecharegistro().toString(), "yyyy-MM-dd"));
			seguimiento.setSegcMedioInformacion(EnumMedioInformacion.FACEBOOK.getCodigo());
			seguimiento.setSegcPregResp(formulario.preg_resp());
			seguimiento.setSegcCliente(formulario.nombre());
			seguimiento.setSegcCorreo(formulario.correo());
			seguimiento.setSegcTelefono(formulario.telefono());
			
			detalle.setDsegId(null);
			detalle.setSeguimientoClientes(seguimiento);
			detalle.setDsegEstado(estado);
			detalle.setDsegFecha(FechaFormato.cambiarStringaDate(formulario.fecharegistro().toString(), "yyyy-MM-dd"));
			detalle.setDsegObservacion(formulario.observacion());
			detalle.setDsegMedioContacto(EnumMedioContacto.WHATSAPP.getCodigo());
			listaTemp.add(detalle);
			
			seguimientoClientesServicioImpl.agregarSeguimiento(seguimiento, listaTemp);
		}
	}

	public void procesaListaFormulario(List<SeguimientoClientes> listaBase, List<SeguimientoClientes> listaExcel, Campania campaniaSeleccionada, boolean isCamposAdicionales) throws Exception {
		for (SeguimientoClientes segE : listaExcel) {
			boolean encontrado = false;
			for (SeguimientoClientes segC : listaBase) {
				if (segE.getSegcCliente().trim().equals(segC.getSegcCliente()))
					encontrado = true;
			}
			if (encontrado == false) {
				SeguimientoClientes seguimiento = new SeguimientoClientes();
				DetalleSeguimiento detalle = new DetalleSeguimiento();
				List<DetalleSeguimiento> listaTemp = new ArrayList<>();
				String estado = "";
				seguimiento = segE;
				estado = EnumEstadosContactoCliente.ENSEGUIMIENTO.getCodigo();
				seguimiento.setCampania(campaniaSeleccionada);
				seguimiento.setCurso(campaniaSeleccionada.getCurso());
				seguimiento.setSegcFechaSolicitud(new Date());
				seguimiento.setSegcId(null);
				seguimiento.setSegcEstado(estado);
				seguimiento.setSegcUltimoSeguimiento("Info enviada");
				seguimiento.setSegcFechaSeguimiento(new Date());
				seguimiento.setSegcMedioInformacion(EnumMedioInformacion.FACEBOOK.getCodigo());
				if (isCamposAdicionales)
					seguimiento.setSegcUbicacionLlamada(segE.getSegcUltimoSeguimiento());
				detalle.setDsegId(null);
				detalle.setSeguimientoClientes(seguimiento);
				detalle.setDsegEstado(estado);
				detalle.setDsegFecha(new Date());
				detalle.setDsegObservacion("Info enviada");
				detalle.setDsegMedioContacto(EnumMedioContacto.WHATSAPP.getCodigo());

				listaTemp.add(detalle);
				seguimientoClientesServicioImpl.agregarSeguimiento(seguimiento, listaTemp);
			}
		}
	}

	private String devuelveValores(Cell celda) {
		String resultado = "";
		try {
			if (celda != null) {
				ByteBuffer buffer;
				String respuesta = celda.getStringCellValue();
				buffer = StandardCharsets.ISO_8859_1.encode(respuesta);
				resultado = StandardCharsets.ISO_8859_1.decode(buffer).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado;
	}

	private LocalDate devuelveFechaProcesada(Cell celda) {
		String fecha = celda.getStringCellValue().substring(0, 10);
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fecha1 = null;
		try {
			fecha1 = LocalDate.parse(fecha, formato);
		} catch (DateTimeParseException e) {
			System.err.println("Error al parsear la fecha: " + e.getMessage());
		}
		return fecha1;
	}
}

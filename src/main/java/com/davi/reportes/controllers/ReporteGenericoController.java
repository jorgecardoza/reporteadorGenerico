package com.davi.reportes.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.davi.reportes.models.GenericResponse;
import com.davi.reportes.request.DocumentRequest;
import com.davi.reportes.service.ConsumeApiService;
import com.davi.reportes.utils.Constantes;
import com.davi.reportes.utils.DocxHandling;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
public class ReporteGenericoController {

	@Autowired
	private ConsumeApiService consumeApiService;
	@Autowired
	private DocxHandling docxHService;

	String ubicacion = "/home/jamaya/reportes/output/";
//	String ubicacion = "C:\\outPut\\";

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getpdf", method = RequestMethod.POST)

	public ResponseEntity<byte[]> getPDF2(@RequestBody DocumentRequest jsonRecibido)
			throws FileNotFoundException, IOException {
		GenericResponse<String> respuesta = new GenericResponse<String>();
		ResponseEntity<byte[]> response=null;
		try {

//Extraer el valor de los campos deljsonImput
			Gson gson = new Gson();

//		JsonObject jsonImpu = gson.fromJson(jsonRecibido, JsonObject.class);
			System.out.println("Json Recibido: " + jsonRecibido.toString());
			String reporte = jsonRecibido.getReporte();
			String sistema = jsonRecibido.getSistema();
			String producto = jsonRecibido.getProducto();
//		String fechaDia = jsonImpu.get("fechaDia").getAsString();
			System.out.println("Reporte:  " + reporte);
			System.out.println("Sistema: " + sistema);
			System.out.println("Producto: " + producto);

			String jsonRespuesta = consumeApiService.getReporte(reporte, sistema, producto);
			System.out.println("Respuesta Api Raul" + jsonRespuesta);
			JsonObject jsonRes = gson.fromJson(jsonRespuesta, JsonObject.class);

			// Convierte la cadena de texto JSON a un objeto JSON
			// JsonObject jsonObject = Jsoner.deserialize(jsonRespuesta, new JsonObject());
			// jsonImpu es el que se recibe de resposebody
			// es json de respuesta de la api de raul

			docxHService.setFilePathPdf(ubicacion + reporte + fecha() + "." + Constantes.EXTENSION_PDF);
			Map<String, Object> datosReporte = jsonRecibido.getDatosReporte();
			JsonArray campos = jsonRes.getAsJsonArray("campos");
			if (campos.size() != datosReporte.size()) {
				return (ResponseEntity<byte[]>) ResponseEntity.status(10);
			} else {
				docxHService.main(jsonRecibido, jsonRes);

			docxHService.main(jsonRecibido, jsonRes);

			byte[] contents = IOUtils.toByteArray(new FileInputStream(docxHService.getFilePathPdf()));

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			// Here you have to set the actual filename of your pdf

			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			}
			return response;

		} catch (Exception e) {
			e.getMessage();
			return (ResponseEntity<byte[]>) ResponseEntity.unprocessableEntity();
			// TODO: handle exception
		}
	}

	@RequestMapping(value = "/getBase64", method = RequestMethod.POST)
	public GenericResponse<String> getbase64(@RequestBody DocumentRequest jsonRecibido) throws Exception {// @RequestBody

		GenericResponse<String> respuesta = new GenericResponse<String>();
		try {

			System.out.println("REQUEST DE Recibido" + jsonRecibido);

			// Extraer el valor de los campos deljsonImput
			Gson gson = new Gson();

			String reporte = jsonRecibido.getReporte();
			String sistema = jsonRecibido.getSistema();
			String producto = jsonRecibido.getProducto();
			System.out.println("Reporte: " + reporte);
			System.out.println("Sistema: " + sistema);
			System.out.println("Producto: " + producto);

			docxHService.setFilePathPdf(ubicacion + reporte + fecha() + "." + Constantes.EXTENSION_PDF);
			String jsonRespuesta = consumeApiService.getReporte(reporte, sistema, producto);
//			 String jsonRespuesta="{\"campos\":[{\"ESTADO\":1,\"NOMBRE_CAMPO\":\"referencia\",\"ID_CAMPO\":373},{\"ESTADO\":1,\"NOMBRE_CAMPO\":\"fechaDia\",\"ID_CAMPO\":374},{\"ESTADO\":1,\"NOMBRE_CAMPO\":\"fechaMes\",\"ID_CAMPO\":374},{\"ESTADO\":1,\"NOMBRE_CAMPO\":\"fechaAnio\",\"ID_CAMPO\":374},{\"ESTADO\":1,\"NOMBRE_CAMPO\":\"montoAprobado\",\"ID_CAMPO\":374},{\"ESTADO\":1,\"NOMBRE_CAMPO\":\"nombreCliente\",\"ID_CAMPO\":374}],\"NOMBRE\":\"CARTAMOVILPYME\",\"URL\":\"C:\\\\Users\\\\61888\\\\Desktop\\\\AlbertoCardoza\\\\Credito Moviles PYME\\\\PLANTILLASGENERADASPORMI\\\\CARTAMOVILPYME.docx\",\"ESTADO\":1,\"COD_REPORTE\":\"CREMOPYME\",\"ID_REPORTE\":221,\"ID_SISTEMA\":2,\"ID_PRODUCTO\":1,\"FECHA_CREACION\":\"2023-09-01T16:32:56.339+00:00\"}";

			JsonObject jsonRes = gson.fromJson(jsonRespuesta, JsonObject.class);

//			JsonObject jsonRes = gson.fromJson(jsonRespuesta, JsonObject.class);
			System.out.println("Respuesta Api Raul" + jsonRespuesta);
			Map<String, Object> datosReporte = jsonRecibido.getDatosReporte();
			JsonArray campos = jsonRes.getAsJsonArray("campos");
			if (campos.size() != datosReporte.size()) {
				respuesta.setMessage(
						"El json recibido es diferente al json de respuesta. Por favor verifique su Joson de Peticion");
				respuesta.setCode(-1);
			} else {
				docxHService.main(jsonRecibido, jsonRes);

//			File tmpFile=new File(ubicacion+UUID.randomUUID().toString()+ "." + Constantes.EXTENSION_PDF);

				File tmpFile = new File(docxHService.getFilePathPdf());

				String base64 = docxHService.fileToStrBase64(tmpFile);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				// ResponseEntity<String> response = new ResponseEntity<>(base64, headers,
				// HttpStatus.OK);
				respuesta.setCode(0);
				respuesta.setMessage("PDF en Base64");
				respuesta.setResponse(base64);
			}
			return respuesta;

		} catch (Exception e) {
			respuesta.setCode(10);
			respuesta.setMessage("Error " + e.getMessage());
			respuesta.setResponse(null);
			return respuesta;

		}

	}

	private String fecha() {
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String fechaComoCadena = dateFormat.format(date);
		String fecha = fechaComoCadena.substring(0, 10);
		String hora = fechaComoCadena.substring(11, 13);
		String minuto = fechaComoCadena.substring(14, 16);
		String segundo = fechaComoCadena.substring(17, 19);
		String fechaCompleta = fecha + " " + hora + " " + minuto + " " + segundo;
		System.out.println("fecha completa" + fechaCompleta);

		return fechaCompleta;
	}
}

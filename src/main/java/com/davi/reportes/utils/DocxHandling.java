package com.davi.reportes.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
//import java.util.Base64;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

import com.davi.reportes.models.GenericResponse;
import com.davi.reportes.request.DocumentRequest;
import com.davi.reportes.request.RegistroAmortizacion;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
//import com.github.cliftonlabs.json_simple.JsonObject;
import com.google.gson.JsonObject;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

@Service
public class DocxHandling {
	String filePathPdf;

	public String getFilePathPdf() {
		return filePathPdf;
	}

	public void setFilePathPdf(String filePathPdf) {
		this.filePathPdf = filePathPdf;
	}

	private Map<String, String> textsToReplace;
	public String originalFilePath;

	public String getOriginalFilePath() {
		return originalFilePath;
	}

	private String temporalFileName;

	private XWPFDocument doc;

	public DocxHandling() {
	}

	public DocxHandling(Map<String, String> textsToReplace, String originalFilePath, String destinationPath) {
		this.textsToReplace = textsToReplace;
		this.originalFilePath = originalFilePath;
	}

	@SuppressWarnings("unused")
	public InputStream replaceTexts(DocumentRequest jsonImput, JsonObject jsonResponse) throws Exception {
		System.out.println("Entra al replaceText 1");
//		System.out.println("JSON DE API DE RAUL:*****" + jsonResponse);
		System.out.println("se inicia replaceText" + "ORIGINAL PATH" + originalFilePath); // Validamos que hayamos
																							// recibido el path del
																							// documento original
		if (originalFilePath == null)
			throw new NullPointerException("Debe proporcionar el path del archivo original");

		// Validamos que hayamos recibido los textos a reemplazar
		if (textsToReplace == null || textsToReplace.isEmpty())
			throw new Exception("Debe proporcionar los textos a reemplazar");

		// Generamos un nombre para nuestro archivo temporal
		temporalFileName = UUID.randomUUID().toString();

		// Abrimos nuestro documento
		doc = new XWPFDocument(new FileInputStream(originalFilePath));
		System.out.println("VIENDO QUE TIENE doc: " + doc);
		// System.out.println("estamos en el metodo que reemplaza");
		// comenzamos con la iteracion de los textos a reemplazar, se modifica para
		// utilizar con un arraylist o json
		// Desde base de datos se debe obtener esta informacion y hacerlo dinamico
		// cuando busque valores
		System.out.println("**se debe tomar la desicion de que pdf se va a generar");

		// Acceder a los campos dentro de datosReporte

//		Map<String, Object> datos = jsonImput.getDatosReporte();

//		JsonObject datosReporte = jsonImput.getAsJsonObject("datosReporte");
//		JsonObject tablaAmortizacion = jsonImput.getAsJsonObject("datosReporte");
//		ArrayList<RegistroAmortizacion> tablaAmortizacion=(ArrayList<RegistroAmortizacion>)datosReporte.get("tablaAmortizacion");

		System.out.println("****json de raul------" + jsonResponse);
		Map<String, Object> datosReporte = jsonImput.getDatosReporte();
		JsonArray campos = jsonResponse.getAsJsonArray("campos");
		System.out.println("Jsoan de Jaime" + datosReporte);
		System.out.println("Jsoan de Raul" + campos);

		boolean flagEncontrado = false; // Bandera para indicar si se encontró alguna coincidencia

		boolean flagCoincidencia = false;
		System.out.println("*****campos.size" + campos.size());
		System.out.println("******Datos reportes" + datosReporte.size());
		/*
		 * for (String key : datosReporte.keySet()) {
		 * System.out.println("valor de la key:" + key); flagCoincidencia = false; if
		 * (campos.size() != datosReporte.size()) {
		 * System.out.println("El array campos tiene menos elementos que el objeto");
		 * break; // Romper el ciclo principal }else {
		 * llamadaPrincipalDellReemplazo(datosReporte); }
		 * 
		 * for (int i = 0; i < campos.size(); i++) { JsonObject campoObj =
		 * campos.get(i).getAsJsonObject(); System.out.println("OBJECTSICE()---" +
		 * campoObj.size()); String nombreCampo =
		 * campoObj.get("NOMBRE_CAMPO").getAsString();
		 * System.out.println("VALOR DE NOMBRE DE CAMPO: " + nombreCampo);
		 * System.out.println("VALOR DEL KEY: " + key + " " + "nOMBRE CAMPO:  " +
		 * nombreCampo); if (nombreCampo.equals(key)) {
		 * 
		 * flagCoincidencia = true;
		 * 
		 * System.out.println( "La clave '" + nombreCampo +
		 * "' está en jsonInput con valor: " + datosReporte.get(key));
		 * 
		 * break; }
		 * 
		 * } //llamadaPrincipalDellReemplazo(datosReporte);
		 * System.out.println("EL CICLO CONTINUA......");
		 * 
		 * }
		 */

		llamadaPrincipalDellReemplazo(datosReporte);

		System.out.println("archivoTemporal " + temporalFileName);
		// Una vez se haya terminado de reemplazar, guardamos el documento en un archivo
		// temporal
		// Para ello general el archivo temporal que pasaremos a nuestro metodo que se
		// encarga de realizar la escritura
		String tmpDestinationPath = Files.createTempFile("temporalFileName", "." + Constantes.EXTENSION_DOCX)
				.toAbsolutePath().toString();
		System.out.println("tmpDestinationPath " + tmpDestinationPath);
		// Guardamos el documento en el archivo
		saveWord(tmpDestinationPath, doc);
		System.out.println("CONTENIDO DE DOC..." + doc);

		// Retornamos un ImputStream por si el usuario va a trabajar con el
		return new FileInputStream(tmpDestinationPath);

	}
//metod principal que llama metodos ya sea para barrer una tabla, parrafos y contruir tabla dinamica
	private void llamadaPrincipalDellReemplazo(Map<String, Object> datosReporte) throws Exception {

		String data = "";

		if (datosReporte.get("tablaAmortizacion") != null) {
			data = datosReporte.get("tablaAmortizacion").toString();
			System.out.println("dataaaaaaaaaaaaaaa" + data);
			RegistroAmortizacion[] tablaAmortizacionArray = new Gson().fromJson(data, RegistroAmortizacion[].class);
			ArrayList<RegistroAmortizacion> tablaAmortizacion = new ArrayList<>();
			for (int i = 0; i < tablaAmortizacionArray.length; i++) {
				tablaAmortizacion.add(tablaAmortizacionArray[i]);

			}
			System.out.println("lONGITUD DE TABLA AMOR" + tablaAmortizacion.size());
			// varDocto(datosReporte);//barre el documento y reemplaza
			iteraEncabezado(doc, datosReporte);// recorre el docto y escribe en los encabezados
			iteraParrafo(doc, datosReporte);// barre todo el documento y escribe en parrafos que no tengan tablas

			iteraTabla(doc, datosReporte);// barre el documento y escribe en tablas
			contruyeTabla(tablaAmortizacion);// llena la tabla dinamicamenmte
		} else {
			// varDocto(datosReporte);
			iteraParrafo(doc, datosReporte);
			iteraEncabezado(doc, datosReporte);
			iteraTabla(doc, datosReporte);
		}
	}

	private void contruyeTabla(ArrayList<RegistroAmortizacion> tablaAmortizacion) {
		// PARA CREAR TITULO DE TABLA
		XWPFTable tabla = doc.createTable(2, 1);
		tabla.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(9700));
		XWPFTableRow row = tabla.getRow(0);
//pone en tabla  el texto BANCO DAVIVIENA SA DE SV
		row.getCell(0).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
		row.getCell(0).setText("BANCO DAVIENDA SA DE CV");
		XWPFTableRow row1 = tabla.getRow(1);

		row1.getCell(0).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
		row1.getCell(0).setText("TABLA DE AMORTIZACION DE CREDITO TEORICA");

		/*
		 * // de esta manera se sentra texto XWPFParagraph paragraph1 =
		 * doc.createParagraph(); paragraph1.setAlignment(ParagraphAlignment.CENTER);
		 * XWPFParagraph paragraph = doc.createParagraph();
		 * paragraph.setAlignment(ParagraphAlignment.CENTER); XWPFRun run =
		 * paragraph.createRun();
		 * run.setText("TABLA DE AMORTIZACION DE CREDITO TEORICA."); XWPFRun run1 =
		 * paragraph1.createRun(); run1.setText("BANCO DAVIENDA SA DE CV");
		 */
		// tabla para llenar de los joson
		int tamanio = tablaAmortizacion.size() + 2;

		XWPFTable table = doc.createTable(tamanio, 6);
		XWPFParagraph paragraph = doc.createParagraph();
		XWPFRun run = paragraph.createRun();
//Columnas que forman la tabla
		table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(1400));
		table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(1850));
		table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(1300));
		table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(1300));
		table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(1300));
		table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(2600));

		XWPFTableRow encabezado = table.getRow(0);
		run.setFontSize(8);
//		run.setBold(false);
//		run.setColor("red");

		run.setFontFamily("Arial");
		run.setColor("45B39D");

		encabezado.getCell(0).setText("N° Cuotas");
		encabezado.getCell(1).setText("Fecha de Pago");
		encabezado.getCell(2).setText("Capital");
		encabezado.getCell(3).setText("Intereses");
		encabezado.getCell(4).setText("Seguros");
		encabezado.getCell(5).setText("Total Cuota a Pagar");

		for (int j = 0; j < tablaAmortizacion.size(); j++) {
			String numeroCuota = tablaAmortizacion.get(j).getNumeroCuota();
			String fechaPago = tablaAmortizacion.get(j).getFechaPago();
			String capital = tablaAmortizacion.get(j).getCapital();
			String interes = tablaAmortizacion.get(j).getInteres();
			String seguros = tablaAmortizacion.get(j).getSeguros();
			String totalCuotaPagar = tablaAmortizacion.get(j).getTotalCuotaPagar();

			/* actualmente nos pinta la tabla y alineada de aca en adelante */

			XWPFTableRow tableOneRowOne = table.getRow(j + 1);// para bajar una posicion la fila
//			
			tableOneRowOne.getCell(0).setText("");
			tableOneRowOne.getCell(1).setText("");
//			tableOneRowOne.getCell(2).getParagraphs().get(0).setAlignment(ParagraphAlignment.LEFT);
			tableOneRowOne.getCell(2).setText("$");
			tableOneRowOne.getCell(3).setText("$");
			tableOneRowOne.getCell(4).setText("$");
			tableOneRowOne.getCell(5).setText("$");
			for (int r = 0; r < 6; r++) {// alinea texto a la derecha r: es el numero de columnas
				tableOneRowOne.getCell(r).getParagraphs().get(0).setAlignment(ParagraphAlignment.RIGHT);// alinea a la
																										// derecha
			}
			tableOneRowOne.getCell(0).setText(numeroCuota);
			tableOneRowOne.getCell(1).setText(fechaPago);
			tableOneRowOne.getCell(2).setText(capital);
			tableOneRowOne.getCell(3).setText(interes);
			tableOneRowOne.getCell(4).setText(seguros);
			tableOneRowOne.getCell(5).setText(totalCuotaPagar);

		}

	}

	private void varDocto(Map<String, Object> datosReporte) {
		for (Map.Entry<String, Object> entry : datosReporte.entrySet()) {
			String variable = entry.getKey();
			String replace = String.valueOf(entry.getValue());

			String var = "{" + variable + "}";
			System.out.println("Variables:...>" + var + " \t Valor a reemplazar...>" + replace);

			doc = replaceText(doc, var, replace);

		}

	}

	private InputStream replaceTextsAndConvertToPDF(DocumentRequest jsonInput, JsonObject jsonResponse)
			throws Exception {
		try {
			System.out.println("llamamos el metodo");
			InputStream in = replaceTexts(jsonInput, jsonResponse);
//			DocxToPdfConverter cwoWord = new DocxToPdfConverter();
			System.out.println("que es in " + in);
			return convert(in);
		} catch (Exception e) {
			throw e;
		}
	}

	// En el se carga el proceso de sustituir las variables de word y convertir a
	// pdf
	public void main(DocumentRequest jsonImpu, JsonObject jsonRecibid) {

		System.out.println(UUID.randomUUID().toString());
		System.out.println("filePathPdf:---" + filePathPdf);

		String codReporte = "";
		String filePath = "";

		if (codReporte.isEmpty() && codReporte != null) {
			// leer del json de Raul.
			filePath = jsonRecibid.get("URL").getAsString();

		}

		try {
			DocxHandling handling = new DocxHandling();

			handling.setOriginalFilePath(filePath);
			handling.setFilePathPdf(filePathPdf);
			System.out.println("iniciamos ");

			handling.setTextsToReplace(Collections.singletonMap("<<nombre>>", "Luis Lemus"));
			InputStream in = handling.replaceTextsAndConvertToPDF(jsonImpu, jsonRecibid);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static XWPFDocument replaceText(XWPFDocument doc, String findText, String replaceText) {
		try {
			for (int i = 0; i < doc.getParagraphs().size(); ++i) {
				// Asignamos el parrafo a una variable para trabajar con ella
				XWPFParagraph s = doc.getParagraphs().get(i);

				// De ese parrafo recorremos todos los Runs
				for (int x = 0; x < s.getRuns().size(); x++) {
					// Asignamos el run en turno a una varibale
					XWPFRun run = s.getRuns().get(x);

					System.out.println("RUNS: " + s.getRuns().get(x));
					// Obtenemos el textoo
					String text = run.text();

					System.out.println("TEXT................." + text);

					if (text.contains("{")) {
						if (s.getRuns().get(x + 2).text().contains("}")) {
							if (("{" + s.getRuns().get(x + 1) + "}").contains(findText)) {
								String valor = findText.replace("{", "").replace("}", "");
								String replaced = s.getRuns().get(x + 1)
										.getText(s.getRuns().get(x + 1).getTextPosition()).replace(valor, replaceText);
								s.getRuns().get(x + 1).setText(replaced, 0);
								s.getRuns().get(x).setText("", 0);
								s.getRuns().get(x + 2).setText("", 0);
							}
						}
					}
					/*
					 * if (text.contains(findText)) { // System.out.println("replaceText "+findText
					 * + " "+replaceText); // Si lo contiene lo reemplazamos y guardamos en una
					 * variable String replaced =
					 * run.getText(run.getTextPosition()).replace(findText, replaceText); // Pasamos
					 * el texto nuevo al run run.setText(replaced, 0);
					 * 
					 * }
					 */

				}
			}
			// Retornamos el documento con los textos ya reemplazados
			return doc;

		} catch (Exception e) {
//		e.getMessage();
			System.out.println("Errer: " + e.getMessage());
			return null;
			// TODO: handle exception
		}

	}

	private static void saveWord(String filePath, XWPFDocument doc) throws FileNotFoundException, IOException {
		FileOutputStream out = null;
		System.out.println("----Entra al Metodo SaveWord");
		try {

			out = new FileOutputStream(filePath);
			doc.write(out);
		} finally {
			out.close();
		}
	}

	public InputStream convert(InputStream in) {
		try {
			System.out.println("--Entro al metodo convert. aca se convertira a pdf--");
			System.out.println("variable filePathPdfr:....." + filePathPdf);
			File tmpFile = new File(filePathPdf);

			XWPFDocument document = new XWPFDocument(in);
//			PdfOptions options = PdfOptions.create();
			PdfOptions options = PdfOptions.create().fontEncoding(filePathPdf);
			OutputStream out = new FileOutputStream(tmpFile);
			document.createNumbering();

			PdfConverter.getInstance().convert(document, out, options);
			System.out.println("Aqui convertimose a pdf");
			return new FileInputStream(tmpFile);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	// Metodo para convertir a base 64
	// llamado fileToStrBase64(new File("Ruta del Archivo"))

	public String fileToStrBase64(File file) throws Exception {
		String fl = "";
		BufferedInputStream rd = null;
		try {
			Base64 base64 = new Base64();
			if (file != null) {
//						BASE64Encoder n = new BASE64Encoder();

				int length = (int) file.length();
				rd = new BufferedInputStream(new FileInputStream(file));
				if (length > 0) {
					System.out.println("-sisissisis");
					byte[] bytes = new byte[length];
					rd.read(bytes, 0, length);
//							fl = n.encode(bytes);

					fl = new String(base64.encode(bytes));
				} else {
					System.out.println("-no");

				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (rd != null) {
					rd.close();
					rd = null;
				}
			} catch (Exception e2) {
				System.err.println("Error en cerrar buffer");
			}
		}
		return fl.replaceAll("[\\n\\r]+", "");
	}

//1

	private void iteraParrafo(XWPFDocument doc, Map<String, Object> datosReporte) throws IOException {
//		System.out.println("Con despligue del miercoles 4 de octubre");
		for (XWPFParagraph paragraph : doc.getParagraphs()) {
			iterateThroughRuns(paragraph, datosReporte);
		}
	}

	private void iteraTabla(XWPFDocument doc, Map<String, Object> datosReporte) throws IOException {
		for (XWPFTable tbl : doc.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph paragraph : cell.getParagraphs()) {
						iterateThroughRuns(paragraph, datosReporte);
					}
				}
			}
		}
	}

	private void iteraEncabezado(XWPFDocument doc, Map<String, Object> datosReporte) throws IOException {
		for (XWPFHeader header : doc.getHeaderList()) {
			for (XWPFParagraph paragraph : header.getParagraphs()) {
				iterateThroughRuns(paragraph, datosReporte);
			}
		}
	}

	private void iterateThroughRuns(XWPFParagraph paragraph, Map<String, Object> datosReporte) throws IOException {
		System.out.println("ENTRO A RECOCRRER FUNCION DE PARRAFO");
		List<XWPFRun> runs = paragraph.getRuns();

		if (runs != null) {
			int runsSize = runs.size();

			for (int index = 0; index < runsSize; index++) {
				XWPFRun currentRun = runs.get(index);
				String text = currentRun.getText(0);

				if (text != null && text.contains("#")) {
					if (text.matches("(?i).*#[a-zA-Z0-9]+#.*")) {
						Matcher m = Pattern.compile("#[a-zA-Z0-9]+#").matcher(text);

						while (m.find()) {
							String variableWithHash = m.group();
							String variableWithoutHash = variableWithHash.replace("#", "");

							if (datosReporte.get(variableWithoutHash) == null) {
								continue;
							}

							String newText = currentRun.getText(0).replace(variableWithHash,
									(CharSequence) datosReporte.get(variableWithoutHash));
							currentRun.setText(newText, 0);
						}
						continue;
					}

					if (("#".equals(text) || " #".equals(text)) && index + 1 < runsSize) {
						replaceVariableBetweenDifferentRuns(index, runs, datosReporte);
						index += 2;
					}
				}
			}
		}
	}

	private void replaceVariableBetweenDifferentRuns(int index, List<XWPFRun> runs, Map<String, Object> datosReporte)
			throws IOException {
		System.out.println("Con despligue del miercoles 4 de octubre");
		XWPFRun currentRun = runs.get(index);
		String text = currentRun.getText(0);

		XWPFRun middleRun = runs.get(index + 1);
		String middleText = middleRun.getText(0);
		System.out.println("middleText...." + middleText);

		Object newVariableValue = datosReporte.get(middleText);
		System.out.println("newVariableValue......" + newVariableValue);

		if (newVariableValue != null) {

			XWPFRun lastRun = runs.get(index + 2);
			String lastText = lastRun.getText(0);

			if (middleText.matches("[a-zA-Z0-9]+") && "#".equals(lastText)) {
				currentRun.setText(text.replace("#", ""), 0);
				middleRun.setText((String) newVariableValue, 0);
				lastRun.setText("", 0);
			} else {
				throw new IOException("ocurrio un problema durante el reemplazo");
			}
		}
	}

	public void setTextsToReplace(Map<String, String> textsToReplace) {
		this.textsToReplace = textsToReplace;
	}

	public void setOriginalFilePath(String originalFilePath) {
		this.originalFilePath = originalFilePath;
	}

}

package com.davi.reportes.request;

import java.util.HashMap;
import java.util.Map;

public class DocumentRequest {

	private String reporte;
	private String sistema;
	private String producto;
	private Map<String, Object> datosReporte = new HashMap<String, Object>();
	private Map<String, String> datos = new HashMap<String, String>();


	

	public Map<String, String> getDatos() {
		return datos;
	}

	public void setDatos(Map<String, String> datos) {
		this.datos = datos;
	}

	public String getReporte() {
	return reporte;
	}

	public void setReporte(String reporte) {
	this.reporte = reporte;
	}

	public String getSistema() {
	return sistema;
	}

	public void setSistema(String sistema) {
	this.sistema = sistema;
	}

	public String getProducto() {
	return producto;
	}

	public void setProducto(String producto) {
	this.producto = producto;
	}

	public Map<String, Object> getDatosReporte() {
		return datosReporte;
	}

	public void setDatosReporte(Map<String, Object> datosReporte) {
		this.datosReporte = datosReporte;
	}

	@Override
	public String toString() {
		return "DocumentRequest [reporte=" + reporte + ", sistema=" + sistema + ", producto=" + producto
				+ ", datosReporte=" + datosReporte + ", datos=" + datos + "]";
	}





	
	
}

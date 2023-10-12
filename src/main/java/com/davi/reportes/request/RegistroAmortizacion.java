package com.davi.reportes.request;

public class RegistroAmortizacion {
	
	private String numeroCuota;
	private String fechaPago;
	private String capital;
	private String interes;
	private String seguros;
	private String totalCuotaPagar;
	public String getNumeroCuota() {
		return numeroCuota;
	}
	public void setNumeroCuota(String numeroCuota) {
		this.numeroCuota = numeroCuota;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getCapital() {
		return capital;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public String getInteres() {
		return interes;
	}
	public void setInteres(String interes) {
		this.interes = interes;
	}
	public String getSeguros() {
		return seguros;
	}
	public void setSeguros(String seguros) {
		this.seguros = seguros;
	}
	public String getTotalCuotaPagar() {
		return totalCuotaPagar;
	}
	public void setTotalCuotaPagar(String totalCuotaPagar) {
		this.totalCuotaPagar = totalCuotaPagar;
	}
	@Override
	public String toString() {
		return "RegistroAmortizacion [numeroCuota=" + numeroCuota + ", fechaPago=" + fechaPago + ", capital=" + capital
				+ ", interes=" + interes + ", seguros=" + seguros + ", totalCuotaPagar=" + totalCuotaPagar + "]";
	}
	


}

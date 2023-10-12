package com.davi.reportes.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient(name="api",url="localhost:8080/")
//@FeignClient(name="api",url="166.160.29.153:8089/")
@FeignClient(name = "api", url = "http://sv4020lap:9080/fronttp-1.0/")
public interface ConsumeApiService {
	@RequestMapping(method = RequestMethod.GET, value = "api/obtenerReporteXCodReporte/{COD_REPORTE}/{ID_PRODUCTO}/{ID_SISTEMA}")
	String getReporte(@PathVariable("COD_REPORTE") String reporte, @PathVariable("ID_SISTEMA") String sistema,@PathVariable("ID_PRODUCTO") String producto);

}

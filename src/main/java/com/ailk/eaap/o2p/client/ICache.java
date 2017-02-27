package com.ailk.eaap.o2p.client;

import java.io.Serializable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(targetNamespace="http://www.chinatelecom.hub.com", serviceName = "ICache", portName = "ICacheHttpPort")
public interface ICache extends Serializable{
	/**
	 * 
	 * @param key
	 * @param value
	 */
	@WebMethod(operationName="put" )
	@WebResult (targetNamespace = "http://www.chinatelecom.hub.com") 
	public void put(@WebParam(name="in0",  targetNamespace = "http://www.chinatelecom.hub.com")String key,
			@WebParam(name="in1",  targetNamespace = "http://www.chinatelecom.hub.com")String value);	
	
	/**
	 * @param key
	 * @param value
	 */
	@WebMethod(operationName="put1" )
	@WebResult (targetNamespace = "http://www.chinatelecom.hub.com") 
	public void put(@WebParam(name="in0",  targetNamespace = "http://www.chinatelecom.hub.com")String key,
			@WebParam(name="in1",  targetNamespace = "http://www.chinatelecom.hub.com")String value, 
			@WebParam(name="in2",  targetNamespace = "http://www.chinatelecom.hub.com")int TTL);
	
	@WebMethod(operationName="put2" )
	@WebResult (targetNamespace = "http://www.chinatelecom.hub.com") 
	public void put(@WebParam(name="in0",  targetNamespace = "http://www.chinatelecom.hub.com")String key,
			@WebParam(name="in1",  targetNamespace = "http://www.chinatelecom.hub.com")String value,
			@WebParam(name="in2",  targetNamespace = "http://www.chinatelecom.hub.com")int TTL,
			@WebParam(name="in3",  targetNamespace = "http://www.chinatelecom.hub.com")boolean isPersistent);
	/**
	 * 
	 * @param key
	 * @return
	 */
	@WebResult (name = "out", targetNamespace = "http://www.chinatelecom.hub.com") 
	public String get(@WebParam(name="in0",  targetNamespace = "http://www.chinatelecom.hub.com")String key);
//	public void flush();
	
}

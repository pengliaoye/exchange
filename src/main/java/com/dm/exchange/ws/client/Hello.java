
package com.dm.exchange.ws.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "Hello", targetNamespace = "http://ws.exchange.dm.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Hello {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://ws.exchange.dm.com/", className = "com.dm.exchange.ws.client.SayHello")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://ws.exchange.dm.com/", className = "com.dm.exchange.ws.client.SayHelloResponse")
    @Action(input = "http://ws.exchange.dm.com/Hello/sayHelloRequest", output = "http://ws.exchange.dm.com/Hello/sayHelloResponse")
    public String sayHello(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}

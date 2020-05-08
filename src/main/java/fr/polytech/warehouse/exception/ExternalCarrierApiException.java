package fr.polytech.warehouse.exception;

import javax.xml.ws.WebFault;
import java.io.Serializable;

@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/dronedelivery/drone")
public class ExternalCarrierApiException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public ExternalCarrierApiException() {
    }

    public ExternalCarrierApiException(String n) {
        super(n);
    }

    public ExternalCarrierApiException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ExternalDroneApiException on " + getMessage() + " ->" + getCause();
    }
}

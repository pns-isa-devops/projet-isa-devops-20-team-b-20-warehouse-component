package fr.polytech.warehouse.exception;

import javax.xml.ws.WebFault;

@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/dronedelivery/delivery")
public class UnknownParcelException extends Exception {

    private static final long serialVersionUID = 1L;
    private String parcelNumber;

    public UnknownParcelException(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public UnknownParcelException() {
    }

    /**
     * @return the parcelNumber
     */
    public String getParcelNumber() {
        return parcelNumber;
    }

    /**
     * @param parcelNumber the parcelNumber to set
     */
    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

}

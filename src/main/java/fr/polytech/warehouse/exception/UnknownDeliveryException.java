package fr.polytech.warehouse.exception;

import java.io.Serializable;

import javax.xml.ws.WebFault;

@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/dronedelivery/delivery")
public class UnknownDeliveryException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;
    private String deliveryNumber;

    public UnknownDeliveryException(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public UnknownDeliveryException() {
    }

    /**
     * @return the deliveryNumber
     */
    public String getParcelNumber() {
        return deliveryNumber;
    }

    /**
     * @param deliveryNumber the deliveryNumber to set
     */
    public void setParcelNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    @Override
    public String getMessage() {
        return "The Delivery with id : " + deliveryNumber + " can't be found.";
    }
}

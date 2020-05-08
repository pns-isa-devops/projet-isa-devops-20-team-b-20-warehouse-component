package fr.polytech.warehouse.components;

import java.util.List;

import javax.ejb.Local;

import fr.polytech.entities.Delivery;
import fr.polytech.warehouse.exception.ExternalCarrierApiException;
import fr.polytech.warehouse.exception.UnknownDeliveryException;
import fr.polytech.warehouse.exception.UnknownParcelException;

@Local
public interface DeliveryModifier {

    /**
     * Find a specific delivery by his id
     *
     * @param deliveryNumber
     * @return the delivery if exist or null
     */
    Delivery findDelivery(String deliveryNumber) throws UnknownDeliveryException;

    /**
     * Call external carrier API to check if exist new parcels, if yes create
     * deliveries and add it in our system.
     *
     * @throws UnknownParcelException
     * @throws ExternalCarrierApiException
     */
    List<Delivery> checkForNewParcels() throws ExternalCarrierApiException, UnknownParcelException;
}

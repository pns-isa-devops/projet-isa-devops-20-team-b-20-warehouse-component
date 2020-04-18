package fr.polytech.warehouse.components;

import java.util.List;

import javax.ejb.Local;

import fr.polytech.entities.Delivery;
import fr.polytech.warehouse.exception.UnknownDeliveryException;
import fr.polytech.warehouse.exception.UnknownParcelException;

@Local
public interface DeliveryModifier {

    /**
     * Retrieve a parcel information from his ID This method also store the parcel
     * in his state
     *
     * @param parcelNumber
     * @return the scanned parcel in a new delivery
     * @throws UnknownParcelException if the parcel those not existe in the database
     *                                of the carrier
     */
    Delivery scanParcel(String parcelNumber) throws UnknownParcelException;

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
     */
    List<Delivery> checkForNewParcels();
}

package fr.polytech.warehouse.components;

import fr.polytech.entities.Delivery;
import fr.polytech.warehouse.exception.ExternalCarrierApiException;

import javax.ejb.Local;
import java.util.List;

/**
 * ControlledParcel
 */
@Local
public interface ControlledParcel extends DeliveryModifier {

    List<Delivery> checkForNewParcelsFromData(String data) throws ExternalCarrierApiException;
}

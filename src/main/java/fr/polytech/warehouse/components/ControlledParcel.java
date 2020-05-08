package fr.polytech.warehouse.components;

import javax.ejb.Local;

import fr.polytech.warehouse.utils.CarrierAPI;

/**
 * ControlledParcel
 */
@Local
public interface ControlledParcel extends DeliveryModifier {

    void useCarrierAPIReference(CarrierAPI api);
}

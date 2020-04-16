package fr.polytech.warehouse.business;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.AbstractWarehouseTest;
import fr.polytech.entities.Delivery;
import fr.polytech.entities.DeliveryStatus;
import fr.polytech.entities.Parcel;
import fr.polytech.warehouse.components.ControlledParcel;
import fr.polytech.warehouse.exception.UnknownDeliveryException;
import fr.polytech.warehouse.exception.UnknownParcelException;
import fr.polytech.warehouse.utils.CarrierAPI;

/**
 * WarehouseTest
 */
@Ignore
@RunWith(Arquillian.class)
public class DeliveryModifierTest extends AbstractWarehouseTest {

    @EJB
    private ControlledParcel controlledParcel;

    private List<Parcel> parcels;

    @Before
    public void setUpContext() {
        initData();
        initMock();
    }

    private void initMock() {
        controlledParcel.useCarrierReference(new CarrierAPI(), parcels);
    }

    private void initData() {
        parcels = new ArrayList<>();
        Parcel p = new Parcel();
        p.setAddress("2255 route des Dolines, 06560 Valbonne");
        p.setCarrier("JPP");
        p.setCustomerName("Jasmine");
        p.setParcelId("JPP1061071");
        parcels.add(p);
        p = new Parcel();
        p.setAddress("5522 avenue des Moutons, 06560 Valbonne");
        p.setCarrier("JPP2");
        p.setCustomerName("Arnold");
        p.setParcelId("JPP1061072");
        p = new Parcel();
        p.setAddress("5555 avenue des Trident, 06560 Valbonne");
        p.setCarrier("JPP3");
        p.setCustomerName("Jasmine");
        p.setParcelId("JPP1061073");
        parcels.add(p);
    }

    @Test(expected = UnknownParcelException.class)
    public void scanParcelNotFoundTest() throws Exception {
        controlledParcel.scanParcel("jpp10x");
    }

    @Test
    public void scanParcelTest() throws UnknownParcelException {
        Delivery d = controlledParcel.scanParcel("JPP1061071");
        assertNotNull(d);
        assertEquals(parcels.get(0), d.getParcel());
        assertEquals(DeliveryStatus.NOT_DELIVERED, d.getStatus());
        assertNotNull(d.getDeliveryId());

    }

    @Test(expected = UnknownDeliveryException.class)
    public void findDeliveryNotFoundTest() throws Exception {
        controlledParcel.findDelivery("unknown");
    }

    @Test
    public void findDeliveryFoundTest() throws UnknownParcelException, UnknownDeliveryException {
        Delivery d = controlledParcel.scanParcel("JPP1061071");
        assertNotNull(controlledParcel.findDelivery(d.getDeliveryId()));
    }

}

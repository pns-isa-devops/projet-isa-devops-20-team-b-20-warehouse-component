package fr.polytech.warehouse.business;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
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
@RunWith(Arquillian.class)
// @Transactional(TransactionMode.COMMIT)
public class DeliveryModifierTest extends AbstractWarehouseTest {

    @EJB
    private ControlledParcel controlledParcel;

    // Test context
    private List<Parcel> parcels;
    private List<Delivery> deliveries;

    @Before
    public void setUpContext() throws Exception {
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
        p.setParcelNumber("jpp106");
        parcels.add(p);
        p = new Parcel();
        p.setAddress("5522 avenue des Moutons, 06560 Valbonne");
        p.setCarrier("JPP2");
        p.setCustomerName("Arnold");
        p.setParcelNumber("jpp206");
        p = new Parcel();
        p.setAddress("5555 avenue des Trident, 06560 Valbonne");
        p.setCarrier("JPP3");
        p.setCustomerName("Jasmine");
        p.setParcelNumber("jpp306");
        parcels.add(p);
    }

    @Test(expected = UnknownParcelException.class)
    public void scanParcelNotFoundTest() throws Exception {
        controlledParcel.scanParcel("jpp10x");
    }

    @Test
    public void scanParcelTest() throws UnknownParcelException {
        Delivery d = controlledParcel.scanParcel("jpp106");
        assertNotNull(d);
        assertEquals(parcels.get(0), d.getParcel());
        assertEquals(DeliveryStatus.NOT_DELIVERED, d.getStatus());
        assertNotNull(d.getDeliveryNumber());

    }

    @Test(expected = UnknownDeliveryException.class)
    public void findDeliveryNotFoundTest() throws Exception {
        controlledParcel.findDelivery("unknown");
    }

    @Test
    public void findDeliveryTest() {

    }
}

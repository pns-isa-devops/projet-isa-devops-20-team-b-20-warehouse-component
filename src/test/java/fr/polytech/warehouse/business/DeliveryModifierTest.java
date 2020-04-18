package fr.polytech.warehouse.business;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
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

import org.apache.commons.io.IOUtils;

/**
 * WarehouseTest
 */

@RunWith(Arquillian.class)
public class DeliveryModifierTest extends AbstractWarehouseTest {

    @EJB
    private ControlledParcel controlledParcel;

    @Before
    public void initMock() throws IOException {
        String data = IOUtils.toString(Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("parcels.json"), "UTF-8");
        controlledParcel.checkForNewParcelsFromData(data);
    }

    @Test(expected = UnknownParcelException.class)
    public void scanParcelNotFoundTest() throws Exception {
        controlledParcel.scanParcel("jpp10x");
    }

    @Test
    public void scanParcelTest() throws UnknownParcelException {
        final Delivery d = controlledParcel.scanParcel("123456789B");
        assertNotNull(d);
        assertEquals(DeliveryStatus.NOT_DELIVERED, d.getStatus());
        assertNotNull(d.getDeliveryId());
    }

    @Test(expected = UnknownDeliveryException.class)
    public void findDeliveryNotFoundTest() throws Exception {
        controlledParcel.findDelivery("unknown");
    }

    @Test
    public void findDeliveryFoundTest() throws UnknownParcelException, UnknownDeliveryException {
        Delivery d = controlledParcel.scanParcel("123456789C");
        assertNotNull(controlledParcel.findDelivery(d.getDeliveryId()));
    }

}

package fr.polytech.warehouse.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import arquillian.AbstractWarehouseTest;
import fr.polytech.entities.Delivery;
import fr.polytech.entities.DeliveryStatus;
import fr.polytech.entities.Parcel;
import fr.polytech.warehouse.components.ControlledParcel;
import fr.polytech.warehouse.exception.ExternalCarrierApiException;
import fr.polytech.warehouse.exception.UnknownDeliveryException;
import fr.polytech.warehouse.exception.UnknownParcelException;
import fr.polytech.warehouse.utils.CarrierAPI;

/**
 * WarehouseTest
 */

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeliveryModifierTest extends AbstractWarehouseTest {

    @EJB
    private ControlledParcel controlledParcel;

    List<Parcel> parcels = new ArrayList<>();
    List<Delivery> deliveries;

    @Before
    public void initMock() throws IOException, ExternalCarrierApiException {
        initParcelsData();
        CarrierAPI mocked = mock(CarrierAPI.class);
        controlledParcel.useCarrierAPIReference(mocked);
        when(mocked.getParcels()).thenReturn(parcels);
    }

    @Test(expected = UnknownDeliveryException.class)
    public void aFirstCheckIfNullDeliveryThrowError() throws UnknownDeliveryException {
        controlledParcel.findDelivery(null);
    }

    @Test(expected = UnknownDeliveryException.class)
    public void aFirstCheckIfNotFoundDeliveryThrowError() throws UnknownDeliveryException {
        controlledParcel.findDelivery("123456789A");
    }

    /**
     * first test to be run to test the parcels and to use it for the other
     *
     * @throws UnknownParcelException
     * @throws ExternalCarrierApiException
     */
    @Test
    public void beforeAllCheckForNewParcelsTest() throws ExternalCarrierApiException, UnknownParcelException {
        this.deliveries = controlledParcel.checkForNewParcels();
        assertEquals(5, deliveries.size());
        for (Delivery d : deliveries) {
            this.parcels.contains(d.getParcel());
        }
    }

    @Test
    public void thenCheckIfDeliveryExist() throws UnknownDeliveryException {
        Delivery d = controlledParcel.findDelivery("123456789A");
        assertNotNull(d);
        assertTrue(this.parcels.contains(d.getParcel()));
    }

    @Test(expected = UnknownDeliveryException.class)
    public void thenCheckIfNullDeliveryThrowError() throws UnknownDeliveryException {
        controlledParcel.findDelivery(null);
    }

    @Test(expected = UnknownDeliveryException.class)
    public void thenCheckIfNotFoundDeliveryThrowError() throws UnknownDeliveryException {
        controlledParcel.findDelivery("326598741A");
    }

    private void initParcelsData() {
        parcels.add(new Parcel("123456789A", "1 Rue Bidon", "Colissimo", "Nadine"));
        parcels.add(new Parcel("123456789B", "2 Rue Bidon", "Colissimo", "François"));
        parcels.add(new Parcel("123456789C", "3 Rue Bidon", "Colissimo", "Guy"));
        parcels.add(new Parcel("123456789D", "4 Rue Bidon", "Colissimo", "Chloé"));
        parcels.add(new Parcel("123456789E", "5 Rue Bidon", "Colissimo", "Sophie"));
    }

}

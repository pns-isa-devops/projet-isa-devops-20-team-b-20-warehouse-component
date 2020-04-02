package fr.polytech.warehouse.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.RandomStringUtils;

import fr.polytech.entities.Delivery;
import fr.polytech.entities.DeliveryStatus;
import fr.polytech.entities.Parcel;
import fr.polytech.warehouse.exception.UncheckedException;
import fr.polytech.warehouse.exception.UnknownDeliveryException;
import fr.polytech.warehouse.exception.UnknownParcelException;
import fr.polytech.warehouse.utils.CarrierAPI;

/**
 * WarehousBean
 */
@Stateful
@Named("warehouse")
public class WarehouseBean implements DeliveryModifier, ControlledParcel {

    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    private CarrierAPI carrier;
    // private List<Delivery> deliveries = new ArrayList<>();

    @Override
    public void useCarrierReference(CarrierAPI carrier) {
        this.carrier = carrier;
    }

    @Override
    public void useCarrierReference(CarrierAPI carrier, List<Parcel> parcels) {
        this.carrier = carrier.withControlledParcels(parcels);
    }

    @Override
    public Delivery scanParcel(String id) throws UnknownParcelException {
        Parcel p = carrier.getParcelInformation(id);
        if (p == null) {
            throw new UnknownParcelException(id);
        }
        Delivery d = new Delivery();
        d.setParcel(p);
        d.setStatus(DeliveryStatus.NOT_DELIVERED);
        d.setDeliveryId(RandomStringUtils.random(10, "0123456789ABCDEFGHIJ"));
        entityManager.persist(p);
        entityManager.persist(d);
        return d;
    }

    @Override
    public Delivery findDelivery(String id) throws UnknownDeliveryException {
        try {
            return findById(id).get();
        } catch (Exception e) {
            throw new UnknownDeliveryException(id);
        }
    }

    @PostConstruct
    /**
     * Init the Carrier API on localhost
     */
    private void initializeRestPartnership() {
        try {
            Properties prop = new Properties();
            prop.load(this.getClass().getResourceAsStream("/carrier.properties"));
            carrier = new CarrierAPI(prop.getProperty("carrierHostName"), prop.getProperty("carrierPortNumber"));
            addMock();
        } catch (Exception e) {
            log.log(Level.INFO, "Cannot read carrier.properties file", e);
            throw new UncheckedException(e);
        }
    }

    private void addMock() {
        List<Parcel> parcels = new ArrayList<>();
        Parcel p = new Parcel();
        p.setAddress("2255 route des Dolines, 06560 Valbonne");
        p.setCarrier("JPP");
        p.setCustomerName("Jasmine");
        p.setParcelId(RandomStringUtils.random(10, "0123456789ABCDEFGHIJ"));
        entityManager.persist(p);
        parcels.add(p);
        p = new Parcel();
        p.setAddress("2255 route des Dolines, 06560 Valbonne");
        p.setCarrier("JPP");
        p.setCustomerName("Alexis");
        p.setParcelId(RandomStringUtils.random(10, "0123456789ABCDEFGHIJ"));
        entityManager.persist(p);
        parcels.add(p);
        p = new Parcel();
        p.setAddress("2255 route des Dolines, 06560 Valbonne");
        p.setCarrier("JPP");
        p.setCustomerName("Betsara");
        p.setParcelId(RandomStringUtils.random(10, "0123456789ABCDEFGHIJ"));
        entityManager.persist(p);
        parcels.add(p);
        p = new Parcel();
        p.setAddress("2255 route des Dolines, 06560 Valbonne");
        p.setCarrier("JPP");
        p.setCustomerName("Arnold");
        p.setParcelId(RandomStringUtils.random(10, "0123456789ABCDEFGHIJ"));
        entityManager.persist(p);
        parcels.add(p);
        this.carrier.withControlledParcels(parcels);
        Delivery d = new Delivery();
        d.setDeliveryId(RandomStringUtils.random(10, "0123456789ABCDEFGHIJ"));
        d.setParcel(parcels.get(0));
        d.setStatus(DeliveryStatus.NOT_DELIVERED);
        entityManager.persist(d);
        d = new Delivery();
        d.setDeliveryId(RandomStringUtils.random(10, "0123456789ABCDEFGHIJ"));
        d.setParcel(parcels.get(1));
        d.setStatus(DeliveryStatus.NOT_DELIVERED);
        entityManager.persist(d);
        d = new Delivery();
        d.setDeliveryId(RandomStringUtils.random(10, "0123456789ABCDEFGHIJ"));
        d.setParcel(parcels.get(2));
        d.setStatus(DeliveryStatus.NOT_DELIVERED);
        entityManager.persist(d);
        d = new Delivery();
        d.setDeliveryId(RandomStringUtils.random(10, "0123456789ABCDEFGHIJ"));
        d.setParcel(parcels.get(3));
        d.setStatus(DeliveryStatus.NOT_DELIVERED);
        entityManager.persist(d);
    }

    private Optional<Delivery> findById(String id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Delivery> criteria = builder.createQuery(Delivery.class);
        Root<Delivery> root = criteria.from(Delivery.class);
        criteria.select(root).where(builder.equal(root.get("deliveryId"), id));

        TypedQuery<Delivery> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            log.log(Level.FINEST, "No result for [" + id + "]", e);
            return Optional.empty();
        }
    }

}

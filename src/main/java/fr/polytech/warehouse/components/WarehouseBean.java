package fr.polytech.warehouse.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fr.polytech.entities.Delivery;
import fr.polytech.entities.DeliveryStatus;
import fr.polytech.entities.Parcel;
import fr.polytech.warehouse.exception.ExternalCarrierApiException;
import fr.polytech.invoice.components.DeliveryBilling;
import fr.polytech.warehouse.exception.UncheckedException;
import fr.polytech.warehouse.exception.UnknownDeliveryException;
import fr.polytech.warehouse.exception.UnknownParcelException;
import fr.polytech.warehouse.utils.CarrierAPI;

/**
 * WarehousBean
 */
@Stateless
@Named("warehouse")
public class WarehouseBean implements ControlledParcel, DeliveryModifier {

    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    private CarrierAPI carrier;

    @EJB
    private DeliveryBilling billingDelivery;

    @Override
    public void useCarrierAPIReference(CarrierAPI api) {
        this.carrier = api;
    }

    @Override
    public Delivery findDelivery(String id) throws UnknownDeliveryException {
        Optional<Delivery> optDelivery = findById(id);
        if (optDelivery.isPresent()) {
            return optDelivery.get();
        }
        throw new UnknownDeliveryException(id);
    }

    @Override
    public List<Delivery> checkForNewParcels() throws ExternalCarrierApiException, UnknownParcelException {
        List<Parcel> parcels = carrier.getParcels();
        List<Delivery> deliveries = new ArrayList<>(parcels.size());
        for (Parcel p : parcels) {
            deliveries.add(makeDelivery(p));
        }
        billingDelivery.generatingInvoice(deliveries);
        return deliveries;
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
        } catch (Exception e) {
            log.log(Level.INFO, "Cannot read carrier.properties file", e);
            throw new UncheckedException(e);
        }
    }

    private Delivery makeDelivery(Parcel newParcel) throws UnknownParcelException {
        if (newParcel == null) {
            throw new UnknownParcelException("null");
        }
        Delivery d = new Delivery();
        d.setParcel(newParcel);
        d.setStatus(DeliveryStatus.NOT_DELIVERED);
        d.setDeliveryId(newParcel.getParcelId());
        entityManager.persist(newParcel);
        entityManager.persist(d);
        return d;
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

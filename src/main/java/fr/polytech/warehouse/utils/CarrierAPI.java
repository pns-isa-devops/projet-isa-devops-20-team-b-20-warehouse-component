package fr.polytech.warehouse.utils;

import java.util.List;

import fr.polytech.entities.Parcel;

/**
 * CarrierAPI
 */
public class CarrierAPI {

    private String url;
    private List<Parcel> parcels;

    /**
     * not to be used for now (mocked value)
     *
     * @param host
     * @param port
     */
    public CarrierAPI(String host, String port) {
        this.url = "http://" + host + ":" + port;
    }

    public CarrierAPI withControlledParcels(List<Parcel> parcels) {
        this.parcels = parcels;
        return this;
    }

    /**
     * prove of concept (mocked value)
     */
    public CarrierAPI() {
        this("localhost", "9090");
    }

    public Parcel getParcelInformation(String parcelNumber) {
        // just to test
        // @formatter:off
        return parcels.stream()
                    .filter(p -> p.getParcelNumber().equals(parcelNumber))
                    .findAny()
                    .orElse(null);
        // @formatter:on
    }
}

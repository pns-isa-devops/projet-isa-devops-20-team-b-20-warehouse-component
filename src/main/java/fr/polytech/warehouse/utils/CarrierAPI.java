package fr.polytech.warehouse.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.client.WebClient;
import org.json.JSONArray;
import org.json.JSONObject;

import fr.polytech.entities.Parcel;
import fr.polytech.warehouse.exception.ExternalCarrierApiException;

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

    public List<Parcel> parseJSONtoParcels(JSONArray json) {
        parcels = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject parcelJSON = json.getJSONObject(i);
            parcels.add(new Parcel(parcelJSON.getString("ParcelNumber"), parcelJSON.getString("Address"),
                    parcelJSON.getString("Carrier"), parcelJSON.getString("CustomerName")));
        }
        return parcels;
    }

    public JSONArray getJSONParcels() throws ExternalCarrierApiException {
        String response;
        try {
            response = WebClient.create(url).path("/carrier/parcel").get(String.class);
        } catch (Exception e) {
            throw new ExternalCarrierApiException(url + "/carrier/parcel", e);
        }
        return new JSONArray(response);
    }

    public List<Parcel> getParcels() throws ExternalCarrierApiException {
        return parseJSONtoParcels(getJSONParcels());
    }

    public List<Parcel> getParcelsFromData(String data) throws ExternalCarrierApiException {
        return parseJSONtoParcels(new JSONArray(data));
    }

    public CarrierAPI withControlledParcels(List<Parcel> parcels) {
        this.parcels = parcels;
        return this;
    }

    /**
     * prove of concept (mocked value)
     */
    public CarrierAPI() {
        this("localhost", "9191");
    }

    public Parcel getParcelInformation(String parcelNumber) {
        // just to test
        // @formatter:off
        return parcels.stream()
                    .filter(p -> p.getParcelId().equals(parcelNumber))
                    .findAny()
                    .orElse(null);
        // @formatter:on
    }
}

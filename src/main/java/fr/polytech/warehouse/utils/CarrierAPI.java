package fr.polytech.warehouse.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.client.WebClient;
import org.json.JSONArray;
import org.json.JSONObject;

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
	
	public List<Parcel> getParcels() throws Exception {
        // Retrieving the drone status
        parcels = new ArrayList<>();
        String response;
        try {
            response = WebClient.create(url).path("/carrier/parcel").get(String.class);
        } catch (Exception e) {
            throw new Exception(url + "/carrier/parcel", e);
        }
        JSONArray json = new JSONArray(response);
        for(int i=0; i<json.length(); i++)
        {
            JSONObject parcelJSON = json.getJSONObject(i);
            //System.out.println(parcelJSON);
            //TODO Change fixed values to real informations (from api ?)
            System.out.println(parcelJSON.toString());
            parcels.add(new Parcel(parcelJSON.getString("ParcelNumber"), "Rue normal", "Colissimo", "Nadine"));
        }
        //parcels.forEach(parcel -> System.out.println(parcel.toString()));
        return parcels;
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

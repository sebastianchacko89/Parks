package com.codelab.parks.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codelab.parks.vo.Place;
import com.codelab.parks.vo.ParkServiceResponse;

@RequestMapping(value ="/api")
@RestController
public class ParkServiceController {

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	private static final String PLACES_API_KEY = "AIzaSyCdW3wpvaiBzIyTfBMHn-Dwu83kBCoqcxQ";
	private static final String TEN_MILES_IN_METRES = "16093";
	private static final String PARK = "park";
	
	/**
	 * This service will return all parks near a user, given his location.
	 * It will use the Google Places API for the same
	 * 
	 * @param lat
	 * @param long
	 * @return ParkServiceResponse
	 */
	@CrossOrigin
	@RequestMapping(value ="/getParks", method =RequestMethod.GET)
	public ParkServiceResponse search(@RequestParam(value="lat", required=true) String lat,
									@RequestParam(value="long", required=true) String lng) {
        ParkServiceResponse response = new ParkServiceResponse();
		ArrayList<Place> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append("?location=" + lat + "," + lng);
            sb.append("&radius=" + String.valueOf(TEN_MILES_IN_METRES));
			sb.append("&type=" + PARK);
			sb.append("&key=" + PLACES_API_KEY);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            response.setIsError(true);
			response.setErrorMessage(e.getMessage());
            return response;
        } catch (IOException e) {
            response.setIsError(true);
			response.setErrorMessage(e.getMessage());
            return response;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<Place>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Place place = new Place();
                place.setName(predsJsonArray.getJSONObject(i).getString("name"));
                resultList.add(place);
            }
        } catch (JSONException e) {
            response.setIsError(true);
			response.setErrorMessage(e.getMessage());
            return response;
        }
		
		response.setPlacesList(resultList);
        return response;
    }

}

package com.example.Ecom_Project.config;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GooglePeopleApiService {

    private static final String GOOGLE_PEOPLE_API_URL =
            "https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses,phoneNumbers,addresses,genders";

    public Map<String, String> fetchExtraDetails(String accessToken) {
        String url = GOOGLE_PEOPLE_API_URL + "&access_token=" + accessToken;
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, String> extracted = new HashMap<>();

        try {
            List<Map<String, Object>> emails = (List<Map<String, Object>>) response.get("emailAddresses");
            List<Map<String, Object>> names = (List<Map<String, Object>>) response.get("names");
            List<Map<String, Object>> phones = (List<Map<String, Object>>) response.get("phoneNumbers");
            List<Map<String, Object>> genders = (List<Map<String, Object>>) response.get("genders");
            List<Map<String, Object>> addresses = (List<Map<String, Object>>) response.get("addresses");

            if (emails != null && !emails.isEmpty()) {
                extracted.put("email", (String) emails.get(0).get("value"));
            }
            if (names != null && !names.isEmpty()) {
                extracted.put("name", (String) names.get(0).get("displayName"));
            }
            if (phones != null && !phones.isEmpty()) {
                extracted.put("phone", (String) phones.get(0).get("value"));
                System.out.print("Phone Number "+ "  " +phones);
            }
            if (genders != null && !genders.isEmpty()) {
                extracted.put("gender", (String) genders.get(0).get("value"));
            }
            if (addresses != null && !addresses.isEmpty()) {
                extracted.put("address", (String) addresses.get(0).get("formattedValue"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return extracted;
    }
}

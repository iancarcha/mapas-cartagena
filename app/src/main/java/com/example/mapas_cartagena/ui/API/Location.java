package com.example.mapas_cartagena.ui.API;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "city",
        "country",
        "latitude",
        "longitude"
})

public class Location {

    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("latitude")
    private float latitude;
    @JsonProperty("longitude")
    private float longitude;

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("latitude")
    public float getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public float getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }


}

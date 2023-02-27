package com.example.mapas_cartagena.ui.API;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "empty_slots",
        "extra"
})

public class Station {

    @JsonProperty("empty_slots")
    private int emptySlots;
    @JsonProperty("extra")
    private Extra extra;
    @JsonProperty("free_bikes")
    private int freeBikes;
    @JsonProperty("id")
    private String id;
    @JsonProperty("latitude")
    private float latitude;
    @JsonProperty("longitude")
    private float longitude;
    @JsonProperty("name")
    private String name;
    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("empty_slots")
    public int getEmptySlots() {
        return emptySlots;
    }

    @JsonProperty("empty_slots")
    public void setEmptySlots(int emptySlots) {
        this.emptySlots = emptySlots;
    }

    @JsonProperty("extra")
    public Extra getExtra() {
        return extra;
    }

    @JsonProperty("extra")
    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    @JsonProperty("free_bikes")
    public int getFreeBikes() {
        return freeBikes;
    }

    @JsonProperty("free_bikes")
    public void setFreeBikes(int freeBikes) {
        this.freeBikes = freeBikes;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
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

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}

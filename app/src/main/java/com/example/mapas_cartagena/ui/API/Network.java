package com.example.mapas_cartagena.ui.API;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "company",
        "href",
        "id",
        "license",
        "location",
        "name",
        "source",
        "stations"
})

public class Network {

    @JsonProperty("company")
    private List<String> company = null;
    @JsonProperty("href")
    private String href;
    @JsonProperty("id")
    private String id;
    @JsonProperty("license")
    private License license;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("name")
    private String name;
    @JsonProperty("source")
    private String source;
    @JsonProperty("stations")
    private List<Station> stations = null;

    @JsonProperty("company")
    public List<String> getCompany() {
        return company;
    }

    @JsonProperty("company")
    public void setCompany(List<String> company) {
        this.company = company;
    }

    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href) {
        this.href = href;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("license")
    public License getLicense() {
        return license;
    }

    @JsonProperty("license")
    public void setLicense(License license) {
        this.license = license;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("stations")
    public List<Station> getStations() {
        return stations;
    }

    @JsonProperty("stations")
    public void setStations(List<Station> stations) {
        this.stations = stations;
    }
}

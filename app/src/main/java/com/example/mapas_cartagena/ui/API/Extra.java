package com.example.mapas_cartagena.ui.API;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "address",
        "banking",
        "bonus",
        "last_update",
        "slots",
        "status",
        "uid"
})

public class Extra {

    @JsonProperty("address")
    private String address;
    @JsonProperty("banking")
    private boolean banking;
    @JsonProperty("bonus")
    private boolean bonus;
    @JsonProperty("last_update")
    private long lastUpdate;
    @JsonProperty("slots")
    private int slots;
    @JsonProperty("status")
    private String status;
    @JsonProperty("uid")
    private int uid;

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("banking")
    public boolean isBanking() {
        return banking;
    }

    @JsonProperty("banking")
    public void setBanking(boolean banking) {
        this.banking = banking;
    }

    @JsonProperty("bonus")
    public boolean isBonus() {
        return bonus;
    }

    @JsonProperty("bonus")
    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    @JsonProperty("last_update")
    public long getLastUpdate() {
        return lastUpdate;
    }

    @JsonProperty("last_update")
    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @JsonProperty("slots")
    public int getSlots() {
        return slots;
    }

    @JsonProperty("slots")
    public void setSlots(int slots) {
        this.slots = slots;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("uid")
    public int getUid() {
        return uid;
    }

    @JsonProperty("uid")
    public void setUid(int uid) {
        this.uid = uid;
    }

}

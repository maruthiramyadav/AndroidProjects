package com.devobal.ogabuzz.Modals;

import java.util.ArrayList;

public class LocationsGroupModal {
    String state;
    ArrayList<LocationsChildModal> locationsChildModals;

    public ArrayList<LocationsChildModal> getLocationsChildModals() {
        return locationsChildModals;
    }

    public void setLocationsChildModals(ArrayList<LocationsChildModal> locationsChildModals) {
        this.locationsChildModals = locationsChildModals;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

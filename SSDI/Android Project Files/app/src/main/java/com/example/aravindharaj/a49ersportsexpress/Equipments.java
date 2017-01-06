package com.example.aravindharaj.a49ersportsexpress;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aravindharaj on 10/27/2016.
 */

public class Equipments {

    public String _id;
    public String equipment_type;
    public String availability;
    public String logo;
    public HashMap<String, Integer> checkedOutBy;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public HashMap<String, Integer> getCheckedOutBy() {
        return checkedOutBy;
    }

    public void setCheckedOutBy(HashMap<String, Integer> checkedOutBy) {
        this.checkedOutBy = checkedOutBy;
    }

    public String getEquipment_type() {
        return equipment_type;
    }

    public void setEquipment_type(String equipment_type) {
        this.equipment_type = equipment_type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}

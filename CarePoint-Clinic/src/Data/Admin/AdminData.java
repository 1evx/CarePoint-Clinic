package Data.Admin;

import Data.UserData;

public class AdminData extends UserData {

    // add attribute
    public AdminData (String id, String name, String icNumber, String pass, String contact) {
        this.id = id;
        this.name = name;
        this.icNumber = icNumber;
        this.pass = pass;
        this.contact = contact;
    }
}


package Data.Patient;


import Data.UserData;

public class PatientData extends UserData {
    private String gender;
    private String dob;

    public PatientData() {
    }

    public PatientData (String id, String name, String icNumber, String pass, String gender, String dob, String contact) {
        this.id = id;
        this.name = name;
        this.icNumber = icNumber;
        this.pass = pass;
        this.gender = gender;
        this.dob = dob;
        this.contact = contact;
    }

    public String getGender () {
        return gender;
    }

    public String getDob () {
        return dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}


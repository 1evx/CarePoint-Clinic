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

    public String getIc () {
        return icNumber;
    }

    public String getGender () {
        return gender;
    }

    public String getDob () {
        return dob;
    }

    //changed wh
    public void setName(String name) {
        this.name = name;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public boolean setPass(String pass) {
        boolean numberFlag = false;
        boolean capitalFlag = false;
        boolean lowercaseFlag = false;
        if (pass.length() < 8 || pass.length() > 16) {
            return false;
        }
        for (int i = 0; i < pass.length(); i++) {
            if (Character.isDigit(pass.charAt(i))) {
                numberFlag = true;
            }
            if (Character.isUpperCase(pass.charAt(i))) {
                capitalFlag = true;
            }
            if (Character.isLowerCase(pass.charAt(i))) {
                lowercaseFlag = true;
            }
            if (numberFlag && capitalFlag && lowercaseFlag) {
                this.pass = pass;
                return true;
            }
        }
        return false;

    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}


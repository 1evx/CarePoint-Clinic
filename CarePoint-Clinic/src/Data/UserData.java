package Data;
import Authentication.*;
import Data.Admin.AdminData;
import Data.Admin.Admins;
import Data.Doctor.DoctorData;
import Data.Doctor.Doctors;
import Data.Patient.PatientData;
import Data.Patient.Patients;

import java.util.List;

public class UserData extends Data {
    protected String name;
    protected String icNumber;
    protected String pass;
    protected String contact;

    public String getName () {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcNumber () {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getPass () {
        return pass;
    }

    public String setPass(String newPass, String confirmPass) {
        boolean numberFlag = false;
        boolean capitalFlag = false;
        boolean lowercaseFlag = false;
        if (newPass.length() < 8 || newPass.length() > 16) {
            return "Your password length must be between 8 and 16 characters";
        }
        for (int i = 0; i < newPass.length(); i++) {
            if (Character.isDigit(newPass.charAt(i))) {
                numberFlag = true;
            }
            if (Character.isUpperCase(newPass.charAt(i))) {
                capitalFlag = true;
            }
            if (Character.isLowerCase(newPass.charAt(i))) {
                lowercaseFlag = true;
            }
            if (numberFlag && capitalFlag && lowercaseFlag) {
                if (newPass.equals(confirmPass)) {
                    this.pass = Authentication.hashPassword(newPass);
                    return null;
                }
                return "Confirm password does not match";
            }
        }
        return "Your password must contain at least one digit, one uppercase letter, and one lowercase";
    }

    public String getContact () {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    // for login, to check the username and password is same or not
    public boolean login(String username, String password) {
        if (!username.equals(icNumber)) {
            return false;
        }
        return Authentication.matchPassword(password, pass);
    }

    // changed wh
    public String checkIcNumber () {
        Patients patients = new Patients();
        List<PatientData> queriedPatient = patients.queryPatient(null, null, icNumber, null, null, null);
        Admins admins = new Admins();
        List<AdminData> queriedAdmin = admins.queryAdmin(null, null, icNumber, null);
        Doctors doctors = new Doctors();
        List<DoctorData> queriedDoctor = doctors.queryDoctor(null, null, icNumber, null, null, null, null, null);
        if (!(queriedPatient.isEmpty() && queriedAdmin.isEmpty() && queriedDoctor.isEmpty())) {
            return icNumber;
        }
        return null;
    }
    // changed wh
}

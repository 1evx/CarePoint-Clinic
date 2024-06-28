package Data;

import Authentication.Authentication;
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

    public String getIcNumber () {
        return icNumber;
    }

    public String getPass () {
        return pass;
    }

    public String getContact () {
        return contact;
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

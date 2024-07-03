package Authentication;

import Data.Admin.AdminData;
import Data.Doctor.DoctorData;
import Data.Patient.PatientData;
import Data.UserData;

public class Cookie {
    private static UserData userCookie;

    public static void setCookie(UserData userData) {
        userCookie = userData;
    }

    public static PatientData getPatientCookie() {
        return (PatientData) userCookie;
    }

    public static AdminData getAdminCookie() {
        return (AdminData) userCookie;
    }

    public static DoctorData getDoctorCookie() {
        return (DoctorData) userCookie;
    }
}

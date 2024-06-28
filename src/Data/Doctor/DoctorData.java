package Data.Doctor;

import Data.UserData;

public class DoctorData extends UserData {
    private String gender;
    private String specialization;
    private String education;
    private String experienceYear;

    public DoctorData (String id, String name, String icNumber, String pass, String gender, String contact, String specialization, String education, String experienceYear) {
        this.id = id;
        this.name = name;
        this.icNumber = icNumber;
        this.pass = pass;
        this.gender = gender;
        this.contact = contact;
        this.specialization = specialization;
        this.education = education;
        this.experienceYear = experienceYear;
    }

    public String getGender () {
        return gender;
    }

    public String getSpecialization () {
        return specialization;
    }

    public String getEducation () {
        return education;
    }

    public String getExperienceYear () {
        return experienceYear;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setExperienceYear(String experienceYear) {
        this.experienceYear = experienceYear;
    }
    // changed wh
}


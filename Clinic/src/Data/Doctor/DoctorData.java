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

    public String getDoctorName () {
        return name;
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

    public void setContact(String contact) {
        this.contact = contact;
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


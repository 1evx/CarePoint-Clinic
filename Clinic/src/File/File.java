package File;
import Data.Admin.AdminData;
import Data.Admin.Admins;
import Data.Appointment.AppointmentData;
import Data.Appointment.Appointments;
import Data.Doctor.DoctorData;
import Data.Doctor.Doctors;
import Data.MedicalRecord.MedicalRecordData;
import Data.MedicalRecord.MedicalRecords;
import Data.Medicine.MedicineData;
import Data.Medicine.Medicines;
import Data.Patient.PatientData;
import Data.Patient.Patients;
import Data.Sequence.SequenceData;
import Data.Sequence.Sequences;
import Data.TimeSlot.TimeSlotData;
import Data.TimeSlot.TimeSlots;

import java.io.*;

public class File {

    public static void readFile(Sequences sequences) {

        try (BufferedReader reader = new BufferedReader(new FileReader("Resource/sequence.txt"))) {
            sequences.addSequenceHeader(reader.readLine());
            for (String data = reader.readLine(); data != null; data = reader.readLine()) {
                String[] dataArray = data.split(",");
                SequenceData sequence = new SequenceData(dataArray[0], dataArray[1], dataArray[2], dataArray[3]);
                sequences.addSequenceData(sequence);
            }
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public static void readFile(Patients patients) {
        try(BufferedReader reader = new BufferedReader(new FileReader("Resource/patient.txt"))) {
            // skip first row
            patients.addPatientHeaders(reader.readLine());
            for (String data = reader.readLine(); data != null; data = reader.readLine()) {
                String[] patientInfo = data.split(",");
                PatientData patient = new PatientData(patientInfo[0], patientInfo[1], patientInfo[2], patientInfo[3], patientInfo[4], patientInfo[5], patientInfo[6]);
                patients.addPatientData(patient);
            }
        }
        catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public static void readFile(Admins admins) {
        try(BufferedReader reader = new BufferedReader(new FileReader("Resource/admin.txt"))) {
            // skip first row
            admins.addAdminHeaders(reader.readLine());
            for (String data = reader.readLine(); data != null; data = reader.readLine()) {
                String[] adminInfo = data.split(",");
                AdminData admin = new AdminData(adminInfo[0], adminInfo[1], adminInfo[2], adminInfo[3], adminInfo[4]);
                admins.addAdminData(admin);
            }
        }
        catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public static void readFile(Doctors doctors) {
        try(BufferedReader reader = new BufferedReader(new FileReader("Resource/doctor.txt"))) {
            // skip first row
            doctors.addDoctorHeaders(reader.readLine());
            for (String data = reader.readLine(); data != null; data = reader.readLine()) {
                String[] doctorInfo = data.split(",");
                DoctorData doctor = new DoctorData(doctorInfo[0], doctorInfo[1], doctorInfo[2], doctorInfo[3], doctorInfo[4], doctorInfo[5], doctorInfo[6], doctorInfo[7], doctorInfo[8]);
                doctors.addDoctorData(doctor);
            }
        }
        catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public static void readFile (TimeSlots timeSlots) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Resource/timeSlot.txt"))) {
            timeSlots.addTimeSlotHeader(reader.readLine());
            for (String data = reader.readLine(); data != null; data = reader.readLine()) {
                String[] timeSlotInfo = data.split(",");
                TimeSlotData timeSlot = new TimeSlotData(timeSlotInfo[0], timeSlotInfo[1], timeSlotInfo[2], timeSlotInfo[3], timeSlotInfo[4], timeSlotInfo[5], timeSlotInfo[6], timeSlotInfo[7]);
                timeSlots.addTimeSlot(timeSlot);
            }
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public static void readFile (Appointments appointments) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Resource/appointment.txt"))) {
            appointments.addAppointmentHeader(reader.readLine());
            for (String data = reader.readLine(); data != null; data = reader.readLine()) {
                String[] appointmentInfo = data.split(",");
                AppointmentData appointment = new AppointmentData(appointmentInfo[0], appointmentInfo[1], appointmentInfo[2], appointmentInfo[3], appointmentInfo[4], appointmentInfo[5], appointmentInfo[6], appointmentInfo[7], appointmentInfo[8]);
                appointments.addAppointment(appointment);
            }
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public static void readFile(MedicalRecords medicalRecords) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Resource/medicalRecord.txt"))) {
            medicalRecords.addMedicalRecordHeader(reader.readLine());
            for (String data = reader.readLine(); data != null; data = reader.readLine()) {
                String[] medicalRecordInfo = data.split(",");
                MedicalRecordData medicalRecord = new MedicalRecordData(medicalRecordInfo[0], medicalRecordInfo[1], medicalRecordInfo[2], medicalRecordInfo[3], medicalRecordInfo[4], medicalRecordInfo[5], medicalRecordInfo[6], medicalRecordInfo[7], medicalRecordInfo[8], medicalRecordInfo[9], medicalRecordInfo[10]);
                medicalRecords.addMedicalRecord(medicalRecord);
            }
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public static void readFile(Medicines medicines) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Resource/medicine.txt"))) {
            medicines.addMedicineHeader(reader.readLine());
            for (String data = reader.readLine(); data != null; data = reader.readLine()) {
                String[] medicineInfo = data.split(",");
                MedicineData medicine = new MedicineData(medicineInfo[0], medicineInfo[1], medicineInfo[2], medicineInfo[3]);
                medicines.addMedicineList(medicine);
            }
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public static void writeFile(String fileName, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Resource/" + fileName))) {
            writer.write(data);
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    public static void appendFile(String fileName, String newData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Resource/" + fileName, true))) {
            writer.write(newData);
            writer.newLine();
            new Sequences().updateId(fileName);
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }
}
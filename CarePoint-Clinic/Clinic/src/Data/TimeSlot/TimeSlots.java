package Data.TimeSlot;

import File.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeSlots {
    private List<String> timeSlotHeaders;
    private final List<TimeSlotData> timeSlotList = new ArrayList<TimeSlotData>();

    public TimeSlots() {
        File.readFile(this);
    }

    public void addTimeSlotHeader(String header) {
        timeSlotHeaders = Arrays.asList(header.split(","));
    }

    public void addTimeSlot (TimeSlotData availableSlot) {
        timeSlotList.add(availableSlot);
    }

    //changed wh
    public List<String> getTimeSlotHeaders() {
        return timeSlotHeaders;
    }

    public List<TimeSlotData> getTimeSlotList() {
        return timeSlotList;
    }
    // changed wh

    public List<TimeSlotData> queryTimeSlot(String queryId, String queryDate, String queryTime, String queryMeridiem, String queryDoctorId, String queryDoctorName, String queryDoctorSpecialization, String queryStatus) {
        return timeSlotList.stream()
                .filter(timeSlot -> queryId == null || timeSlot.getId().equals(queryId))
                .filter(timeSlot -> queryDate == null || timeSlot.getDate().equals(queryDate))
                .filter(timeSlot -> queryTime == null || timeSlot.getTime().equals(queryTime))
                .filter(timeSlot -> queryMeridiem == null || timeSlot.getMeridiem().equals(queryMeridiem))
                .filter(timeSlot -> queryDoctorId == null || timeSlot.getDoctorId().equals(queryDoctorId))
                .filter(timeSlot -> queryDoctorName == null || timeSlot.getDoctorName().equals(queryDoctorName))
                .filter(timeSlot -> queryDoctorSpecialization == null || timeSlot.getDoctorSpecialization().equals(queryDoctorSpecialization))
                .filter(timeSlot -> queryStatus == null || timeSlot.getStatus().equals(queryStatus))
                .toList();
    }

    public void deleteTimeSlot (String deletedId) {
        List<TimeSlotData> deletedTimeSlot = queryTimeSlot(deletedId, null, null, null, null, null, null, null);
        timeSlotList.removeAll(deletedTimeSlot);
        updateTimeSlots();
    }

    // changed wh
    public void editTimeSlot(String id, String date, String time, String meridiem, String doctorId, String doctorName, String doctorSpecialization, String status) {

    }
    // changed wh

    public void updateTimeSlots() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.join(",", timeSlotHeaders)).append("\n");
        for (TimeSlotData timeSlot : timeSlotList) {
            stringBuilder.append(timeSlot.getId()).append(",")
                    .append(timeSlot.getDate()).append(",")
                    .append(timeSlot.getTime()).append(",")
                    .append(timeSlot.getMeridiem()).append(",")
                    .append(timeSlot.getDoctorId()).append(",")
                    .append(timeSlot.getDoctorName()).append(",")
                    .append(timeSlot.getDoctorSpecialization()).append(",")
                    .append(timeSlot.getStatus())
                    .append("\n");
        }
        File.writeFile("timeSlot.txt", stringBuilder.toString());
    }
}

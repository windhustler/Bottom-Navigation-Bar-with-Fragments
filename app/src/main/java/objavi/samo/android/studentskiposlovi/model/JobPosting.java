package objavi.samo.android.studentskiposlovi.model;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JobPosting { // the same names as in the database

    private String category;
    private String jobDescription;
    private String phoneNumber;
    private String jobLocation;
    private String hourlyPay;
    private String userId;
    private String email;
    private String currentTime;

    public JobPosting() { }
    public JobPosting(String category, String jobDescription, String phoneNumber, String jobLocation, String hourlyPay, String userId, String email, String currentTime) {
        this.category = category;
        this.jobDescription = jobDescription;
        this.phoneNumber = phoneNumber;
        this.jobLocation = jobLocation;
        this.hourlyPay = hourlyPay;
        this.userId = userId;
        this.email = email;
        this.currentTime = currentTime;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getHourlyPay() {
        return hourlyPay;
    }

    public void setHourlyPay(String hourlyPay) {
        this.hourlyPay = hourlyPay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "JobPosting{" +
                "category='" + category + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", jobLocation='" + jobLocation + '\'' +
                ", hourlyPay='" + hourlyPay + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", currentTime=" + currentTime +
                '}';
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("category", category);
        result.put("jobDescription", jobDescription);
        result.put("phoneNumber", phoneNumber);
        result.put("jobLocation", jobLocation);
        result.put("hourlyPay", hourlyPay);
        result.put("userId", userId);
        result.put("email", email);
        result.put("currentTime", currentTime);

        return result;

    }
}

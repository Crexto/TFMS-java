package com.tfms.model.entity;
import java.time.LocalDate;

public class Attendance {
    private int id;
    private LocalDate date;
    private int employee_id; 
    private String status;
    private int recorded_by;

    public Attendance(LocalDate date, int employee_id, String status, int recorded_by) {
        this.date = date;
        this.employee_id = employee_id;
        this.status = status;
        this.recorded_by = recorded_by;
    }
    
//    public Attendance(int id, LocalDate date, int employee_id, String employee_name, String status, int recorded_by) {
//        this(date, employee_id, status, recorded_by);
//        this.id = id;
//        this.employee_name = employee_name;
//        
//    }

    public int getId() { return id; }
    public int getEmpId() { return employee_id; }
    public String getStatus() { return status; }
//    public String getEmpName() { return employee_name; }
    public LocalDate getDate() { return date; }
    public int getRecordedBy() { return recorded_by; }
}
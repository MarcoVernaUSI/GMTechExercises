package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.manliogit.javatags.element.Element;

public class Seminar {
    
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION = "location";
    public static final String SEATS_LEFT = "Seats left";
    public static final String TOTAL_SEATS = "totalSeats";
    public static final String START = "start date";
    
    private final String _location;
    private final int _totalSeats;
    private final String _courseName;
    private final String _description;
    private final List<Student> _enrollments;
    private final String _startDate;
    private final int _id;
    private Details _details;

    public Seminar(int id, String location, int totalSeats, String courseName, String courseDescription, String startDate) {
        _id = id;
        _location = location;
        _totalSeats = totalSeats;
        _courseName = courseName;
        _description = courseDescription;
        _startDate = startDate;
        _enrollments = new ArrayList<Student>();
    }
    
    public Seminar(Map<String, String> inputs){
        this(
            //Integer.parseInt(inputs.get(ID)),
            -1,
            inputs.get(LOCATION),
            Integer.parseInt(inputs.get(TOTAL_SEATS)),
            inputs.get(NAME),
            inputs.get(DESCRIPTION),
            inputs.get(START)
            );
    }
    
    public void addEnrollment(Student enrollment) {
        _enrollments.add(enrollment);
    }

    public int getSeatLeft() {
        return _totalSeats - _enrollments.size();
    }

    public String getLocation() {
        return _location;
    }

    public List<Student> getStudentsList() {
        return _enrollments;
    }

    public String getName() {
        return _courseName;
    }

    public String getDescription() {
        return _description;
    }

    public String getStartDate() {
        return _startDate;
    }

    public int getId() {
        return _id;
    }
    
    public int getTotalSeats() {
        return _totalSeats;
    }

    public List<Element> getDetails() {
        return _details.print();
    }

    public void setDetails(Details details) {
        _details = details;
    }

    public boolean valid() {
        //return (!_location.equals("") 
        //    && _totalSeats > 0 
        //    && !_courseName.equals("") 
        //    && !_description.equals("") 
        //    && !_startDate.equals("")
        //    );
        return false;
    }
    
    
  
}



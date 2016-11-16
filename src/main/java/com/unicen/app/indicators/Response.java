package com.unicen.app.indicators;

import java.util.DoubleSummaryStatistics;
import java.util.Vector;

public class Response implements Comparable<Response>{
    private Integer schoolId;
    private String schoolName;
    private String cityName;
    private Double value;

    public Response(Integer schoolId, String schoolName, String cityName, Double value) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.cityName = cityName;
        this.value = value;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getCityName() { return cityName; }

    public Double getValue() {
        return value;
    }

    public Vector<Object> asVector(){
        Vector<Object> v = new Vector<>();
        v.add(this.getSchoolName());
        v.add(this.getCityName());
        v.add(this.getValue());
        return v;
    }

    //para pruebas. Quizas despues no haga falta
    public boolean equals (Object o) {
        Response other = (Response) o;
        return this.getSchoolId().intValue()==other.getSchoolId().intValue();
    }

    public int compareTo(Response r) {
        if (this.getSchoolId().intValue() < r.getSchoolId().intValue())
            return 1;
        if (this.getSchoolId().intValue() > r.getSchoolId().intValue())
            return -1;
        return 0;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}

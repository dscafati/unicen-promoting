package com.unicen.app.indicators;

import java.util.DoubleSummaryStatistics;

public class Response {
    private Integer schoolId;
    private String schoolName;
    private Double value;

    public Response(Integer schoolId, String schoolName, Double value) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.value = value;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public Double getValue() {
        return value;
    }
}

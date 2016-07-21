package com.unicen.app.indicators;

public class Response {
    private String schoolId;
    private String schoolName;
    private Float value;

    public Response(String schoolId, String schoolName, Float value) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.value = value;
    }
}

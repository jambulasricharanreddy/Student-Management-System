package com.smartstudenthub.dto;

import jakarta.validation.constraints.NotNull;

public class AttendanceSessionRequest {

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    private int durationMinutes = 15;

    public AttendanceSessionRequest() {}

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
}

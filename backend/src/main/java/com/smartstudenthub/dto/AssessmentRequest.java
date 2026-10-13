package com.smartstudenthub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AssessmentRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotBlank(message = "Type is required")
    private String type; // INTERNAL_EXAM, SEMESTER_EXAM, ASSIGNMENT, QUIZ

    @NotNull(message = "Maximum marks is required")
    private BigDecimal maxMarks;

    private BigDecimal weightagePercent = new BigDecimal("100.00");

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    private int semester;

    public AssessmentRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getMaxMarks() { return maxMarks; }
    public void setMaxMarks(BigDecimal maxMarks) { this.maxMarks = maxMarks; }

    public BigDecimal getWeightagePercent() { return weightagePercent; }
    public void setWeightagePercent(BigDecimal weightagePercent) { this.weightagePercent = weightagePercent; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
}

package com.smartstudenthub.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MarkEntryDto {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Marks obtained is required")
    private BigDecimal marksObtained;

    private String grade;
    private String remarks;

    public MarkEntryDto() {}

    public MarkEntryDto(Long studentId, BigDecimal marksObtained, String grade, String remarks) {
        this.studentId = studentId;
        this.marksObtained = marksObtained;
        this.grade = grade;
        this.remarks = remarks;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public BigDecimal getMarksObtained() { return marksObtained; }
    public void setMarksObtained(BigDecimal marksObtained) { this.marksObtained = marksObtained; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}

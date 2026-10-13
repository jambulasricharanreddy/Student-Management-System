package com.smartstudenthub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FeeAssignRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Fee category ID is required")
    private Long feeCategoryId;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    private int semester;

    private BigDecimal amountDue; // Optional override

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    public FeeAssignRequest() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getFeeCategoryId() { return feeCategoryId; }
    public void setFeeCategoryId(Long feeCategoryId) { this.feeCategoryId = feeCategoryId; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public BigDecimal getAmountDue() { return amountDue; }
    public void setAmountDue(BigDecimal amountDue) { this.amountDue = amountDue; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}

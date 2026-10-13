package com.smartstudenthub.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardStatsDto {

    private long totalStudents;
    private long activeStudents;
    private long totalLecturers;
    private long totalCourses;
    private long totalDepartments;
    private long todayAttendanceCount;
    private double overallAttendancePercentage;
    private long upcomingExamsCount;
    private long pendingAssignmentsCount;
    private long upcomingEventsCount;
    private long pendingGrievancesCount;
    private BigDecimal totalFeeCollected;
    private BigDecimal totalFeeDue;
    private BigDecimal totalFeeBalance;

    private List<Map<String, Object>> enrollmentByCourse;
    private List<Map<String, Object>> attendanceTrends;
    private List<Map<String, Object>> subjectMarksAverages;
    private List<Map<String, Object>> feeCollectionOverview;
    private List<Map<String, Object>> recentStudents;

    public DashboardStatsDto() {}

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getActiveStudents() { return activeStudents; }
    public void setActiveStudents(long activeStudents) { this.activeStudents = activeStudents; }

    public long getTotalLecturers() { return totalLecturers; }
    public void setTotalLecturers(long totalLecturers) { this.totalLecturers = totalLecturers; }

    public long getTotalCourses() { return totalCourses; }
    public void setTotalCourses(long totalCourses) { this.totalCourses = totalCourses; }

    public long getTotalDepartments() { return totalDepartments; }
    public void setTotalDepartments(long totalDepartments) { this.totalDepartments = totalDepartments; }

    public long getTodayAttendanceCount() { return todayAttendanceCount; }
    public void setTodayAttendanceCount(long todayAttendanceCount) { this.todayAttendanceCount = todayAttendanceCount; }

    public double getOverallAttendancePercentage() { return overallAttendancePercentage; }
    public void setOverallAttendancePercentage(double overallAttendancePercentage) { this.overallAttendancePercentage = overallAttendancePercentage; }

    public long getUpcomingExamsCount() { return upcomingExamsCount; }
    public void setUpcomingExamsCount(long upcomingExamsCount) { this.upcomingExamsCount = upcomingExamsCount; }

    public long getPendingAssignmentsCount() { return pendingAssignmentsCount; }
    public void setPendingAssignmentsCount(long pendingAssignmentsCount) { this.pendingAssignmentsCount = pendingAssignmentsCount; }

    public long getUpcomingEventsCount() { return upcomingEventsCount; }
    public void setUpcomingEventsCount(long upcomingEventsCount) { this.upcomingEventsCount = upcomingEventsCount; }

    public long getPendingGrievancesCount() { return pendingGrievancesCount; }
    public void setPendingGrievancesCount(long pendingGrievancesCount) { this.pendingGrievancesCount = pendingGrievancesCount; }

    public BigDecimal getTotalFeeCollected() { return totalFeeCollected; }
    public void setTotalFeeCollected(BigDecimal totalFeeCollected) { this.totalFeeCollected = totalFeeCollected; }

    public BigDecimal getTotalFeeDue() { return totalFeeDue; }
    public void setTotalFeeDue(BigDecimal totalFeeDue) { this.totalFeeDue = totalFeeDue; }

    public BigDecimal getTotalFeeBalance() { return totalFeeBalance; }
    public void setTotalFeeBalance(BigDecimal totalFeeBalance) { this.totalFeeBalance = totalFeeBalance; }

    public List<Map<String, Object>> getEnrollmentByCourse() { return enrollmentByCourse; }
    public void setEnrollmentByCourse(List<Map<String, Object>> enrollmentByCourse) { this.enrollmentByCourse = enrollmentByCourse; }

    public List<Map<String, Object>> getAttendanceTrends() { return attendanceTrends; }
    public void setAttendanceTrends(List<Map<String, Object>> attendanceTrends) { this.attendanceTrends = attendanceTrends; }

    public List<Map<String, Object>> getSubjectMarksAverages() { return subjectMarksAverages; }
    public void setSubjectMarksAverages(List<Map<String, Object>> subjectMarksAverages) { this.subjectMarksAverages = subjectMarksAverages; }

    public List<Map<String, Object>> getFeeCollectionOverview() { return feeCollectionOverview; }
    public void setFeeCollectionOverview(List<Map<String, Object>> feeCollectionOverview) { this.feeCollectionOverview = feeCollectionOverview; }

    public List<Map<String, Object>> getRecentStudents() { return recentStudents; }
    public void setRecentStudents(List<Map<String, Object>> recentStudents) { this.recentStudents = recentStudents; }
}

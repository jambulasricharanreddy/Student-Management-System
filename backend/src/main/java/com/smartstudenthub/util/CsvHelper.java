package com.smartstudenthub.util;

public class CsvHelper {

    /**
     * Escapes CSV values and protects against CSV formula injection (CVE-2014-3524 / CWE-1236)
     * Prepends single quote if value begins with =, +, -, @
     */
    public static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String sanitized = value.trim();
        if (sanitized.startsWith("=") || sanitized.startsWith("+") || sanitized.startsWith("-") || sanitized.startsWith("@")) {
            sanitized = "'" + sanitized;
        }
        if (sanitized.contains(",") || sanitized.contains("\"") || sanitized.contains("\n") || sanitized.contains("\r")) {
            sanitized = "\"" + sanitized.replace("\"", "\"\"") + "\"";
        }
        return sanitized;
    }
}

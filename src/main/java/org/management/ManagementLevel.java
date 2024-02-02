package org.management;

public enum ManagementLevel {
    TOP_LEVEL("1"),
    MID_LEVEL("2"),
    FIRST_LEVEL("3");

    private final String level;

    ManagementLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public static ManagementLevel getLevelFromValue(String number) {
        for (ManagementLevel managementLevel : ManagementLevel.values()) {
            if (managementLevel.getLevel().equals(number)) {
                return managementLevel;
            }
        }
        return null;
    }
}

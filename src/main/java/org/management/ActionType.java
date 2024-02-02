package org.management;

public enum ActionType {
    EXIT("0"),
    CREATE_NEW_DEPARTMENT("1"),
    UPDATE_THE_DEPARTMENT("2"),
    DELETE_THE_DEPARTMENT("3"),
    ASSIGN_AN_EMPLOYEE_TO_THE_DEPARTMENT("4"),
    REASSIGN_THE_EMPLOYEE_FROM_THE_DEPARTMENT("5"),
    CREATE_NEW_PROJECT("6"),
    UPDATE_THE_PROJECT("7"),
    DELETE_THE_PROJECT("8"),
    ASSIGN_AN_EMPLOYEE_TO_THE_PROJECT("9"),
    REASSIGN_THE_EMPLOYEE_FROM_THE_PROJECT("10"),
    ASSIGN_A_MANAGER_TO_THE_DEPARTMENT("11"),
    REASSIGN_THE_MANAGER_FROM_THE_DEPARTMENT("12"),
    CREATE_NEW_EMPLOYEE("13"),
    UPDATE_THE_EMPLOYEE("14"),
    DELETE_THE_EMPLOYEE("15"),
    CREATE_NEW_MANAGER("16");

    private final String action;

    ActionType(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public static ActionType getActionFromValue(String number) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.getAction().equals(number)) {
                return actionType;
            }
        }
        return null;
    }





}
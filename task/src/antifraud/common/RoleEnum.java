package antifraud.common;

public enum RoleEnum {
    ROLE_ADMINISTRATOR ("ADMINISTRATOR"),
    ROLE_MERCHANT ("MERCHANT"),
    ROLE_SUPPORT ("SUPPORT");

    private final String text;
    RoleEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }




}

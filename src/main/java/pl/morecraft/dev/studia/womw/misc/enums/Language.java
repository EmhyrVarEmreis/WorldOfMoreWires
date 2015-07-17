package pl.morecraft.dev.studia.womw.misc.enums;

public enum Language {

    XX("xx", "XX"), PL("pl", "Polski"), EN("en", "English");

    private String code;
    private String description;

    Language(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

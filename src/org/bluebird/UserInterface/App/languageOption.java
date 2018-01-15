package org.bluebird.UserInterface.App;

public enum languageOption {
    CSharp, Java;

    languageOption() {}

    public String value() {
        return name();
    }

    public static languageOption fromValue(String v) {
        return valueOf(v);
    }
}

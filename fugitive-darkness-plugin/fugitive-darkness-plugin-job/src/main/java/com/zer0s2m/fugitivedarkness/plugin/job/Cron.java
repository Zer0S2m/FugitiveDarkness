package com.zer0s2m.fugitivedarkness.plugin.job;

public enum Cron {

    REGEX("^((((\\d+,)+\\d+|(\\d+(\\/|-|#)\\d+)|\\d+L?|\\*(\\/\\d+)?|L(-\\d+)?|\\?|[A-Z]{3}(-[A-Z]{3})?) ?){5,7})" +
            "|(@(annually|yearly|monthly|weekly|daily|hourly|reboot))|(@every (\\d+(ns|us|µs|ms|s|m|h))+)$");

    private final String value;

    Cron(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}

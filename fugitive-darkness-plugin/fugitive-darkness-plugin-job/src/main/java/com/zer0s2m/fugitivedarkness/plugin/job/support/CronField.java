package com.zer0s2m.fugitivedarkness.plugin.job.support;

import java.time.DateTimeException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.function.BiFunction;

abstract class CronField {

    private static final String[] MONTHS = new String[]
            {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    private static final String[] DAYS = new String[]
            {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    private final Type type;


    protected CronField(Type type) {
        this.type = type;
    }

    /**
     * Return a {@code CronField} enabled for 0 nanoseconds.
     */
    public static CronField zeroNanos() {
        return BitsCronField.ZERO_NANOS;
    }

    /**
     * Parse the given value into a seconds {@code CronField}, the first entry of a cron expression.
     */
    public static CronField parseSeconds(String value) {
        return BitsCronField.parseSeconds(value);
    }

    /**
     * Parse the given value into a minutes {@code CronField}, the second entry of a cron expression.
     */
    public static CronField parseMinutes(String value) {
        return BitsCronField.parseMinutes(value);
    }

    /**
     * Parse the given value into an hours {@code CronField}, the third entry of a cron expression.
     */
    public static CronField parseHours(String value) {
        return BitsCronField.parseHours(value);
    }

    /**
     * Parse the given value into a days of months {@code CronField}, the fourth entry of a cron expression.
     */
    public static CronField parseDaysOfMonth(String value) {
        return parseList(value, Type.DAY_OF_MONTH, (field, type) -> BitsCronField.parseDaysOfMonth(field));
    }

    /**
     * Parse the given value into a month {@code CronField}, the fifth entry of a cron expression.
     */
    public static CronField parseMonth(String value) {
        value = replaceOrdinals(value, MONTHS);
        return BitsCronField.parseMonth(value);
    }

    /**
     * Parse the given value into a days of week {@code CronField}, the sixth entry of a cron expression.
     */
    public static CronField parseDaysOfWeek(String value) {
        return parseList(value, Type.DAY_OF_WEEK, (field, type) -> BitsCronField.parseDaysOfWeek(field));
    }

    private static CronField parseList(String value, Type type, BiFunction<String, Type, CronField> parseFieldFunction) {
        String[] fields = value.split(",");
        CronField[] cronFields = new CronField[fields.length];
        for (int i = 0; i < fields.length; i++) {
            cronFields[i] = parseFieldFunction.apply(fields[i], type);
        }
        return CompositeCronField.compose(cronFields, type, value);
    }

    private static String replaceOrdinals(String value, String[] list) {
        value = value.toUpperCase();
        for (int i = 0; i < list.length; i++) {
            String replacement = Integer.toString(i + 1);
            value = replace(value, list[i], replacement);
        }
        return value;
    }

    public abstract <T extends Temporal & Comparable<? super T>> T nextOrSame(T temporal);

    protected Type type() {
        return this.type;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends Temporal & Comparable<? super T>> T cast(Temporal temporal) {
        return (T) temporal;
    }

    /**
     * Represents the type of cron field, i.e. seconds, minutes, hours,
     * day-of-month, month, day-of-week.
     */
    protected enum Type {

        NANO(ChronoField.NANO_OF_SECOND, ChronoUnit.SECONDS),

        SECOND(ChronoField.SECOND_OF_MINUTE, ChronoUnit.MINUTES, ChronoField.NANO_OF_SECOND),

        MINUTE(ChronoField.MINUTE_OF_HOUR, ChronoUnit.HOURS, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND),

        HOUR(ChronoField.HOUR_OF_DAY, ChronoUnit.DAYS, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND),

        DAY_OF_MONTH(ChronoField.DAY_OF_MONTH, ChronoUnit.MONTHS, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND),

        MONTH(ChronoField.MONTH_OF_YEAR, ChronoUnit.YEARS, ChronoField.DAY_OF_MONTH, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND),

        DAY_OF_WEEK(ChronoField.DAY_OF_WEEK, ChronoUnit.WEEKS, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND);

        private final ChronoField field;

        private final ChronoUnit higherOrder;

        private final ChronoField[] lowerOrders;

        Type(ChronoField field, ChronoUnit higherOrder, ChronoField... lowerOrders) {
            this.field = field;
            this.higherOrder = higherOrder;
            this.lowerOrders = lowerOrders;
        }

        /**
         * Return the value of this type for the given temporal.
         *
         * @return the value of this type
         */
        public int get(Temporal date) {
            return date.get(this.field);
        }

        /**
         * Return the general range of this type. For instance, this method
         * will return 0-31 for {@link #MONTH}.
         *
         * @return the range of this field
         */
        public ValueRange range() {
            return this.field.range();
        }

        /**
         * Check whether the given value is valid, i.e. whether it falls in
         * {@linkplain #range() range}.
         *
         * @param value the value to check
         * @return the value that was passed in
         * @throws IllegalArgumentException if the given value is invalid
         */
        public int checkValidValue(int value) {
            if (this == DAY_OF_WEEK && value == 0) {
                return value;
            } else {
                try {
                    return this.field.checkValidIntValue(value);
                } catch (DateTimeException ex) {
                    throw new IllegalArgumentException(ex.getMessage(), ex);
                }
            }
        }

        public <T extends Temporal & Comparable<? super T>> T elapseUntil(T temporal, int goal) {
            int current = get(temporal);
            ValueRange range = temporal.range(this.field);
            if (current < goal) {
                if (range.isValidIntValue(goal)) {
                    return cast(temporal.with(this.field, goal));
                } else {
                    // goal is invalid, eg. 29th Feb, so roll forward
                    long amount = range.getMaximum() - current + 1;
                    return this.field.getBaseUnit().addTo(temporal, amount);
                }
            } else {
                long amount = goal + range.getMaximum() - current + 1 - range.getMinimum();
                return this.field.getBaseUnit().addTo(temporal, amount);
            }
        }

        public <T extends Temporal & Comparable<? super T>> T rollForward(T temporal) {
            T result = this.higherOrder.addTo(temporal, 1);
            ValueRange range = result.range(this.field);
            return this.field.adjustInto(result, range.getMinimum());
        }

        public <T extends Temporal> T reset(T temporal) {
            for (ChronoField lowerOrder : this.lowerOrders) {
                if (temporal.isSupported(lowerOrder)) {
                    temporal = lowerOrder.adjustInto(temporal, temporal.range(lowerOrder).getMinimum());
                }
            }
            return temporal;
        }

        @Override
        public String toString() {
            return this.field.toString();
        }
    }

    private static String replace(String inString, String oldPattern, String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()) {
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);

        int pos = 0;  // our position in the old string
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }

        // append any characters to the right of a match
        sb.append(inString, pos, inString.length());
        return sb.toString();
    }

    private static boolean hasLength(CharSequence str) {
        return (str != null && !str.isEmpty());
    }

}
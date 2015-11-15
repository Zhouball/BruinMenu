package zhou.allen.bruinmenu;

/**
 * Created by Preetham on 11/8/2015.
 */
public final class StaticVariables {

    public static class HallTimesWeekdays {
        public static final int INTERVAL_HOUR = 3600000;
        public static final int INTERVAL_MINUTE = 60000;

        public static final int DENEVE_BREAKFAST_START = 7*INTERVAL_HOUR;
        public static final int DENEVE_BREAKFAST_END = 10*INTERVAL_HOUR;
        public static final int BPLATE_BREAKFAST_START = 7*INTERVAL_HOUR;
        public static final int BPLATE_BREAKFAST_END = 9*INTERVAL_HOUR;

        public static final int COVEL_LUNCH_START = 11*INTERVAL_HOUR;
        public static final int COVEL_LUNCH_END = 14*INTERVAL_HOUR;
        public static final int DENEVE_LUNCH_START = 11*INTERVAL_HOUR;
        public static final int DENEVE_LUNCH_END = 14*INTERVAL_HOUR;
        public static final int FEAST_LUNCH_START = 11*INTERVAL_HOUR;
        public static final int FEAST_LUNCH_END = 14*INTERVAL_HOUR;
        public static final int BPLATE_LUNCH_START = 11*INTERVAL_HOUR;
        public static final int BPLATE_LUNCH_END = 14*INTERVAL_HOUR;

        public static final int COVEL_DINNER_START = 17*INTERVAL_HOUR;
        public static final int COVEL_DINNER_END = 21*INTERVAL_HOUR;
        public static final int DENEVE_DINNER_START = 17*INTERVAL_HOUR;
        public static final int DENEVE_DINNER_END = 20*INTERVAL_HOUR;
        public static final int FEAST_DINNER_START = 17*INTERVAL_HOUR;
        public static final int FEAST_DINNER_END = 20*INTERVAL_HOUR;
        public static final int BPLATE_DINNER_START = 17*INTERVAL_HOUR;
        public static final int BPLATE_DINNER_END = 20*INTERVAL_HOUR;
    }

    public static class HallTimesWeekends {
        public static final int INTERVAL_HOUR = 3600000;
        public static final int INTERVAL_MINUTE = 60000;

        public static final int COVEL_LUNCH_START = 9*INTERVAL_HOUR+30*INTERVAL_MINUTE;
        public static final int COVEL_LUNCH_END = 14*INTERVAL_HOUR;
        public static final int DENEVE_LUNCH_START = 9*INTERVAL_HOUR+30*INTERVAL_MINUTE;
        public static final int DENEVE_LUNCH_END = 14*INTERVAL_HOUR;
        public static final int FEAST_LUNCH_START = 11*INTERVAL_HOUR;
        public static final int FEAST_LUNCH_END = 14*INTERVAL_HOUR;
        public static final int BPLATE_LUNCH_START = 10*INTERVAL_HOUR;
        public static final int BPLATE_LUNCH_END = 14*INTERVAL_HOUR;

        public static final int COVEL_DINNER_START = 17*INTERVAL_HOUR;
        public static final int COVEL_DINNER_END = 21*INTERVAL_HOUR;
        public static final int DENEVE_DINNER_START = 17*INTERVAL_HOUR;
        public static final int DENEVE_DINNER_END = 20*INTERVAL_HOUR;
        public static final int FEAST_DINNER_START = 17*INTERVAL_HOUR;
        public static final int FEAST_DINNER_END = 20*INTERVAL_HOUR;
        public static final int BPLATE_DINNER_START = 17*INTERVAL_HOUR;
        public static final int BPLATE_DINNER_END = 20*INTERVAL_HOUR;
    }
}

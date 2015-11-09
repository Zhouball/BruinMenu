package zhou.allen.bruinmenu;

import java.util.Calendar;

public class Hall {
    private String mealTime;
    private String item;
    private long id;
    private int startTime = -3600000;
    private int endTime = -3600000;

    public Hall(String n, String mt, long i) {
        item = n;
        mealTime = mt;
        id = i;
        getHallStartTime();
        getHallEndTime();
    }
    public Hall(String n) { item = n; }

    public String getItem() {
        return item;
    }

    public String getMealTime() {
        return mealTime;
    }

    public long getId() {
        return id;
    }

    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }

    public void setStartTime(int st) { startTime=st; }
    public void setEndTime(int et) { endTime=et; }



    private int getHallStartTime() {
        int startTime = -3600000;
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(!(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
            if ("breakfast".equals(mealTime)) {
                if(item.toUpperCase().contains("DE NEVE")) startTime = StaticVariables.HallTimesWeekdays.DENEVE_BREAKFAST_START;
                else if(item.toUpperCase().contains("PLATE")) startTime = StaticVariables.HallTimesWeekdays.BPLATE_BREAKFAST_START;
            } else if ("lunch".equals(mealTime)) {
                if(item.toUpperCase().contains("COVEL")) startTime = StaticVariables.HallTimesWeekdays.COVEL_LUNCH_START;
                else if(item.toUpperCase().contains("DE NEVE")) startTime = StaticVariables.HallTimesWeekdays.DENEVE_LUNCH_START;
                else if(item.toUpperCase().contains("FEAST")) startTime = StaticVariables.HallTimesWeekdays.FEAST_LUNCH_START;
                else if(item.toUpperCase().contains("PLATE")) startTime = StaticVariables.HallTimesWeekdays.BPLATE_LUNCH_START;
            } else if("dinner".equals(mealTime)) {
                if(item.toUpperCase().contains("COVEL")) startTime = StaticVariables.HallTimesWeekdays.COVEL_DINNER_START;
                else if(item.toUpperCase().contains("DE NEVE")) startTime = StaticVariables.HallTimesWeekdays.DENEVE_DINNER_START;
                else if(item.toUpperCase().contains("FEAST")) startTime = StaticVariables.HallTimesWeekdays.FEAST_DINNER_START;
                else if(item.toUpperCase().contains("PLATE")) startTime = StaticVariables.HallTimesWeekdays.BPLATE_DINNER_START;
            }
        } else {
            if ("lunch".equals(mealTime)) {
                if(item.toUpperCase().contains("COVEL")) startTime = StaticVariables.HallTimesWeekends.COVEL_LUNCH_START;
                else if(item.toUpperCase().contains("DE NEVE")) startTime = StaticVariables.HallTimesWeekends.DENEVE_LUNCH_START;
                else if(item.toUpperCase().contains("FEAST")) startTime = StaticVariables.HallTimesWeekends.FEAST_LUNCH_START;
                else if(item.toUpperCase().contains("PLATE")) startTime = StaticVariables.HallTimesWeekends.BPLATE_LUNCH_START;
            } else if("dinner".equals(mealTime)) {
                if(item.toUpperCase().contains("COVEL")) startTime = StaticVariables.HallTimesWeekends.COVEL_DINNER_START;
                else if(item.toUpperCase().contains("DE NEVE")) startTime = StaticVariables.HallTimesWeekends.DENEVE_DINNER_START;
                else if(item.toUpperCase().contains("FEAST")) startTime = StaticVariables.HallTimesWeekends.FEAST_DINNER_START;
                else if(item.toUpperCase().contains("PLATE")) startTime = StaticVariables.HallTimesWeekends.BPLATE_DINNER_START;
            }
        }
        return startTime;
    }

    private int getHallEndTime() {
        int endTime = -3600000;
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(!(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
            if ("breakfast".equals(mealTime)) {
                if(item.toUpperCase().contains("DE NEVE")) endTime = StaticVariables.HallTimesWeekdays.DENEVE_BREAKFAST_END;
                else if(item.toUpperCase().contains("PLATE")) endTime = StaticVariables.HallTimesWeekdays.BPLATE_BREAKFAST_END;
            } else if ("lunch".equals(mealTime)) {
                if(item.toUpperCase().contains("COVEL")) endTime = StaticVariables.HallTimesWeekdays.COVEL_LUNCH_END;
                else if(item.toUpperCase().contains("DE NEVE")) endTime = StaticVariables.HallTimesWeekdays.DENEVE_LUNCH_END;
                else if(item.toUpperCase().contains("FEAST")) endTime = StaticVariables.HallTimesWeekdays.FEAST_LUNCH_END;
                else if(item.toUpperCase().contains("PLATE")) endTime = StaticVariables.HallTimesWeekdays.BPLATE_LUNCH_END;
            } else if("dinner".equals(mealTime)) {
                if(item.toUpperCase().contains("COVEL")) endTime = StaticVariables.HallTimesWeekdays.COVEL_DINNER_END;
                else if(item.toUpperCase().contains("DE NEVE")) endTime = StaticVariables.HallTimesWeekdays.DENEVE_DINNER_END;
                else if(item.toUpperCase().contains("FEAST")) endTime = StaticVariables.HallTimesWeekdays.FEAST_DINNER_END;
                else if(item.toUpperCase().contains("PLATE")) endTime = StaticVariables.HallTimesWeekdays.BPLATE_DINNER_END;
            }
        } else {
            if ("lunch".equals(mealTime)) {
                if(item.toUpperCase().contains("COVEL")) endTime = StaticVariables.HallTimesWeekends.COVEL_LUNCH_END;
                else if(item.toUpperCase().contains("DE NEVE")) endTime = StaticVariables.HallTimesWeekends.DENEVE_LUNCH_END;
                else if(item.toUpperCase().contains("FEAST")) endTime = StaticVariables.HallTimesWeekends.FEAST_LUNCH_END;
                else if(item.toUpperCase().contains("PLATE")) endTime = StaticVariables.HallTimesWeekends.BPLATE_LUNCH_END;
            } else if("dinner".equals(mealTime)) {
                if(item.toUpperCase().contains("COVEL")) endTime = StaticVariables.HallTimesWeekends.COVEL_DINNER_END;
                else if(item.toUpperCase().contains("DE NEVE")) endTime = StaticVariables.HallTimesWeekends.DENEVE_DINNER_END;
                else if(item.toUpperCase().contains("FEAST")) endTime = StaticVariables.HallTimesWeekends.FEAST_DINNER_END;
                else if(item.toUpperCase().contains("PLATE")) endTime = StaticVariables.HallTimesWeekends.BPLATE_DINNER_END;
            }
        }
        return endTime;
    }
}

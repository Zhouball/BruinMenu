package zhou.allen.bruinmenu;

public class Hall {
    private String mealTime;
    private String item;
    private long id;

    public Hall(String n, String mt, long i) {
        item = n;
        mealTime = mt;
        id = i;
    }

    public String getItem() {
        return item;
    }

    public String getMealTime() {
        return mealTime;
    }

    public long getId() {
        return id;
    }
}

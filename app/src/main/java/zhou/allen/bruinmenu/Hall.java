package zhou.allen.bruinmenu;

public class Hall {
    private String mealTime;
    private String item;
    private long id;

    public Hall(String mt, String n, long i) {
        mealTime = mt;
        item = n;
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

package zhou.allen.bruinmenu;

public class Kitchen {
private String item;
private long id;

    public Kitchen(String n, long i) {
        item = n;
        id = i;
    }

    public String getItem() {
        return item;
    }

    public long getId() {
        return id;
    }
}

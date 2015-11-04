package zhou.allen.bruinmenu;

/**
 * Created by Owner on 11/4/2015.
 */
public class Kitchen {
private String item;
private int id;

    public Kitchen(String n, int i) {
        item = n;
        id = i;
    }

    public String getItem() {
        return item;
    }

    public int getId() {
        return id;
    }
}

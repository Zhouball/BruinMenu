package zhou.allen.bruinmenu;

public class MenuItem {
    private String item;
    private String nutriurl;
    private boolean veg;
    private boolean fav;
    private int id;

    public MenuItem(String n, String nu, boolean ve, boolean fa, int i) {
        item = n;
        nutriurl = nu;
        veg = ve;
        fav = fa;
        id = i;
    }

    public String getItem() {
        return item;
    }

    public String getNutriurl() {
        return nutriurl;
    }

    public boolean isVegetarian() {
        return veg;
    }

    public boolean isFavorite() {
        return fav;
    }

    public int getId() {
        return id;
    }
}

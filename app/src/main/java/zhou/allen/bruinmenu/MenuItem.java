package zhou.allen.bruinmenu;

public class MenuItem {
    private String item;
    private String nutriurl;
    private int veg;
    private boolean fav;
    private long id;

    public MenuItem(String n, String nu, int ve, boolean fa, long i) {
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

    public int getVeg() {
        return veg;
    }

    public boolean isFavorite() {
        return fav;
    }

    public long getId() {
        return id;
    }

    public void setFavorite(boolean favorite) {
        this.fav = favorite;
    }
}

/**
 * Created by ttx on 13/09/2017.
 */
public class Category {
    private String name;
    private int cat_id;
    private int parent_id;

    public int getId() {
        return cat_id;
    }

    public void setId(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}

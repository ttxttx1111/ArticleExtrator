import java.util.List;

/**
 * Created by ailly on 17-9-7.
 */
public class Article {
    private String _id;
    private String types;
    private String source;
    private String channel;
    //for category [a_id, b_id], category = {{a_id_leaf...a_id_root},{a_name_left...a_name_root},{b_id_leaf...}...}
    private List<String > tags;
    private String url;
    private String title;


    private String content;
    private String code;
    private List<String> imgID;
    private List<List<Category>> category;

    public Article() {
    }



    public Article(String _id, String types, String source, String channel, List<List<Category>> category, List<String> tags, List<String> keywords, String url, String title, String content, List<String> imgID, List<String> imgURL, List<String> imgOriginalURL, List<String> imgDescription) {
        this._id = _id;
        this.types = types;
        this.source = source;
        this.channel = channel;
        this.category = category;
        this.tags = tags;
        this.url = url;
        this.title = title;
        this.content = content;
        this.imgID = imgID;

    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<List<Category>> getCategory() {
        return category;
    }

    public void setCategory(List<List<Category>> category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgID() {
        return imgID;
    }

    public void setImgID(List<String> imgID) {
        this.imgID = imgID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

import java.util.List;

/**
 * Created by ttx on 15/09/2017.
 */
public class ValidImage {
    
    private String _id;
    private String imgURL;
    private String imgOriginalURL;
    private String imgDescription;
    private List<Category> imgCategory;
    private String articleID;
    private String articleURL;
    private Integer offset;
    private String contextAbove;
    private String contextBelow;
    private String imgSource;

    private Integer imgType;
    private Integer deprecatedFlag;
    private Integer esFlag;


    public int getDeprecatedFlag() {
        return deprecatedFlag;
    }

    public void setDeprecatedFlag(int deprecatedFlag) {
        this.deprecatedFlag = deprecatedFlag;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public int getEsFlag() {
        return esFlag;
    }

    public void setEsFlag(int esFlag) {
        this.esFlag = esFlag;
    }

    public String getImgSource() {
        return imgSource;
    }

    public void setImgSource(String imgSource) {
        this.imgSource = imgSource;
    }


    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getContextAbove() {
        return contextAbove;
    }

    public void setContextAbove(String contextAbove) {
        this.contextAbove = contextAbove;
    }

    public String getContextBelow() {
        return contextBelow;
    }

    public void setContextBelow(String contextBelow) {
        this.contextBelow = contextBelow;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setImgType(Integer imgType) {
        this.imgType = imgType;
    }

    public void setDeprecatedFlag(Integer deprecatedFlag) {
        this.deprecatedFlag = deprecatedFlag;
    }

    public void setEsFlag(Integer esFlag) {
        this.esFlag = esFlag;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getImgOriginalURL() {
        return imgOriginalURL;
    }

    public void setImgOriginalURL(String imgOriginalURL) {
        this.imgOriginalURL = imgOriginalURL;
    }

    public String getImgDescription() {
        return imgDescription;
    }

    public void setImgDescription(String imgDescription) {
        this.imgDescription = imgDescription;
    }

    public List<Category> getImgCategory() {
        return imgCategory;
    }

    public void setImgCategory(List<Category> imgCategory) {
        this.imgCategory = imgCategory;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }


}

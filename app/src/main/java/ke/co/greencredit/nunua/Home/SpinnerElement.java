package ke.co.greencredit.nunua.Home;

/**
 * Created by eorenge on 5/4/16.
 */
public class SpinnerElement {
    String description;
    int imageID;

    public SpinnerElement(String description, int imageID) {
        this.description = description;
        this.imageID = imageID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}

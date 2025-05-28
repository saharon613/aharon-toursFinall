package aharon.tours;

import com.google.gson.annotations.SerializedName;

public class Artwork {
    public String title;
    @SerializedName("image_id")
    public String imageId;
    public String description;
}
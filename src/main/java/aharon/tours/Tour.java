package aharon.tours;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tour {
    public String title;
    public String image;
    @SerializedName("artwork_titles")
    public List<String> artworkTitles;
}
package albrizy.support.mobileads.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class Ad {

    public static final String TYPE_ADMOB = "admob";
    public static final String TYPE_FACEBOOK = "facebook";
    public static final String TYPE_STARTAPP = "startapp";
    public static final String TYPE_UNITY = "unity";
    public static final String TYPE_NONE = "none";

    @SerializedName("id") public String id;
    @SerializedName("type") public String type;
    @SerializedName("banners") public String[] banners;
    @SerializedName("rectangles") public String[] rectangles;
    @SerializedName("interstitials") public String[] interstitials;

}

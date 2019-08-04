package albrizy.support.mobileads.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class AdUnit {

    public static final String BANNER = "banner";
    public static final String RECTANGLE = "rectangle";
    public static final String INTERSTITIAL = "interstitial";

    public static final String TYPE_SPLASH = "splash";
    public static final String TYPE_MAIN = "main";
    public static final String TYPE_POPUP = "popup";
    public static final String TYPE_OTHER = "other";

    public static boolean isReady(@Nullable AdUnit unit) {
        return unit != null && unit.enabled;
    }

    public static AdUnit getDefault() {
        AdUnit unit = new AdUnit();
        unit.type = Ad.TYPE_NONE;
        unit.format = Ad.TYPE_NONE;
        unit.provider = Ad.TYPE_NONE;
        unit.enabled = false;
        return unit;
    }

    @SerializedName("enabled")  public boolean enabled;
    @SerializedName("type")     public String type;
    @SerializedName("format")   public String format;
    @SerializedName("provider") public String provider;

    public AdUnit() {}

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return obj != null && obj.toString()
                .equals(toString());
    }

    @Override
    public String toString() {
        return type + "_"
                + provider + "_"
                + format + "_";
    }
}

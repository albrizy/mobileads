package albrizy.support.mobileads.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import albrizy.support.config.Config;

@SuppressWarnings("WeakerAccess")
public class AdConfig {

    @SerializedName("code") public int code;
    @SerializedName("enabled") public boolean enabled;
    @SerializedName("provider") public Map<String, Ad> ads;
    @SerializedName("placement") public Map<String, AdUnit> adUnits;

    public static boolean isReady(@Nullable Config config) {
        return config != null
                && isReady(config.adConfig);
    }

    public static boolean isReady(@Nullable AdConfig config) {
        int index = (int) Math.floor(Math.random() * 1);
        return config != null
                && config.ads != null
                && config.adUnits != null
                && config.enabled;
    }
}

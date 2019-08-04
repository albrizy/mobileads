package albrizy.support.config;

import com.google.gson.annotations.SerializedName;

import albrizy.support.mobileads.model.AdConfig;

@SuppressWarnings("WeakerAccess")
public class Config {

    @SerializedName("ads") public AdConfig adConfig;
    @SerializedName("updater") public ConfigUpdater configUpdater;
    @SerializedName("maintenance") public ConfigServer serverConfig;

    public Config() {}
}

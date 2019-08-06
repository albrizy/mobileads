package albrizy.support.config;

import com.google.gson.annotations.SerializedName;

public class ConfigUpdater {

    @SerializedName("enabled") public boolean enabled;
    @SerializedName("cancelEnabled") public boolean cancelEnabled;
    @SerializedName("newAppId") public String id;
    @SerializedName("title") public String title;
    @SerializedName("desc") public String desc;
    @SerializedName("action") public String action;
    @SerializedName("cancel") public String cancel;
}

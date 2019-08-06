package albrizy.support.config;

import com.google.gson.annotations.SerializedName;

public class ConfigServer {

    @SerializedName("enabled") public boolean error;
    @SerializedName("title") public String errorTitle;
    @SerializedName("desc") public String errorDesc;
}

package albrizy.support.mobileads.facebook;

import android.app.Activity;
import android.content.Context;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;

import static com.facebook.ads.AdSettings.MultiprocessSupportMode.MULTIPROCESS_SUPPORT_MODE_AUTO;

@SuppressWarnings("unused")
public class FacebookAdapter {

    public static void initialize(Activity activity, String id, boolean debug) {
        Context context = activity.getApplicationContext();
        if (!AudienceNetworkAds.isInAdsProcess(context)) {
            AdSettings.setTestMode(debug);
            AdSettings.setMultiprocessSupportMode(MULTIPROCESS_SUPPORT_MODE_AUTO);
            AudienceNetworkAds
                    .buildInitSettings(context)
                    .initialize();
        }
    }
}

package albrizy.support.mobileads.startapp;

import android.app.Activity;

import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

@SuppressWarnings("unused")
public class StartappAdapter {

    public static void initialize(Activity activity, String id, boolean debug) {
        StartAppSDK.init(activity, id, false);
        StartAppAd.disableAutoInterstitial();
        StartAppAd.disableSplash();
    }
}

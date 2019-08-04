package albrizy.support.mobileads.admob;

import android.app.Activity;

import com.google.android.gms.ads.MobileAds;

@SuppressWarnings("unused")
public class AdmobAdapter {

    static final String TEST_DEVICE = "54F1573E47965AE4697AC89D4C6F0D7E";

    public static void initialize(Activity activity, String adId, boolean debug) {
        MobileAds.initialize(activity.getApplicationContext(), adId);
    }
}

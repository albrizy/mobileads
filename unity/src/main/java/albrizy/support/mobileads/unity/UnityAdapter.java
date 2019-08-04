package albrizy.support.mobileads.unity;

import android.app.Activity;

import com.unity3d.ads.UnityAds;


@SuppressWarnings("unused")
public class UnityAdapter {

    public static void initialize(Activity activity, String adId, boolean debug) {
        UnityAds.initialize(activity, adId, null, debug);
    }
}

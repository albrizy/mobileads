package albrizy.support.mobileads.unity;

import android.app.Activity;
import android.content.Context;

import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds.FinishState;
import com.unity3d.ads.UnityAds.UnityAdsError;
import com.unity3d.ads.UnityAds;

import albrizy.support.mobileads.AdInterstitial;
import albrizy.support.mobileads.AdInterstitialAdapter;

@SuppressWarnings("unused")
public class UnityInterstitialAdapter extends AdInterstitialAdapter {

    public UnityInterstitialAdapter(AdInterstitial instance) {
        super(instance);
    }

    @Override
    protected void loadAd(Context c) {
        String adUnit = getCurrentAdUnitId();
        Activity activity = (Activity) c;
        activity.getWindow().getDecorView()
                .postDelayed(() ->
                        notifyAdRequested(UnityAds.isReady(adUnit)),
                        3000);
    }

    @Override
    protected void showAd(Context c) {
        UnityAds.removeListener(listener);
        UnityAds.addListener(listener);
        String adUnit = getCurrentAdUnitId();
        if (UnityAds.isInitialized() && UnityAds.isReady(adUnit)) {
            Activity activity = (Activity) c;
            UnityAds.show(activity, adUnit);
        }
    }

    @Override
    protected void release() {
        UnityAds.removeListener(listener);
        super.release();
    }

    private final IUnityAdsListener listener = new IUnityAdsListener() {
        @Override
        public void onUnityAdsReady(String id) {
            notifyAdRequested(true);
        }

        @Override
        public void onUnityAdsError(UnityAdsError error, String id) {
            notifyAdRequested(false);
        }

        @Override
        public void onUnityAdsStart(String id) {}
        public void onUnityAdsFinish(String id, FinishState state) {
            if (state == FinishState.COMPLETED) {
                notifyAdClicked();
            }
        }
    };
}

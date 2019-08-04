package albrizy.support.mobileads.facebook;

import android.content.Context;

import androidx.annotation.Nullable;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.util.EnumSet;

import albrizy.support.mobileads.AdInterstitial;
import albrizy.support.mobileads.AdInterstitialAdapter;

@SuppressWarnings("unused")
public class FacebookInterstitialAdapter extends AdInterstitialAdapter {

    @Nullable
    private InterstitialAd interstitial;

    public FacebookInterstitialAdapter(AdInterstitial instance) {
        super(instance);
    }

    @Override
    protected void loadAd(Context c) {
        if (interstitial == null) {
            String adUnit = getCurrentAdUnitId();
            if (adUnit == null) {
                notifyAdRequested(false);
                return;
            }
            interstitial = new InterstitialAd(c, adUnit);
            interstitial.setAdListener(listener);
        }

        if (interstitial != null && !interstitial.isAdLoaded()) {
            interstitial.loadAd(EnumSet.of(CacheFlag.VIDEO));
        }
    }

    @Override
    protected void showAd(Context c) {
        if (interstitial != null) {
            interstitial.show();
        }
    }

    @Override
    protected void release() {
        super.release();
        if (interstitial != null) {
            interstitial.destroy();
            interstitial = null;
        }
    }

    private final InterstitialAdListener listener = new InterstitialAdListener() {

        @Override
        public void onAdLoaded(Ad ad) {
            if (ad == interstitial) {
                notifyAdRequested(true);
            }
        }

        @Override
        public void onError(Ad ad, AdError e) {
            if (ad == interstitial) {
                notifyAdRequested(false, e.getErrorCode());
            }
        }

        @Override
        public void onInterstitialDisplayed(Ad ad) {}
        public void onInterstitialDismissed(Ad ad) {}
        public void onLoggingImpression(Ad ad) {}
        public void onAdClicked(Ad ad) {
            if (ad == interstitial) {
                notifyAdClicked();
            }
        }
    };
}

package albrizy.support.mobileads.startapp;

import android.content.Context;

import androidx.annotation.Nullable;

import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import albrizy.support.mobileads.AdInterstitial;
import albrizy.support.mobileads.AdInterstitialAdapter;

@SuppressWarnings("unused")
public class StartappInterstitialAdapter extends AdInterstitialAdapter {

    @Nullable
    private StartAppAd interstitial;
    private Listener listener;

    public StartappInterstitialAdapter(AdInterstitial instance) {
        super(instance);
        this.listener = new Listener();
    }

    @Override
    protected void loadAd(Context c) {
        if (interstitial == null) {
            interstitial = new StartAppAd(c);
        }

        if (!isLoaded) {
            interstitial.loadAd(listener);
        }
    }

    @Override
    protected void showAd(Context c) {
        if (interstitial != null) {
            interstitial.showAd(listener);
        }
    }

    @Override
    protected void release() {
        super.release();
        if (interstitial != null) {
            interstitial.close();
            interstitial = null;
        }
    }

    private class Listener implements AdEventListener, AdDisplayListener {
        @Override
        public void onReceiveAd(Ad ad) {
            notifyAdRequested(true);
        }

        @Override
        public void onFailedToReceiveAd(Ad ad) {
            notifyAdRequested(false);
        }

        @Override
        public void adHidden(Ad ad) {}
        public void adDisplayed(Ad ad) {}
        public void adNotDisplayed(Ad ad) {}
        public void adClicked(Ad ad) {
            notifyAdClicked();
        }
    }
}

package albrizy.support.mobileads.admob;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import albrizy.support.mobileads.AdInterstitial;
import albrizy.support.mobileads.AdInterstitialAdapter;

@SuppressWarnings("unused")
public class AdmobInterstitialAdapter extends AdInterstitialAdapter {

    @Nullable
    private InterstitialAd interstitial;
    private AdRequest.Builder adBuilder;

    public AdmobInterstitialAdapter(AdInterstitial instance) {
        super(instance);
        this.adBuilder = new AdRequest.Builder();
        this.adBuilder.addTestDevice(AdmobAdapter.TEST_DEVICE);
    }

    @Override
    protected void loadAd(Context c) {
        if (interstitial == null) {
            String adUnit = getCurrentAdUnitId();
            if (adUnit == null) {
                notifyAdRequested(false);
                return;
            }
            interstitial = new InterstitialAd(c);
            interstitial.setAdUnitId(adUnit);
            interstitial.setAdListener(listener);
        }

        if (interstitial != null && !interstitial.isLoaded()) {
            interstitial.loadAd(adBuilder.build());
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
        interstitial = null;
    }

    private final AdListener listener = new AdListener() {
        @Override
        public void onAdLoaded() {
            notifyAdRequested(true);
        }

        @Override
        public void onAdFailedToLoad(int error) {
            notifyAdRequested(false, error);
        }

        @Override
        public void onAdLeftApplication() {
            notifyAdClicked();
        }

        @Override
        public void onAdClosed() {
            if (interstitial != null) {
                interstitial.loadAd(adBuilder.build());
            }
        }
    };
}

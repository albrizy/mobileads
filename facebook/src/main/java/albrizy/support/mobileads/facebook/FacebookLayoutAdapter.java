package albrizy.support.mobileads.facebook;

import android.content.Context;

import androidx.annotation.Nullable;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import albrizy.support.mobileads.AdLayout;
import albrizy.support.mobileads.AdLayoutAdapter;
import albrizy.support.mobileads.model.AdUnit;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

@SuppressWarnings("unused")
public class FacebookLayoutAdapter extends AdLayoutAdapter {

    @Nullable
    private AdView adView;

    public FacebookLayoutAdapter(AdLayout instance) {
        super(instance);
    }

    private AdSize getAdSize() {
        AdUnit unit = instance.getAdUnit();
        return unit.format.equals(AdUnit.RECTANGLE)
                ? AdSize.RECTANGLE_HEIGHT_250 : isLandscape()
                ? AdSize.BANNER_HEIGHT_90
                : AdSize.BANNER_HEIGHT_50;
    }

    private boolean isLandscape() {
        return instance.getResources().getConfiguration()
                .orientation == ORIENTATION_LANDSCAPE;
    }

    @Override
    protected void loadAd(Context c) {
        if (adView == null) {
            String adUnit = getCurrentAdUnitId();
            if (adUnit == null) {
                notifyAdRequested(false);
                return;
            }
            adView = new AdView(c, adUnit, getAdSize());
            adView.setAdListener(listener);
            instance.removeAllViews();
            instance.addView(adView);
        }

        if (!isLoading && !isLoaded) {
            if (adView != null) {
                adView.loadAd();
                isLoading = true;
            }
        }
    }

    @Override
    protected void pause() {}
    protected void release() {
        super.release();
        if (adView != null) {
            adView.setAdListener(null);
            adView.destroy();
            adView = null;
        }
    }

    private final AdListener listener = new AdListener() {

        @Override
        public void onAdLoaded(Ad ad) {
            if (ad == adView) {
                notifyAdRequested(true);
            }
        }

        @Override
        public void onError(Ad ad, AdError e) {
            if (ad == adView) {
                notifyAdRequested(false, e.getErrorCode());
                isLoading = false;
            }
        }

        @Override
        public void onLoggingImpression(Ad ad) {}
        public void onAdClicked(Ad ad) {
            if (ad == adView) {
                notifyAdClicked();
            }
        }
    };
}

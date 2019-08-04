package albrizy.support.mobileads.admob;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import albrizy.support.mobileads.AdLayout;
import albrizy.support.mobileads.AdLayoutAdapter;
import albrizy.support.mobileads.model.AdUnit;

@SuppressWarnings("unused")
public class AdmobLayoutAdapter extends AdLayoutAdapter {

    @Nullable
    private AdView adView;
    private AdRequest.Builder adBuilder;

    public AdmobLayoutAdapter(AdLayout instance) {
        super(instance);
        this.adBuilder = new AdRequest.Builder();
        this.adBuilder.addTestDevice(AdmobAdapter.TEST_DEVICE);
    }

    private AdSize getAdSize() {
        AdUnit unit = instance.getAdUnit();
        return unit.format.equals(AdUnit.RECTANGLE)
                ? AdSize.MEDIUM_RECTANGLE
                : AdSize.SMART_BANNER;
    }

    @Override
    protected void loadAd(Context c) {
        if (adView == null) {
            String adUnit = getCurrentAdUnitId();
            if (adUnit == null) {
                notifyAdRequested(false);
                return;
            }
            adView = new AdView(c);
            adView.setAdSize(getAdSize());
            adView.setAdUnitId(adUnit);
            adView.setAdListener(listener);
            instance.removeAllViews();
            instance.addView(adView);
        }

        if (!isLoading && adView != null) {
            if (isLoaded) {
                adView.resume();
            } else {
                adView.loadAd(adBuilder.build());
                isLoading = true;
            }
        }
    }

    @Override
    protected void pause() {
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    protected void release() {
        super.release();
        if (adView != null) {
            adView.destroy();
            adView = null;
        }
    }

    private final AdListener listener = new AdListener() {
        @Override
        public void onAdLoaded() {
            notifyAdRequested(true);
        }

        @Override
        public void onAdFailedToLoad(int code) {
            notifyAdRequested(false, code);
        }

        @Override
        public void onAdLeftApplication() {
            notifyAdClicked();
        }
    };
}

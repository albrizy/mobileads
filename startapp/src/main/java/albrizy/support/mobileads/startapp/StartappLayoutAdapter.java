package albrizy.support.mobileads.startapp;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.android.publish.ads.banner.Mrec;

import albrizy.support.mobileads.AdLayout;
import albrizy.support.mobileads.AdLayoutAdapter;
import albrizy.support.mobileads.model.AdUnit;

@SuppressWarnings("unused")
public class StartappLayoutAdapter extends AdLayoutAdapter {

    @Nullable
    private Banner adView;

    public StartappLayoutAdapter(AdLayout instance) {
        super(instance);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void loadAd(Context c) {
        if (adView == null) {
            AdUnit u = instance.getAdUnit();
            adView = u.format.equals(AdUnit.RECTANGLE) ? new Mrec(c) : new Banner(c);
            adView.setBannerListener(listener);
            instance.removeAllViews();
            instance.addView(adView);
        }
    }

    @Override
    protected void pause() {}
    protected void release() {
        super.release();
        adView = null;
    }

    private final BannerListener listener = new BannerListener() {
        @Override
        public void onReceiveAd(View view) {
            notifyAdRequested(true);
        }

        @Override
        public void onFailedToReceiveAd(View view) {
            notifyAdRequested(false);
        }

        @Override
        public void onClick(View view) {
            notifyAdClicked();
        }
    };
}

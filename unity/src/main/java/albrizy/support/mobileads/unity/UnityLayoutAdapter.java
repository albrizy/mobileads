package albrizy.support.mobileads.unity;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;

import albrizy.support.mobileads.AdLayout;
import albrizy.support.mobileads.AdLayoutAdapter;

@SuppressWarnings("unused")
public class UnityLayoutAdapter extends AdLayoutAdapter {

    public UnityLayoutAdapter(AdLayout instance) {
        super(instance);
    }

    @Override
    protected void loadAd(Context c) {
        String adUnit = getCurrentAdUnitId();
        if (adUnit == null) {
            notifyAdRequested(false);
            return;
        }

        if (!isLoading && !isLoaded) {
            isLoading = true;
            Activity activity = (Activity) c;
            UnityBanners.setBannerListener(listener);
            UnityBanners.loadBanner(activity, adUnit);
        }
    }

    @Override
    protected void pause() {}
    protected void release() {
        super.release();
        UnityBanners.setBannerListener(null);
        UnityBanners.destroy();
    }

    private final IUnityBannerListener listener = new IUnityBannerListener() {

        private View resolve(View view) {
            if (view.getParent() != null) {
                ViewGroup vg = (ViewGroup) view.getParent();
                vg.removeView(view);
            }
            return view;
        }

        @Override
        public void onUnityBannerLoaded(String id, View view) {
            instance.removeAllViews();
            try {
                instance.addView(view);
            } catch (Exception e) {
                instance.addView(resolve(view));
            } finally {
                notifyAdRequested(true);
            }
        }

        @Override
        public void onUnityBannerUnloaded(String id) {}
        public void onUnityBannerError(String id) {
            notifyAdRequested(false);
        }

        @Override
        public void onUnityBannerShow(String id) {}
        public void onUnityBannerHide(String id) {}
        public void onUnityBannerClick(String id) {
            notifyAdClicked();
        }
    };
}

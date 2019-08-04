package albrizy.support.mobileads;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

import albrizy.support.config.Logger;
import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.model.AdUnit;

abstract class AdAdapter {

    @Nullable
    private String unitId;
    private Random random;
    protected boolean isLoading;
    protected boolean isLoaded;

    AdAdapter() {
        random = new Random();
    }

    @NonNull
    protected abstract AdUnit getAdUnit();

    @Nullable
    protected synchronized final String getCurrentAdUnitId() {
        if (unitId == null) {
            AdUnit unit = getAdUnit();
            String[] adUnits = getAdUnitIds(unit);
            if (adUnits != null) {
                int index = random.nextInt(adUnits.length);
                unitId = adUnits[index];
            }
        }
        return unitId;
    }

    @Nullable
    private static String[] getAdUnitIds(@NonNull AdUnit unit) {
        Ad ad = MobileAds.getInstance().getAd(unit.provider);
        if (ad != null) {
            switch (unit.format) {
                case AdUnit.BANNER: return ad.banners;
                case AdUnit.RECTANGLE: return ad.rectangles;
                case AdUnit.INTERSTITIAL: return ad.interstitials;
                default: break;
            }
        }
        return null;
    }

    protected abstract void loadAd(Context c);

    protected void release() {
        isLoading = false;
        isLoaded = false;
        unitId = null;
    }

    protected abstract void notifyAdClicked();

    protected void notifyAdRequested(boolean loaded) {
        notifyAdRequested(loaded, 0);
    }

    protected void notifyAdRequested(boolean loaded, int error) {
        isLoaded = loaded;
        isLoading = false;
        Logger.ads().d("Ad requested: %d_%s_%s%s", error, loaded,
                getAdUnit().toString(), unitId);

    }
}

package albrizy.support.mobileads;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import albrizy.support.mobileads.model.AdUnit;

public final class AdInterstitial {

    @Nullable private AdListener listener;
    @Nullable private AdInterstitialAdapter adapter;

    @NonNull
    private AdUnit unit = AdUnit.getDefault();
    static final AdHandler handler = new AdHandler();
    private int showAfterClicks;
    private int requestCount;

    public AdInterstitial(Context context) {
        this.showAfterClicks = context.getResources().getInteger(
                R.integer.ad_interstitial_show_after_clicks
        );
    }

    @NonNull
    public AdUnit getAdUnit() {
        return unit;
    }

    @Nullable
    public AdListener getAdListener() {
        return listener;
    }

    public void setRequestCount() {
        this.requestCount = showAfterClicks -1;
    }

    public void setRequestCount(int count) {
        this.requestCount = count;
    }

    public void setAdUnit(@NonNull AdUnit unit) {
        if (!this.unit.equals(unit)) {
            this.unit = unit;
            this.release();
        }
    }

    public void setAdListener(@Nullable AdListener listener) {
        this.listener = listener;
    }

    public boolean isAdLoaded() {
        return adapter != null
                && adapter.isLoaded;
    }

    public void show(Context context) {
        this.show(context, false);
    }

    public synchronized void show(Context context, boolean force) {
        if (handler.isClicked()) return;
        if (adapter == null)
            adapter = AdInterstitialAdapter.create(this, unit);
        if (adapter != null) {
            if (!adapter.isLoaded) adapter.loadAd(context);
            if (force) requestCount = showAfterClicks;
            if (requestCount >= showAfterClicks) {
                requestCount = 0;
                adapter.showAd(context);
            } else requestCount++;
        }
    }

    public void release() {
        if (adapter != null) {
            adapter.release();
            adapter = null;
        }
    }
}

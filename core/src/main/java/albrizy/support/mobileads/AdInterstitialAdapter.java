package albrizy.support.mobileads;

import android.content.Context;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import albrizy.support.config.Logger;
import albrizy.support.mobileads.model.AdUnit;

public abstract class AdInterstitialAdapter extends AdAdapter {

    @Nullable
    static AdInterstitialAdapter create(AdInterstitial instance, AdUnit unit) {
        String clazz = "InterstitialAdapter";
        try {
            clazz = AdUtil.resolve(unit.provider, clazz);
            return (AdInterstitialAdapter) Class.forName(clazz)
                    .getConstructor(instance.getClass())
                    .newInstance(instance);
        } catch (Exception e) {
            Logger.ads().w(e, "Failed to create adapter: %s", clazz);
            return null;
        }
    }

    protected final AdInterstitial instance;

    protected AdInterstitialAdapter(AdInterstitial instance) {
        this.instance = instance;
    }

    @NotNull
    @Override
    protected AdUnit getAdUnit() {
        return instance.getAdUnit();
    }

    protected abstract void showAd(Context c);

    @Override
    protected void notifyAdRequested(boolean loaded, int error) {
        super.notifyAdRequested(loaded, error);
        AdListener listener = instance.getAdListener();
        if (listener != null) {
            listener.onAdRequested(loaded, error);
        }
    }

    @Override
    protected void notifyAdClicked() {
        AdInterstitial.handler.handleClick();
        AdListener listener = instance.getAdListener();
        if (listener != null) {
            listener.onAdClicked();
        }
    }
}

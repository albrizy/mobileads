package albrizy.support.mobileads;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import albrizy.support.config.Logger;
import albrizy.support.mobileads.model.AdUnit;

public abstract class AdLayoutAdapter extends AdAdapter {

    @Nullable
    static AdLayoutAdapter create(AdLayout instance, AdUnit unit) {
        String clazz = "LayoutAdapter";
        try {
            clazz = AdUtil.resolve(unit.provider, clazz);
            return (AdLayoutAdapter) Class.forName(clazz)
                    .getConstructor(instance.getClass())
                    .newInstance(instance);
        } catch (Exception e) {
            Logger.ads().w(e, "Failed to create adapter: %s", clazz);
            return null;
        }
    }

    protected final AdLayout instance;

    protected AdLayoutAdapter(AdLayout instance) {
        this.instance = instance;
    }

    @NotNull
    @Override
    protected AdUnit getAdUnit() {
        return instance.getAdUnit();
    }

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
        AdLayout.handler.handleClick();
        AdListener listener = instance.getAdListener();
        if (listener != null) {
            listener.onAdClicked();
        }
    }

    final void resume() {
        this.loadAd(instance.getContext());

    }

    protected abstract void pause();
}

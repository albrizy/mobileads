package albrizy.support.mobileads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Map;

import albrizy.support.config.Config;
import albrizy.support.config.Logger;
import albrizy.support.config.RemoteConfig;
import albrizy.support.config.RemoteConfig.Callback;
import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.model.AdConfig;
import albrizy.support.mobileads.model.AdType;
import albrizy.support.mobileads.model.AdUnit;

public final class MobileAds {

    private static MobileAds instance;
    public static MobileAds getInstance() {
        return instance;
    }

    public static void initialize(Application app, RemoteConfig config) {
        instance = new MobileAds(app, config);
    }

    public static boolean initialized() {
        return instance.getAdConfig() != null;
    }

    @Nullable
    private AdConfig getAdConfig() {
        return AdConfig.isReady(config)
                ? config.adConfig
                : null;
    }

    @Nullable
    public Ad getAd(@AdType String type) {
        AdConfig config = getAdConfig();
        return config != null
                ? config.ads.get(type)
                : null;
    }

    @Nullable
    public AdUnit getAdUnit(String type) {
        AdConfig config = getAdConfig();
        if (config != null) {
            AdUnit unit = config.adUnits.get(type);
            if (AdUnit.isReady(unit)) {
                return unit;
            }
        }
        return null;
    }

    @Nullable
    public Config config;
    private AdInterstitial interstitial;
    public AdInterstitial getInterstitial() {
        return interstitial;
    }

    private RemoteConfig client;
    private WeakReference<Activity> ref;

    private MobileAds(Context context, RemoteConfig client) {
        this.interstitial = new AdInterstitial(context);
        this.client = client;
        this.client.init(context);
    }

    public void sync(Activity activity) {
        sync(activity, null);
    }

    public void sync(Activity activity, @Nullable Callback callback) {
        if (config != null) {
            if (callback != null) {
                callback.onResponse(config);
            }
            return;
        }
        ref = new WeakReference<>(activity);
        client.sync(c -> initialize(c, callback));
    }

    public void unsync() {
        client.unsync();
        if (ref != null) {
            ref.clear();
            ref = null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void initialize(@Nullable Config c, @Nullable Callback callback) {
        this.config = c;
        try {
            AdConfig config = c.adConfig;
            if (AdConfig.isReady(c)) {
                Logger.ads().d("Preparing AdConfig: %d", config.code);
                for (Map.Entry<String, Ad> v : config.ads.entrySet()) {
                    Ad ad = v.getValue();
                    Logger.ads().d("Initializing Ad: %s", ad.type);
                    String clazz = null;
                    try {
                        clazz = AdUtil.resolve(ad.type, "Adapter");
                        Class.forName(clazz)
                                .getMethod("initialize", Activity.class, String.class, boolean.class)
                                .invoke(null, ref.get(), ad.id, client.isDebug());
                    } catch (Exception e) {
                        Logger.ads().w("Failed to initialize %s", clazz);
                    }
                }
            } else {
                Logger.ads().w("AdConfig is not ready. code: %d", config.code);
            }
        } catch (Exception ignored) {}
        finally {
            if (callback != null) {
                callback.onResponse(c);
            }
        }
    }
}

package albrizy.support.config;

import timber.log.Timber;

public class Logger {

    public static Timber.Tree ads() {
        return Timber.tag("MobileAds");
    }

    public static Timber.Tree config() {
        return Timber.tag("RemoteConfig");
    }
}

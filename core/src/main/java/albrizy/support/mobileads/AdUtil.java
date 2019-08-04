package albrizy.support.mobileads;

import androidx.annotation.NonNull;

import albrizy.support.mobileads.model.AdType;

@SuppressWarnings("WeakerAccess")
public class AdUtil {

    private static final String TAG = "albrizy.support.mobileads.%s.%s";

    @NonNull
    public static String resolve(@AdType String type, String name) {
        String key = type.substring(0, 1).toUpperCase() + type.substring(1);
        return String.format(TAG, type, (key + name));
    }
}

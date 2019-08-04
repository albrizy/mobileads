package albrizy.support.mobileads.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static albrizy.support.mobileads.model.AdUnit.*;
import static albrizy.support.mobileads.model.Ad.TYPE_NONE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@StringDef({
        BANNER,
        RECTANGLE,
        INTERSTITIAL,
        TYPE_NONE
})
public @interface AdFormat {}

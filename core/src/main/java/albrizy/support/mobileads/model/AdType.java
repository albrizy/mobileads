package albrizy.support.mobileads.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static albrizy.support.mobileads.model.Ad.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@StringDef({
        TYPE_ADMOB,
        TYPE_FACEBOOK,
        TYPE_STARTAPP,
        TYPE_UNITY,
        TYPE_NONE
})
public @interface AdType {}

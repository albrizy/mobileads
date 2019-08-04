package okhttp3;

import android.content.Context;
import android.util.Base64;

import albrizy.support.mobileads.R;

import static android.util.Base64.DEFAULT;

public class OkHttpUrl {

    private static final String QUERY = "path=config&key=%s&id=%s&sync=%s";

    private static String getUrl(Context context) {
        String url = context.getString(R.string.config_url);
        if (url.startsWith("http")) {
            url = url.substring(url.lastIndexOf("=") + 1);
        }
        return url;
    }

    public static HttpUrl create(Context context, boolean sync) {
        String url = getUrl(context);
        String key = context.getString(R.string.config_key);
        String token = context.getString(R.string.config_token);
        return HttpUrl.parse(
                String.format(new String(Base64.decode(token, DEFAULT)), key) +
                String.format(QUERY, context.getPackageName(), url, sync)
        );
    }
}

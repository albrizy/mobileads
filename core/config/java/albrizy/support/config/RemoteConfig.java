package albrizy.support.config;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import albrizy.support.mobileads.MobileAds;
import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.model.AdConfig;
import albrizy.support.mobileads.model.AdUnit;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public final class RemoteConfig {

    public static void initialize(Application app, RemoteConfig c) {
        MobileAds.initialize(app, c);
    }

    @Nullable
    public static ConfigServer getConfigServer() {
        Config config = MobileAds.getInstance().config;
        return config != null
                ? config.serverConfig
                : null;
    }

    @Nullable
    public static ConfigUpdater getConfigUpdater() {
        Config config = MobileAds.getInstance().config;
        return config != null
                ? config.configUpdater
                : null;
    }

    @Nullable
    private Task task;
    private Gson parser;
    private Builder param;
    private Request request;

    private RemoteConfig(Builder param) {
        this.param = param;
    }

    public void init(Context context) {
        this.parser = new GsonBuilder().create();
        this.request = new Request.Builder()
                .url(OkHttpUrl.create(context, param.sync))
                .header("Cache-Control", "public, max-age=3600")
                .header("Cache-Exp", "3600")
                .build();
    }

    public void sync(@NonNull Callback callback) {
        task = new Task(this, callback);
        task.execute();
    }

    public void unsync() {
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

    public boolean isDebug() {
        return param.debug;
    }

    public interface Callback {
        void onResponse(@Nullable Config c);
    }

    private static class Task extends AsyncTask<Void, Void, Config> {

        @Nullable
        private Call call;
        private Callback callback;
        private RemoteConfig instance;

        private Task(RemoteConfig instance, Callback callback) {
            this.instance = instance;
            this.callback = callback;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        protected Config doInBackground(Void... voids) {
            OkHttpClient client = instance.param.client;
            call = client.newCall(instance.request);
            try {
                Response res = call.execute();
                Config config = doConfig(res.body().string());
                Logger.config().d("Config response: %s", instance.parser.toJson(config));
                return config;
            } catch (Exception e) {
                Logger.config().w(e, "Unable to parse config");
                return null;
            }
        }

        private Config doConfig(String json) {
            Config c = instance.parser.fromJson(json, Config.class);
            String defConfig = instance.param.config;
            if (defConfig != null) {
                Config local = instance.parser.fromJson(defConfig, Config.class);
                if (c.adConfig == null) {
                    c.adConfig = local.adConfig;
                } else c.adConfig.adUnits = local.adConfig.adUnits;
            }
            doAdConfig(c.adConfig);
            return c;
        }

        private static void doAdConfig(AdConfig c) {
            Map<String, Ad> adMap = new HashMap<>();
            Set<Map.Entry<String, AdUnit>> set = c.adUnits.entrySet();
            Iterator<Map.Entry<String, AdUnit>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, AdUnit> entry = it.next();
                Ad ad = c.ads.get(entry.getValue().provider);
                if (ad != null) {
                    adMap.put(ad.type, ad);
                } else it.remove();
            }
            c.ads = adMap;
        }

        private Config doRemoteConfig(String json) {
            Config c = instance.parser.fromJson(json, Config.class);
            Logger.config().d("Remote response: %s", json);
            return c;
        }

        private Config doLocalConfig(Config c) {
            String json = instance.param.config;
            Config local = instance.parser.fromJson(json, Config.class);
            if (c.adConfig == null) {
                c.adConfig = local.adConfig;
            } else c.adConfig.adUnits = local.adConfig.adUnits;
            Logger.config().d("Local response: %s", json);
            return c;
        }

        @Override
        protected void onPostExecute(Config config) {
            callback.onResponse(config);
        }

        @Override
        protected void onCancelled() {
            if (call != null) {
                call.cancel();
                call = null;
            }
        }
    }

    public static class Builder {

        @Nullable
        private String config;
        private OkHttpClient client;
        private boolean debug;
        private boolean sync;

        private static final Boolean[] SYNC = {true, false};

        public Builder() {
            Random random = new Random();
            sync = SYNC[random.nextInt(SYNC.length)];
        }

        public Builder setClient(@NonNull OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder setDefaultConfig(@Nullable String config) {
            this.config = config;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public RemoteConfig build() {
            return new RemoteConfig(this);
        }
    }
}

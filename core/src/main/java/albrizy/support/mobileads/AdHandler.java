package albrizy.support.mobileads;

import android.os.Handler;

import java.util.HashSet;
import java.util.Set;

class AdHandler {

    private final Set<Callback> callbacks;
    private final Handler handler;

    private boolean clicked;

    AdHandler() {
        this.callbacks = new HashSet<>();
        this.handler = new Handler();
    }

    boolean isClicked() {
        return clicked;
    }

    void register(Callback callback) {
        callbacks.add(callback);
    }

    void unregister(Callback callback) {
        callbacks.remove(callback);
    }

    void handleClick() {
        handler.postDelayed(() ->
        notifyOnClickedChangeListener(clicked = false), 15000);
        notifyOnClickedChangeListener(clicked = true);
    }

    private void notifyOnClickedChangeListener(boolean clicked) {
        for (Callback c : callbacks) {
            c.onClickedChange(clicked);
        }
    }

    interface Callback {
        void onClickedChange(boolean clicked);
    }
}

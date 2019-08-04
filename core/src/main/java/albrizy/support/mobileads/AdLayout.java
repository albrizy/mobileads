package albrizy.support.mobileads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import albrizy.support.mobileads.model.AdUnit;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public final class AdLayout extends FrameLayout {

    @Nullable private AdListener listener;
    @Nullable private AdLayoutAdapter adapter;

    @NonNull
    private AdUnit unit = AdUnit.getDefault();
    static  final AdHandler handler = new AdHandler();
    private final AdHandler.Callback callback;
    private final LayoutParams params;

    public AdLayout(Context context) {
        this(context, null);
    }

    public AdLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdLayout(Context context, AttributeSet attrs, int styleAttr) {
        super(context, attrs, styleAttr);
        this.params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        this.params.gravity = Gravity.CENTER;
        this.callback = clicked -> {
            if (clicked) {
                removeAllViews();
                if (adapter != null) {
                    adapter.release();
                }
            } else {
                if (adapter != null) {
                    adapter.resume();
                }
            }
        };
    }

    @NonNull
    public AdUnit getAdUnit() {
        return unit;
    }

    @Nullable
    public AdListener getAdListener() {
        return listener;
    }

    public void setAdListener(@Nullable AdListener listener) {
        this.listener = listener;
    }

    public void setAdUnit(@NonNull AdUnit unit) {
        if (!this.unit.equals(unit)) {
            this.unit = unit;
            this.releaseAdapter();
            this.onResume();
        }
    }

    public boolean isAdLoaded() {
        return adapter != null
                && adapter.isLoaded;
    }

    @Override
    public void addView(View child) {
        if (getChildCount() == 0) {
            super.addView(child, params);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.register(callback);
    }

    public synchronized void onResume() {
        if (handler.isClicked()) return;
        if (adapter == null) {
            adapter = AdLayoutAdapter.create(this, unit);
        }

        if (adapter != null) {
            adapter.resume();
        }
    }

    public void onPause() {
        if (adapter != null) {
            adapter.pause();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.releaseAdapter();
        handler.unregister(callback);
    }

    private void releaseAdapter() {
        if (adapter != null) {
            adapter.release();
            adapter = null;
        }
    }
}

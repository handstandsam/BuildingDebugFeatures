package com.handstandsam.buildingdebugfeatures.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.handstandsam.buildingdebugfeatures.R;

import timber.log.Timber;


public class HomeScreenShortcutBuilder {

    private final Context applicationContext;

    private final String shortcutDisplayName;

    private Intent deepLinkIntent;

    private int iconRes;

    public HomeScreenShortcutBuilder(@NonNull Context context, String shortcutDisplayName,
                                     @NonNull Intent deepLinkIntent) {
        this.applicationContext = context.getApplicationContext();
        this.shortcutDisplayName = shortcutDisplayName;
        this.deepLinkIntent = deepLinkIntent;
        iconRes = R.mipmap.ic_launcher_round;
    }

    public HomeScreenShortcutBuilder icon(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    public void create() {
        if (deepLinkIntent == null) {
            Timber.e("Can't create without a deepLinkIntent");
            return;
        }

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, deepLinkIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutDisplayName);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(applicationContext, iconRes));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        Timber.v("**** Add Intent:");

        applicationContext.sendBroadcast(addIntent);

        showToast("Shortcut Created: " + shortcutDisplayName + ", Go To Home Screen To See It");
    }

    private void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package detrack.zaryansgroup.com.detrack.activity.app_setttings;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import org.jetbrains.annotations.NotNull;

import detrack.zaryansgroup.com.detrack.BuildConfig;
import timber.log.Timber;


public class AppSettings extends Application {
    private Context context;
    private static AppSettings settings = null;

    public static AppSettings init() {
        if (settings == null) {
            settings = new AppSettings();
        }
        return settings;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private void hInitTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, @NotNull String message, Throwable t) {
                    super.log(priority, String.format(hTag, tag), message, t);
                }
            });
        }
    }

    public static final String hTag = "hashimTimberTags %s";
    @Override
    public void onCreate() {
        super.onCreate();
        hInitTimber();
    }
}

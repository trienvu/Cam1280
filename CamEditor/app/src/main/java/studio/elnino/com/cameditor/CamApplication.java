package studio.elnino.com.cameditor;

import android.app.Application;
import com.aviary.android.feather.sdk.IAviaryClientCredentials;

/**
 * Created by Trienvd on 8/3/15.
 */
public class CamApplication extends Application implements IAviaryClientCredentials {
    private static final String TAG = CamApplication.class.getSimpleName();
    private static final String APP_ID = "120d22e28267468db3802b9c359bab77";
    private static final String APP_SECRET = "a8a6b6fe-3f67-4da0-8f43-d8025743ba85";
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public String getBillingKey() {
        return "";
    }

    @Override
    public String getClientID() {
        return APP_ID;
    }

    @Override
    public String getClientSecret() {
        return APP_SECRET;
    }
}

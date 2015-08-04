package studio.elnino.com.cameditor;

import android.app.Application;

import com.adobe.creativesdk.foundation.internal.auth.AdobeAuthManager;
import com.aviary.android.feather.sdk.IAviaryClientCredentials;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by browan2306 on 8/3/15.
 */
public class CamApplication extends Application implements IAviaryClientCredentials {
    private static final String TAG = CamApplication.class.getSimpleName();
    private static final String API_KEY = "*120d22e28267468db3802b9c359bab77";
    private static final String API_SECRET = "8a6b6fe-3f67-4da0-8f43-d8025743ba85";

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    public void initialize() {
        AdobeAuthManager manager = AdobeAuthManager.sharedAuthManager();
        manager.initWithApplicationContext(this);
        try {
            manager.setAuthenticationParameters(API_KEY, API_SECRET, null);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBillingKey() {
        return null;
    }

    @Override
    public String getClientID() {
        return API_KEY;
    }

    @Override
    public String getClientSecret() {
        return API_SECRET;
    }
}

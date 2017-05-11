package mivadounou.projet2.uut.ucao.mivadounou.activities;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;
import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.entities.FabricAuth;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.DigitsFragment;

public class DigitsActivity extends AppCompatActivity {
    private AuthCallback authCallback;

    public class FabricAuth extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
            Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
            authCallback = new AuthCallback() {
                @Override
                public void success(DigitsSession session, String phoneNumber) {
                    // Do something with the session
                }

                @Override
                public void failure(DigitsException exception) {
                    // Do something on failure
                }
            };
        }

        public AuthCallback getAuthCallback(){
            return authCallback;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digits);
        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(((FabricAuth) getApplication()).getAuthCallback());
        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
                .withAuthCallBack(authCallback)
                .withPhoneNumber("+228");

        Digits.authenticate(authConfigBuilder.build());
    }

    }



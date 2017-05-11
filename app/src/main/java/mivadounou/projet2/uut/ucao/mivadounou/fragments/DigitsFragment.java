package mivadounou.projet2.uut.ucao.mivadounou.fragments;


import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import mivadounou.projet2.uut.ucao.mivadounou.activities.DigitsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitsFragment extends Fragment {
    private AuthCallback authCallback;


//    public class FabricAuth extends Application {
//        @Override
//        public void onCreate() {
//            super.onCreate();
//            TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
//            Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
//            authCallback = new AuthCallback() {
//                @Override
//                public void success(DigitsSession session, String phoneNumber) {
//                    // Do something with the session
//                }
//
//                @Override
//                public void failure(DigitsException exception) {
//                    // Do something on failure
//                }
//            };
//        }
//
//        public AuthCallback getAuthCallback(){
//            return authCallback;
//        }
//    }
//
//
//    public DigitsFragment() {
//        // Required empty public constructor
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_digits, container, false);

    }
}
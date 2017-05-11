package mivadounou.projet2.uut.ucao.mivadounou.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mivadounou.projet2.uut.ucao.mivadounou.R;


/**
 * Created by light on 09/05/17.
 */

public class Splash2 extends AppCompatActivity {

    private static int SPLASHTIMEOUT=3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysplash);


        SharedPreferences shared = getPreferences(Context.MODE_PRIVATE);
        String valeur = shared.getString("splash", "oui");
        Log.i("VALEUR DE LA PREF : ", "VALEUR :"+valeur);

        if(valeur.contentEquals("non"))
        {
            Intent i  = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else {
            run();
        }



    }




    public void run()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i  = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASHTIMEOUT);
    }
}

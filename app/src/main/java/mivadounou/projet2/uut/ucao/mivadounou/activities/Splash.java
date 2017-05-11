package mivadounou.projet2.uut.ucao.mivadounou.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import mivadounou.projet2.uut.ucao.mivadounou.R;

/**
 * Created by light on 08/05/17.
 */

public class Splash extends AppCompatActivity implements Animation.AnimationListener{

    Animation zoomin,zoomout,blink;
    ImageView splash;
    Intent  i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysplash);

   /*     if (Prefs.getString("splash", "oui").contentEquals("non"))
            i = new Intent(this, MainActivity.class);
            startActivity(i); */

        splash = (ImageView)findViewById(R.id.ivsplash);

        zoomin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        zoomout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        zoomin.setAnimationListener(this);
        zoomout.setAnimationListener(this);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        blink.setAnimationListener(this);

        splash.setAnimation(zoomin);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (animation == zoomin)
            i = new Intent(this, MainActivity.class);
            startActivity(i);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

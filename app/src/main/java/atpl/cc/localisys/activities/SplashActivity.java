package atpl.cc.localisys.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import atpl.cc.localisys.R;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences sp2;
    public static DatabaseReference mDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        sp2 = getSharedPreferences("sp2", Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sp.getString("u_email", "").equalsIgnoreCase("")) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finishAffinity();
                } else {
                    startActivity(new Intent(SplashActivity.this, Localisys_HomeActivity.class));
                    finishAffinity();
                }
            }
        }, 3000);

        recordScreenView();
    }

    private void recordScreenView() {
        String screenName = "splash";
        try {
            mFirebaseAnalytics.setCurrentScreen(this, screenName, null /* class override */);
            // [END set_current_screen]
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

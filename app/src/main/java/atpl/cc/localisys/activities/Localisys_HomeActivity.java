package atpl.cc.localisys.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;

import atpl.cc.localisys.R;
import atpl.cc.localisys.fragments.HomeFragment;

public class Localisys_HomeActivity extends AppCompatActivity {

    static public ImageView plus_button, img_serch, img_notification, img_acount;
    static public ImageView img_home, location_icon;
    SharedPreferences sp;
    public static String user_id = "";
    public static String user_name = "";
    public static String user_image = "";
    public static FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisys__home);

        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        user_name = sp.getString("u_name", "");
        Log.d("user_name", user_name);
        user_image = sp.getString("u_image", "");
        Log.d("user_image", user_image);
        user_id = sp.getString("user_id","");
        Log.d("user_id", user_id);

        plus_button=(ImageView) findViewById(R.id.plus_button);
        img_serch = (ImageView) findViewById(R.id.img_serch);
        img_home = (ImageView) findViewById(R.id.img_home);
        img_notification=(ImageView)findViewById(R.id.img_notification);
        img_acount=(ImageView)findViewById(R.id.img_acount);
        img_home.setImageResource(R.drawable.footer_home_blue);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commitNowAllowingStateLoss();
    }
}

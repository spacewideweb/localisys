package atpl.cc.localisys.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;

import atpl.cc.localisys.R;

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
    }
}

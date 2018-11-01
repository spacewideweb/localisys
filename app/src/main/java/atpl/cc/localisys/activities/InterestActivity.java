package atpl.cc.localisys.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import atpl.cc.localisys.R;
import atpl.cc.localisys.Constants;
import atpl.cc.localisys.adapter.InterestAdapter;
import atpl.cc.localisys.adapter.SkillAdapter;
import atpl.cc.localisys.modal.User;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static android.R.attr.data;
import static android.R.attr.focusable;


public class InterestActivity extends AppCompatActivity {

    Button start,skip_interest;
    AlertDialog alert;
    ImageView img_check1;
    LinearLayout lin_hint1;
    int click =1;
    MyRecyclerViewAdapter adapter;
    ArrayList<String> arrayList_result = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<String> arrayList;
    RecyclerView rv_interest;
    InterestAdapter interestAdapter;
    String interests = "";
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor;
    String user_id = "";
    EditText search;
    ImageView img_search;
    ProgressDialog pd;
    Map<String,Boolean> map = new HashMap<>();
    LinearLayout linEdit;
    TextView tvCancel;
    LinearLayout linSave;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        sp2 = getSharedPreferences("sp2", Context.MODE_PRIVATE);
        user_id = sp.getString("user_id","");
        linEdit = (LinearLayout)findViewById(R.id.linEdit);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        pd = new ProgressDialog(InterestActivity.this,R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setIndeterminateDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.my_progress_indeterminate,null));


        start = (Button) findViewById(R.id.start);

        arrayList = new ArrayList<String>();
       /* arrayList.add("Woodwork");
        arrayList.add("Cooking");
        arrayList.add("Sewing");
        arrayList.add("Gardening");
        arrayList.add("Baby sitting");
        arrayList.add("Electronics");
        arrayList.add("Dog Walking");
        arrayList.add("Farming");
        arrayList.add("Needlework");
        arrayList.add("Driving");
        arrayList.add("Hair Dressing");
        arrayList.add("Plumbering");
        arrayList.add("Cleaning");
        arrayList.add("Floristics");
        arrayList.add("Engineering");
        arrayList.add("Dancing");*/

        //AddEvent();



        tvCancel = (TextView)findViewById(R.id.tvCancel);
        linSave = (LinearLayout)findViewById(R.id.linSave);
        recyclerView = (RecyclerView) findViewById(R.id.re_view);
        rv_interest = (RecyclerView) findViewById(R.id.rv_interest);
        search = (EditText) findViewById(R.id.search);
        img_search = (ImageView) findViewById(R.id.img_search);

        fetchInterestHashTag();

        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(InterestActivity.this, arrayList_result);
        recyclerView.setAdapter(adapter);

        rv_interest.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InterestActivity.this.finish();
            }
        });



    img_search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        interestsSearchPerformed();
        arrayList.add(0,search.getText().toString());
        interestAdapter.notifyDataSetChanged();
    }
});


        skip_interest=(Button)findViewById(R.id.skip_interest);
        img_check1 = (ImageView)findViewById(R.id.img_check1);
        lin_hint1 = (LinearLayout)findViewById(R.id.lin_hint1);
        if (sp.getString("fr","").equalsIgnoreCase("edit")) {
            linSave.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);

            getAddedInterest();

            linEdit.setVisibility(View.GONE);

            linSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayList_result.size()>0) {
                        update_Details(user_id);

                    }else {
                        Constants.Information_Dialog(InterestActivity.this,"Please select your interest.");
                    }
                }
            });
        }else {
            linEdit.setVisibility(View.VISIBLE);

            linSave.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interestsNextButtonTapped();
                    if (arrayList_result.size() > 0) {
                        editor = sp2.edit();
                        editor.putString("allow_id_inter", "inter").commit();
                        update_Details(user_id);
                        show_dialog(InterestActivity.this);
                    } else {
                        Constants.Information_Dialog(InterestActivity.this, "Please select your interest.");
                    }
                }
            });
        }

        skip_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor=sp2.edit();
                editor.putString("allow_id_inter","inter").commit();
                skipInterestsSelection();

               LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.popup_done,
                        (ViewGroup) findViewById(R.id.lin_done));
                ImageView my_cross =(ImageView)view.findViewById(R.id.im_cancel);
                ImageView my_tick = (ImageView) view.findViewById(R.id.im_done);
                my_tick.setVisibility(View.GONE);
                my_cross.setVisibility(View.VISIBLE);

                ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(my_cross,
                        PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f),
                        PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f),
                        PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f));
                scaleDown.setDuration(2000);
                scaleDown.start();


                final Toast toast = new Toast(InterestActivity.this);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                scaleDown.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toast.cancel();
                        startActivity(new Intent(InterestActivity.this, Localisys_HomeActivity.class));
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                //stop animation

                //startActivity(new Intent(getApplicationContext(), SortActivity.class));
            }
        });

        ((ImageView)findViewById(R.id.back_from_notifiction)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        img_check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestsHelpButtonTapped();
                if (click == 1){
                    lin_hint1.setVisibility(View.VISIBLE);
                    click = 0;
                }else {
                    lin_hint1.setVisibility(View.GONE);
                    click = 1;
                }
            }
        });
        recordScreenView();

    }

    public void skipInterestsSelection(){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("skipInterestsSelection",bundle);
    }

    public void interestsHelpButtonTapped(){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("interestsHelpButtonTapped",bundle);
    }

    public void interestsSearchPerformed(){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("interestsSearchPerformed",bundle);
    }

    public void interestsNextButtonTapped(){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("interestsNextButtonTapped",bundle);
    }



    public void getAddedInterest(){
        arrayList_result.clear();
        pd.show();
        try {

            DatabaseReference skilldatabaseRef = FirebaseDatabase.getInstance().getReference("User");
            skilldatabaseRef.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pd.dismiss();
                    if(dataSnapshot.exists())
                    {
                        DataSnapshot user = dataSnapshot.child("interestHashtags");
                        if (user.exists()) {
                            for (DataSnapshot dd : user.getChildren()) {
                                Log.d("childer", dd.getKey() + "");
                                add(dd.getKey());
                                remove(dd.getKey());
                            }
                        }
                    }else {
                        Log.d("database","empty");
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    pd.dismiss();
                    Log.d("errr",databaseError.getMessage()+"");
                }
            });
        }catch (Exception e){
            pd.dismiss();
        }
    }


    private void recordScreenView() {
        String screenName = "Interest";
        try {
            mFirebaseAnalytics.setCurrentScreen(this, screenName, null /* class override */);
            // [END set_current_screen]
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void fetchInterestHashTag(){

        pd.show();
        try {

            DatabaseReference skilldatabaseRef = FirebaseDatabase.getInstance().getReference("SuggestedInterestHashtags");
            skilldatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pd.dismiss();
                    if(dataSnapshot.exists())
                    {
                        for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                        {
                            arrayList.add(postSnapShot.getValue().toString());
                            Log.d("databasenodes",postSnapShot.getValue().toString()+"");

                        }
                        interestAdapter = new InterestAdapter(InterestActivity.this,arrayList);
                        rv_interest.setAdapter(interestAdapter);

                    }else {
                        Log.d("database","empty");
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    pd.dismiss();
                    Log.d("errr",databaseError.getMessage()+"");
                }
            });
        }catch (Exception e){
            pd.dismiss();
            SplashActivity.mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        }

        ////////////////////////////


    }

    public void show_dialog(Context context){
        timer_async();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_first,null);
        ImageView img_go = (ImageView)view.findViewById(R.id.img_go);
        builder.setView(view);
        alert = builder.create();
        alert.setCancelable(false);
        alert.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
       /* img_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                startActivity(new Intent(InterestActivity.this, Localisys_HomeActivity.class));
                finishAffinity();
            }
        });*/
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alert!=null){
            alert.dismiss();
        }
    }

    public void timer_async(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(InterestActivity.this, Localisys_HomeActivity.class));
                finishAffinity();

            }
        },3000);
    }




    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void add(String name){
        Log.d("logggg","calll");
        arrayList_result.add(name);
        map.put(name,true);
        adapter.notifyDataSetChanged();


    }


    public void remove(String name){
        if (arrayList.size()>0){
            for (int i=0;i<arrayList.size();i++){
                if (arrayList.get(i).equalsIgnoreCase(name)){
                    arrayList.remove(i);
                }
            }
        }
    }

    public void add_add(String name){
        Log.d("log","calll");
        arrayList.add(name);
        interestAdapter.notifyDataSetChanged();
    }


    public void update_Details(String userId){
        pd.show();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
        Map<String,Object> param = new HashMap<>();
        param.put("interestHashtags", map);
        mDatabase.child(userId).updateChildren(param, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.d("inserted", "inserted");
                    pd.dismiss();

                    if (sp.getString("fr","").equalsIgnoreCase("edit")){
                        arrayList_result.clear();
                        adapter.notifyDataSetChanged();
                        Information_Dialog(InterestActivity.this,"Your interest are successfully updated.");
                    }
                    //sp.edit().putString("u_skill",skills).commit();
                    //show_animations();
                }else {
                    pd.dismiss();
                    Log.d("errr","errr");
                }
            }
        });
    }

    public void Information_Dialog(Context context,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_infodialog,null);
        TextView tv_Infomsg = (TextView)view.findViewById(R.id.tv_Infomsg);
        TextView btn_ok = (TextView)view.findViewById(R.id.btn_ok);
        tv_Infomsg.setText(msg);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                InterestActivity.this.finish();
            }
        });
        dialog.show();
    }



   /* public void AddEvent(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SuggestedInterestHashtags");
        String userId = mDatabase.push().getKey();
       // SkillPost post = new SkillPost(SelectPlaceActivity.location, price, desc, arrayList_result, db_images, isSecret, Localisys_HomeActivity.user_id, suspension, currentDateTimeString, title, "event");
        //AddService as = new AddService(image,title,desc,hashtag,location,price,"",user_email,"services","",user_name,user_image);
        mDatabase.setValue(arrayList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){


                }else {
                    //Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });
    }*/

}

package atpl.cc.localisys.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import atpl.cc.localisys.R;
import atpl.cc.localisys.Constants;
import atpl.cc.localisys.adapter.MySkillRecyclerAdapter;
import atpl.cc.localisys.adapter.SkillAdapter;
import atpl.cc.localisys.activities.fragments.SkillDetiilsFragment;
import atpl.cc.localisys.modal.User;

public class SkillsActivity extends AppCompatActivity{

    Button skill_next, skip_skill;
    ImageView img_help;
    int click = 1;
    LinearLayout lin_hint;
    MySkillRecyclerAdapter adapter;
    ArrayList<String> arrayList_result = new ArrayList<>();
    RecyclerView recyclerView,grid_view;
    ArrayList<String> arrayList;
    SkillAdapter skillAdapter;
    String skills = "";
    SharedPreferences sp;
    SharedPreferences sp2;
    SharedPreferences.Editor editor;
    EditText search;
    String user_id = "";
    ImageView img_search;
    Map<String,Boolean> map = new HashMap<>();
    ProgressDialog pd;
    LinearLayout linEditSk,linSave;
    TextView tvCancel;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        sp2 = getSharedPreferences("sp2", Context.MODE_PRIVATE);
        user_id = sp.getString("user_id","");
        linEditSk = (LinearLayout)findViewById(R.id.linEditSk);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        tvCancel = (TextView)findViewById(R.id.tvCancel);
        linSave = (LinearLayout)findViewById(R.id.linSave);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkillsActivity.this.finish();
            }
        });

        pd = new ProgressDialog(SkillsActivity.this,R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setIndeterminateDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.my_progress_indeterminate,null));

        arrayList = new ArrayList<String>();


        fetchSkillHashTag();


        recyclerView = (RecyclerView) findViewById(R.id.re_view);
        grid_view = (RecyclerView)findViewById(R.id.grid_viewv);
        search = (EditText) findViewById(R.id.search);
        img_search = (ImageView) findViewById(R.id.img_search);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MySkillRecyclerAdapter(SkillsActivity.this, arrayList_result);
        recyclerView.setAdapter(adapter);
        grid_view.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));

        skill_next=(Button)findViewById(R.id.skill_next);
        skip_skill=(Button)findViewById(R.id.skip_skill);
        img_help = (ImageView)findViewById(R.id.img_help);
        lin_hint = (LinearLayout)findViewById(R.id.lin_hint);

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skillsSearchPerformed();
                arrayList.add(0,search.getText().toString());
                skillAdapter.notifyDataSetChanged();
            }
        });





        if (sp.getString("sr","").equalsIgnoreCase("edit")) {
            linEditSk.setVisibility(View.GONE);


            getAddedSkills();
            linSave.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);

            linSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayList_result.size()>0) {
                        update_Details(user_id);
                    }else {
                        Constants.Information_Dialog(SkillsActivity.this,"Please select your interest.");
                    }
                }
            });
        }else {
            linEditSk.setVisibility(View.VISIBLE);

            linSave.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);

            skill_next.setText("Next");
            skill_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    skillsNextButtonTapped();

                    if (arrayList_result.size()>0){
                        editor=sp2.edit();
                        editor.putString("allow_id_skill","skill").commit();
                        update_Details(user_id);
                    }else {
                        Constants.Information_Dialog(SkillsActivity.this,"Please select your skill.");
                    }
                }
            });
        }

        skip_skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor=sp2.edit();
                editor.putString("allow_id_skill","skill");
                editor.commit();
                skipSkillsSelection();


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


                final Toast toast = new Toast(SkillsActivity.this);
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
                        startActivity(new Intent(SkillsActivity.this, InterestActivity.class));
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });



                //stop animation

            }
        });

        ((ImageView)findViewById(R.id.back_from_notifiction)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skillsHelpButtonTapped();
                if (click == 1){
                    lin_hint.setVisibility(View.VISIBLE);
                    click = 0;
                }else {
                    lin_hint.setVisibility(View.GONE);
                    click = 1;
                }
            }
        });

        recordScreenView();

    }

    public void skipSkillsSelection(){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("skipSkillsSelection",bundle);
    }

    public void skillsHelpButtonTapped(){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("skillsHelpButtonTapped",bundle);
    }

    public void skillsSearchPerformed(){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("skillsSearchPerformed",bundle);
    }

    public void skillsNextButtonTapped(){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("skillsNextButtonTapped",bundle);
    }

    private void recordScreenView() {
        String screenName = "skill";
        try {
            mFirebaseAnalytics.setCurrentScreen(this, screenName, null /* class override */);
            // [END set_current_screen]
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getAddedSkills(){
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
                        DataSnapshot user = dataSnapshot.child("skillHashtags");
                        if (user.exists()) {
                            for (DataSnapshot dd : user.getChildren()) {
                                Log.d("childer", dd.getKey() + "");
                                //arrayList_result.add(dd.getKey());
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


    public void fetchSkillHashTag(){

        pd.show();
        try {

            DatabaseReference skilldatabaseRef = FirebaseDatabase.getInstance().getReference("SuggestedSkillHashtags");
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
                        skillAdapter = new SkillAdapter(SkillsActivity.this,arrayList);
                        grid_view.setAdapter(skillAdapter);

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

    public void add(String name){
        Log.d("logggg","calll");
         arrayList_result.add(name);
         map.put(name,true);
        adapter.notifyDataSetChanged();



    }

    public void show_animations(){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_done,
                (ViewGroup) findViewById(R.id.lin_done));
        ImageView my_tick=(ImageView)view.findViewById(R.id.im_done);

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(my_tick,
                PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f),
                PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f),
                PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f));
        scaleDown.setDuration(2000);
        scaleDown.start();


        final Toast toast = new Toast(SkillsActivity.this);
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
                startActivity(new Intent(SkillsActivity.this, InterestActivity.class));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


    public void update_Details(String userId){
        pd.show();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
        Map<String,Object> param = new HashMap<>();
        param.put("skillHashtags", map);
        mDatabase.child(userId).updateChildren(param, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.d("inserted", "inserted");
                    pd.dismiss();
                    //sp.edit().putString("u_skill",skills).commit();
                    //show_animations();
                    if (sp.getString("sr","").equalsIgnoreCase("edit")){
                        Information_Dialog(SkillsActivity.this,"Your skills are successfully updated.");
                        arrayList_result.clear();
                        adapter.notifyDataSetChanged();
                    }else {
                        startActivity(new Intent(SkillsActivity.this, InterestActivity.class));
                    }
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
                SkillsActivity.this.finish();
            }
        });
        dialog.show();
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
        skillAdapter.notifyDataSetChanged();
    }


}

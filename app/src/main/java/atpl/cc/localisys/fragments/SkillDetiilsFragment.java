package atpl.cc.localisys.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import atpl.cc.localisys.R;
import atpl.cc.localisys.activities.Constants;
import atpl.cc.localisys.activities.activities.Localisys_HomeActivity;
import atpl.cc.localisys.activities.activities.SelectPlaceActivity;
import atpl.cc.localisys.activities.adapter.HorizontalEventsAdapter;
import atpl.cc.localisys.activities.adapter.SlidingImage_Adapter;
import atpl.cc.localisys.activities.adapter.TagPeopleAdapter;
import atpl.cc.localisys.activities.classes.DashboardClass;
import atpl.cc.localisys.activities.classes.SkillPost;
import atpl.cc.localisys.activities.modal.User;
import atpl.cc.localisys.classes.SkillPost;

public class SkillDetiilsFragment extends Fragment {

    ImageView loc_img;
  View view;
    ImageView back, options;
    Button btn_request;
    String data,from="";
    public static SkillPost dc;
    ImageView contact_image_ID;
    TextView tvTitle,tvDesc,loc_text,price,price_type;
    RecyclerView re_view;
    ImageView menu_op;
    ProgressDialog pd;
    SharedPreferences sp;
    public static String id="";
    String isSuspended = "";
    private FirebaseAnalytics mFirebaseAnalytics;
    LinearLayout linHide;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_skill_detiils, container, false);
        contact_image_ID = view.findViewById(R.id.contact_image_ID);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        pd = new ProgressDialog(getActivity(),R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setIndeterminateDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.my_progress_indeterminate,null));

        tvTitle = view.findViewById(R.id.tvTitle);
        linHide = view.findViewById(R.id.linHide);
        tvDesc = view.findViewById(R.id.tvDesc);
        loc_text = view.findViewById(R.id.loc_text);
        price = view.findViewById(R.id.price);
        price_type = view.findViewById(R.id.price_type);
        re_view = view.findViewById(R.id.re_view);
        menu_op = view.findViewById(R.id.menu_op);
        re_view.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        try {

            Bundle bundle = getArguments();
            if (bundle!=null){
                data = bundle.getString("data");
                from = bundle.getString("from");
                id = bundle.getString("id");
                dc = new Gson().fromJson(data,SkillPost.class);
            }

            tvTitle.setText(dc.getTitle());
            tvDesc.setText(dc.getDescription());
            if (dc.getLocation().getAddressString()!=null){
                loc_text.setText(dc.getLocation().getAddressString());
            }
            if (dc.getType()!=null){
                if (dc.getType().equalsIgnoreCase("question")){
                    linHide.setVisibility(View.GONE);
                }else {
                    linHide.setVisibility(View.VISIBLE);
                }
            }

            if (dc.getSuspension()!=null){
                isSuspended = dc.getSuspension();
            }else {
                isSuspended = "";
            }

            Log.d("isSuspended",isSuspended);



            if (dc.getHashtags()!=null){
                Log.d("hshtagsize",dc.getHashtags().size()+"");
                if (dc.getHashtags().size()>0){
                    re_view.setAdapter(new RecyclerViewAdapter(getActivity(),dc.getHashtags()));
                }
            }

            if (dc.getType().equalsIgnoreCase("question")){
                view.findViewById(R.id.linPrice).setVisibility(View.GONE);
            }else {
                view.findViewById(R.id.linPrice).setVisibility(View.VISIBLE);
            }

            if (dc.getPrice()==null){
                price.setText("");
                price.setVisibility(View.GONE);
            }
            else {
                if (dc.getPrice().getType().equalsIgnoreCase("fixed")){
                    price.setVisibility(View.VISIBLE);
                    price.setText("$"+dc.getPrice().getAmount());
                }else if (dc.getPrice().getType().equalsIgnoreCase("hourly")){
                    price.setVisibility(View.VISIBLE);
                    price.setText("$"+dc.getPrice().getMinimumAmount()+" - "+dc.getPrice().getMaximumAmount());
                }else {
                    price.setText("");
                    price.setVisibility(View.GONE);
                }
            }

            if (dc.getPrice()==null){
                price_type.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                price_type.setTextSize(15);
                price_type.setTextColor(ContextCompat.getColor(getActivity(),R.color.black));
                price_type.setText("Free");
            }
            else {
                if (dc.getPrice().getType().equalsIgnoreCase("none")){
                    price_type.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                    price_type.setTextSize(15);
                    price_type.setTextColor(ContextCompat.getColor(getActivity(),R.color.black));
                    price_type.setText("Free");
                }else {
                    if (dc.getPrice().getType().equalsIgnoreCase("fixed")) {
                        price_type.setText("Fixed Price");
                    }else if (dc.getPrice().getType().equalsIgnoreCase("hourly")){
                        price_type.setText("Hourly Price");
                    }
                }
            }

            if (dc.getImageURLs()!=null){
                if (dc.getImageURLs().size()>0){
                    Glide.with(getActivity()).load(dc.getImageURLs().get(0)).centerCrop().into(contact_image_ID);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        //Show_users(dc.postCreatorID,contact_image_ID);

        loc_text = (TextView) view.findViewById(R.id.loc_text);
        loc_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAEvent("tapAddressButton");
                //getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container,new MapFragment(),null).commit();
            }
        });

        loc_img = (ImageView) view.findViewById(R.id.loc_img);
        loc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAEvent("tapAddressButton");
                //getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container,new MapFragment(),null).commit();

            }
        });

        back=(ImageView)view.findViewById(R.id.img_back);
        btn_request = (Button)view.findViewById(R.id.btn_request);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
        options=(ImageView)view.findViewById(R.id.menu_op);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_infodialog,null);
                TextView tv_Infomsg = (TextView)view.findViewById(R.id.tv_Infomsg);
                TextView btn_ok = (TextView)view.findViewById(R.id.btn_ok);
                builder.setView(view);

                final AlertDialog dialog = builder.create();
                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        btn_request.setVisibility(View.GONE);

        menu_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAEvent("tapMoreButton");
                showOptionDialog();
            }
        });



        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });

        return view;
    }


    public void AddAEvent(String eventname){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(eventname,bundle);
    }


    public void showOptionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_skilldialog,null);
        builder.setView(view);
        TextView tvEdit = view.findViewById(R.id.tvEdit);
        final TextView tvSuspend = view.findViewById(R.id.tvSuspend);
        if (isSuspended.equalsIgnoreCase("Suspended")){
            tvSuspend.setText("Remove Suspension");
        }else {
            tvSuspend.setText("Suspend");
        }
        TextView tvDelete = view.findViewById(R.id.tvDelete);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        final AlertDialog dialog = builder.create();

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AddAEvent("tapEditSkillButton");
                if (from.equalsIgnoreCase("service")){
                    Call();
                }else if (from.equalsIgnoreCase("question")){
                    Call_Skill2();
                }else if (from.equalsIgnoreCase("event")){
                    Call_Event();
                }
            }
        });

        tvSuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AddAEvent("tapSuspendButton");
                if (tvSuspend.getText().toString().equalsIgnoreCase("Suspend")) {
                    show_suspend();
                }else {
                    RemoveSuspension();
                }
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AddAEvent("tapDeleteButton");
                show_deletepopup();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AddAEvent("tapCancel");
            }
        });

        dialog.show();

    }

    public void Call(){
        AddServiceFragment addServiceFragment = new AddServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("what","edit");
        addServiceFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,addServiceFragment).addToBackStack(null).commit();
    }

    public void Call_Event(){
        AddEventsFragment addEventsFragment = new AddEventsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("what","edit");
        addEventsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,addEventsFragment).addToBackStack(null).commit();
    }

    public void Call_Skill2(){
        sp.edit().putString("private_post","no").apply();
        sp.edit().putString("nos","0").apply();
        AddPostFragment.checked = null;
        AddPostFragment.checkedsection = null;
        TagPeopleAdapter.count = 0;
        TagPeople.count = 0;
        TagPeopleAdapter.map.clear();
        QuestionsFragment questionsFragment = new QuestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("what","edit");
        questionsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,questionsFragment).addToBackStack(null).commit();
    }

    public void show_deletepopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_request,null);
        TextView tv_requestcancel = (TextView)view.findViewById(R.id.tv_requestcancel);
        TextView tv_request = (TextView)view.findViewById(R.id.tv_request);
        TextView textdel = (TextView)view.findViewById(R.id.textdel);
        textdel.setText("Are you sure you want to remove\nthe selected items");
        tv_request.setText("Delete");
        tv_request.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.holo_red_dark));
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tv_requestcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        tv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                onDelete(id);
            }
        });
        dialog.show();
    }

    int count = 0;
    String timeLineTick ="";

    public void show_suspend(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_suspend,null);
        TextView tv_suspendcancel = (TextView)view.findViewById(R.id.tv_suspendcancel);
        TextView tv_suspend = (TextView)view.findViewById(R.id.tv_suspend);
        final DatePicker timePicker = view.findViewById(R.id.timePicker);
        final ImageView refresh_img = view.findViewById(R.id.refresh_img);


        refresh_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count == 0){
                    refresh_img.setImageResource(R.drawable.tick_blue_sharp);
                    timeLineTick = "yes";
                    count = 1;
                }else {
                    refresh_img.setImageResource(R.drawable.border_circle);
                    timeLineTick = "";
                    count= 0;

                }
            }
        });

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tv_suspendcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                AddAEvent("tapCancelSuspensionButton");
            }
        });



        tv_suspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                int   day  = timePicker.getDayOfMonth();
                int   month= timePicker.getMonth();
                int   year = timePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formatedDate = sdf.format(calendar.getTime());

                AddAEvent("tapSuspendPostButton");
                Log.d("timePicker",timePicker.getDayOfMonth()+" "+timePicker.getMonth()+" "+timePicker.getYear());
                String date = formatedDate+"T"+new SimpleDateFormat("hh:mm:ss").format(new Date())+"Z";
                if (timeLineTick.equalsIgnoreCase("yes")){
                    UpdateSuspension("Indefinite");
                }else {
                    UpdateSuspension(date);
                }
                Log.d("timePicker",date);
            }
        });
        dialog.show();

    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "HH:mm:ss dd-MM-yyyy";
        String outputPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public void onDelete(final String childId){
        Log.d("skiidddd",id);
        pd.show();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SkillPost");

        mDatabase.child(childId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();

                try {
                    if (dataSnapshot.exists()) {

                        DataSnapshot dd =  dataSnapshot.child(childId);
                        dataSnapshot.getRef().removeValue();
                        Log.d("ddd",dataSnapshot.getValue()+"");
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                pd.dismiss();

            }
        });

        /*
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        mDatabase.child(childId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
            }
        });*/
    }

    public void UpdateSuspension(String date){
        pd.show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SkillPost");
        Map<String,Object> map = new HashMap<>();
        map.put("suspension","Suspended");
        map.put("suspensionDate",date);

        //SkillPost post = new SkillPost(SelectPlaceActivity.location, price, desc, arrayList_result, img_url, isSecret, Localisys_HomeActivity.user_id, suspension, currentDateTimeString, title, "productService");
        mDatabase.child(id).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    isSuspended = "Suspended";
                    timeLineTick = "";
                    Constants.Information_Dialog(getActivity(),"Suspension Added.");
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });
    }


    public void RemoveSuspension(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SkillPost");

        Map<String,Object> map = new HashMap<>();
        map.put("suspension","Not Suspended");

        //SkillPost post = new SkillPost(SelectPlaceActivity.location, price, desc, arrayList_result, img_url, isSecret, Localisys_HomeActivity.user_id, suspension, currentDateTimeString, title, "productService");
        mDatabase.child(id).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    isSuspended = "Not Suspended";
                    timeLineTick = "";
                    Constants.Information_Dialog(getActivity(),"Suspension removed successfully.");
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });

        mDatabase.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();

                try {
                    if (dataSnapshot.exists()) {

                        DataSnapshot dd =  dataSnapshot.child("suspensionDate");
                        dd.getRef().removeValue();
                        Log.d("ddd",dataSnapshot.getValue()+"");
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                pd.dismiss();

            }
        });
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        ArrayList<String> arr;
        Activity context;


        public RecyclerViewAdapter(Activity c, ArrayList<String> arrayList) {
            this.arr = arrayList;
            this.context = c;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recycler_layout, parent, false);
            return new RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, final int position) {

            holder.myTextView.setText(arr.get(position));
            /*holder.myTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((InterestActivity)context).add_add(arr.get(position));
                    arr.remove(position);
                    notifyDataSetChanged();
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return arr.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            Button myTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                myTextView = (Button) itemView.findViewById(R.id.flex_button);
            }

        }
    }


    public void Show_users(String id, final ImageView userImage){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Log.d("User name: ", user.getUsername() + ", email " + user.getEmail());
                //user_name.setText(user.getUsername());
                if (user.getProfileImageURL()!=null){
                    Glide.with(getActivity()).load(user.getProfileImageURL()).placeholder(R.drawable.avtar).centerCrop().into(userImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });

    }

    public void show_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_request,null);
        TextView tv_requestcancel = (TextView)view.findViewById(R.id.tv_requestcancel);
        TextView tv_request = (TextView)view.findViewById(R.id.tv_request);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tv_requestcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

}

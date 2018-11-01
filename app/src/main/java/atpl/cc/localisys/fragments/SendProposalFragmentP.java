package atpl.cc.localisys.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import atpl.cc.localisys.Constants;
import atpl.cc.localisys.Messaging.ChatActivity;
import atpl.cc.localisys.R;
import atpl.cc.localisys.activities.Localisys_HomeActivity;
import atpl.cc.localisys.adapter.MyAdapter;
import atpl.cc.localisys.classes.PostOffer;
import atpl.cc.localisys.classes.Price;
import atpl.cc.localisys.classes.RangeBar;
import atpl.cc.localisys.classes.Timeline;
import atpl.cc.localisys.modal.User;


public class SendProposalFragmentP extends Fragment implements View.OnClickListener {

    LinearLayout lin_propose,lin_offer,hide_btn;
    LinearLayout btn_sendoffer;
    Button btn_cancel;
    TabLayout tabPrice,tabTime;
    ImageView back;
    RecyclerView horizontal_recycler_view_service;
    TextView btn_makeoffer;
    LinearLayout lin_btnoption,lin_message,lin_pro;
    String click = "";
    ImageView img_location;
    ImageView small_profile_img, large_img_profile,hide_arrow;
    TextView name_profile,hide_txt, profile_distance, profile_heading, profile_description, txt_location, profile_price, txt_fixed,
            txt_timeline,txt_timelineDate;
    Button btn_accept,btn_reject,btn_Successfully;
    int tag=0;
    LinearLayout lin_timeline,loc,lin_price;
    TextView txt1,txt2;
    ArrayList<String> hashtags=new ArrayList<>();
    String lat="", lng="";
    LinearLayout linPostDetails,linMakeOffer;
    String distance="",userID="",posttype="", postDescription="", timestamp="",pricetv="", priceType="", timeLine="",postTitle="",imageUrl="",locationAddress="";
    LinearLayout seekbar_layout;
    String priceSelected = "no price",final_price="";
    RangeBar serrangebar;
    String timeline1 = "none";
    View bottomSheetView;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;
    TextView tvTo, tvFrom, tvDatee;
    String which2 = "",user_id="",pricedollr_left="0",pricedollr_right="0";
    int seletedTab = 0;
    Button btn_send;
    ProgressDialog pd;
    String postId = "";
    Price dc;
    Timeline timelineData;
    TextView tvValue;
    ImageView hide_arrow1;
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView tvNOSend;
    TextView tvOfferTimeLine,tvOfferPrice,tvOfferPriceType,tvOfferTimeDate;
    LinearLayout linAcRe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send_proposal_provider, container, false);
        seekbar_layout = (LinearLayout) view.findViewById(R.id.seekbar_layout);
        tvValue = view.findViewById(R.id.tvValue);
        serrangebar = (RangeBar)view.findViewById(R.id.serrangebar);
        hide_arrow1 = view.findViewById(R.id.hide_arrow1);
        tvNOSend = view.findViewById(R.id.tvNOSend);

        tvOfferTimeLine = view.findViewById(R.id.tvOfferTimeLine);
        tvOfferPrice = view.findViewById(R.id.tvOfferPrice);
        tvOfferPriceType = view.findViewById(R.id.tvOfferPriceType);
        tvOfferTimeDate = view.findViewById(R.id.tvOfferTimeDate);
        linAcRe = view.findViewById(R.id.linAcRe);
        btn_Successfully = view.findViewById(R.id.btn_Successfully);


        CheckPaymentMethod();
        Fetch();


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        pd = new ProgressDialog(getActivity(),R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setIndeterminateDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.my_progress_indeterminate,null));

        //
        /* if (dc.getLocation().getLongitude()!=null&&!dc.getLocation().getLongitude().toString().equalsIgnoreCase("")&&dc.getLocation().getLatitude()!=null&&!dc.getLocation().getLatitude().toString().equalsIgnoreCase("")){

                    MapFragment mapFragment = new MapFragment();
                    Bundle args = new Bundle();
                    args.putString("Longitudb",String.valueOf(dc.getLocation().getLongitude()));
                    args.putString("Latitudeb",String.valueOf(dc.getLocation().getLatitude()));
                    args.putString("posttitle",dc.getTitle()+"");
                    args.putString("posttype",dc.getType()+"");
                    mapFragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,mapFragment).addToBackStack(null).commit();
                }*/

        Bundle bundle=getArguments();
        if (bundle!=null){
            distance=bundle.getString("distance");
            userID=bundle.getString("userID");
            postDescription=bundle.getString("postDescription");
            timestamp=bundle.getString("timestamp");
            pricetv=bundle.getString("price");
            priceType=bundle.getString("priceType");
            timeLine=bundle.getString("timeLine");
            postTitle=bundle.getString("postTitle");
            posttype=bundle.getString("posttype");
            imageUrl=bundle.getString("imageUrl");
            locationAddress=bundle.getString("locationAddress");
            hashtags=bundle.getStringArrayList("hashtags");
            lat=bundle.getString("lat");
            lng=bundle.getString("lng");
            postId = bundle.getString("postId");

            Log.d("timeline",timeLine+" "+pricetv +" "+postId);

            try {
                dc = new Gson().fromJson(pricetv,Price.class);
                timelineData = new Gson().fromJson(timeLine,Timeline.class);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        ((Button)view.findViewById(R.id.btn_chat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEventTo("openChat");
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("postCreatorID",userID).putExtra("postId",postId).putExtra("from","provider"));
            }
        });



        linPostDetails = (LinearLayout)view.findViewById(R.id.linPostDetails);
        txt_timelineDate = view.findViewById(R.id.txt_timelineDate);
        linMakeOffer = (LinearLayout)view.findViewById(R.id.linMakeOffer);
        ListView listView = (ListView)view.findViewById(R.id.listView);
        lin_propose = (LinearLayout) view.findViewById(R.id.lin_propose);
        lin_offer = (LinearLayout) view.findViewById(R.id.lin_offer);
        btn_sendoffer = (LinearLayout) view.findViewById(R.id.btn_sendoffer);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        tabPrice = (TabLayout)view.findViewById(R.id.tabPrice);
        tabTime = (TabLayout)view.findViewById(R.id.tabTime);
        hide_btn=(LinearLayout)view.findViewById(R.id.hide_btn);
        hide_arrow=(ImageView)view.findViewById(R.id.hide_arrow);
        hide_txt=(TextView)view.findViewById(R.id.hide_txt);
        lin_timeline=(LinearLayout)view.findViewById(R.id.timeline);
        loc=(LinearLayout)view.findViewById(R.id.loc);
        lin_price=(LinearLayout)view.findViewById(R.id.price);
        txt1=(TextView)view.findViewById(R.id.profile_description);
        txt2=(TextView)view.findViewById(R.id.profile_date);

        initViews();
        initListeners();


        img_location = (ImageView) view.findViewById(R.id.img_location);
        small_profile_img = (ImageView) view.findViewById(R.id.small_profile_img);
        large_img_profile = (ImageView) view.findViewById(R.id.large_img_profile);
        name_profile = (TextView) view.findViewById(R.id.name_profile);
        profile_distance = (TextView) view.findViewById(R.id.profile_distance);
        profile_heading = (TextView) view.findViewById(R.id.profile_heading);
        profile_description = (TextView) view.findViewById(R.id.profile_description);
        txt_location = (TextView) view.findViewById(R.id.txt_location);
        profile_price = (TextView) view.findViewById(R.id.profile_price);
        txt_fixed = (TextView) view.findViewById(R.id.txt_fixed);
        txt_timeline = (TextView) view.findViewById(R.id.txt_timeline);


        btn_makeoffer = (TextView)view.findViewById(R.id.btn_makeoffer);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        lin_btnoption = (LinearLayout)view.findViewById(R.id.lin_btnoption);
        //gone
        //lin_btnoption.setVisibility(View.GONE);
        lin_message = (LinearLayout)view.findViewById(R.id.lin_message);
        lin_pro = (LinearLayout)view.findViewById(R.id.lin_pro);
        btn_accept = (Button)view.findViewById(R.id.btn_accept);
        btn_reject = (Button)view.findViewById(R.id.btn_reject);
        //btn_contract = (Button)view.findViewById(R.id.btn_contract);

        horizontal_recycler_view_service= (RecyclerView)view.findViewById(R.id.home_recycler);
        try {
            if(hashtags!=null){
                if (hashtags.size()>0){
                    horizontal_recycler_view_service.setAdapter(new RecyclerViewAdapter(getActivity(),hashtags));
                    //horizontal_recycler_view_service.setAdapter(new HorizontalHomeAdapter(getActivity(),SendProposalFragmentP.this,hashtags));
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        horizontal_recycler_view_service.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontal_recycler_view_service.setLayoutManager(llm);

        back=(ImageView)view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        hide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEventTo("toggleConnectTopView");
                tag=tag;
                if (tag==0){
                    hide_txt.setText("More");
                    hide_arrow.setImageResource(R.drawable.down_arrow_blue);
                    lin_timeline.setVisibility(View.GONE);
                    loc.setVisibility(View.GONE);
                    lin_price.setVisibility(View.GONE);
                    txt1.setVisibility(View.GONE);
                    txt2.setVisibility(View.GONE);
                    linPostDetails.setVisibility(View.GONE);
                    tag=1;

                }
                else {
                    hide_txt.setText("Hide");
                    hide_arrow.setImageResource(R.drawable.up_arrow_blue);
                    lin_timeline.setVisibility(View.VISIBLE);
                    loc.setVisibility(View.VISIBLE);
                    lin_price.setVisibility(View.VISIBLE);
                    txt1.setVisibility(View.VISIBLE);
                    txt2.setVisibility(View.VISIBLE);
                    linPostDetails.setVisibility(View.VISIBLE);
                    tag=0;
                }
            }
        });

        listView.setAdapter(new MyAdapter(getActivity(),SendProposalFragmentP.this));
        setListViewHeightBasedOnChildren(listView);
        btn_sendoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lin_propose.setVisibility(View.GONE);
                // lin_offer.setVisibility(View.VISIBLE);
                AddEventTo("tapMakeOffer");
            }
        });
        tabPrice.addTab(tabPrice.newTab().setText("No price"));
        tabPrice.addTab(tabPrice.newTab().setText("Hourly"));
        tabPrice.addTab(tabPrice.newTab().setText("Fixed"));


        tabPrice.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if (tab.getText().toString().equals("No price")) {
                    seekbar_layout.setVisibility(View.GONE);
                    priceSelected = "none";
                    final_price="no Price";

                } else if (tab.getText().toString().equals("Hourly")) {
                    seekbar_layout.setVisibility(View.VISIBLE);
                    priceSelected = "hourly";
                    serrangebar.setRangeBarEnabled(true);
                    serrangebar.setTickStart(1);
                    serrangebar.setTickEnd(200);
                    serrangebar.setTickInterval(1);
                    serrangebar.setSeekPinByValue(200);
                    tvValue.setText("$1 - $200");

                } else if (tab.getText().toString().equals("Fixed")) {
                    seekbar_layout.setVisibility(View.VISIBLE);
                    serrangebar.setRangeBarEnabled(false);
                    serrangebar.setTickStart(1);
                    serrangebar.setTickEnd(10001);
                    serrangebar.setTickInterval(1);
                    serrangebar.setSeekPinByValue(1);
                    priceSelected = "fixed";
                    tvValue.setText("$1");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        tabTime.addTab(tabTime.newTab().setText("No time"));
        tabTime.addTab(tabTime.newTab().setText("Period"));
        tabTime.addTab(tabTime.newTab().setText("Certain"));

        tabTime.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("No time")) {
                    timeline1 = "none";
                    if(!selectedTab.equalsIgnoreCase("none")) {
                        initViews();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        bottomSheetDialog.dismiss();
                        seletedTab = 0;
                    }
                } else if (tab.getText().toString().equals("Period")) {
                    timeline1 = "period";
                    if(!selectedTab.equalsIgnoreCase("period")) {
                        seletedTab = 1;
                        initViews();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        bottomSheetDialog.show();
                    }


                } else if (tab.getText().toString().equals("Certain")) {

                    timeline1 = "certain";
                    if(!selectedTab.equalsIgnoreCase("certain")) {
                        seletedTab = 2;
                        initViews();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        bottomSheetDialog.show();
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        serrangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                pricedollr_left = String.valueOf(leftPinValue);
                pricedollr_right = String.valueOf(rightPinValue);
                if (priceSelected.equalsIgnoreCase("hourly")){
                    if (pricedollr_left.equalsIgnoreCase("0")){
                        pricedollr_left = "1";
                    }
                    tvValue.setText("$"+pricedollr_left+" - $"+pricedollr_right);
                }else if (priceSelected.equalsIgnoreCase("fixed")){
                   /* if (pricedollr_right.equalsIgnoreCase("0")){
                        tvValue.setText("$1");
                    }else {*//**/
                    pricedollr_right = String.valueOf(rightPinIndex);
                    if (pricedollr_right.equalsIgnoreCase("0")){
                        pricedollr_right = "1";
                    }
                    tvValue.setText("$" + pricedollr_right);
                   /* }*/
                }

                Log.d("pricedollr_left",pricedollr_left);
                Log.d("pricedollr_right",pricedollr_right);
            }
        });






        btn_makeoffer.setOnClickListener(this);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAcceptedProvider();
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRejectedProvider();
            }
        });



    /*    btn_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_paymentMethod();
            }
        });*/

        /*String distance="",userID="", postDescription="", timestamp="",pricetv="", priceType="", timeLine="";
        Double lat, lng;*/


        Picasso.with(getActivity()).load(imageUrl).placeholder(R.drawable.profile_dummy)
                .error(R.drawable.profile_dummy).into(large_img_profile);
        if (distance==null){
            profile_distance.setText("0 m");
        }
        else {
            profile_distance.setText(distance+"");
        }

        profile_distance.setSelected(true);

        if (postTitle==null){
            profile_heading.setText("Unknown");
        }
        else {
            profile_heading.setText(postTitle+"");
        }

        if (postDescription==null){
            profile_description.setText("No Description found.");

        }
        else {
            profile_description.setText(postDescription+"");

        }

        if (timestamp==null||timestamp.equalsIgnoreCase("null")){
            txt2.setText("No date/Time");
        }
        else {
            txt2.setText("Posted "+parseDateToddMMyyyy(timestamp.replace("T"," ").replace("Z"," ")));
        }

        if (locationAddress==null||locationAddress.equalsIgnoreCase("null")){

            txt_location.setText("Unknown");
        }
        else {
            txt_location.setText(locationAddress+"");

        }

        /*if (pricetv==null||pricetv.equalsIgnoreCase("null")){
            profile_price.setText("Free");
        }
        else {
            profile_price.setText(pricetv+"");
        }*/


        if (dc ==null){
            profile_price.setText("");
        }
        else {
            if (dc.getType().equalsIgnoreCase("fixed")){
                if (dc.getAmount()!=null) {
                    if (dc.getAmount().toString().equalsIgnoreCase("null")) {
                        profile_price.setText("$" + "0");
                    }else {
                        profile_price.setText("$" + dc.getAmount());
                    }
                }else {
                    profile_price.setText("$" + "0");
                }
            }else if (dc.getType().equalsIgnoreCase("hourly")){
                profile_price.setText("$"+dc.getMinimumAmount()+" - "+dc.getMaximumAmount());
            }else {
                profile_price.setText("");
            }
        }

        if (dc ==null){
            txt_fixed.setText("Free");
        }
        else {
            if (dc.getType().equalsIgnoreCase("none")){
                txt_fixed.setText("Free");
            }else {
                if (dc.getType().equalsIgnoreCase("fixed")){
                    txt_fixed.setText("Fixed Price");
                }else if (dc.getType().equalsIgnoreCase("hourly")){
                    txt_fixed.setText("Hourly Price");
                }
                //price_type.setText(dc.getPrice().getType() + "");
            }

        }

        /*if (priceType==null||priceType.equalsIgnoreCase("null")){
            txt_fixed.setText("Free");
        }
        else {
            txt_fixed.setText(priceType+"");
        }*/

       /* if (timeLine==null||timeLine.equalsIgnoreCase("null")){

            txt_timeline.setText("No time line");
        }
        else {
            if (timeLine.equalsIgnoreCase("timeline")){
                txt_timeline.setText("NONE");
            }else if(timeLine.equalsIgnoreCase("To to FROM")){
                txt_timeline.setText("NONE");
            }else {
                txt_timeline.setText(timeLine + "");
            }

        }*/
       try {
           if (timelineData==null){
               txt_timelineDate.setVisibility(View.GONE);
               txt_timeline.setText("NONE");
           }else {
          /* txt_timelineDate.setVisibility(View.VISIBLE);
           txt_timelineDate.setText(parseDateToddMMyyyy(timelineData.getDate().replace("T"," ").replace("Z"," ")));
           if (timelineData.getType().equalsIgnoreCase("certain")) {
               txt_timeline.setVisibility(View.GONE);
           }else {
               txt_timeline.setVisibility(View.VISIBLE);
               txt_timeline.setText(timelineData.getType() + "");
           }*/

               if (timelineData.getType().equalsIgnoreCase("certain")) {
                   txt_timeline.setVisibility(View.VISIBLE);
                   txt_timelineDate.setVisibility(View.GONE);
                   txt_timeline.setText(parseDateToddMMyyyy(timelineData.getDate().replace("T"," ").replace("Z"," ")));
               }else if (timelineData.getType().equalsIgnoreCase("period")){
                   txt_timeline.setVisibility(View.VISIBLE);
                   txt_timelineDate.setVisibility(View.GONE);
                   txt_timeline.setText(Constants.getDateAgo(timelineData.getStartDate(),timelineData.getEndDate()));
                   Log.d("difference",Constants.getDateAgo(timelineData.getStartDate(),timelineData.getEndDate()));
               }else {
                   txt_timeline.setVisibility(View.VISIBLE);
                   txt_timelineDate.setVisibility(View.GONE);
                   txt_timeline.setText("NONE");
               }

           /*txt_timelineDate.setVisibility(View.VISIBLE);
           txt_timelineDate.setText(parseDateToddMMyyyy(timelineData.getDate().replace("T"," ").replace("Z"," ")));
           txt_timeline.setText(timelineData.getType());*/
           }
       }catch (Exception e){
           e.printStackTrace();
       }


        txt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFragment mapFragment=new MapFragment();
                Bundle bundle1=new Bundle();

                bundle1.putString("Latitudeb",lat);
                bundle1.putString("Longitudb",lng);
                bundle1.putString("posttitle",postTitle);
                bundle1.putString("posttype",posttype);
                mapFragment.setArguments(bundle1);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,mapFragment).addToBackStack(null).commit();
            }
        });

        img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapFragment mapFragment=new MapFragment();
                Bundle bundle1=new Bundle();

                bundle1.putString("Latitudeb",lat);
                bundle1.putString("Longitudb",lng);
                bundle1.putString("posttitle",postTitle);
                bundle1.putString("posttype",posttype);
                mapFragment.setArguments(bundle1);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,mapFragment).addToBackStack(null).commit();
            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");

        mDatabase.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getProfileImageURL()!=null&&!user.getProfileImageURL().equalsIgnoreCase("")) {
                    Picasso.with(getActivity()).load(user.getProfileImageURL()).placeholder(R.drawable.profile_dummy)
                            .error(R.drawable.profile_dummy).into(small_profile_img);
                }


                if (user.getUsername()!=null){
                    name_profile.setText(user.getUsername()+"");
                }else {
                    name_profile.setText("Unknown");
                }


               /* if (user.getFirstName()==null&&user.getLastName()!=null){
                    name_profile.setText(user.getLastName()+"");
                }
                if (user.getLastName()==null&&user.getFirstName()!=null){
                    name_profile.setText(user.getFirstName()+"");
                }
                if (user.getFirstName()!=null&&user.getLastName()!=null){
                    name_profile.setText(user.getFirstName() + " " + user.getLastName());
                }

                if (user.getFirstName()==null&&user.getLastName()==null){
                    name_profile.setText("Unknown");
                }*/

            }

            @Override
            public void onCancelled(DatabaseError error) {


                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddEventTo("sendOffer");

                if (priceSelected.equalsIgnoreCase("no price")) {
                    price = new Price("none");
                } else if (priceSelected.equalsIgnoreCase("fixed")){
                    price = new Price(Integer.parseInt(pricedollr_right), priceSelected);
                }else if (priceSelected.equalsIgnoreCase("hourly")){
                    price = new Price(Integer.parseInt(pricedollr_right), Integer.parseInt(pricedollr_left), priceSelected);
                }

                if(paymentMethod && userMethod){
                    if (post_offer_id.equalsIgnoreCase("")) {
                        SendOffer();
                    }else {
                        UpdateOffer();
                    }
                }else {
                    beforMakingOffer(getActivity());
                }





            }
        });


        getJob();

        return view;
    }


    public void onAcceptedProvider(){

        Map<String,Object> map = new HashMap<>();
        map.put(post_offer_id,true);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Job");
        mDatabase.child(postId).child("Accepted").updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    Log.d("makingOffer","inserted");
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });

        mDatabase.child(postId).child("status").setValue("inProgress", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    Log.d("makingOffer","inserted");
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });
    }

    public void onRejectedProvider(){

        Map<String,Object> map = new HashMap<>();
        map.put(post_offer_id,true);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Job");
        mDatabase.child(postId).child("RejectedByProvider").updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    Log.d("makingOffer","inserted");
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });

        mDatabase.child(postId).child("status").setValue("contractFunded", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    Log.d("makingOffer","inserted");
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });
    }


    String post_offer_id = "";

    public void getJob(){
        pd.show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Job");
        mDatabase.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                if (dataSnapshot.exists()){
                    btn_makeoffer.setText("Edit offer");
                    tvNOSend.setVisibility(View.VISIBLE);
                    lin_message.setVisibility(View.VISIBLE);
                    tvNOSend.setText("Make Offer");

                    Log.d("getJob",dataSnapshot.getValue()+"");
                    DataSnapshot jobStatus = dataSnapshot.child("status");
                    if (jobStatus.exists()){
                        //JobStatus jobStatus = dataSnapshot.getValue(JobStatus.class);
                        if (jobStatus.getValue()!=null) {
                            Log.d("jobStatus", jobStatus.getValue() + "");
                                if (jobStatus.getValue().toString().equalsIgnoreCase("inProgress")) {
                                    tvNOSend.setText("In Progress");
                                    btn_accept.setVisibility(View.GONE);
                                    btn_reject.setVisibility(View.GONE);
                                    btn_Successfully.setVisibility(View.GONE);
                                    btn_sendoffer.setVisibility(View.GONE);
                                } else if (jobStatus.getValue().toString().equalsIgnoreCase("contractFunded")) {
                                    tvNOSend.setText("Contract funded");
                                    btn_accept.setVisibility(View.GONE);
                                    btn_reject.setVisibility(View.GONE);
                                    btn_Successfully.setVisibility(View.GONE);
                                    btn_sendoffer.setVisibility(View.GONE);
                                } else {
                                    DataSnapshot Pending = dataSnapshot.child("Pending");
                                    if (Pending.exists()) {
                                        tvNOSend.setText("Pending");
                                        btn_sendoffer.setVisibility(View.GONE);
                                        linAcRe.setVisibility(View.VISIBLE);
                                    } else {
                                        linAcRe.setVisibility(View.GONE);
                                        btn_sendoffer.setVisibility(View.VISIBLE);
                                    }
                                }
                        }

                    }

                    //Offer
                    DataSnapshot Offer = dataSnapshot.child("Offers");
                    if (Offer.exists()){
                        for (DataSnapshot offerChild : Offer.getChildren()){
                            Log.d("offerChild",offerChild.getKey()+"");
                            getPostOffer(offerChild.getKey());
                            post_offer_id = offerChild.getKey();
                        }
                    }else {
                        post_offer_id = "";
                    }

                    Log.d("getJob",dataSnapshot.getKey()+"");
                    for (DataSnapshot dd : dataSnapshot.getChildren()){
                        Log.d("getJob",dataSnapshot.getKey()+"");
                        Log.d("getJob",dd.getValue()+"");

                    }
                }else {
                    btn_makeoffer.setText("Make offer");
                    lin_message.setVisibility(View.GONE);
                    tvNOSend.setVisibility(View.VISIBLE);
                    tvNOSend.setText("Make Offer");
                    Log.d("getJob","notfound");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Errror","eer");
                pd.dismiss();
            }
        });


    }

    @Override
    public void onDestroyView() {
        if (pd!=null){
            if(pd.isShowing()){
                pd.dismiss();
            }
        }
        super.onDestroyView();
    }

    void getPostOffer(String idd){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostOffer");
        mDatabase.child(idd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Log.d("getPostOffer","found");
                    PostOffer postOffer = dataSnapshot.getValue(PostOffer.class);
                    if (postOffer!=null){



                        try {
                            if (postOffer.getPrice() == null){
                                tvOfferPrice.setText("");
                            }
                            else {
                                if (postOffer.getPrice().getType().equalsIgnoreCase("fixed")){
                                    if (postOffer.getPrice().getAmount()!=null) {
                                        if (postOffer.getPrice().getAmount().toString().equalsIgnoreCase("null")) {
                                            tvOfferPrice.setText("$" + "0");
                                        }else {
                                            tvOfferPrice.setText("$" + postOffer.getPrice().getAmount());
                                        }
                                    }else {
                                        tvOfferPrice.setText("$" + "0");
                                    }
                                }else if (postOffer.getPrice().getType().equalsIgnoreCase("hourly")){
                                    tvOfferPrice.setText("$"+dc.getMinimumAmount()+" - "+postOffer.getPrice().getMaximumAmount());
                                }else {
                                    tvOfferPrice.setText("");
                                }
                            }

                            if (postOffer.getPrice() ==null){
                                tvOfferPriceType.setText("Free");
                            }
                            else {
                                if (postOffer.getPrice().getType().equalsIgnoreCase("none")){
                                    tvOfferPriceType.setText("Free");
                                }else {
                                    if (postOffer.getPrice().getType().equalsIgnoreCase("fixed")){
                                        tvOfferPriceType.setText("Fixed Price");
                                    }else if (postOffer.getPrice().getType().equalsIgnoreCase("hourly")){
                                        tvOfferPriceType.setText("Hourly Price");
                                    }
                                    //price_type.setText(dc.getPrice().getType() + "");
                                }

                            }

                            if (postOffer.getTimeline()==null){
                                tvOfferTimeDate.setVisibility(View.GONE);
                                tvOfferTimeLine.setText("NONE");
                            }else {
                                if (postOffer.getTimeline().getType().equalsIgnoreCase("certain")) {
                                    tvOfferTimeLine.setVisibility(View.VISIBLE);
                                    tvOfferTimeDate.setVisibility(View.GONE);
                                    tvOfferTimeLine.setText(parseDateToddMMyyyy(postOffer.getTimeline().getDate().replace("T"," ").replace("Z"," ")));
                                }else if (postOffer.getTimeline().getType().equalsIgnoreCase("period")){
                                    tvOfferTimeLine.setVisibility(View.VISIBLE);
                                    tvOfferTimeDate.setVisibility(View.GONE);
                                    tvOfferTimeLine.setText(Constants.getDateAgo(postOffer.getTimeline().getStartDate(),postOffer.getTimeline().getEndDate()));
                                    Log.d("difference",Constants.getDateAgo(postOffer.getTimeline().getStartDate(),postOffer.getTimeline().getEndDate()));
                                }else {
                                    tvOfferTimeLine.setVisibility(View.VISIBLE);
                                    tvOfferTimeDate.setVisibility(View.GONE);
                                    tvOfferTimeLine.setText("NONE");
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    Log.d("getPostOffer","notfound");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getPostOffer","eer");
                //pd.dismiss();
            }
        });
    }

    boolean isFound = false;


    ArrayList<PostOffer> plist = new ArrayList<>();
    public void getAllData(){
        plist.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostOffer");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        PostOffer pf = dataSnapshot1.getValue(PostOffer.class);
                        if (pf.getUserId()!=null && pf.getPostId()!=null) {
                            plist.add(pf);
                        }
                    }

                    if (plist.size()>0){
                        Log.d("plistSize",plist.size()+"");
                        for (int i =0;i<plist.size();i++){

                                if (plist.get(i).getUserId().equalsIgnoreCase(Localisys_HomeActivity.user_id) && plist.get(i).getPostId().equalsIgnoreCase(postId)) {

                                    isFound = true;
                                }

                        }
                    }
                }else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Err","err"+databaseError.getMessage());
            }
        });


    }

    boolean paymentMethod,userMethod;


    public void CheckPaymentMethod(){

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PaymentMethod");
            mDatabase.child(Localisys_HomeActivity.user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Log.d("myCode",dataSnapshot.getValue()+"");
                        Log.d("myCode","Found");
                        paymentMethod = true;
                    }else {
                        paymentMethod = false;
                        Log.d("myCode","not Found");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Error","err"+databaseError.getMessage());
                }
            });
    }

    public void Fetch(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");

        mDatabase.child(Localisys_HomeActivity.user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Log.d("myCode1","found");
                    User user = dataSnapshot.getValue(User.class);

                    Log.d("User name: ", user.getUsername() + ", email " + user.getEmail());

                    Log.d("imageURL", user.getProfileImageURL() + "");

                    try {
                        if (user.firstName !=null){
                            if (!user.firstName.equalsIgnoreCase("null") && !user.firstName.equalsIgnoreCase("")){
                                userMethod = true;
                            }else {
                                userMethod = false;
                            }
                        }else {
                            userMethod = false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }else {
                    userMethod = false;
                    Log.d("myCode1","not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });
    }


    public void beforMakingOffer(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_paymentmethod, null);

        TextView tv_recancel = view.findViewById(R.id.tv_pmcancel);
        TextView tv_reset = view.findViewById(R.id.tv_pmsubmit);

        TextView headerTitle=view.findViewById(R.id.headertitle);
        headerTitle.setText("Add your info");
        TextView subTitleheader=view.findViewById(R.id.subTitle);
        subTitleheader.setText("To make an offer you need to fill out your contact info \nand add payment method if you didn't yet.\nWe need this info to make sure that you are real person \nand make sure that you will be paid.");
        TextView textInblue=view.findViewById(R.id.textInblue);
        textInblue.setText("Payment method");
        FrameLayout frameLayout=view.findViewById(R.id.frm);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////////////////////////////
            }
        });

        ImageView tick=view.findViewById(R.id.tick);
        Button btnContactInfo=view.findViewById(R.id.btn_bank_acct);
        btnContactInfo.setText("Contact info");

        btnContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ////////////////////
            }
        });

        tick.setVisibility(View.GONE);

        tv_recancel.setText("Cancel");
        tv_reset.setText("Submit");
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tv_recancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SendOffer();
                //mychanges
                lin_btnoption.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void AddEventTo(String eName){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(eName,bundle);
    }


    public String parseDateToddMMyyyy1(String time) {
        String inputPattern = "hh:mm a dd-MM-yyyy";
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


    public String parseDateToddMMyyyy(String time) {
        //2018-03-03T17:36:12Z
        String inputPattern = "yyyy-MM-dd HH:mm:ss ";
        String outputPattern = "dd MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    Price price;
    Timeline timeline;


    public void SendOffer(){
        pd.show();
        String currentDateTimeString ="";

        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
        //timeline = new Timeline(currentDateTimeString,timeline1);
        String to="",fro="";
        if (timeline1.equalsIgnoreCase("none")){
            timeline = new Timeline(timeline1);
        }else if(timeline1.equalsIgnoreCase("period")){

            if (tvTo.getText().toString().isEmpty()){
                to =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
            }else {
                to = parseDateToddMMyyyy(tvTo.getText().toString());
            }

            if (tvFrom.getText().toString().isEmpty()){
                fro =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
            }else {
                fro = parseDateToddMMyyyy(tvFrom.getText().toString());
            }

            timeline = new Timeline(timeline1,to,fro);

        }else if (timeline1.equalsIgnoreCase("certain")){
            if (which2.equalsIgnoreCase("cer")) {
                if (tvDatee.getText().toString().isEmpty()){
                    currentDateTimeString =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
                    timeline = new Timeline(timeline1,currentDateTimeString);
                }else {
                    currentDateTimeString = parseDateToddMMyyyy(tvDatee.getText().toString());
                    timeline = new Timeline(timeline1,currentDateTimeString);
                }
            }
        }

        Log.d("timelineData",new Gson().toJson(timeline));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostOffer");
        String userId = mDatabase.push().getKey();
        PostOffer post = new PostOffer( price, timeline, postId,  timestamp,Localisys_HomeActivity.user_id);
        //AddService as = new AddService(image,title,desc,hashtag,location,price,"",user_email,"services","",user_name,user_image);
        mDatabase.child(userId).setValue(post, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    if (postId!=null) {
                        AddDataToJob(postId, databaseReference.getKey());
                        AddUserJobRelation(postId,databaseReference.getKey());
                    }
                    Constants.Information_Dialog(getActivity(),"Offer is added.");
                    linMakeOffer.setVisibility(View.GONE);
                    btn_sendoffer.setBackgroundResource(R.drawable.new_blue_b);
                    hide_arrow1.setBackgroundResource(R.drawable.dropdown_arrow_white);
                    btn_makeoffer.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                    btn_makeoffer.setText("Edit offer");
                    isFound = true;
                    click2 = 0;


                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });

    }

    public void AddUserJobRelation(String postId, String post_offer_id){
        Map<String,Object> map = new HashMap<>();
        map.put(Localisys_HomeActivity.user_id,post_offer_id);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("UserJobRelation");
        mDatabase.child(postId).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null){
                    Log.d("UserJobRelation","inserted");
                }else {
                    Log.d("UserJobRelation","no inserted");
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });

    }

    public void UpdateOffer(){
            pd.show();
            String currentDateTimeString ="";

            String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
            //timeline = new Timeline(currentDateTimeString,timeline1);
            String to="",fro="";
            if (timeline1.equalsIgnoreCase("none")){
                timeline = new Timeline(timeline1);
            }else if(timeline1.equalsIgnoreCase("period")){

                if (tvTo.getText().toString().isEmpty()){
                    to =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
                }else {
                    to = parseDateToddMMyyyy(tvTo.getText().toString());
                }

                if (tvFrom.getText().toString().isEmpty()){
                    fro =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
                }else {
                    fro = parseDateToddMMyyyy(tvFrom.getText().toString());
                }

                timeline = new Timeline(timeline1,to,fro);

            }else if (timeline1.equalsIgnoreCase("certain")){
                if (which2.equalsIgnoreCase("cer")) {
                    if (tvDatee.getText().toString().isEmpty()){
                        currentDateTimeString =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
                        timeline = new Timeline(timeline1,currentDateTimeString);
                    }else {
                        currentDateTimeString = parseDateToddMMyyyy(tvDatee.getText().toString());
                        timeline = new Timeline(timeline1,currentDateTimeString);
                    }
                }
            }

            Log.d("timelineData",new Gson().toJson(timeline));

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostOffer");

            Map<String,Object> map = new HashMap<>();
            map.put("price",price);
            map.put("timeline",timeline);
            map.put("postId",postId);
            map.put("timestamp",timestamp);
            map.put("userId",Localisys_HomeActivity.user_id);

            //AddService as = new AddService(image,title,desc,hashtag,location,price,"",user_email,"services","",user_name,user_image);
            mDatabase.child(post_offer_id).updateChildren(map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    pd.dismiss();
                    if (databaseError == null){
                        if (postId!=null) {

                        }
                        Constants.Information_Dialog(getActivity(),"Offer is updated.fff");
                        linMakeOffer.setVisibility(View.GONE);
                        btn_sendoffer.setBackgroundResource(R.drawable.new_blue_b);
                        hide_arrow1.setBackgroundResource(R.drawable.dropdown_arrow_white);
                        btn_makeoffer.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                        btn_makeoffer.setText("Edit offer");
                        isFound = true;
                        click2 = 0;


                    }else {
                        Constants.Information_Dialog(getActivity(),"error in server.");
                    }
                }
            });


    }


    public void AddDataToJob(String postId, String post_offer_id){
        Map<String,Object> map = new HashMap<>();
        map.put(post_offer_id,true);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Job");
        mDatabase.child(postId).child("Offers").updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null){
                    Log.d("makingOffer","inserted");
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });

        mDatabase.child(postId).child("status").setValue("makingOffer", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null){
                    Log.d("makingOffer","inserted");
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });


    }


    String selectedTab = "";

    private void initViews() {
        bottomSheetView = getActivity().getLayoutInflater().inflate(R.layout.addbottomservice, null);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        TabLayout evtabLayout = (TabLayout) bottomSheetView.findViewById(R.id.evtabLayout);
        final LinearLayout lin1 = (LinearLayout) bottomSheetView.findViewById(R.id.lin1);
        final LinearLayout lin2 = (LinearLayout) bottomSheetView.findViewById(R.id.lin2);
        tvTo = (TextView) bottomSheetView.findViewById(R.id.tvTo);
        tvFrom = (TextView) bottomSheetView.findViewById(R.id.tvFrom);
        tvDatee = (TextView) bottomSheetView.findViewById(R.id.tvDatee);
        evtabLayout.addTab(evtabLayout.newTab().setText("No Time"));
        evtabLayout.addTab(evtabLayout.newTab().setText("Period"));
        evtabLayout.addTab(evtabLayout.newTab().setText("Certain"));

        evtabLayout.getTabAt(seletedTab).select();
        if (seletedTab==1){
            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.GONE);
        }
        else if (seletedTab==2){
            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);
        }

        evtabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("No Time")) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    bottomSheetDialog.dismiss();
                    seletedTab = 0;
                    selectedTab = "none";
                    tabTime.getTabAt(0).select();
                } else if (tab.getText().toString().equals("Period")) {

                    lin1.setVisibility(View.VISIBLE);
                    lin2.setVisibility(View.GONE);
                    seletedTab = 0; selectedTab = "period";
                    tabTime.getTabAt(1).select();

                } else if (tab.getText().toString().equals("Certain")) {
                    selectedTab = "certain";
                    tabTime.getTabAt(2).select();
                    lin1.setVisibility(View.GONE);
                    lin2.setVisibility(View.VISIBLE);
                    seletedTab = 0;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                which2 = "to";
                datePicker();
            }
        });

        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                which2 = "fr";
                datePicker();
            }
        });

        tvDatee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                which2 = "cer";
                datePicker();
            }
        });

       /* RelativeLayout re1l1 = (RelativeLayout) bottomSheetView.findViewById(R.id.re1l1);
        re1l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });*/

        TextView tvCancel = (TextView)bottomSheetView.findViewById(R.id.tvCancel);
        TextView tvSet = (TextView)bottomSheetView.findViewById(R.id.tvSet);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initViews();
                //
                /*bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheetDialog.dismiss();*/
                // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetDialog.dismiss();
                seletedTab = 0;
                tabTime.getTabAt(0).select();
            }
        });

        tvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initViews();
                if (timeline1.equalsIgnoreCase("certain")){
                    if(tvDatee.getText().toString().isEmpty()){
                        Constants.Information_Dialog(getActivity(),"Please select date");
                    }else {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        bottomSheetDialog.dismiss();
                        seletedTab = 0;
                    }
                }else if(timeline1.equalsIgnoreCase("period")){
                    if (tvTo.getText().toString().isEmpty() || tvFrom.getText().toString().isEmpty()){
                        Constants.Information_Dialog(getActivity(),"Please select start or end date.");
                    }else {

                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        bottomSheetDialog.dismiss();
                        seletedTab = 0;
                    }
                }else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    bottomSheetDialog.dismiss();
                    seletedTab = 0;
                }
            }
        });


    }

    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;
    String date_time = "";

    private void datePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        tiemPicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;
                        if (which2.equalsIgnoreCase("to")) {
                            tvTo.setText(getTime(hourOfDay, minute) + " " + date_time);
                        } else if (which2.equalsIgnoreCase("fr")) {
                            if (tvTo.getText().toString().isEmpty()){
                                Constants.Information_Dialog(getActivity(),"Please select start date first.");
                            }else if (Constants.getDateDiffer(tvTo.getText().toString(),getTime(hourOfDay, minute) + " " + date_time).equalsIgnoreCase("0") || Constants.getDateDiffer(tvTo.getText().toString(),getTime(hourOfDay, minute) + " " + date_time).contains("-")){
                                Constants.Information_Dialog(getActivity(),"End date must be greater than start date.");
                            }else {
                                tvFrom.setText(getTime(hourOfDay, minute) + " " + date_time);
                            }
                        } else {
                            tvDatee.setText(getTime(hourOfDay, minute) + " " + date_time);
                        }
                        //et_show_date_time.setText(hourOfDay + ":" + minute+" "+date_time);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private String getTime(int hr, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        Format formatter;
        formatter = new SimpleDateFormat("hh:mm a");
        return formatter.format(cal.getTime());
    }




    /**
     * method to initialize the listeners
     */
    private void initListeners() {
        // register the listener for button click


        // Capturing the callbacks for bottom sheet
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });


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

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new WorkDoneFragment(), "Work Done");
        adapter.addFragment(new InterruptionFragment(), "Interruption");
        viewPager.setAdapter(adapter);
    }
    int click2 = 0;

    @Override
    public void onClick(View v) {
        if (v == btn_makeoffer){

           /* if (click.equalsIgnoreCase("sub")){

                lin_pro.setVisibility(View.GONE);
            }else {
                lin_message.setVisibility(View.GONE);
                lin_pro.setVisibility(View.VISIBLE);
                show_Payment();
            }*/
           if(paymentMethod && userMethod){
               if (isFound){
                   if (click2 == 0) {
                       AddEventTo("tapMakeOffer");
                       btn_makeoffer.setText("Cancel");
                       btn_sendoffer.setBackgroundResource(R.drawable.edit_style_switch);
                       hide_arrow1.setImageResource(R.drawable.ic_down_blue);
                       btn_makeoffer.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_blue));
                       linMakeOffer.setVisibility(View.VISIBLE);
                       click2 = 1;
                   } else {
                       AddEventTo("cancelMakeOffer");
                       btn_sendoffer.setBackgroundResource(R.drawable.new_blue_b);
                       hide_arrow1.setBackgroundResource(R.drawable.dropdown_arrow_white);
                       btn_makeoffer.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                       btn_makeoffer.setText("Edit offer");
                       linMakeOffer.setVisibility(View.GONE);
                       click2 = 0;
                   }
               }else {
                   if (click2 == 0) {
                       AddEventTo("tapMakeOffer");
                       btn_makeoffer.setText("Cancel");
                       btn_sendoffer.setBackgroundResource(R.drawable.edit_style_switch);
                       hide_arrow1.setImageResource(R.drawable.ic_down_blue);
                       btn_makeoffer.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_blue));
                       linMakeOffer.setVisibility(View.VISIBLE);
                       click2 = 1;
                   } else {
                       AddEventTo("cancelMakeOffer");
                       btn_sendoffer.setBackgroundResource(R.drawable.new_blue_b);
                       hide_arrow1.setBackgroundResource(R.drawable.dropdown_arrow_white);
                       btn_makeoffer.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                       btn_makeoffer.setText("Make offer");
                       linMakeOffer.setVisibility(View.GONE);
                       click2 = 0;
                   }
               }
           }else {
               beforMakingOffer(getActivity());
               //Toast.makeText(getActivity(),"Please add at least  a PayPal or bank account payment method or first name",Toast.LENGTH_SHORT).show();
           }
            Log.d("Click","ccccccc");


        }
        /*if(v==btn_cancel){

        }*/
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void show_Payment(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_payment,null);
        TextView tv_paysubmit =(TextView)view.findViewById(R.id.tv_paysubmit);
        TextView tv_paycancel =(TextView)view.findViewById(R.id.tv_paycancel);
        Button btn_contact_info = (Button) view.findViewById(R.id.btn_contact_info);

        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tv_paycancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        btn_contact_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new EditProfileActivity()).addToBackStack(null).commit();
                alert.cancel();
                AddEventTo("tapAddContactInfo");

            }
        });
        tv_paysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // click = "sub";
                alert.dismiss();
                lin_btnoption.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                AddEventTo("tapAddPaymentInfo");
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("click","ff");
                        lin_message.setVisibility(View.VISIBLE);
                        lin_pro.setVisibility(View.GONE);
                    }
                });
            }
        });
        alert.show();

    }

    public void show_paymentMethod(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_paymentmethod,null);
        TextView tv_paysubmit =(TextView)view.findViewById(R.id.tv_pmsubmit);
        TextView tv_paycancel =(TextView)view.findViewById(R.id.tv_pmcancel);
        TextView btn_bank_acct = (Button) view.findViewById(R.id.btn_bank_acct);

        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tv_paycancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        tv_paysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Constants.showingDialog(getActivity());
            }
        });
        alert.show();
    }
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        ArrayList<String> arr;
        Activity context;


        public RecyclerViewAdapter(Activity c, ArrayList<String> arrayList) {
            this.arr = arrayList;
            this.context = c;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recycler_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.myTextView.setText(arr.get(position));
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
}
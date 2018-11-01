package atpl.cc.localisys.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import atpl.cc.localisys.Constants;
import atpl.cc.localisys.R;
import atpl.cc.localisys.classes.Price;
import atpl.cc.localisys.classes.RangeBar;
import atpl.cc.localisys.classes.Timeline;


public class SendProposalFragmentR extends Fragment implements View.OnClickListener {

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
    Button btn_accept,btn_contract;
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
    TextView tvOfferTimeLine,tvOfferPrice,tvOfferPriceType,tvOfferTimeDate,tvOfferUserName;
    ImageView ivOfferUserImage;
    Button btn_reject,btn_Successfully;
    TextView tvNOSend;
    TextView tvNoData;
    LinearLayout linOfferlayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send_proposal_requester, container, false);


        tvOfferTimeLine = view.findViewById(R.id.tvOfferTimeLine);
        tvNOSend = view.findViewById(R.id.tvNOSend);
        tvOfferPrice = view.findViewById(R.id.tvOfferPrice);
        tvOfferPriceType = view.findViewById(R.id.tvOfferPriceType);
        tvOfferTimeDate = view.findViewById(R.id.tvOfferTimeDate);
        ivOfferUserImage = view.findViewById(R.id.ivOfferUserImage);
        tvOfferUserName = view.findViewById(R.id.tvOfferUserName);
        btn_reject = view.findViewById(R.id.btn_reject);
        btn_Successfully = view.findViewById(R.id.btn_Successfully);
        tvNoData = view.findViewById(R.id.tvNoData);
        linOfferlayout = view.findViewById(R.id.linOfferlayout);






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
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("postCreatorID",userID).putExtra("postId",postId).putExtra("from","requester"));
            }
        });



        linPostDetails = (LinearLayout)view.findViewById(R.id.linPostDetails);
        txt_timelineDate = view.findViewById(R.id.txt_timelineDate);
        linMakeOffer = (LinearLayout)view.findViewById(R.id.linMakeOffer);
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
        //lin_message = (LinearLayout)view.findViewById(R.id.lin_message);
        lin_pro = (LinearLayout)view.findViewById(R.id.lin_pro);
        btn_accept = (Button)view.findViewById(R.id.btn_accept);
        //btn_reject = (Button)view.findViewById(R.id.btn_reject);
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











        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onAcceptedRequester();
               /* btn_reject.setVisibility(View.GONE);
                btn_accept.setVisibility(View.GONE);
                btn_contract.setVisibility(View.VISIBLE);*/
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRejectedRequester();
            }
        });

       /* btn_contract.setOnClickListener(new View.OnClickListener() {
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

        btn_Successfully.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.showingDialog(getActivity());
            }
        });


        getJob();



        return view;
    }

    @Override
    public void onDestroyView() {
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
        super.onDestroyView();
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
                        linOfferlayout.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                        Log.d("getJob",dataSnapshot.getValue()+"");
                        DataSnapshot jobStatusShot = dataSnapshot.child("status");
                        if (jobStatusShot.exists()){
                            Log.d("jobStatus",jobStatusShot.getValue()+"");
                            if (jobStatusShot.getValue()!=null){
                                    Log.d("jobStatus",jobStatusShot.getValue()+"");
                                    if (jobStatusShot.getValue().toString().equalsIgnoreCase("inProgress")){
                                        tvNOSend.setText("In Progress");
                                        btn_accept.setVisibility(View.GONE);
                                        btn_reject.setVisibility(View.GONE);
                                        btn_Successfully.setVisibility(View.VISIBLE);
                                    }else if (jobStatusShot.getValue().toString().equalsIgnoreCase("contractFunded")){
                                        tvNOSend.setText("Contract funded");
                                        btn_accept.setVisibility(View.GONE);
                                        btn_reject.setVisibility(View.GONE);
                                        btn_Successfully.setVisibility(View.GONE);
                                    }else {
                                        DataSnapshot Pending = dataSnapshot.child("Pending");
                                        if (Pending.exists()) {
                                            tvNOSend.setText("Pending");
                                        }
                                    }

                            }
                        }

                        //JobStatus jobStatus = dataSnapshot.getValue(JobStatus.class);


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
                        linOfferlayout.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
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


    void getPostOffer(String idd){
//        pd.show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostOffer");
        mDatabase.child(idd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // pd.dismiss();
                if (dataSnapshot.exists()){
                    Log.d("getPostOffer","found");
                    PostOffer postOffer = dataSnapshot.getValue(PostOffer.class);
                    if (postOffer!=null){



                        try {
                            userID = postOffer.getUserId();
                            Show_users(postOffer.getUserId(),tvOfferUserName,ivOfferUserImage);
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


    public void Show_users(String id, final TextView user_name, final ImageView userImage){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");

        mDatabase.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Log.d("User name: ", user.getUsername() + ", email " + user.getEmail());

                user_name.setText(user.getUsername());
                if (user.getProfileImageURL()!=null&&!user.getProfileImageURL().equalsIgnoreCase("")){
                    Glide.with(getActivity()).load(user.getProfileImageURL()).asBitmap()
                            .centerCrop()
                            .placeholder(R.drawable.profile_dummy)
                            .error(R.drawable.profile_dummy)
                            .into(new BitmapImageViewTarget(userImage){
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),resource);
                                    drawable.setCircular(true);
                                    userImage.setImageDrawable(drawable);
                                }
                            });
                }
                else {
                    userImage.setImageResource(R.drawable.profile_dummy);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });
    }

    public void onAcceptedRequester(){

            Map<String,Object> map = new HashMap<>();
            map.put(post_offer_id,true);

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Job");
            mDatabase.child(postId).child("Pending").updateChildren(map, new DatabaseReference.CompletionListener() {
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

        mDatabase.child(postId).child("status").setValue("makingOffer", new DatabaseReference.CompletionListener() {
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

    public void onRejectedRequester(){

        Map<String,Object> map = new HashMap<>();
        map.put(post_offer_id,true);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Job");
        mDatabase.child(postId).child("RejectedByRequester").updateChildren(map, new DatabaseReference.CompletionListener() {
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




    boolean isFound = false;
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



    @Override
    public void onClick(View v) {
        if (v == btn_makeoffer){

        }

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




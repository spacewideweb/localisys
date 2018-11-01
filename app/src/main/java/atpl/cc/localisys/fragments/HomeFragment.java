package atpl.cc.localisys.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import atpl.cc.localisys.R;
import atpl.cc.localisys.activities.Localisys_HomeActivity;
import atpl.cc.localisys.activities.MapActivity;
import atpl.cc.localisys.activities.SplashActivity;
import atpl.cc.localisys.adapter.RecyclerViewAdapter;
import atpl.cc.localisys.classes.DashboardClass;

public class HomeFragment extends Fragment {

    ImageView location_icon;
    public static ArrayList<DashboardClass> slist = new ArrayList<>();


    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> myValues = new ArrayList<>();
    ProgressDialog pd;
    RecyclerViewAdapter adapter;
    RecyclerView myView;
    TextView nodata;
    public static int myPosition=0;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the exlayout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        pd = new ProgressDialog(getActivity(),R.style.MyTheme);
        pd.setCancelable(false);
        nodata=view.findViewById(R.id.noData);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setIndeterminateDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.my_progress_indeterminate,null));
        //  myValues.add("john");
        //  myValues.add("smith");
        //  myValues.add("james");

        myView =  (RecyclerView)view.findViewById(R.id.recyclerview);
        myView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);

        location_icon = (ImageView) view.findViewById(R.id.location_icon);
        location_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new MapFragment()).addToBackStack(null).commit();
                startActivity(new Intent(getActivity(),MapActivity.class));
            }
        });

        /*adapter = new RecyclerViewAdapter(myValues,getActivity(),HomeFragment.this);
        myView.setAdapter(adapter);*/

        Log.d("userIDDDD",Localisys_HomeActivity.user_id);

        try {
            getMylikes();
            show_wishlist();
            showDetails();
        }catch (Exception e){
            pd.dismiss();
            e.printStackTrace();
        }

        return view;
    }

    public void AddAEvent(String eventname){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(eventname,bundle);
    }
    public void AddAEventParam(String eventname,String type){
        Bundle bundle = new Bundle();
        bundle.putString("post_type",type);
        mFirebaseAnalytics.logEvent(eventname,bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void call_contact(String distance, String userID, String postDescription, String timestamp, String lat, String lng, String price, String priceType, String timeLine, String postTitle, String imageUrl, String locationAddress,String postId,String posttype){
        if (Localisys_HomeActivity.user_id.equalsIgnoreCase(userID)){
            SendProposalFragmentR spFragment1 = new SendProposalFragmentR();
            AddAEventParam("viewFullPost", posttype);
            Bundle args = new Bundle();
            args.putString("distance", distance);
            args.putString("userID", userID);
            args.putString("postDescription", postDescription);
            args.putString("timestamp", timestamp);
            args.putString("lat", lat);
            args.putString("lng", lng);
            args.putString("price", price);
            args.putString("priceType", priceType);
            args.putString("timeLine", timeLine);
            args.putString("postTitle", postTitle);
            args.putString("imageUrl", imageUrl);
            args.putString("locationAddress", locationAddress);
            args.putString("postId", postId);
            args.putString("posttype", posttype);
            //  args.putString("YourKey", );
            spFragment1.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, spFragment1).addToBackStack(null).commit();
        }else {
            SendProposalFragmentP spFragment = new SendProposalFragmentP();
            AddAEventParam("viewFullPost", posttype);
            Bundle args = new Bundle();
            args.putString("distance", distance);
            args.putString("userID", userID);
            args.putString("postDescription", postDescription);
            args.putString("timestamp", timestamp);
            args.putString("lat", lat);
            args.putString("lng", lng);
            args.putString("price", price);
            args.putString("priceType", priceType);
            args.putString("timeLine", timeLine);
            args.putString("postTitle", postTitle);
            args.putString("imageUrl", imageUrl);
            args.putString("locationAddress", locationAddress);
            args.putString("postId", postId);
            args.putString("posttype", posttype);
            //  args.putString("YourKey", );
            spFragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, spFragment).addToBackStack(null).commit();
        }
    }

    public void call_ContactActivity(String id , String skills1,String skills2){
        ContactActivity contactActivity = new ContactActivity();
        Bundle args = new Bundle();
        args.putString("userIDkey",id);
        args.putString("skill1",skills1);
        args.putString("skill2",skills2);

        contactActivity.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,contactActivity).addToBackStack(null).commit();
    }

    public void call(String data,String postId,String postcreatorID,String postType){

        AddAEventParam("viewFullPostMoreButton",postType);
        Bundle bundle = new Bundle();
        bundle.putString("data",data);
        bundle.putString("postId",postId);
        bundle.putString("postCreatorID",postcreatorID);
        bundle.putString("postType",postType);
        ViewPostFragment vp = new ViewPostFragment();
        vp.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,vp).addToBackStack(null).commit();
        AddCountToFire(postId);
    }


    public void AddCountToFire(final String postId){
        /*PostViewCountRelation*/
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostViewCountRelation");
        mDatabase.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.d("Postssss","Found");
                    Log.d("Postssss",dataSnapshot.getValue()+"");
                    if (dataSnapshot.getValue()!=null){
                        try {
                            AddToPostCount(dataSnapshot.getKey(),Integer.parseInt(dataSnapshot.getValue().toString()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    Log.d("Postssss","notFound");
                    AddToPostCountNew(postId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void AddToPostCountNew(String postId){
        try {
            Map<String,Object> map = new HashMap<>();
            map.put(postId,1);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostViewCountRelation");
            mDatabase.updateChildren(map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Log.d("Postssss","2nd insert");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void AddToPostCount(String postId,int value){
        try {
            Map<String,Object> map = new HashMap<>();
            map.put(postId,value+1);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostViewCountRelation");
            mDatabase.updateChildren(map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Log.d("Postssss","1st insert");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void callToRating(){

        RatingActivity vp = new RatingActivity();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,vp).addToBackStack(null).commit();
    }

    public void callHome(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commitNowAllowingStateLoss();
    }

    public void call_comment(String comment_sender_id,String data,String postId) {
        startActivity(new Intent(getActivity(), CommentActivity.class).putExtra("postId",postId).putExtra("data",data).putExtra("comment_sender_id",comment_sender_id));
       /* CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        commentFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, commentFragment).addToBackStack(null).commit();*/
    }

    public void gotoUserProfile(){

        Localisys_HomeActivity.plus_button.setImageResource(R.drawable.footer_add);
        Localisys_HomeActivity.img_serch.setImageResource(R.drawable.footer_search);
        Localisys_HomeActivity.img_notification.setImageResource(R.drawable.footer_notification);
        Localisys_HomeActivity.img_acount.setImageResource(R.drawable.footer_user_blue);
        Localisys_HomeActivity.img_home.setImageResource(R.drawable.footer_home);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new UserProfileActivity()).commit();

    }

    public void showDetails(){
        slist.clear();
        ids.clear();
        pd.show();
        try {

            SplashActivity.mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pd.dismiss();
                    if(dataSnapshot.exists())
                    {
                        nodata.setVisibility(View.GONE);
                        myView.setVisibility(View.VISIBLE);
                        for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                        {
                            DashboardClass addService  = postSnapShot.getValue(DashboardClass.class);
                            ids.add(postSnapShot.getKey());
                            slist.add(addService);
                            // String  addressString=postSnapShot.child("Location").getValue().toString();
                        }


                        adapter = new RecyclerViewAdapter(slist,ids,postids,getActivity(),HomeFragment.this,likeList);
                        myView.setAdapter(adapter);
                        Log.d("slistsize",slist.size()+"");

                    }else {
                        nodata.setVisibility(View.VISIBLE);
                        myView.setVisibility(View.GONE);
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

    private void fetchData(DataSnapshot dataSnapshot)
    {
        slist.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            String description = ds.getValue(AddService.class).getDescription();

        }

    }

    public void on_Like(String myuserId, String postID, final int pos){
        Log.d("likeOrDislike","like");

        pd.show();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Wishlist");

        Map<String,Object> map = new HashMap<>();
        map.put(postID, true);

        mDatabase.child(myuserId).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    pd.dismiss();
                    Log.d("inserted", "inserted");
                    adapter.notifyDataSetChanged();
                    getMylikes();



                }else {
                    Log.d("errr","errr");
                    pd.dismiss();
                }
            }
        });
    }

    public void on_DisLike(String myuserId,String postID, final int pos){
        Log.d("likeOrDislike","dislike");
        pd.show();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Wishlist");

        Map<String,Object> map = new HashMap<>();
        map.put(postID, false);

        mDatabase.child(myuserId).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    pd.dismiss();
                    Log.d("inserted", "inserted");
                    adapter.notifyDataSetChanged();
                    getMylikes();


                }else {
                    Log.d("errr","errr");
                    pd.dismiss();
                }
            }
        });
    }


    public static class WishClass{

        public String key;
        public String status;
        public String id;

        public WishClass() {
        }

        public WishClass(String key, String status) {
            this.key = key;
            this.status = status;
            this.id=id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    ArrayList<WishClass> postids = new ArrayList<>();
    ArrayList<WishClass> likeList = new ArrayList<>();


    public void getMylikes(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Wishlist");
        mDatabase.child(Localisys_HomeActivity.user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot nottSnapShot : dataSnapshot.getChildren()) {
                            likeList.add(new WishClass(nottSnapShot.getKey(), nottSnapShot.getValue().toString()));
                        }
                        if (likeList.size() > 0) {
                            for(int k=0;k<likeList.size();k++){
                                Log.d("postLikeOrNot",likeList.get(k).getKey());
                            }
                        }

                    } else {
                        Log.d("database", "empty");
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                pd.dismiss();
                Log.w("Failed to read value.", error.toException());
            }
        });
    }

    public void show_wishlist(){
        postids.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Wishlist");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot nottSnapShot : dataSnapshot.getChildren()) {
                            postids.add(new WishClass(nottSnapShot.getKey(), nottSnapShot.getValue().toString()));

                        }
                        /*Log.d("wishlistTag", postids.size() + "");
                        if (postids.size() > 0) {
                            defaultText.setVisibility(View.GONE);
                            expListView.setAdapter(new WishlistActivity.VerticalWishAdapter(getActivity(), postids));
                        } else {
                            defaultText.setVisibility(View.VISIBLE);
                        }*/

                        //adapter = new RecyclerViewAdapter(slist,getActivity(),HomeFragment.this);
                        //myView.setAdapter(adapter);
                        // Log.d("slistsize",slist.size()+"");
                    } else {
                        Log.d("database", "empty");
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }
}

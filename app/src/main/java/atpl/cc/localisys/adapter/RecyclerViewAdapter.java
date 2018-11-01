package atpl.cc.localisys.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.maps.model.LatLng;
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

import atpl.cc.localisys.R;
import atpl.cc.localisys.activities.Localisys_HomeActivity;
import atpl.cc.localisys.classes.DashboardClass;
import atpl.cc.localisys.classes.GpsTracker;
import atpl.cc.localisys.fragments.HomeFragment;
import atpl.cc.localisys.modal.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 29/5/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    public ArrayList<DashboardClass> myValues;
    View listItem;
    Context context;
    Fragment fragment;
    int click = 1;
    SpannableString str;
    String[] filterCat;
    int lineCnt=0;
    public ArrayList<String> ids;

    int dis_value=0;

    boolean[] cliked;

    SharedPreferences sharedPreferences;



    ArrayList<HomeFragment.WishClass> postids;
    ArrayList<HomeFragment.WishClass> likeList;

    int distanceC = 0;

    public RecyclerViewAdapter(ArrayList<DashboardClass> myValues, ArrayList<String> ids, ArrayList<HomeFragment.WishClass> postids, Context context, Fragment fragment,ArrayList<HomeFragment.WishClass> likeList) {
        this.myValues = myValues;
        this.ids = ids;
        this.context = context;
        this.fragment = fragment;
        this.postids=postids;
        this.likeList=likeList;
        cliked = new boolean[myValues.size()];


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        sharedPreferences=context.getSharedPreferences("clickedPost",Context.MODE_PRIVATE);


        return new MyViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        try {
            if (myValues.size()>0) {
                holder.hashtag_text.setText(myValues.get(position).getTitle());
                //holder.hashtag_text.setText(myValues.get(position).getHashtags().get(0));

                String post_key = ids.get(position);
                for (int k = 0; k < likeList.size(); k++) {
                    if (post_key.equalsIgnoreCase(likeList.get(k).getKey())) {
                        if (likeList.get(k).getStatus().equalsIgnoreCase("true")) {
                            Log.d("matchKeysHere", likeList.get(k).getKey() + "=" + post_key);
                            Log.d("checkStatusHere", likeList.get(k).getStatus());
                            holder._fav.setImageResource(R.drawable.star_blue);
                            cliked[position] = true;
                        } else {
                            holder._fav.setImageResource(R.drawable.star_border_blue);
                            cliked[position] = false;
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MMMM dd, yyyy");
        String strDate = "Posted " + mdformat.format(calendar.getTime());

        if (myValues.get(position).getTimestamp()!=null) {
            holder.date_text.setText("Posted "+parseDateToddMMyyyy(myValues.get(position).getTimestamp().replace("T"," ").replace("Z"," ")));
        }

        if (myValues.get(position).isSecret==true){

        }

        GpsTracker gpsTracker=new GpsTracker(context);
        double longitude=gpsTracker.getLongitude();
        double latitude=gpsTracker.getLatitude();

        LatLng latLngA = new LatLng(latitude,longitude);
       try {
           Log.d("latlngTAG","latitudeget:"+myValues.get(position).getLocation().getLatitude()+" \nlongitudeget:"+myValues.get(position).getLocation().getLatitude());

           if (myValues.get(position).getLocation().getLatitude() != null && myValues.get(position).getLocation().getLongitude() != null) {

               LatLng latLngB = new LatLng(myValues.get(position).getLocation().getLatitude(), myValues.get(position).getLocation().getLongitude());
               Location locationA = new Location("point A");
               locationA.setLatitude(latLngA.latitude);
               locationA.setLongitude(latLngA.longitude);
               Location locationB = new Location("point B");
               locationB.setLatitude(latLngB.latitude);
               locationB.setLongitude(latLngB.longitude);
               final double distance = locationA.distanceTo(locationB);
               Double d = new Double(distance);
               dis_value = d.intValue();
               if (dis_value < 1000) {
                   holder.distance.setText(String.valueOf(dis_value) + "m away");
               } else {
                   holder.distance.setText(String.valueOf(dis_value / 1000) + "km away");
               }

               holder.distance.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       ((HomeFragment)fragment).AddAEvent("tapDistanceAwayButton");
                       ((HomeFragment)fragment).AddAEvent("tapDistanceButton");
                       if (distanceC == 0){
                           if (myValues.get(position).getLocation().getAddressString()!=null) {
                               holder.distance.setText(myValues.get(position).getLocation().getAddressString()+"");
                           }
                           distanceC = 1;
                       }else {
                           if (dis_value < 1000) {
                               holder.distance.setText(String.valueOf(dis_value) + "m away");
                           } else {
                               holder.distance.setText(String.valueOf(dis_value / 1000) + "km away");
                           }
                           distanceC = 0;
                       }
                   }
               });

           }
       }
        catch (Exception ex){
            ex.printStackTrace();
        }


        try {

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            Show_users(myValues.get(position).postCreatorID,holder.myTextView,holder.profile_image,myValues.get(position).isSecret);

        }catch (Exception e){
            e.printStackTrace();
        }


        holder._fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeFragment)fragment).AddAEvent("tapWishlistButton");

                if (cliked[position]){
                    ((HomeFragment) fragment).on_DisLike(Localisys_HomeActivity.user_id, ids.get(position),position);
                    cliked[position]=true;
                    holder._fav.setImageResource(R.drawable.star_border_blue);

                }
                else {
                    ((HomeFragment) fragment).on_Like(Localisys_HomeActivity.user_id, ids.get(position),position);
                    cliked[position]=false;
                    holder._fav.setImageResource(R.drawable.star_blue);


                }
                notifyDataSetChanged();
            }
        });



        holder.share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((HomeFragment)fragment).AddAEvent("tapSharePostButton");

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                String shareBody = "Localisys: "+myValues.get(position).getTitle()+"\n"+myValues.get(position).getDescription();
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "You can share via.."));
            }
        });

        if (myValues.get(position).imageURLs!=null){
            if (myValues.get(position).imageURLs.size()>1){
                holder.customtab.setVisibility(View.VISIBLE);
            }else {
                holder.customtab.setVisibility(View.GONE);
            }
        }else {
            holder.customtab.setVisibility(View.GONE);
        }
        holder.mPager.setAdapter(new SlidingImage_Adapter(listItem.getContext(), myValues.get(position).imageURLs));
        holder.customtab.setupWithViewPager(holder.mPager,true);
        holder.contactID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeFragment)fragment).AddAEvent("connectToPost");
                //context.startActivity(new Intent(context, ChatActivity.class).putExtra("postCreatorID",myValues.get(position).postCreatorID));
                Log.d("FrontUID",myValues.get(position).postCreatorID);
                //sharedPreferences.edit().putString("FrontUID",myValues.get(position).postCreatorID).apply();

                String imgUrl="";
                if (myValues.get(position).getImageURLs().size()>0){
                    imgUrl=myValues.get(position).getImageURLs().get(0);
                }
                ((HomeFragment)fragment).call_contact(holder.distance.getText().toString(),myValues.get(position).getPostCreatorID(),myValues.get(position).getDescription(),myValues.get(position).getTimestamp(),String.valueOf(myValues.get(position).getLocation().getLatitude()),String.valueOf(myValues.get(position).getLocation().getLongitude()),new Gson().toJson(myValues.get(position).getPrice()),new Gson().toJson(myValues.get(position).getPrice()),new Gson().toJson(myValues.get(position).getTimeline()),myValues.get(position).getTitle(),imgUrl,myValues.get(position).getLocation().getAddressString(),ids.get(position),myValues.get(position).getType());
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeFragment)fragment).AddAEvent("viewComments");
                String data = new Gson().toJson(myValues.get(position));
                ((HomeFragment)fragment).call_comment(myValues.get(position).postCreatorID,data,ids.get(position));
            }
        });

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myValues.get(position).getPostCreatorID().equalsIgnoreCase(Localisys_HomeActivity.user_id)){
                    ((HomeFragment)fragment).AddAEvent("viewProfileTab");
                    ((HomeFragment)fragment).gotoUserProfile();
                }
                else {
                    //Toast.makeText(context,ids.get(position)+"",Toast.LENGTH_SHORT).show();
                    ((HomeFragment)fragment).AddAEvent("viewUserProfileHomeFeed");

                    if (myValues.get(position).isSecret==false){
                        try {
                            String skill_sec = "Other.";
                            String skill_frst = "Other.";
                            if (myValues.get(position).getHashtags().size() > 1) {
                                skill_sec = myValues.get(position).getHashtags().get(1);
                                skill_frst = myValues.get(position).getHashtags().get(0);
                            } else if (myValues.get(position).getHashtags().size() == 1) {
                                skill_frst = myValues.get(position).getHashtags().get(0);
                            } else {
                                skill_sec = "Other.";
                                skill_frst = "Other.";
                            }
                            ((HomeFragment) fragment).call_ContactActivity(myValues.get(position).postCreatorID, skill_frst, skill_sec);

                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(context, "This profile is secure.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        holder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myValues.get(position).getPostCreatorID().equalsIgnoreCase(Localisys_HomeActivity.user_id)){
                    ((HomeFragment)fragment).AddAEvent("viewProfileTab");
                    ((HomeFragment)fragment).gotoUserProfile();
                }
                else {
                    //  Toast.makeText(context,ids.get(position)+"",Toast.LENGTH_SHORT).show();
                    ((HomeFragment)fragment).AddAEvent("viewUserProfileHomeFeed");
                    ((HomeFragment)fragment).AddAEvent("viewUserProfile");
                    if (myValues.get(position).isSecret==false){
                        String skill_sec="Other";
                        String skill_frst="Other";
                        if (myValues.get(position).getHashtags()!=null) {
                            if (myValues.get(position).getHashtags().size() > 1) {
                                skill_sec = myValues.get(position).getHashtags().get(1);
                                skill_frst = myValues.get(position).getHashtags().get(0);
                            } else if (myValues.get(position).getHashtags().size() == 1) {
                                skill_frst = myValues.get(position).getHashtags().get(0);
                            } else {
                                skill_sec = "Other.";
                                skill_frst = "Other.";
                            }
                        }
                        ((HomeFragment)fragment).call_ContactActivity(myValues.get(position).postCreatorID,skill_frst,skill_sec);

                    }
                    else {
                        Toast.makeText(context, "This profile is secure.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (myValues.get(position).getType().equalsIgnoreCase("event")){
            holder.root_layout.setBackgroundResource(R.drawable.card_edge_yellow);
            holder.comment.setVisibility(View.VISIBLE);
            holder.contactID.setVisibility(View.VISIBLE);
            holder.refresh_img.setBackgroundResource(R.drawable.hand);
           // holder.comment.setBackgroundResource(R.drawable.button_style_yellow);
        }else if (myValues.get(position).getType().equalsIgnoreCase("productService")){
            holder.root_layout.setBackgroundResource(R.drawable.card_edge2);
            holder.comment.setVisibility(View.VISIBLE);
            holder.contactID.setVisibility(View.VISIBLE);
            holder.refresh_img.setBackgroundResource(R.drawable.tick_black);
            //holder.comment.setBackgroundResource(R.drawable.button_style);
        } else{
            holder.root_layout.setBackgroundResource(R.drawable.card_edge_purple);
            holder.comment.setVisibility(View.VISIBLE);
            holder.contactID.setVisibility(View.GONE);
            holder.refresh_img.setBackgroundResource(R.drawable.ser);
            //holder.comment.setBackgroundResource(R.drawable.button_style_purple);
        }


        Log.d("linenumber",lineCnt+"");
                if (myValues.get(position).description.length()>250){
                    Log.d("linenumber2",lineCnt+"");
                    str = new SpannableString(myValues.get(position).description.substring(0,myValues.get(position).description.length()/2)+" more...");
                    str.setSpan(new ForegroundColorSpan(Color.parseColor("#2196f3")), str.length()-7, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.client_desc.setText(str);
                    holder.client_desc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (click == 1) {
                                str = new SpannableString(myValues.get(position).description+" less...");
                                str.setSpan(new ForegroundColorSpan(Color.parseColor("#2196f3")), str.length()-7, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                //holder.lin_bdetail.setVisibility(View.VISIBLE);
                                //holder.tv_inappro.setVisibility(View.VISIBLE);
                                FrameLayout.LayoutParams  lllp=(FrameLayout.LayoutParams)holder.contactID.getLayoutParams();
                                lllp.gravity=Gravity.CENTER;
                                holder.contactID.setLayoutParams(lllp);
                                holder.comment.setVisibility(View.GONE);
                                click = 0;
                                String data = new Gson().toJson(myValues.get(position));
                                ((HomeFragment)fragment).call(data,ids.get(position),myValues.get(position).getPostCreatorID(),myValues.get(position).getType());
                            }else {
                                if(position==0){
                                    holder.comment.setVisibility(View.GONE);
                                }
                                else{
                                    holder.comment.setVisibility(View.VISIBLE);
                                }
                                //holder.lin_bdetail.setVisibility(View.GONE);
                                //holder.tv_inappro.setVisibility(View.GONE);
                                FrameLayout.LayoutParams  lllp=(FrameLayout.LayoutParams)holder.contactID.getLayoutParams();
                                lllp.gravity=Gravity.LEFT;
                                holder.contactID.setLayoutParams(lllp);
                                str = new SpannableString(myValues.get(position).description+" more...");
                                str.setSpan(new ForegroundColorSpan(Color.parseColor("#2196f3")), str.length()-7, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                click = 1;
                                String data = new Gson().toJson(myValues.get(position));
                                ((HomeFragment)fragment).call(data,ids.get(position),myValues.get(position).getPostCreatorID(),myValues.get(position).getType());

                            }
                        }
                    });

                }else {
                    holder.client_desc.setText(myValues.get(position).description);
                    holder.client_desc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((HomeFragment)fragment).AddAEvent("viewFullPostMoreButton");
                            if (click == 1) {

                                //holder.lin_bdetail.setVisibility(View.VISIBLE);
                                //holder.tv_inappro.setVisibility(View.VISIBLE);
                                FrameLayout.LayoutParams  lllp=(FrameLayout.LayoutParams)holder.contactID.getLayoutParams();
                                lllp.gravity=Gravity.CENTER;
                                holder.contactID.setLayoutParams(lllp);
                                holder.comment.setVisibility(View.GONE);
                                click = 0;
                                String data = new Gson().toJson(myValues.get(position));
                                ((HomeFragment)fragment).call(data,ids.get(position),myValues.get(position).getPostCreatorID(),myValues.get(position).getType());
                            }else {
                                if(position==0){
                                    holder.comment.setVisibility(View.GONE);
                                }
                                else{
                                    holder.comment.setVisibility(View.VISIBLE);
                                }
                                //holder.lin_bdetail.setVisibility(View.GONE);
                                //holder.tv_inappro.setVisibility(View.GONE);
                                FrameLayout.LayoutParams  lllp=(FrameLayout.LayoutParams)holder.contactID.getLayoutParams();
                                lllp.gravity=Gravity.LEFT;
                                holder.contactID.setLayoutParams(lllp);
                                click = 1;
                                String data = new Gson().toJson(myValues.get(position));
                                ((HomeFragment)fragment).call(data,ids.get(position),myValues.get(position).getPostCreatorID(),myValues.get(position).getType());

                            }
                        }
                    });
                }
                // Perform any actions you want based on the line count here.


        holder.tv_inappro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_inprop(context);
            }
        });

    }




    @Override
    public int getItemCount() {
        return myValues.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView myTextView;
        ViewPager mPager;
        Button contactID, comment;
        LinearLayout root_layout,lin_bdetail;
        TabLayout customtab;
        CircleImageView profile_image;
        TextView client_desc,distance;
        ImageView share_icon,_fav,_fav2;

        MaterialFavoriteButton star_rating;
        TextView tv_inappro,hashtag_text,date_text,location_text,price_text;
        ImageView refresh_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView)itemView.findViewById(R.id.cardview);
            mPager = (ViewPager) itemView.findViewById(R.id.pager);
            contactID = (Button) itemView.findViewById(R.id.contactID);
            hashtag_text = (TextView) itemView.findViewById(R.id.hashtag_text);
            date_text = (TextView) itemView.findViewById(R.id.date_text);
            distance = (TextView) itemView.findViewById(R.id.distance);
            distance.setSelected(true);
            profile_image=(CircleImageView)itemView.findViewById(R.id.profile_image);
            customtab = (TabLayout) itemView.findViewById(R.id.customtab);
            comment=(Button)itemView.findViewById(R.id.comment);

           // star_rating = (MaterialFavoriteButton) itemView.findViewById(R.id.star_rating);
            share_icon = (ImageView) itemView.findViewById(R.id.share_icon);
            price_text = (TextView) itemView.findViewById(R.id.price_text);
            location_text = (TextView) itemView.findViewById(R.id.location_text);
            root_layout=(LinearLayout)itemView.findViewById(R.id.ly_root);
            lin_bdetail=(LinearLayout)itemView.findViewById(R.id.lin_bdetail);
            client_desc = (TextView)itemView.findViewById(R.id.client_desc);
            tv_inappro = (TextView)itemView.findViewById(R.id.tv_inappro);
            refresh_img = (ImageView)itemView.findViewById(R.id.refresh_img);
            _fav = (ImageView)itemView.findViewById(R.id._fav);
            _fav2 = (ImageView)itemView.findViewById(R.id._fav2);
        }
    }


    public void show_inprop(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_inprop,null);
        TextView reportsubmit = (TextView)view.findViewById(R.id.reportsubmit);
        Spinner reason_options = (Spinner) view.findViewById(R.id.reason_options);
        TextView reportcancel = (TextView)view.findViewById(R.id.reportcancel);
        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        filterCat = context.getResources().getStringArray(R.array.report_reason);

        atpl.cc.localisys.activities.adapter.MyReportAdapter categories_filter_adapter=new atpl.cc.localisys.activities.adapter.MyReportAdapter(context,filterCat){

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = null;

// If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(context);
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
// Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }

// Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };

        reason_options.setAdapter(categories_filter_adapter);
        reason_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  TextView nme = (TextView) view.findViewById(R.id.text1);
              //  nme.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            //   nme.setTextColor(getResources().getColor(R.color.btn_black));
              //  nme.setGravity(Gravity.LEFT);
            //    nme.setTypeface(face);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reportcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        reportsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
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

    public void Show_users(String id, final TextView user_name, final ImageView userImage,final boolean isSecret){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Log.d("User name: ", user.getUsername() + ", email " + user.getEmail());
                if (isSecret){
                    user_name.setText("Secret "+user.getUsername());
                }else {
                    user_name.setText(user.getUsername());
                }
                user_name.setText(user.getUsername());
                if (user.getProfileImageURL()!=null&&!user.getProfileImageURL().equalsIgnoreCase("")){
                    Glide.with(context).load(user.getProfileImageURL()).placeholder(R.drawable.profile_dummy).error(R.drawable.profile_dummy).into(userImage);
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

        /*Query queryRef = mDatabase.orderByKey().equalTo(id);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getValue());
                    User user = dataSnapshot.getValue(User.class);
                    user_name.setText(user.email);
                    if (user.profileImageURL!=null) {
                        Glide.with(context).load(user.profileImageURL).into(userImage);
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    public void Show_usersLike(String id, final ImageView userImage){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Wishlist");

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    if (ds.getValue().equals(true)){
                       /* myTrueFalses.add(new MyTrueFalse(ds.getKey(),ds.getValue().toString()));
                        Log.d("valuueee",ds.getValue().toString());
                        Log.d("valuueeek",ds.getKey());*/

                        userImage.setImageResource(R.drawable.star_blue);
                        //userImage.setFavorite(true);
                    }else {
                        userImage.setImageResource(R.drawable.star_border_blue);
                        //userImage.setFavorite(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });

    }
    ArrayList<MyTrueFalse> myTrueFalses=new ArrayList<>();
    public class MyTrueFalse{

        String id;
        String status;

        public MyTrueFalse(String id, String status) {
            this.id = id;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


    public double getdistance(double get_lat,double get_lng){
        GpsTracker gpsTracker=new GpsTracker(context);
        double longitude=gpsTracker.getLongitude();
        double latitude=gpsTracker.getLatitude();

        /////
        double theta = get_lng - longitude;
        double dist = Math.sin(deg2rad(get_lat))
                * Math.sin(deg2rad(latitude))
                + Math.cos(deg2rad(get_lat))
                * Math.cos(deg2rad(latitude))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}

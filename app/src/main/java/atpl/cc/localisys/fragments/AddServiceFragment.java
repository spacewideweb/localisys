package atpl.cc.localisys.fragments;


import android.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import atpl.cc.localisys.R;
import atpl.cc.localisys.Constants;

import atpl.cc.localisys.activities.Localisys_HomeActivity;
import atpl.cc.localisys.activities.SelectPlaceActivity;
import atpl.cc.localisys.adapter.MySkillRecyclerAdapter;
import atpl.cc.localisys.activities.classes.ExifUtil;
import atpl.cc.localisys.activities.classes.Location;
import atpl.cc.localisys.activities.classes.Price;
import atpl.cc.localisys.activities.classes.RangeBar;
import atpl.cc.localisys.activities.classes.SkillPost;
import atpl.cc.localisys.activities.modal.AddService;
import atpl.cc.localisys.adapter.MySkillRecyclerAdapter;
import atpl.cc.localisys.classes.Price;
import atpl.cc.localisys.classes.RangeBar;
import atpl.cc.localisys.classes.SkillPost;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class AddServiceFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    TextView txt_cancel;
    TabLayout tabLayout;
    Button submit;
    LinearLayout seekbar_layout;
    ImageView facebook,instagram,twitter;
    ImageView contact_image_ID;
    File f;
    EditText edit_title,edit_description;
    EditText edit_hashtag;
    String image="",title ="",desc = "",pricedollr_left="1",pricedollr_right="1",final_price="";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int CPERMISSION_REQUEST_CODE = 2;
    ProgressDialog pd;
    ArrayList<String> img_url=new ArrayList<>();

    String image_upload="";
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask;

    StorageReference mStorageRef;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    String which="";
    String user_email = "",user_name="",user_image="";
    SharedPreferences sp;
    RangeBar serrangebar;
    //TextView edit_australia;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 113;
    ArrayList<String> hashtag = new ArrayList<>();

    ArrayList<String> arrayList_result=new ArrayList();
    ArrayList<String> arrayList=new ArrayList<>();
    String twestx = "";
    Map<String,Boolean> map = new HashMap<>();
    MySkillRecyclerAdapter adapter;
    ImageView lock;
    public boolean isSecret=false;
    TextView textView;
    ImageView ivMultiple;
    String priceSelected = "no price",what="";
    TextView tvValue;
    Location location;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =  inflater.inflate(R.layout.fragment_add, container, false);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);



        textView=(TextView)view.findViewById(R.id.text_profile);
        ivMultiple=(ImageView) view.findViewById(R.id.ivMultiple);
        tvValue=(TextView) view.findViewById(R.id.tvValue);

        lock=(ImageView)view.findViewById(R.id.lock);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEToFire("createProductServiceSkillPostSecret");
                if (isSecret==false){
                    lock.setImageResource(R.drawable.ic_lock_on_blue);
                    textView.setText("Invisible in your profile");
                    isSecret=true;
                }
                else {
                    lock.setImageResource(R.drawable.ic_lock_out);
                    textView.setText("Visible in your profile");
                    isSecret=false;
                }

            }
        });

        mStorageRef = storage.getReference();
        txt_cancel = (TextView)view.findViewById(R.id.txt_cancel);
        seekbar_layout =(LinearLayout) view.findViewById(R.id.seekbar_layout);

        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        user_email = sp.getString("u_email","");
        user_name = sp.getString("u_name","");
        user_image = sp.getString("u_image","");


        try {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), this)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }

        pd = new ProgressDialog(getActivity(),R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setIndeterminateDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.my_progress_indeterminate,null));

        facebook=(ImageView)view.findViewById(R.id.img_fb);
        instagram=(ImageView)view.findViewById(R.id.instagram);
        twitter=(ImageView)view.findViewById(R.id.twitter_blue);

        contact_image_ID = (ImageView)view.findViewById(R.id.contact_image_ID);
        edit_title = (EditText)view.findViewById(R.id.edit_title);
        edit_description = (EditText)view.findViewById(R.id.edit_description);
        edit_hashtag = (EditText)view.findViewById(R.id.edit_hashtag);
        ServiceActivity.places = (TextView) view.findViewById(R.id.edit_australia);
        serrangebar = (RangeBar)view.findViewById(R.id.serrangebar);


        facebook.setOnClickListener(this);
        instagram.setOnClickListener(this);
        twitter.setOnClickListener(this);
        ServiceActivity.places.setOnClickListener(this);
        contact_image_ID.setOnClickListener(this);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEToFire("createProductServiceSkillPostCancel");
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
        submit=(Button)view.findViewById(R.id.btn_addservice);
        submit.setOnClickListener(this);

        serrangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                pricedollr_left = String.valueOf(leftPinValue);
                pricedollr_right = String .valueOf(rightPinValue);

                Log.d("pricedollr_left",pricedollr_left);
                Log.d("pricedollr_right",pricedollr_right);
                if (priceSelected.equalsIgnoreCase("hourly")){
                    if (pricedollr_left.equalsIgnoreCase("0")){
                        pricedollr_left = "1";
                    }
                    tvValue.setText("$"+pricedollr_left+" - $"+pricedollr_right);
                }else if (priceSelected.equalsIgnoreCase("fixed")){
                    pricedollr_right = String.valueOf(rightPinIndex);
                    if (pricedollr_right.equalsIgnoreCase("0")){
                        pricedollr_right = "1";
                    }
                    tvValue.setText("$" + pricedollr_right);
                }
            }
        });

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("No price"));
        tabLayout.addTab(tabLayout.newTab().setText("Hourly"));
        tabLayout.addTab(tabLayout.newTab().setText("Fixed"));

       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {


               if (tab.getText().toString().equals("No price")) {
                   seekbar_layout.setVisibility(View.GONE);
                   priceSelected = "no price";
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

        arrayList_result.clear();
        //edit_hashtag.setText("");

        edit_hashtag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (edit_hashtag.getText().toString().isEmpty()) {
                        edit_hashtag.setText("#");
                    }
                }
            }
        });



        edit_hashtag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()>0) {
                    if (!s.toString().contains("#")) {
                        edit_hashtag.setText("#"+s.toString());
                        Selection.setSelection(edit_hashtag.getText(), edit_hashtag.getText().length());
                    } else {

                        String result = s.toString().replaceAll(" ", "\t#");
                        if (!s.toString().equals(result)) {
                            edit_hashtag.setText(result);
                            edit_hashtag.setSelection(result.length());
                            // alert the user
                        }
                    }
                }else {

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

        });

        try {
            Bundle bundle = getArguments();
            if (bundle!=null){
                if (bundle.getString("what")!=null){
                    what = bundle.getString("what");
                    Log.d("what",what);
                    if (what.equalsIgnoreCase("view")){
                        submit.setText("Add");
                    }else {
                        try {

                            if (SkillDetiilsFragment.dc != null) {
                                edit_title.setText(SkillDetiilsFragment.dc.getTitle());
                                edit_description.setText(SkillDetiilsFragment.dc.getDescription());
                                if (SkillDetiilsFragment.dc.getLocation().getAddressString() != null) {
                                    ServiceActivity.places.setText(SkillDetiilsFragment.dc.getLocation().getAddressString());
                                    location = new Location();
                                    location.setAddressString(SkillDetiilsFragment.dc.getLocation().getAddressString());
                                    location.setLatitude(SkillDetiilsFragment.dc.getLocation().getLatitude());
                                    location.setLongitude(SkillDetiilsFragment.dc.getLocation().getLongitude());
                                    location.setSurroundingDistance(SkillDetiilsFragment.dc.getLocation().getSurroundingDistance());
                                } else {
                                    location = new Location();
                                }

                                if (SkillDetiilsFragment.dc.getHashtags() != null) {
                                    Log.d("hshtagsize", SkillDetiilsFragment.dc.getHashtags().size() + "");
                                    if (SkillDetiilsFragment.dc.getHashtags().size() > 0) {
                                        String hash = TextUtils.join("\t#", SkillDetiilsFragment.dc.getHashtags());
                                        edit_hashtag.setText("#" + hash);
                                        Log.d("hash", hash);
                                    }
                                }
/*
                        if (SkillDetiilsFragment.dc.getPrice()==null){
                            price.setText("");
                        }
                        else {
                            if (SkillDetiilsFragment.dc.getPrice().getType().equalsIgnoreCase("fixed")){
                                price.setText("$"+SkillDetiilsFragment.dc.getPrice().getMaximumAmount());
                            }else if (SkillDetiilsFragment.dc.getPrice().getType().equalsIgnoreCase("hourly")){
                                price.setText("$"+SkillDetiilsFragment.dc.getPrice().getMinimumAmount()+" - "+dc.getPrice().getMaximumAmount());
                            }else {
                                price.setText("");
                            }
                        }*/

                       /* if (SkillDetiilsFragment.dc.getPrice()==null){
                            price_type.setText("Free");
                        }
                        else {
                            price_type.setText(SkillDetiilsFragment.dc.getPrice().getType()+"");
                        }*/

                                if (SkillDetiilsFragment.dc.getImageURLs() != null) {
                                    if (SkillDetiilsFragment.dc.getImageURLs().size() > 0) {
                                        image = SkillDetiilsFragment.dc.getImageURLs().get(0);
                                        img_url.add(image);
                                        Glide.with(getActivity()).load(SkillDetiilsFragment.dc.getImageURLs().get(0)).centerCrop().into(contact_image_ID);

                                    }
                                }
                            }

                            submit.setText("Update");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }




        return view;
    }


    public void AddEToFire(String eName){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(eName,bundle);
    }

    public boolean checkHashtag(){
        Log.d("ddddd",edit_hashtag.getText().toString().replace("\t",""));
        Pattern word = Pattern.compile("##");
        return word.matcher(edit_hashtag.getText().toString().replace("\t","")).find();
    }



    public void getHashtag(){
        arrayList_result.clear();
        if (!edit_hashtag.getText().toString().equalsIgnoreCase("")){
            String[] items = edit_hashtag.getText().toString().replace("\t", "").split("#");
            for (String item : items) {
                Log.d("item = " ,item);
                if(!item.equalsIgnoreCase("")) {
                    arrayList_result.add(item);
                }
            }
        }else {
            arrayList_result.add("sunset");
        }
    }


    @Override
    public void onClick(View v) {
        if (v==facebook){
            Constants.facebook(getActivity());
        }
        if (v==instagram){
            Constants.instagram(getActivity());
        }
        if (v==twitter){
            Constants.twitter(getActivity());
        }
        if(v==ServiceActivity.places){
            AddEToFire("createProductServiceSkillPostOpenLocationMap");
            /*final AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();

            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .setFilter(typeFilter)
                        .build(getActivity());
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }*/
            startActivity(new Intent(getActivity(), SelectPlaceActivity.class));
            /*PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }*/

        }
        if(v==submit) {
            AddEToFire("createProductServiceSkillPostAdd");

            title = edit_title.getText().toString();
            desc = edit_description.getText().toString();




            if (priceSelected.equalsIgnoreCase("no price")) {
                price = new Price("none");
            } else if (priceSelected.equalsIgnoreCase("fixed")){
                price = new Price(Integer.parseInt(pricedollr_right), priceSelected);
            }else if (priceSelected.equalsIgnoreCase("hourly")){
                price = new Price(Integer.parseInt(pricedollr_right), Integer.parseInt(pricedollr_left), priceSelected);
            }


                if (what.equalsIgnoreCase("edit")){
                    SelectPlaceActivity.location = location;
                    UpdateService();
                }else {
                    if (image.isEmpty()||title.isEmpty()||desc.isEmpty() ||SelectPlaceActivity.location==null || edit_hashtag.getText().toString().trim().equalsIgnoreCase("") || edit_hashtag.getText().toString().equalsIgnoreCase("#")){
                        Constants.Information_Dialog(getActivity(),"All fields are required.");
                    }else if (checkHashtag()){
                        Constants.Information_Dialog(getActivity(), "Please enter valid Hashtag");
                    }else {
                        getHashtag();
                        Addservice();
                    }
              }

        }
        if (v == contact_image_ID){
            which = "pic";
            onImageClick();
            AddEToFire("createProductServiceSkillPostOpenPhotoSelection");
        }
    }

    String suspension = "";
    Price price;

    public void Addservice(){
        pd.show();
        String currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());

        Log.d("finalprice",final_price);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SkillPost");
        String userId = mDatabase.push().getKey();



        SkillPost post = new SkillPost(SelectPlaceActivity.location, price, desc, arrayList_result, img_url, isSecret, Localisys_HomeActivity.user_id, suspension, currentDateTimeString, title, "productservice");
        //AddService as = new AddService(image,title,desc,hashtag,location,price,"",user_email,"services","",user_name,user_image);
        mDatabase.child(userId).setValue(post, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    AddUserPostRelation(databaseReference.getKey());
                    AddPostLocationRelation(databaseReference.getKey(),SelectPlaceActivity.stateStr);
                    Constants.Information_Dialog(getActivity(),"Service is added.");
                    if (arrayList_result.size()>0){
                        for (int i=0;i<arrayList_result.size();i++){
                            AddPostHashtagRelation(databaseReference.getKey(),arrayList_result.get(i));
                        }
                    }
                    arrayList_result.clear();
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });
    }

    public void AddUserPostRelation(String key){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostUserRelation");

        Map<String,Object> map = new HashMap<>();
        map.put(key,true);

        mDatabase.child(Localisys_HomeActivity.user_id).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null) {

                } else {

                }
            }
        });
    }

    public void AddPostHashtagRelation(String id,String key){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostHashtagRelation");
        Log.d("dhdhdhd",arrayList_result.size()+"");

        Map<String,Object> map = new HashMap<>();
        map.put(id,true);

        mDatabase.child(key).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null) {

                } else {

                }
            }
        });
    }

    public void AddPostLocationRelation(String id,String key){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostLocationRelation");
        Log.d("location",SelectPlaceActivity.stateStr+"");

        Map<String,Object> map = new HashMap<>();
        map.put(id,true);

        mDatabase.child(key).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                } else {

                }
            }
        });
    }

    public void UpdateService(){
        pd.show();
        String currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());

        Log.d("finalprice",final_price);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SkillPost");

        Map<String,Object> map = new HashMap<>();
        map.put("location",SelectPlaceActivity.location);
        map.put("price",price);
        map.put("description",desc);
        map.put("hashtags",arrayList_result);
        map.put("imageURLs",img_url);
        map.put("isSecret",isSecret);
        map.put("postCreatorID",Localisys_HomeActivity.user_id);
        map.put("suspension",suspension);
        map.put("timestamp",currentDateTimeString);
        map.put("title",title);
        map.put("type","productservice");

        //SkillPost post = new SkillPost(SelectPlaceActivity.location, price, desc, arrayList_result, img_url, isSecret, Localisys_HomeActivity.user_id, suspension, currentDateTimeString, title, "productService");
        mDatabase.child(SkillDetiilsFragment.id).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    Constants.Information_Dialog(getActivity(),"Service is successfully updated.");
                    arrayList_result.clear();
                }else {
                    Constants.Information_Dialog(getActivity(),"error in server.");
                }
            }
        });
    }

    public void onImageClick() {

        final CharSequence[] items = {"Open Camera", "Choose from gallery","Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Select Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                switch (item) {
                    case 0:
                        if(checkPermissionForCamera()) {
                            if (checkPermissionForWrite()) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                startActivityForResult(intent, 1);
                            } else {
                                requestPermissionForWrite();
                            }
                        }else {
                            requestPermissionForCamera();
                        }

                        break;
                    case 1:
                        Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
                        //set limit on number of images that can be selected, default is 10
                        intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 3);
                        //intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES,4);
                        startActivityForResult(intent, 2);

                       /* if(checkPermissionForWrite()) {
                            Intent intent1 = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent1.setType("image*//*");
                            startActivityForResult(
                                    Intent.createChooser(intent1, "Select File"), 2);
                        }else {
                            requestPermissionForWrite();
                        }*/
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }


    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermissionForCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)){

            Toast.makeText(getActivity(),"write permission allows us to write data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CPERMISSION_REQUEST_CODE);
        }
    }

    public boolean checkPermissionForWrite() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermissionForWrite(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            Toast.makeText(getActivity(),"write permission allows us to write data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission","Permission granted");
                    onImageClick();
                    //Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {
                    Log.d("Permission","Permission Denied");
                    //Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();
                }
                break;
            case CPERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission","Permission granted");
                    onImageClick();
                    //Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {
                    Log.d("Permission","Permission Denied");
                    //Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();
                }
                break;

        }
    }


    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {


            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    //bitmapOptions.inSampleSize = 8;
                    bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()), 120, 120, true);
                    //bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    Bitmap mbitmap = ExifUtil.rotateBitmap(f.getAbsolutePath(), bitmap);
                    image_upload = f.getAbsolutePath();
                    contact_image_ID.setImageBitmap(mbitmap);
                    if(!image_upload.equalsIgnoreCase("")){
                        upload();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                Log.d("numberOfimg",images.size()+"");
                contact_image_ID.setImageResource(R.drawable.edit_image);
                //contact_image_ID.setImageURI(Uri.parse(images.get(0).path));

                if (!images.isEmpty()){
                    if (images.size()==1){
                        ivMultiple.setVisibility(View.GONE);
                        contact_image_ID.setImageURI(Uri.parse(images.get(0).path));
                        upload_multiple_image(images.get(0).path);
                    }else {
                        ivMultiple.setVisibility(View.VISIBLE);
                        contact_image_ID.setImageURI(Uri.parse(images.get(0).path));
                        for (int i = 0; i < images.size(); i++) {
                            upload_multiple_image(images.get(i).path);
                        }
                    }
                    Toast.makeText(getActivity(), images.size()+" Images are uploaded successfully", Toast.LENGTH_SHORT).show();
                }

                if (!images.isEmpty()){
                    for (int i=0;i<images.size();i++){
                        upload_multiple_image(images.get(i).path);
                    }
                    Toast.makeText(getActivity(), images.size()+" Images are uploaded successfully", Toast.LENGTH_SHORT).show();
                }
/*
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Bitmap mbitmap = ExifUtil.rotateBitmap(picturePath, thumbnail);
                image_upload = picturePath;
                contact_image_ID.setImageBitmap(mbitmap);
                if(!image_upload.equalsIgnoreCase("")){
                    upload();
                }*/
            }
        }

      /*  if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
        }*/

            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    Log.i("Place: " ,place.getAddress()+"");
                    //edit_australia.setText(place.getAddress()+"");
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    // TODO: Handle the error.
                    Log.i("Errr", status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {

                }
            }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(),data);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                Log.d("selectedplaces",stBuilder.toString());
                //edit_australia.setText(place.getAddress() + "");
                //location=new Location();
                //location.setSurroundingDistance(500);
                //location.setLongitude(Double.parseDouble(longitude));
                //location.setLatitude(Double.parseDouble(latitude));
                //location.setAddressString(address);
                //tvPlaceDetails.setText();
            }else if (resultCode == PlacePicker.RESULT_ERROR) {
                /*Status status = PlacePicker.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("PlaceErrr", status.getStatusMessage());*/

            }
        }

        }
    @SuppressWarnings("VisibleForTests")
    public void upload_multiple_image(String imagePath){
        pd.show();

        StorageReference childRef = mStorageRef.child("PostImages/service"+System.currentTimeMillis());


        //uploading the image
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(imagePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = childRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Upload Failed -> " + exception, Toast.LENGTH_SHORT).show();
                Log.d("error", exception.toString());
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                image = downloadUrl.toString();
                img_url.add(image);
                Log.d("downloadUrl",image);
                Log.d("success", "ff");
            }
        });
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            if (which.equalsIgnoreCase("pic")) {
                image_upload = destination.getAbsolutePath();
                Log.d("image_upload", image_upload);
                contact_image_ID.setImageBitmap(thumbnail);
            }
            if(!image_upload.equalsIgnoreCase("")){
                upload();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("VisibleForTests")
    public void upload(){
        pd.show();

        StorageReference childRef = mStorageRef.child("PostImages/services"+System.currentTimeMillis());


        //uploading the image
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(image_upload));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = childRef.putStream(stream);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Upload Failed -> " + exception, Toast.LENGTH_SHORT).show();
                Log.d("error", exception.toString());
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                image = downloadUrl.toString();
                img_url.add(image);
                Log.d("downloadUrl",image);
                Toast.makeText(getActivity(), "Image Uploaded successfully", Toast.LENGTH_SHORT).show();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Log.d("success", "ff");
            }
        });
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

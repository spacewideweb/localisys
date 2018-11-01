package atpl.cc.localisys.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import atpl.cc.localisys.adapter.HorizontalImageAdapter;
import atpl.cc.localisys.adapter.MySkillRecyclerAdapter;
import atpl.cc.localisys.adapter.TagPeopleAdapter;
import atpl.cc.localisys.classes.ExifUtil;
import atpl.cc.localisys.classes.PostClass;
import atpl.cc.localisys.classes.Price;
import atpl.cc.localisys.classes.Timeline;
import atpl.cc.localisys.modal.AddService;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AskQuestionActivity extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private static final int MULTI_CHOICE = 3;

    TextView tv_title,txt_cancel,tv_notime, tv_hours, tv_days;
    Button submit ,btn_tag_people,btn_notime,btn_hours,btn_days;
    ImageView instagram,twitter,facebook;
    ImageView ques_image_ID;
    EditText ques_title,ques_description;
    EditText ques_hashtag;
    Button ques_valid_time;

    String image="",title="",desc="",final_price="",pricedollr_left="",pricedollr_right="",user_email="",valid_time="",user_name="",user_image="";

    ArrayList<String>img_url=new ArrayList<>();
    ProgressDialog pd;
    SharedPreferences sp;
    String which="";
    File f;
    String image_upload="";
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask;
    private int[] IMAGES = {R.drawable.pic,R.drawable.pic_2,R.drawable.pic_3};

    StorageReference mStorageRef;

    HorizontalImageAdapter mAdapter;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private static int RESULT_LOAD_IMAGE = 10;

    ArrayList<String> mArrayData = new ArrayList<>();

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int CPERMISSION_REQUEST_CODE = 2;
    Bitmap mbitmap;
    Button btn_asktag;
    boolean status = false;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 113;
    ArrayList<String> hashtag = new ArrayList<>();
    ArrayList<String> arrayList=new ArrayList<>();

    ArrayList<String> arrayList_result=new ArrayList();
    String twestx = "";
    Map<String,Boolean> map = new HashMap<>();
    MySkillRecyclerAdapter adapter;
    ImageView lock;
    TextView textView;
    boolean isSecret=false;
    boolean isPrivate=false;
    TextView prvtText;

    ImageView ivMultiple;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ask_question,container,false);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        textView=(TextView)view.findViewById(R.id.text_profile);
        prvtText=(TextView)view.findViewById(R.id.prvtText);
        lock=(ImageView)view.findViewById(R.id.lock);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSecret==true){
                    lock.setImageResource(R.drawable.ask_lock);
                    textView.setText("Your profile is visible.");
                    isSecret=false;
                }
                else {
                    lock.setImageResource(R.drawable.ic_lock);
                    textView.setText("No one will see your profile");
                    isSecret=true;
                }

                AddEToFire("tapMakePostSecret");

            }
        });

        mStorageRef = storage.getReference();

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


        facebook=(ImageView)view.findViewById(R.id.img_fb);
        instagram=(ImageView)view.findViewById(R.id.instagram);
        twitter=(ImageView)view.findViewById(R.id.twitter_light);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        btn_asktag = (Button)view.findViewById(R.id.btn_asktag);
        ivMultiple = (ImageView)view.findViewById(R.id.ivMultiple);

        pd = new ProgressDialog(getActivity(),R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setIndeterminateDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.my_progress_indeterminate,null));

        ques_image_ID = (ImageView)view.findViewById(R.id.ques_image_ID);
        ques_title = (EditText)view.findViewById(R.id.ques_title);
        ques_description = (EditText)view.findViewById(R.id.ques_description);
        ques_hashtag = (EditText)view.findViewById(R.id.ques_hashtag);
        ServiceActivity.places = (TextView) view.findViewById(R.id.ques_location);
        ques_valid_time = (Button)view.findViewById(R.id.ques_valid_time);

        facebook.setOnClickListener(this);
        instagram.setOnClickListener(this);
        twitter.setOnClickListener(this);
        ques_valid_time.setOnClickListener(this);
        ServiceActivity.places.setOnClickListener(this);
        ques_image_ID.setOnClickListener(this);

        ServiceActivity.places.setSelected(true);
        ServiceActivity.places.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        ServiceActivity.places.setSingleLine(true);
        ServiceActivity.places.setMarqueeRepeatLimit(1);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("private_post","no").apply();
                AddEToFire("tapCancelCreateQuestion");
            }
        });

        submit = (Button) view.findViewById(R.id.btn_quessubmit);
        submit.setOnClickListener(this);
        btn_asktag.setOnClickListener(this);

        arrayList_result.clear();
        //ques_hashtag.setText("");

        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        String private_post=sp.getString("private_post","");

        if (private_post.equalsIgnoreCase("yes")){
            isPrivate=true;
            //Private Post. 1 user tagged’
            if (sp.getString("nos","0").equalsIgnoreCase("0")){
                prvtText.setText("Private post.");
            }else {
                if (sp.getString("nos","0").equalsIgnoreCase("1")){
                    prvtText.setText("Private Post. " + sp.getString("nos", "") + " user tagged.");
                }else {
                    prvtText.setText("Private Post. " + sp.getString("nos", "") + " users tagged.");
                }
            }

            //private_txt.setText("Private post.");
        }
        else {
            isPrivate=false;
            if (sp.getString("nos","0").equalsIgnoreCase("0")){
                prvtText.setText("Tag people/make post private.");
            }else {
                if (sp.getString("nos","0").equalsIgnoreCase("1")) {
                    prvtText.setText(sp.getString("nos", "") + " user tagged.");
                }else {
                    prvtText.setText(sp.getString("nos", "") + " users tagged.");
                }
            }


        }

      /*  ques_hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashtagPopup();
            }
        });
*/

        ques_hashtag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (ques_hashtag.getText().toString().isEmpty()) {
                        ques_hashtag.setText("#");
                    }
                }
            }
        });

        ques_hashtag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

               /* if(!s.toString().contains("#")){
                    ques_hashtag.setText("#");
                    Selection.setSelection(ques_hashtag.getText(), ques_hashtag.getText().length());
                }else {

                    String result = s.toString().replace(" ", "\t#");
                    if (!s.toString().equals(result)) {
                        ques_hashtag.setText(result);
                        ques_hashtag.setSelection(result.length());
                        // alert the user
                    }
                }*/
                if (s.toString().length()>0) {
                    if (!s.toString().contains("#")) {
                        ques_hashtag.setText("#"+s.toString());
                        Selection.setSelection(ques_hashtag.getText(), ques_hashtag.getText().length());
                    } else {

                        String result = s.toString().replaceAll(" ", "\t#");
                        if (!s.toString().equals(result)) {
                            ques_hashtag.setText(result);
                            ques_hashtag.setSelection(result.length());
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


        return view;
    }

    public void AddEToFire(String eName){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(eName,bundle);
    }


    public void getHashtag(){
        arrayList_result.clear();
        if (!ques_hashtag.getText().toString().equalsIgnoreCase("")){
            String[] items = ques_hashtag.getText().toString().replace("\t", "").split("#");
            for (String item : items) {
                Log.d("item = " ,item);
                if(!item.equalsIgnoreCase("")) {
                    arrayList_result.add(item);
                }
            }
        }else {

        }
    }


    public void showDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.submit_dialog);
        final TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView submit = (TextView) dialog.findViewById(R.id.submit);
        Button choose_provider = (Button) dialog.findViewById(R.id.choose_provider);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AddQues();
            }
        });

        choose_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new InviteFragment(),null).addToBackStack(null).commit();
            }
        });

        dialog.show();
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
            AddEToFire("tapOpenLocationMap");

            startActivity(new Intent(getActivity(), SelectPlaceActivity.class));

            /*PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }*/

        }


        if (v == submit){
            AddEToFire("tapSubmitQuestion");
            title = ques_title.getText().toString();
            desc = ques_description.getText().toString();



            //hashtag = ques_hashtag.getText().toString();
            //location = ques_location.getText().toString();
            Log.d("title",title);
            Log.d("desc",desc);
            Log.d("desc",desc);
            //Log.d("hashtag",hashtag);
            Log.d("location","");
            if (image.isEmpty()||title.isEmpty()||desc.isEmpty()||SelectPlaceActivity.location==null || ques_hashtag.getText().toString().trim().equalsIgnoreCase("") || ques_hashtag.getText().toString().equalsIgnoreCase("#")){
                Constants.Information_Dialog(getActivity(),"All fields are required.");
            }else if (checkHashtag()){
                Constants.Information_Dialog(getActivity(), "Please enter valid Hashtag");
            }else {
                getHashtag();
                showDialog();

            }
        }
        if (v==ques_image_ID){
            which = "pic";
            onImageClick();
            AddEToFire("tapOpenPhotoSelection");
          //  onImageClick();
        }
        if (v==ques_valid_time){
            //show_timeDialog();
            valid_Time();
            AddEToFire("tapOpenValidTime");
        }

        if (v == btn_asktag){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new TagPeople()).addToBackStack(null).commit();
            AddEToFire("tapOpenTagPeople");
        }

    }

    public boolean checkHashtag(){
        Log.d("ddddd",ques_hashtag.getText().toString().replace("\t",""));
        Pattern word = Pattern.compile("##");
        return word.matcher(ques_hashtag.getText().toString().replace("\t","")).find();
    }

    public void valid_Time(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_validtime,null);
        builder.setView(view);
        final TextView tvDay = (TextView)view.findViewById(R.id.tvDay);
        final TextView tvWeek = (TextView)view.findViewById(R.id.tvWeek);
        final TextView tvMonth = (TextView)view.findViewById(R.id.tvMonth);
        final TextView tvYear = (TextView)view.findViewById(R.id.tvYear);
        final TextView tvLifetime = (TextView)view.findViewById(R.id.tvLifetime);
        final AlertDialog alert = builder.create();
        alert.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        tvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                ques_valid_time.setText(tvDay.getText().toString());
                valid_time = tvDay.getText().toString().toLowerCase();
            }
        });
        tvWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                ques_valid_time.setText(tvWeek.getText().toString());
                valid_time = tvWeek.getText().toString().toLowerCase();
            }
        });
        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                ques_valid_time.setText(tvMonth.getText().toString());
                valid_time = tvMonth.getText().toString().toLowerCase();
            }
        });
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                ques_valid_time.setText(tvYear.getText().toString());
                valid_time = tvYear.getText().toString().toLowerCase();
            }
        });
        tvLifetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                ques_valid_time.setText(tvLifetime.getText().toString());
                valid_time = tvLifetime.getText().toString().toLowerCase();
            }
        });

        alert.show();

    }

    public void show_timeDialog(){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog dialog;
        dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Log.d("time",String.format("%02d:%02d", selectedHour, selectedMinute));
                ques_valid_time.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                //valid_time = String.format("%02d:%02d", selectedHour, selectedMinute);

            }
        },hour, minute, true);
        dialog.setTitle("Select Time");
        dialog.show();
    }

    Price price;
    Timeline timeline;

    public void AddQues(){
        pd.show();

        String currentDateTimeString =  String.valueOf(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        String userId = mDatabase.push().getKey();

        price = new Price(0,0,"none");
        timeline = new Timeline("none");



        //PostClass(Location location, String description, ArrayList<String> hashtags, ArrayList<String> imageURLs, Boolean isPrivate, Boolean isSecret, String postCreatorID, String timestamp, String title, String type, String validTime, Price price, Timeline timeline)

        PostClass pc = new PostClass(SelectPlaceActivity.location, desc, arrayList_result, img_url, isPrivate,  isSecret, Localisys_HomeActivity.user_id, currentDateTimeString, title, "question", valid_time , TagPeopleAdapter.map);

        //AddService as = new AddService(desc, arrayList_result, img_url, isSecret, isSecret, Localisys_HomeActivity.user_id, "status", currentDateTimeString, title, "Question", valid_time,"","",SelectPlaceActivity.location,"timeline");
        //AddService as = new AddService(image,title,desc,hashtag,location,"","",user_email,"ques",valid_time,user_name,user_image);
        mDatabase.child(userId).setValue(pc, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null){
                    AddUserPostRelation(databaseReference.getKey());
                    AddPostLocationRelation(databaseReference.getKey(),SelectPlaceActivity.stateStr);
                    mbitmap = null;
                    imageUri = null;
                    Information_Dialog(getActivity(),"Question is added.");
                    if (arrayList_result.size()>0){
                        for (int i=0;i<arrayList_result.size();i++){
                            AddPostHashtagRelation(databaseReference.getKey(),arrayList_result.get(i));
                        }
                    }
                    arrayList_result.clear();
                    ivMultiple.setVisibility(View.GONE);
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


    public void Information_Dialog(Context context,String msg){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_infodialog,null);
        TextView tv_Infomsg = (TextView)view.findViewById(R.id.tv_Infomsg);
        TextView btn_ok = (TextView)view.findViewById(R.id.btn_ok);
        tv_Infomsg.setText(msg);
        builder.setView(view);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new AddPostFragment()).commit();
            }
        });
        dialog.show();
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

    @SuppressWarnings("VisibleForTests")
    public void upload_multiple_image(String imagePath){
        pd.show();

        final StorageReference childRef = mStorageRef.child("PostImages/question"+System.currentTimeMillis());


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
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image = uri.toString();
                        img_url.add(image);
                        Log.d("downloadUrl",image);
                        Log.d("success", "ff");
                    }
                });
            }
        });
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

    Uri imageUri = null;

    @Override
    public void onResume() {
        super.onResume();

        if (mbitmap != null) {
            ques_image_ID.setImageBitmap(mbitmap);
        }
        if (imageUri != null){
            ques_image_ID.setImageURI(imageUri);
        }

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
                    mbitmap = mbitmap;
                    ques_image_ID.setImageBitmap(mbitmap);
                    if(!image_upload.equalsIgnoreCase("")){
                        upload();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                Log.d("numberOfimg",images.size()+"");
                //ques_image_ID.setImageResource(R.drawable.pruple_hint);
                //ques_image_ID.setImageURI(Uri.parse(images.get(0).path));

                if (!images.isEmpty()){
                    if (images.size()==1){
                        ivMultiple.setVisibility(View.GONE);
                        imageUri = Uri.parse(images.get(0).path);
                        ques_image_ID.setImageURI(Uri.parse(images.get(0).path));
                        upload_multiple_image(images.get(0).path);
                    }else {
                        ivMultiple.setVisibility(View.VISIBLE);
                        imageUri = Uri.parse(images.get(0).path);
                        ques_image_ID.setImageURI(Uri.parse(images.get(0).path));
                        for (int i = 0; i < images.size(); i++) {
                            upload_multiple_image(images.get(i).path);
                        }
                    }
                    Toast.makeText(getActivity(), images.size()+" Images are uploaded successfully", Toast.LENGTH_SHORT).show();
                }
/*
                if (!images.isEmpty()){
                    for (int i=0;i<images.size();i++){
                        upload_multiple_image(images.get(i).path);
                    }
                    Toast.makeText(getActivity(), images.size()+" Images are uploaded successfully", Toast.LENGTH_SHORT).show();
                }*/

               /* Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Bitmap mbitmap = ExifUtil.rotateBitmap(picturePath, thumbnail);
                image_upload = picturePath;
                ques_image_ID.setImageBitmap(mbitmap);
                if(!image_upload.equalsIgnoreCase("")){
                    upload();
                }*/
            }
        }


       /* if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }*/

            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    Log.i("Place: " ,place.getAddress()+"");
                    //ques_location.setText(place.getAddress()+"");
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    // TODO: Handle the error.
                    Log.i("Errr", status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {

                }
            }

            /*Parcelable[] parcelableUris = data.getParcelableArrayExtra(ImagePickerActivity.TAG_IMAGE_URI);
            Uri[] uris = new Uri[parcelableUris.length];
            System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
            if (requestCode == MULTI_CHOICE) {
                mArrayData.clear();
                for (Uri uri : uris) {
                    File file = new File(uri.getPath());
                    if (!file.exists()) {
                        return;
                    }
                    mArrayData.add("file:" + file.getAbsolutePath());
                    mAdapter.notifyDataSetChanged();
                }
            }*/

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
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
                Log.d("selectedplaces", stBuilder.toString());
                //ques_location.setText(place.getAddress() + "");
                //location=new Location();
                //location.setAddressString(address);
                //location.setLatitude(Double.parseDouble(latitude));
                //location.setLongitude(Double.parseDouble(longitude));
                //location.setSurroundingDistance(500);
                //tvPlaceDetails.setText();
            } else if (resultCode == PlacePicker.RESULT_ERROR) {
                /*Status status = PlacePicker.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("PlaceErrr", status.getStatusMessage());*/

            }
        }


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
                ques_image_ID.setImageBitmap(thumbnail);
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

        final StorageReference childRef = mStorageRef.child("PostImages/questions"+System.currentTimeMillis());


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
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image = uri.toString();
                        img_url.add(image);
                        Log.d("downloadUrl",image);
                        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Log.d("success", "ff");
                    }
                });
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();


                image_upload = destination.getAbsolutePath();
                Log.d("image_upload",image_upload);
                if(!image_upload.equalsIgnoreCase("")){
                    upload();
                }

                ques_image_ID.setImageBitmap(bm);
              //  status = true;

              /*  if(status == true){
                    showImageDialog();
                }*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void showImageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialog_view = LayoutInflater.from(getActivity()).inflate(R.layout.image_dialog_layout,null);

      //  ViewPager mPager = (ViewPager) dialog_view.findViewById(R.id.pager);
        TabLayout customtab = (TabLayout) dialog_view.findViewById(R.id.customtab);
        RecyclerView image_recycler = (RecyclerView) dialog_view.findViewById(R.id.recycler_view);
        //ImageView cancel_btn = (ImageView) dialog_view.findViewById(R.id.cancel_btn);
       // Button pickMultiImage = (Button) dialog_view.findViewById(R.id.btn_multi_image);



        image_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mAdapter = new HorizontalImageAdapter(getActivity(), mArrayData);
        image_recycler.setAdapter(mAdapter);
        /*pickMultiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickerChooser = new Intent(ImagePickerActivity.ACTION_INTENT);
                startActivityForResult(pickerChooser, MULTI_CHOICE);
            }
        });*/

      /*  image_recycler.setAdapter(new HorizontalImageAdapter(getActivity(),AskQuestionActivity.this));
        image_recycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        image_recycler.setLayoutManager(llm);*/

      //  mPager.setAdapter(new FullScreenImageAdapter(getActivity(), IMAGES));
      //  customtab.setupWithViewPager(mPager,true);

        builder.setView(dialog_view);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
/*
      //  cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
       // dialog.show();*/
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

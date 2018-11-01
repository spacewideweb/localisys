package atpl.cc.localisys.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import atpl.cc.localisys.R;
import atpl.cc.localisys.Constants;
import atpl.cc.localisys.activities.ProfileActivity;
import atpl.cc.localisys.activities.SelectPlaceActivity;
import atpl.cc.localisys.adapter.MySkillRecyclerAdapter;
import atpl.cc.localisys.adapter.TagPeopleAdapter;
import atpl.cc.localisys.classes.ExifUtil;
import atpl.cc.localisys.classes.Location;
import atpl.cc.localisys.classes.PostClass;
import atpl.cc.localisys.classes.Price;
import atpl.cc.localisys.classes.RangeBar;
import atpl.cc.localisys.classes.Timeline;
import atpl.cc.localisys.modal.AddService;
import atpl.cc.localisys.adapter.MySkillRecyclerAdapter;
import atpl.cc.localisys.classes.RangeBar;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ServiceActivity extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    TextView txt_cancel;
    CircleImageView plus_button;
    Button submit, btn_tag_people;
    TabLayout tabLayout, tabLayout2;
    Dialog dialog;
    ImageView facebook, instagram, twitter;
    ImageView servie_image_ID;
    EditText service_title, ser_description;
    EditText ser_hashtag;
    ArrayList<String> hashtaglist = new ArrayList<>();
    String image = "", title = "", desc = "", hashtag = "", priceSelected = "no price",pricedollr_left="1",pricedollr_right="1",final_price="", timeline = "none", user_email = "", user_name = "", user_image = "";
    ProgressDialog pd;
    SharedPreferences sp;
    File f;
    String which = "";
    String image_upload = "";
    int REQUEST_CAMERA = 0, SELECT_FILE = 1,SELECT_FILE_FROM_CAM=3;
    String userChoosenTask;
    public static TextView places;

    String file_name = "";

    StorageReference mStorageRef;
    ArrayList<String> db_images = new ArrayList<>();

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int CPERMISSION_REQUEST_CODE = 2;
    Bitmap mbitmap;
    Button btn_stag;

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 113;
    LinearLayout seekbar_layout;
    View bottomSheetView;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;
    TextView tvTo, tvFrom, tvDatee;
    String which2 = "",user_id="";

    ArrayList<String> arrayList_result=new ArrayList();
    ArrayList<String> arrayList=new ArrayList<>();
    String twestx = "";
    Map<String,Boolean> map = new HashMap<>();
    MySkillRecyclerAdapter adapter;
    TextView textView,private_txt;
    ImageView lock;
    boolean isSecret=false;
    boolean isPrivate=false;
    RangeBar serrangebar;
    ImageView ivMultiple;
    //Location location;
    int seletedTab = 0;
    TextView tvValue;
    boolean idd=false;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_service, container, false);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        lock=(ImageView)view.findViewById(R.id.lock);
        textView=(TextView)view.findViewById(R.id.text_profile);
        private_txt=(TextView)view.findViewById(R.id.private_txt);
        serrangebar=(RangeBar)view.findViewById(R.id.serrangebar);
        ivMultiple = (ImageView)view.findViewById(R.id.ivMultiple);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        tvValue = view.findViewById(R.id.tvValue);
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
                    //pricedollr_left = String.valueOf(leftPinIndex);
                    pricedollr_right = String.valueOf(rightPinIndex);
                   if (pricedollr_right.equalsIgnoreCase("0")){
                       pricedollr_right = "1";
                   }
                   tvValue.setText("$" + pricedollr_right);

               }

            }
        });
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEToFire("tapMakePostSecret");
                if (isSecret==false){
                    textView.setText("No one will see your profile.");
                    lock.setImageResource(R.drawable.service_lock1);
                    isSecret=true;
                }
                else {
                    textView.setText("Your profile is visible.");
                    lock.setImageResource(R.drawable.service_lock);
                    isSecret=false;
                }
            }
        });


        String private_post=sp.getString("private_post","");
        if (private_post.equalsIgnoreCase("yes")){
            isPrivate=true;
            //Private Post. 1 user tagged’
            if (sp.getString("nos","0").equalsIgnoreCase("0")){
                private_txt.setText("Private post.");
            }else {
                if (sp.getString("nos","0").equalsIgnoreCase("1")){
                    private_txt.setText("Private Post. " + sp.getString("nos", "") + " user tagged.");
                }else {
                    private_txt.setText("Private Post. " + sp.getString("nos", "") + " users tagged.");
                }
            }
        }
        else {
            isPrivate=false;
            if (sp.getString("nos","0").equalsIgnoreCase("0")){
                private_txt.setText("Tag people/make post private.");
            }else {
                if (sp.getString("nos","0").equalsIgnoreCase("1")) {
                    private_txt.setText(sp.getString("nos", "") + " user tagged.");
                }else {
                    private_txt.setText(sp.getString("nos", "") + " users tagged.");
                }
            }

        }

        places = (TextView) view.findViewById(R.id.place_autocomplete);
        seekbar_layout = (LinearLayout) view.findViewById(R.id.seekbar_layout);

        try {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), this)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }


        final AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        initViews();
        initListeners();

        places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* try {
                   Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);*/
               /* PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }*/
               AddEToFire("tapOpenLocationMap");

                startActivity(new Intent(getActivity(), SelectPlaceActivity.class));
               /* } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }*/
            }
        });

        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);

        mStorageRef = storage.getReference();

        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        user_email = sp.getString("u_email", "");
        user_name = sp.getString("u_name", "");
        user_image = sp.getString("u_image", "");
        user_id = sp.getString("user_id", "");

        pd = new ProgressDialog(getActivity(), R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setIndeterminateDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.my_progress_indeterminate, null));

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayoutprice);
        tabLayout.addTab(tabLayout.newTab().setText("No price"));
        tabLayout.addTab(tabLayout.newTab().setText("Hourly"));
        tabLayout.addTab(tabLayout.newTab().setText("Fixed"));
        facebook = (ImageView) view.findViewById(R.id.img_fb);
        instagram = (ImageView) view.findViewById(R.id.instagram);
        twitter = (ImageView) view.findViewById(R.id.twitter_light);
        btn_stag = (Button) view.findViewById(R.id.btn_stag);

        tabLayout2 = (TabLayout) view.findViewById(R.id.tabLayouttimeline);
        tabLayout2.addTab(tabLayout2.newTab().setText("No time"));
        tabLayout2.addTab(tabLayout2.newTab().setText("Period"));
        tabLayout2.addTab(tabLayout2.newTab().setText("Certain"));

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

        tabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("No time")) {
                    timeline = "none";
                    if(!selectedTab.equalsIgnoreCase("none")) {
                        initViews();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        bottomSheetDialog.dismiss();
                        seletedTab = 0;
                    }
                } else if (tab.getText().toString().equals("Period")) {
                    timeline = "period";
                    if(!selectedTab.equalsIgnoreCase("period")) {
                        seletedTab = 1;
                        initViews();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        bottomSheetDialog.show();
                    }


                } else if (tab.getText().toString().equals("Certain")) {

                    timeline = "certain";
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



        servie_image_ID = (ImageView) view.findViewById(R.id.servie_image_ID);
        service_title = (EditText) view.findViewById(R.id.service_title);
        ser_description = (EditText) view.findViewById(R.id.ser_description);
        ser_hashtag = (EditText) view.findViewById(R.id.ser_hashtag);

        //Selection.setSelection(ser_hashtag.getText(), ser_hashtag.getText().length());
        ser_hashtag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (ser_hashtag.getText().toString().isEmpty()) {
                        ser_hashtag.setText("#");
                    }
                }
            }
        });
      /*  ser_hashtag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!ser_hashtag.getText().toString().contains("#")){
                    ser_hashtag.setText("#");
                }
                return false;
            }
        });*/

//        ser_hashtag.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.KEYCODE_BACKSLASH){
//                    if (ser_hashtag.getText().toString().length()==0){
//                        ser_hashtag.clearFocus();
//                    }
//                }
//
//                return false;
//            }
//        });

        ser_hashtag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

             /*   if(!s.toString().contains("#")){
                    if (ser_hashtag.getText().toString().length()==1 && !ser_hashtag.getText().toString().equalsIgnoreCase("#")){
                        ser_hashtag.setText("#");
                        if (ser_hashtag.getText().toString().length()==0){
                            Selection.setSelection(ser_hashtag.getText(), ser_hashtag.getText().length());
                        }
                        else {
                            Selection.setSelection(ser_hashtag.getText(), ser_hashtag.getText().length()+1);
                        }

                    }

                    //
                }else {

                    String result = s.toString().replaceAll(" ", "\t#");
                    if (!s.toString().equals(result)) {
                        ser_hashtag.setText(result);
                        ser_hashtag.setSelection(result.length());
                        // alert the user
                    }
                }*/
                if (s.toString().length()>0) {
                        if (!s.toString().contains("#")) {
                            ser_hashtag.setText("#" + s.toString());
                            Selection.setSelection(ser_hashtag.getText(), ser_hashtag.getText().length());
                        } else {
                            String result = s.toString().replaceAll(" ", "\t#");
                            if (!s.toString().equals(result)) {
                                ser_hashtag.setText(result);
                                ser_hashtag.setSelection(result.length());
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



        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        instagram.setOnClickListener(this);
        servie_image_ID.setOnClickListener(this);
        btn_stag.setOnClickListener(this);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEToFire("tapCancelCreateServiceProduct");
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("private_post","no").apply();
            }
        });


        submit = (Button) view.findViewById(R.id.btn_ser_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Addservice();
                AddEToFire("tapSubmitServiceProduct");

                title = service_title.getText().toString();
                desc = ser_description.getText().toString();
                hashtag = ser_hashtag.getText().toString();





                if (priceSelected.equalsIgnoreCase("no price")) {
                    price = new Price("none");
                } else if (priceSelected.equalsIgnoreCase("fixed")){
                    price = new Price(Integer.parseInt(pricedollr_right), priceSelected);
                }else if (priceSelected.equalsIgnoreCase("hourly")){
                    price = new Price(Integer.parseInt(pricedollr_right), Integer.parseInt(pricedollr_left), priceSelected);
                }

                if (image.isEmpty() || title.isEmpty() || desc.isEmpty()||SelectPlaceActivity.location==null || ser_hashtag.getText().toString().trim().equalsIgnoreCase("") || ser_hashtag.getText().toString().equalsIgnoreCase("#")) {

                    Constants.Information_Dialog(getActivity(), "All fields are required.");
                }else if (checkHashtag()){
                    Constants.Information_Dialog(getActivity(), "Please enter valid Hashtag");
                }else {

                        getHashtag();
                        dialog.setContentView(R.layout.submit_dialog);
                        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
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
                                title = service_title.getText().toString();
                                desc = ser_description.getText().toString();
                                hashtag = ser_hashtag.getText().toString();


                                Log.d("timeline", timeline);
                                Log.d("title", title);
                                Log.d("desc", desc);
                                Log.d("desc", desc);
                                Log.d("hashtag", hashtag);
                                Log.d("location", "");

                                if (image.isEmpty() || title.isEmpty() || desc.isEmpty()||SelectPlaceActivity.location==null) {
                                    Constants.Information_Dialog(getActivity(), "All fields are required.");
                                } else {
                                    Addservice();
                                }
                                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new AddPostFragment(),null).addToBackStack(null).commit();
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
            }
        });


        arrayList_result.clear();
        //ser_hashtag.setText("");

        /*ser_hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashtagPopup();
            }
        });*/

        return view;
    }

    String checkedH = "false";

    public boolean checkHashtag(){
        Log.d("ddddd",ser_hashtag.getText().toString().replace("\t",""));
        Pattern word = Pattern.compile("##");
        return word.matcher(ser_hashtag.getText().toString().replace("\t","")).find();
    }




    public void AddEToFire(String eName){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(eName,bundle);
    }

    String selectedTab = "";




    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

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
                    tabLayout2.getTabAt(0).select();
                } else if (tab.getText().toString().equals("Period")) {

                    lin1.setVisibility(View.VISIBLE);
                    lin2.setVisibility(View.GONE);
                    seletedTab = 0;
                    selectedTab = "period";
                    tabLayout2.getTabAt(1).select();
                } else if (tab.getText().toString().equals("Certain")) {
                    selectedTab = "certain";
                    tabLayout2.getTabAt(2).select();
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
                tabLayout2.getTabAt(0).select();
            }
        });

        tvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initViews();
                if (timeline.equalsIgnoreCase("certain")){
                    if(tvDatee.getText().toString().isEmpty()){
                        Constants.Information_Dialog(getActivity(),"Please select date");
                    }else {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        bottomSheetDialog.dismiss();
                        seletedTab = 0;
                    }
                }else if(timeline.equalsIgnoreCase("period")){
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

    @Override
    public void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        try {
            mGoogleApiClient.disconnect();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        super.onStop();
    }


    @SuppressWarnings("VisibleForTests")
    public void upload() {
        pd.show();

        StorageReference childRef = mStorageRef.child("PostImages/service" + System.currentTimeMillis());


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
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                image = downloadUrl.toString();
                db_images.add(image);
                Log.d("downloadUrl", image);
                Toast.makeText(getActivity(), "Image Uploaded successfully", Toast.LENGTH_SHORT).show();
                Log.d("success", "ff");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == facebook) {
            Constants.facebook(getActivity());
        }
        if (v == instagram) {
            Constants.instagram(getActivity());
        }
        if (v == twitter) {
            Constants.twitter(getActivity());
        }
        if (v == servie_image_ID) {
            which = "pic";
            onImageClick();
            AddEToFire("tapOpenPhotoSelection");
        }
        if (v == btn_stag) {
            AddEToFire("tapOpenTagPeople");
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new TagPeople()).addToBackStack(null).commit();
        }
    }
    private void cameraIntent() {
        /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, SELECT_FILE_FROM_CAM);*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, SELECT_FILE_FROM_CAM);
    }
    public void onImageClick() {
        final CharSequence[] items = {"Open Camera", "Choose from gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                switch (item) {
                    case 0:
                        if(checkPermissionForCamera()) {
                            if (checkPermissionForWrite()) {
                                cameraIntent();
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
                        //intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES,3);
                        startActivityForResult(intent, SELECT_FILE);

                        /*if(checkPermissionForWrite()) {
                            Intent intent1 = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent1.setType("image*//*");
                            startActivityForResult(
                                    Intent.createChooser(intent1, "Select File"), SELECT_FILE);
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
        AlertDialog alert = builder.create();
        alert.show();
    }





    public void getHashtag(){
        arrayList_result.clear();
        if (!ser_hashtag.getText().toString().equalsIgnoreCase("#")){
            String[] items = ser_hashtag.getText().toString().replace("\t", "").split("#");
            for (String item : items) {
                Log.d("item = " ,item);
                if(!item.equalsIgnoreCase("")) {
                    arrayList_result.add(item);
                    Log.d("item = ",arrayList_result.size()+"");
                }
            }
        }else {

        }
    }

    String status = "", validTime ="lifetime";
    Price price;
    Timeline timelineData;

    public String parseDateToddMMyyyy(String time) {
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

    public void Addservice() {
        pd.show();

        String currentDateTimeString ="";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());

        String to="", fro= "";

        if (timeline.equalsIgnoreCase("none")){
            timelineData = new Timeline(timeline);
        }else if(timeline.equalsIgnoreCase("period")){

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

            timelineData = new Timeline(timeline,to,fro);

        }else if (timeline.equalsIgnoreCase("certain")){
            if (which2.equalsIgnoreCase("cer")) {
                if (tvDatee.getText().toString().isEmpty()){
                    currentDateTimeString =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
                    timelineData = new Timeline(timeline,currentDateTimeString);
                }else {
                    currentDateTimeString = parseDateToddMMyyyy(tvDatee.getText().toString());
                    timelineData = new Timeline(timeline,currentDateTimeString);
                }
            }
        }

        Log.d("timelineData",new Gson().toJson(timelineData));





        String final_time="";
        if (tvTo!=null&&!tvTo.getText().toString().equalsIgnoreCase("")&&tvFrom!=null&&!tvFrom.getText().toString().equalsIgnoreCase("")){
            final_time=tvTo.getText().toString()+" to "+tvFrom.getText().toString();
        }
        else {
            final_time="No Time";
        }
        if (which2.equalsIgnoreCase("cer")){
            final_time=date_time+"";
        }


        Log.d("final_price",final_price);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        String userId = mDatabase.push().getKey();
        Log.d("dhdhdhd",arrayList_result.size()+"");

        PostClass pc = new PostClass(SelectPlaceActivity.location, desc, arrayList_result, db_images, isPrivate,  isSecret, user_id, timeStamp, title, "productService", price, timelineData,TagPeopleAdapter.map);
//PostUserRelation
        //AddService as = new AddService(desc, arrayList_result, db_images, isSecret, isSecret, user_id, "status", currentDateTimeString, title, "Service","validtime",price,final_price,SelectPlaceActivity.location,final_time);
        // AddService as = new AddService(image, title, desc, hashtag, location, price, timeline, user_email, "services", "", user_name, user_image, db_images);
        mDatabase.child(userId).setValue(pc, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pd.dismiss();
                if (databaseError == null) {
                    mbitmap = null;
                    imageUri = null;
                    AddUserPostRelation(databaseReference.getKey());
                    AddPostLocationRelation(databaseReference.getKey(),SelectPlaceActivity.stateStr);

                    Information_Dialog(getActivity(), "Service is added.");
                    ivMultiple.setVisibility(View.GONE);
                    if (arrayList_result.size()>0){
                        for (int i=0;i<arrayList_result.size();i++){
                            AddPostHashtagRelation(databaseReference.getKey(),arrayList_result.get(i));
                        }
                    }
                    arrayList_result.clear();
                } else {
                    Constants.Information_Dialog(getActivity(), "error in server.");
                }
            }
        });
    }

    public void AddUserPostRelation(String key){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PostUserRelation");

        Map<String,Object> map = new HashMap<>();
        map.put(key,true);

        mDatabase.child(user_id).updateChildren(map, new DatabaseReference.CompletionListener() {
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

    //

   /* public void onImageClick() {

        final CharSequence[] items = {"Open Camera", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                switch (item) {
                    case 0:
                        if (checkPermissionForCamera()) {
                            if (checkPermissionForWrite()) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                Fragment frag = ServiceActivity.this;
                                *//** Pass your fragment reference **//*
                                getActivity().startActivityForResult(intent, 1);
                            } else {
                                requestPermissionForWrite();
                            }
                        } else {
                            requestPermissionForCamera();
                        }

                        break;
                    case 1:
                        if (checkPermissionForWrite()) {
                            Intent intent1 = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent1.setType("image*//*");
                            Fragment frag = ServiceActivity.this;
                            */

    /**
     * Pass your fragment reference
     **//*
                            frag.startActivityForResult(Intent.createChooser(intent1, "Select File"), 2);
                            getActivity().startActivityForResult(
                                    Intent.createChooser(intent1, "Select File"), 2);
                        } else {
                            requestPermissionForWrite();
                        }
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }*/
    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionForCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)) {

            Toast.makeText(getActivity(), "write permission allows us to write data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, CPERMISSION_REQUEST_CODE);
        }
    }

    public boolean checkPermissionForWrite() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionForWrite() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(getActivity(), "write permission allows us to write data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "Permission granted");
                    onImageClick();
                    //Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {
                    Log.d("Permission", "Permission Denied");
                    //Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();
                }
                break;
            case CPERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "Permission granted");
                    onImageClick();
                    //Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {
                    Log.d("Permission", "Permission Denied");
                    //Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();
                }
                break;

        }
    }

    //Bitmap mbitmap;
    Uri imageUri = null;

    @Override
    public void onResume() {
        super.onResume();

        if (mbitmap != null) {
            servie_image_ID.setImageBitmap(mbitmap);
        }
        if (imageUri != null){
            servie_image_ID.setImageURI(imageUri);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            File f = new File(Environment.getExternalStorageDirectory().toString());
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE){
                    ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                    Log.d("numberOfimg",images.size()+"");
                    // servie_image_ID.setImageURI(Uri.parse(images.get(0).path));
                    //servie_image_ID.setImageResource(R.drawable.edit_image);
                    if (!images.isEmpty()){
                        if (images.size()==1){
                            ivMultiple.setVisibility(View.GONE);
                            imageUri = Uri.parse(images.get(0).path);
                            servie_image_ID.setImageURI(Uri.parse(images.get(0).path));
                            upload_multiple_image(images.get(0).path);
                        }else {
                            ivMultiple.setVisibility(View.VISIBLE);
                            imageUri = Uri.parse(images.get(0).path);
                            servie_image_ID.setImageURI(Uri.parse(images.get(0).path));
                            for (int i = 0; i < images.size(); i++) {
                                upload_multiple_image(images.get(i).path);
                            }
                        }

                        Toast.makeText(getActivity(), images.size()+" Images are uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                }
                //onSelectFromGalleryResult(data);

                else if (requestCode == SELECT_FILE_FROM_CAM)
                    //onCaptureImageResult(data);

                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                try {
                /*Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()), 120, 120, true);
                Bitmap mbitmap = ExifUtil.rotateBitmap(f.getAbsolutePath(), bitmap);
                image_upload = f.getAbsolutePath();
                img_profile_pic.setImageBitmap(mbitmap);
                if(!image_upload.equalsIgnoreCase("")){
                    upload();
                }*/
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = ExifUtil.rotateBitmap(f.getAbsolutePath(), bitmap);
                    Log.d("getAbsolutPath", f.getAbsolutePath());
                    FileOutputStream fo;
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    f.createNewFile();
                    fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    if (which.equalsIgnoreCase("pic")) {
                        image_upload = f.getAbsolutePath();
                        Log.d("image_upload", image_upload);
                        mbitmap = bitmap;
                        servie_image_ID.setImageBitmap(bitmap);
                        // image.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    if (!image_upload.equalsIgnoreCase("")) {
                        upload();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                        Log.i("Place: ", place.getAddress() + "");
                        places.setText(place.getAddress() + "");
                    } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                        Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                        // TODO: Handle the error.
                        Log.i("Errr", status.getStatusMessage());

                    } else if (resultCode == RESULT_CANCELED) {
                    }
                }

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
                        places.setText(place.getAddress() + "");

                        /*location=new Location();
                        location.setSurroundingDistance(500);
                        location.setLatitude(Double.parseDouble(latitude));
                        location.setLongitude(Double.parseDouble(longitude));
                        location.setAddressString(address);*/
                        //tvPlaceDetails.setText();
                    } else if (resultCode == PlacePicker.RESULT_ERROR) {
                /*Status status = PlacePicker.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("PlaceErrr", status.getStatusMessage());*/

                    }
                }

            }

        }
    }

    @Override
    public void onConnectionFailed (@NonNull ConnectionResult connectionResult){

    }

   /* @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
*/


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
                db_images.add(image);
                Log.d("downloadUrl",image);
                Log.d("success", "ff");
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
                Log.d("image_upload", image_upload);
                if (!image_upload.equalsIgnoreCase("")) {
                    upload();
                }

                servie_image_ID.setImageBitmap(bm);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

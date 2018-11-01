package atpl.cc.localisys.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atpl.cc.localisys.R;
import atpl.cc.localisys.Constants;
import atpl.cc.localisys.activities.Localisys_HomeActivity;
import atpl.cc.localisys.classes.UserDetailClass;
import atpl.cc.localisys.modal.User;
import pl.droidsonroids.gif.GifImageButton;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_OK;

public class EditProfileActivity extends Fragment implements View.OnClickListener {


    Button btn_interest,btn_skills;
    TextView save,cancel;
    ImageView back,facebook,twitter,instagram;
    Spinner country, state, country_code;
    Context context;
    ImageView profile_pic;
    EditText edit_fname,edit_lname,edit_addressd,edit_con,edit_bio,edit_zip;
    TextView edit_dob;
    int year;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    int month;
    int day;

    String image_upload="";
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask;

    StorageReference mStorageRef;
    public static final int CAMERA_INT=1;
    public static final int SD_INT=2;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Calendar myCalendar;
    Calendar c;
    Bitmap mbitmap;
    String image = "";
    GifImageView loading;
    File f;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int CPERMISSION_REQUEST_CODE = 2;
    String f_name="",last_name="",postal="",address="",phone_no="",bio="",dob="",coun="",coun_code="",states="";
    ProgressDialog pd;
    String user_id="";
    String which="";
    SharedPreferences sp;
    String udata = "",user_detail="";
    DatePickerDialog.OnDateSetListener date;

    ArrayList<String> stateList = new ArrayList<>();
    public static ArrayList<UserDetailClass> userDetailClassArrayList=new ArrayList<>();
    Bitmap addbitmap;

    /**/
    public static final String[] countryAreaCodes = { "Country code","93", "355", "213",
            "376", "244", "672", "54", "374", "297", "61", "43", "994", "973",
            "880", "375", "32", "501", "229", "975", "591", "387", "267", "55",
            "673", "359", "226", "95", "257", "855", "237", "1", "238", "236",
            "235", "56", "86", "61", "61", "57", "269", "242", "682", "506",
            "385", "53", "357", "420", "45", "253", "670", "593", "20", "503",
            "240", "291", "372", "251", "500", "298", "679", "358", "33",
            "689", "241", "220", "995", "49", "233", "350", "30", "299", "502",
            "224", "245", "592", "509", "504", "852", "36", "91", "62", "98",
            "964", "353", "44", "972", "39", "225", "1876", "81", "962", "7",
            "254", "686", "965", "996", "856", "371", "961", "266", "231",
            "218", "423", "370", "352", "853", "389", "261", "265", "60",
            "960", "223", "356", "692", "222", "230", "262", "52", "691",
            "373", "377", "976", "382", "212", "258", "264", "674", "977",
            "31", "687", "64", "505", "227", "234", "683", "850", "47", "968",
            "92", "680", "507", "675", "595", "51", "63", "870", "48", "351",
            "1", "974", "40", "7", "250", "590", "685", "378", "239", "966",
            "221", "381", "248", "232", "65", "421", "386", "677", "252", "27",
            "82", "34", "94", "290", "508", "249", "597", "268", "46", "41",
            "963", "886", "992", "255", "66", "228", "690", "676", "216", "90",
            "993", "688", "971", "256", "44", "380", "598", "1", "998", "678",
            "39", "58", "84", "681", "967", "260", "263" };
    String skills="",interest="";

    String[] countryCode;

    TextView edit_email;
    User userData;
    String[] countryArray;
    TextView tvStates;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_edit_profile,container,false);
      /*  ((ImageView)view.findViewById(R.id.img_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();

            }
        });*/

        countryCode=getResources().getStringArray(R.array.CountryCodes);
        countryArray = getResources().getStringArray(R.array.country_name);
        tvStates = view.findViewById(R.id.tvStates);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        mStorageRef = storage.getReference();

        sp = getActivity().getSharedPreferences("sp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("LastSavedImage","").apply();

        udata = sp.getString("u_data","");
        user_id = sp.getString("user_id","");
        Log.d("loginUserID",user_id+" /v");
        try {
            userData  = new Gson().fromJson(udata,User.class);
            Log.d("address",userData.address);

        }catch (Exception e){
            e.printStackTrace();
        }


        loading=(GifImageView) view.findViewById(R.id.loading);

        pd = new ProgressDialog(getActivity(),R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setIndeterminateDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.my_progress_indeterminate,null));

        save=(TextView) view.findViewById(R.id.save);
        cancel=(TextView) view.findViewById(R.id.cancel);
        back=(ImageView)view.findViewById(R.id.back);
        country=(Spinner)view.findViewById(R.id.spinner_country);
        state=(Spinner)view.findViewById(R.id.spinner_state);
        country_code=(Spinner)view.findViewById(R.id.spinner_country_code);
        facebook=(ImageView)view.findViewById(R.id.facebook_buttonID_edit_profile);
        instagram=(ImageView)view.findViewById(R.id.instagram);
        twitter=(ImageView)view.findViewById(R.id.twitter);

        profile_pic = (ImageView)view.findViewById(R.id.profile_pic);
        edit_fname = (EditText)view.findViewById(R.id.edit_fname);
        edit_lname = (EditText)view.findViewById(R.id.edit_lname);
        edit_addressd = (EditText)view.findViewById(R.id.edit_addressd);
        edit_con = (EditText)view.findViewById(R.id.edit_con);
        edit_bio = (EditText)view.findViewById(R.id.edit_bio);
        edit_zip = (EditText)view.findViewById(R.id.edit_zip);
        edit_dob = (TextView)view.findViewById(R.id.edit_dob);

        try {
            /*edit_dob.setText(userData.dob);
            edit_addressd.setText(userData.address);
            edit_con.setText(userData.phoneNumber);
            edit_zip.setText(userData.postCode);*/

        }catch (Exception e){
            e.printStackTrace();
        }

       /* myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };*/



        if (sp.getString("u_skill","").equalsIgnoreCase("")){
           // skills = user.skills;
        }else {
            skills = sp.getString("u_skill","");
        }

        if (sp.getString("u_interst","").equalsIgnoreCase("")){
           // interest = user.interests;
        }else {
            interest = sp.getString("u_interst","");
        }

        Log.d("u_skills",skills);
        Log.d("u_interest",interest);

        edit_fname.setText(f_name);
        edit_lname.setText(last_name);
        edit_addressd.setText(address);
        edit_con.setText(phone_no);
        edit_bio.setText("");
        edit_zip.setText(postal);
        edit_dob.setText(dob);
        stateList.add("select");

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        image=sp.getString("imageURL","");
        Log.d("imageURL",image+"mm");

        /*if (image == null || image.equalsIgnoreCase("")){
            profile_pic.setImageResource(R.drawable.avtar);
        }else {
            if (getImage(image)!=null) {
                profile_pic.setImageBitmap(getImage(image));
            }else {
                profile_pic.setImageResource(R.drawable.avtar);
            }
        }*/

        facebook.setOnClickListener(this);
        instagram.setOnClickListener(this);
        twitter.setOnClickListener(this);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        back.setOnClickListener(this);
        edit_dob.setOnClickListener(this);
        profile_pic.setOnClickListener(this);

        tvStates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Constants.Information_Dialog(getActivity(),"Please select country first.");
            }
        });


        ArrayAdapter countryAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, countryArray);

        //ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.country_name,android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(countryAdapter);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               try {

                   if (!country.getSelectedItem().toString().equalsIgnoreCase("Country")) {
                       stateList.clear();
                       tvStates.setVisibility(View.GONE);

                       if (country.getSelectedItem().toString().equalsIgnoreCase("australia")){
                           getStates("australia.json");
                       }

                       if (country.getSelectedItem().toString().equalsIgnoreCase("united-states")){
                           getStates("united-states.json");
                       }

                       /*coun = country.getSelectedItem().toString();
                       getStates(country.getSelectedItem().toString());*/
                   }else {
                       stateList.add(0,"State");
                       state.setSelection(0);
                       tvStates.setVisibility(View.VISIBLE);
                   }
                   if (position == 0) {
                       ((TextView)parent.getChildAt(0)).setTextColor(ResourcesCompat.getColor(getResources(),R.color.color1,null));
                   }
                   else {
                       ((TextView)parent.getChildAt(0)).setTextColor(ResourcesCompat.getColor(getResources(),R.color.black,null));
                       coun = country.getSelectedItem().toString();
                   }
               }
               catch (Exception ex){
                   ex.printStackTrace();
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(stateAdapter);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(ResourcesCompat.getColor(getResources(),R.color.color1,null));
                if (position==0){
                    ((TextView)parent.getChildAt(0)).setTextColor(ResourcesCompat.getColor(getResources(),R.color.color1,null));
                }else {
                    ((TextView)parent.getChildAt(0)).setTextColor(ResourcesCompat.getColor(getResources(),R.color.black,null));
                    states = state.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> countryCodeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, countryCode);
        countryCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_code.setAdapter(countryCodeAdapter);
        country_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0){
                    ((TextView)parent.getChildAt(0)).setTextColor(ResourcesCompat.getColor(getResources(),R.color.color1,null));
                    //((TextView)parent.getChildAt(0)).setText(coun_code);
                }else {
                    ((TextView)parent.getChildAt(0)).setTextColor(ResourcesCompat.getColor(getResources(),R.color.black,null));
                    coun_code = country_code.getSelectedItem().toString();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_skills = (Button) view.findViewById(R.id.btn_skills);
        //btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_interest = (Button) view.findViewById(R.id.btn_interest);

        btn_skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("sr","edit").apply();
                AddEventToFirebase("viewSkills");
                startActivity(new Intent(getActivity(), SkillsActivity.class));
            }
        });
      /*  btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container,new UserProfileActivity(),null).commit();

            }
        });*/

        btn_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("fr","edit").apply();
                AddEventToFirebase("viewInterests");
                startActivity(new Intent(getActivity(), InterestActivity.class));
            }
        });

        loading.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
            }
        },3000);

        //getUserDetails


        edit_email = view.findViewById(R.id.edit_email);
        edit_email.setText(sp.getString("u_email",""));
        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.Information_Dialog(getActivity(),"To change your email address, please go to the Settings screen.");
            }
        });
        Fetch();


        return view;
    }

    public void AddEventToFirebase(String name){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(name,bundle);
    }


    public void Fetch(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");

        mDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Log.d("User name: ", user.getUsername() + ", email " + user.getEmail());

                Log.d("imageURL",user.getProfileImageURL()+"");

                try {
                    if (user != null&&!user.getProfileImageURL().isEmpty()&&!user.getProfileImageURL().equalsIgnoreCase("")){
                        Picasso.with(getActivity()).load(user.getProfileImageURL()).placeholder(R.drawable.profile_dummy)
                                .error(R.drawable.profile_dummy).into(profile_pic);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

                edit_fname.setText(user.getFirstName());
                edit_lname.setText(user.getLastName());
                edit_zip.setText(user.getPostCode());
                edit_addressd.setText(user.getAddress());
                edit_con.setText(user.getPhoneNumber());
                edit_dob.setText(user.getDob());
                coun=user.getCountry();
                if (coun!=null){
                    if (!coun.equalsIgnoreCase("")){
                        Log.d("country",coun);
                        tvStates.setVisibility(View.GONE);
                        country.setSelection(getCountry(coun));
                        if (coun.equalsIgnoreCase("australia")){
                            getStates("australia.json");
                        }else if (coun.equalsIgnoreCase("united-states")){
                            getStates("united-states.json");
                        }
                    }

                }

                coun_code=user.getCountryCode();
                if (coun_code != null){
                    if (!coun_code.equalsIgnoreCase("")){
                        country_code.setSelection(getCountryCode(coun_code));

                    }
                }

                states=user.getState();
                if (states != null){
                    if (!states.equalsIgnoreCase("")){
                        Log.d("countrys",states);
                        state.setSelection(getState(states));
                    }
                }

                bio=user.getBiography();
                edit_bio.setText(bio);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });
    }


    public int getCountry(String country){
        int count = 0;
        if (countryArray.length>0){
            for (int i=0;i<countryArray.length;i++){

                    if (countryArray[i].equalsIgnoreCase(country)) {
                        count = i;
                        Log.d("coCount", count + ""+countryArray[i]);
                    }

            }
        }
        return count;
    }

    public int getState(String state){
        int count = 0;
        if (stateList.size()>0){
            for (int i=0;i<stateList.size();i++){
                if (stateList.get(i).equalsIgnoreCase(state)){
                    count=i;
                }
            }
        }
        return count;
    }

    public int getCountryCode(String code){
        int count = 0;
        if (countryCode.length>0){
            for (int i=0;i<countryCode.length;i++){
                if (countryCode[i].equalsIgnoreCase(code)){
                    count++;
                }
            }
        }
        return count;
    }





    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edit_dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        if (v==back || v==cancel){
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container,new UserProfileActivity(),null).commit();
        }
        if (v==instagram){
            Constants.instagram(getActivity());
        }
        if (v==facebook){
            Constants.facebook(getActivity());
        }
        if (v==twitter){
            Constants.twitter(getActivity());
        }
        if (v == edit_dob){

           /* new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
            show_datePicker();
        }
        if (v==save){


            update_Details(user_id);

        }
        if (v == profile_pic){
            which = "pic";
            onImageClick();
            AddEventToFirebase("changeProfilePicture");
        }
    }


    public void show_datePicker(){

        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                year = i;
                month = i1;
                day = i2;
                edit_dob.setText(new StringBuilder().append(day).append("-").append(month + 1)
                        .append("-").append(year)
                        .append(" "));
            }
        },year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.setTitle(getString(R.string.SelectDate));
        datePickerDialog.show();
    }


    public void update_Details(String userId){
        pd.show();



        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
        final User user = new User();
        Map<String,Object> map = new HashMap<>();

        f_name = edit_fname.getText().toString();
        last_name = edit_lname.getText().toString();
        postal = edit_zip.getText().toString();
        address = edit_addressd.getText().toString();
        phone_no = edit_con.getText().toString();
        bio = edit_bio.getText().toString();
        dob = edit_dob.getText().toString();

        AddEventToFirebase("changeFirstName");
        AddEventToFirebase("changeLastName");
        AddEventToFirebase("changeDOB");
        AddEventToFirebase("changeCountry");
        AddEventToFirebase("changeState");
        AddEventToFirebase("changePostCode");
        AddEventToFirebase("changeAddress");
        AddEventToFirebase("changeCountryCode");
        AddEventToFirebase("changePhoneNumber");
        AddEventToFirebase("changeBiography");

        map.put("address", address);

      //  map.put("country", country.getSelectedItem().toString());
        map.put("country",coun);

        //map.put("countryCode", country_code.getSelectedItem().toString());
        map.put("countryCode", coun_code);

        map.put("dob", dob);

        map.put("firstName", f_name);

        map.put("profileImageURL", image);

        map.put("lastName", last_name);

        map.put("phoneNumber", phone_no);

        map.put("postCode", postal);

        //map.put("state", state.getSelectedItem().toString());
        map.put("state", states);

        map.put("biography",bio);

        mDatabase.child(userId).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    pd.dismiss();
                    Log.d("inserted", "inserted");



                   /* sp.edit().putString("u_image",image).commit();
                    user_detail = new Gson().toJson(user);
                    sp.edit().putString("u_data",user_detail).commit();*/

                    //

                    if (!image_upload.equalsIgnoreCase("")) {
                        upload();

                    }else {
                        Toast.makeText(getActivity(), "Data updated successfully.", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new UserProfileActivity()).addToBackStack(null).commit();
                    }



                }else {
                    Log.d("errr","errr");
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        });
    }

    public Bitmap getImage(String image){
        Bitmap bitmap = null;
        try {
            byte [] encodeByte= Base64.decode(image, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

   /* public void getStates(String arrayname){
        stateList.clear();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray(arrayname);
            String names = "";

            for (int i = 0; i < m_jArry.length(); i++) {
                names = m_jArry.getString(i);
                Log.d("Details-->",names);
                stateList.add(names);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("countriesToCities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }*/

    public String loadJSONFromAsset(String json_name) {
        String json = null;
        try {
            //InputStream is = ProfileActivity.this.getAssets().open("provinces.json");
            /*"countriesToCities.json"*/
            InputStream is = ((Localisys_HomeActivity)getActivity()).getAssets().open(json_name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    public void getStates(String jsonName){
        stateList.clear();
        try {
            /*afghanistan.json*/
            JSONArray jsonArray=new JSONArray(loadJSONFromAsset(jsonName));
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String state_names = "";
                state_names=jsonObject.getString("code");
                Log.d("Details-->",state_names);
                stateList.add(state_names);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                                f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                startActivityForResult(intent, CAMERA_INT);
                            } else {
                                requestPermissionForWrite();
                            }
                        }else {
                            requestPermissionForCamera();
                        }

                        break;
                    case 1:
                        if(checkPermissionForWrite()) {
                            Intent intent1 = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent1.setType("image/*");
                            startActivityForResult(
                                    Intent.createChooser(intent1, "Select File"), SD_INT);
                        }else {
                            requestPermissionForWrite();
                        }
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
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionForCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),android.Manifest.permission.CAMERA)){

            Toast.makeText(getActivity(),"write permission allows us to write data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
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

    private void requestPermissionForWrite(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            Toast.makeText(getActivity(),"write permission allows us to write data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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


            if (requestCode == CAMERA_INT) {

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
                    bitmapOptions.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = ExifUtil.rotateBitmap(f.getAbsolutePath(), bitmap);
                    Log.d("getAbsolutPath",f.getAbsolutePath());
                    FileOutputStream fo;
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    f.createNewFile();
                    fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    if (which.equalsIgnoreCase("pic")) {
                        image_upload = f.getAbsolutePath();
                        Log.d("image_upload", image_upload);
                        addbitmap = bitmap;
                        profile_pic.setImageBitmap(bitmap);

                    }
                    /*if (!image_upload.equalsIgnoreCase("")) {
                        upload();
                    }*/

                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(requestCode == SD_INT) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = 1;
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath,bitmapOptions));
                Bitmap mbitmap = ExifUtil.rotateBitmap(picturePath, thumbnail);
                image_upload = picturePath;
                profile_pic.setImageBitmap(mbitmap);
                addbitmap = mbitmap;
                /*if(!image_upload.equalsIgnoreCase("")){
                    upload();
                }*/
            }
        }

    }

    @SuppressWarnings("VisibleForTests")
    public void upload(){
        pd.show();

        StorageReference childRef = mStorageRef.child("UserProfileImages/user"+System.currentTimeMillis());
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

                SharedPreferences.Editor editor=sp.edit();
                editor.putString("imageURL",image).apply();

                Toast.makeText(getActivity(), "Data updated successfully.", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new UserProfileActivity()).addToBackStack(null).commit();



                Log.d("downloadUrl",image);
                //Toast.makeText(getActivity(), "Image Upload successfully", Toast.LENGTH_SHORT).show();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
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
                profile_pic.setImageBitmap(thumbnail);
                // image.setScaleType(ImageView.ScaleType.FIT_XY);
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

                profile_pic.setImageBitmap(bm);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

}

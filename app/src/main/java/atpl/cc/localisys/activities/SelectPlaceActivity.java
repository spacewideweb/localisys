package atpl.cc.localisys.activities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import atpl.cc.localisys.R;
import atpl.cc.localisys.classes.GpsTracker;
import atpl.cc.localisys.activities.classes.Location;
import atpl.cc.localisys.fragments.ServiceActivity;

public class SelectPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    LatLng current;
    GoogleMap mMap;
    public static String currentAdddress="",clat ="",clng="";

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBCPumIw4XxfyTvy6WnpZo-0urQYVnpxdU";
    public static Location location;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static String stateStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        try {
            GpsTracker gpsTracker=new GpsTracker(SelectPlaceActivity.this);
            double longitude=gpsTracker.getLongitude();
            double latitude=gpsTracker.getLatitude();
            LatLng latLngA = new LatLng(latitude,longitude);
            current = latLngA;
        }catch (Exception e){
            e.printStackTrace();
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        ((View)findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("Place", "Place: " + place.getName());
                currentAdddress = place.getAddress().toString();
                clat = String.valueOf(place.getLatLng().latitude);
                clng = String.valueOf(place.getLatLng().longitude);
                current = place.getLatLng();


                try {
                    GetAddresss();
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(current));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                    AddEToFire("tapSearchLocationMap");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Place", "An error occurred: " + status);
            }
        });

        findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEToFire("tapCancelLocationMap");
                SelectPlaceActivity.this.finish();
            }
        });


        findViewById(R.id.btn_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEToFire("tapApplyLocationMap");
                location=new Location();
                location.setSurroundingDistance(500);
                location.setLatitude(Double.parseDouble(clat));
                location.setLongitude(Double.parseDouble(clng));
                location.setAddressString(currentAdddress);
                ServiceActivity.places.setText(currentAdddress);
                stateStr = state;
                Log.d("stateStr",stateStr);

                SelectPlaceActivity.this.finish();
            }
        });

       /* AutoCompleteTextView autoCompView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getApplicationContext(), android.R.layout.simple_list_item_1));
        autoCompView.setOnItemClickListener(this);*/

       recordScreenView();
       AddEToFire("tapSetRadiusLocationMap");


    }

    public void AddEToFire(String eName){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(eName,bundle);
    }

    private void recordScreenView() {
        String screenName = "selectlocation";
        try {
            mFirebaseAnalytics.setCurrentScreen(this, screenName, null /* class override */);
            // [END set_current_screen]
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    String state = "";

    public void GetAddresss(){
        try {
            clat = String.valueOf(current.latitude);
            clng = String.valueOf(current.longitude);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(current.latitude, current.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            currentAdddress = address;
            String city = addresses.get(0).getLocality();
            state = addresses.get(0).getLocality();
            String stStr = addresses.get(0).getAdminArea();

            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(current));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
        GetAddresss();
    }


    public static ArrayList autocomplete(String input) {

        ArrayList resultList = null;



        HttpURLConnection conn = null;

        StringBuilder jsonResults = new StringBuilder();

        try {

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);

            sb.append("?key=" + API_KEY);

            sb.append("&components=country:gr");

            sb.append("&input=" + URLEncoder.encode(input, "utf8"));



            URL url = new URL(sb.toString());

            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());



            // Load the results into a StringBuilder

            int read;

            char[] buff = new char[1024];

            while ((read = in.read(buff)) != -1) {

                jsonResults.append(buff, 0, read);

            }

        } catch (MalformedURLException e) {

            Log.e("ee", "Error processing Places API URL", e);

            return resultList;

        } catch (IOException e) {

            Log.e("ee", "Error connecting to Places API", e);

            return resultList;

        } finally {

            if (conn != null) {

                conn.disconnect();

            }

        }

        try {

            // Create a JSON object hierarchy from the results

            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            Log.d("predictions",predsJsonArray.toString());



            // Extract the Place descriptions from the results

            resultList = new ArrayList(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {

                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));

                System.out.println("============================================================");

                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

            }

        } catch (JSONException e) {

            Log.e("ee", "Cannot process JSON results", e);

        }



        return resultList;

    }

   /* @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }*/



    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {

        private ArrayList<String> resultList = new ArrayList<>();

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }



        @Override

        public int getCount() {
            return resultList.size();
        }



        @Override

        public String getItem(int index) {
            return resultList.get(index);
        }



        @Override

        public Filter getFilter() {

            Filter filter = new Filter() {

                @Override

                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();

                    if (constraint != null) {

                        // Retrieve the autocomplete results.

                        resultList = autocomplete(constraint.toString());



                        // Assign the data to the FilterResults

                        filterResults.values = resultList;

                        filterResults.count = resultList.size();

                    }

                    return filterResults;

                }



                @Override

                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count > 0) {

                        notifyDataSetChanged();

                    } else {

                        notifyDataSetInvalidated();

                    }

                }

            };

            return filter;

        }

    }


}

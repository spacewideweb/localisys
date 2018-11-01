package atpl.cc.localisys.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import atpl.cc.localisys.R;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    View view;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    double lat;
    double lng;
    String title="";
    String type="";
    ImageView exitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the exlayout for this fragment
        view= inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        exitmap=(ImageView)view.findViewById(R.id.exitmap);
        exitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        mapFragment.getMapAsync(this);

        //get latlng
        Bundle bundle = getArguments();
        if (bundle!=null) {
            lat = Double.parseDouble(bundle.getString("Latitudeb"));
            lng = Double.parseDouble(bundle.getString("Longitudb"));
            title=bundle.getString("posttitle");
            type=bundle.getString("posttype");
        }
        else {
            lat=36.8485;
            lng=174.7633;
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        if (googleMap!=null){
            googleMap.clear();
        }
        googleMap = gMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {

        }

        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng placeLocation = new LatLng(lat,lng);
       // Marker placeMarker = googleMap.addMarker(new MarkerOptions().position(placeLocation).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 1000, null);

        if (type.equalsIgnoreCase("productService")){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.service_location))).setTag(0);
        }
        if (type.equalsIgnoreCase("event")){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.event_location))).setTag(0);
        }
        if (type.equalsIgnoreCase("question")){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.ask_location))).setTag(0);
        }
    }

}

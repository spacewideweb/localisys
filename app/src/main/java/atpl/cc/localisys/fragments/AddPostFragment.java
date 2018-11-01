package atpl.cc.localisys.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;

import atpl.cc.localisys.R;
import atpl.cc.localisys.adapter.TagPeopleAdapter;

public class AddPostFragment extends Fragment implements View.OnClickListener{

    LinearLayout lin_blue,lin_yellow,lin_purple;
    public static boolean[] checked;
    public static boolean[] checkedsection;
    SharedPreferences sp;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        lin_blue = (LinearLayout) view.findViewById(R.id.linear_blue);
        lin_yellow = (LinearLayout)view. findViewById(R.id.linear_yellow);
        lin_purple = (LinearLayout) view.findViewById(R.id.linear_purple);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        lin_blue.setOnClickListener(this);
        lin_yellow.setOnClickListener(this);
        lin_purple.setOnClickListener(this);

        return view;
    }

    public void AddEToFire(String eName){
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(eName,bundle);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.linear_blue:
                sp.edit().putString("private_post","no").apply();
                sp.edit().putString("nos","0").apply();
                checked = null;
                checkedsection = null;
                TagPeopleAdapter.count = 0;
                TagPeople.count = 0;
                TagPeopleAdapter.map.clear();
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new ServiceActivity()).addToBackStack(null).commit();
               AddEToFire("tapCreateServiceProduct");
               break;
            case R.id.linear_yellow:
                sp.edit().putString("private_post","no").apply();
                sp.edit().putString("nos","0").apply();
                checked = null;
                checkedsection = null;
                TagPeopleAdapter.count = 0;
                TagPeople.count = 0;
                TagPeopleAdapter.map.clear();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new LookinEventActivity()).addToBackStack(null).commit();
                AddEToFire("tapCreateEvent");
                break;
            case R.id.linear_purple:
                sp.edit().putString("private_post","no").apply();
                sp.edit().putString("nos","0").apply();
                checked = null;
                checkedsection = null;
                TagPeopleAdapter.count = 0;
                TagPeople.count = 0;
                TagPeopleAdapter.map.clear();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new AskQuestionActivity()).addToBackStack(null).commit();
                AddEToFire("tapCreateQuestion");
                break;
        }

    }
}

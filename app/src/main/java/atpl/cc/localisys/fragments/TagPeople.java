package atpl.cc.localisys.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atpl.cc.localisys.R;
import atpl.cc.localisys.Constants;
import atpl.cc.localisys.activities.Localisys_HomeActivity;
import atpl.cc.localisys.adapter.TagPeopleAdapter;
import atpl.cc.localisys.classes.UserDetailClass;
import atpl.cc.localisys.modal.FollowList;
import atpl.cc.localisys.modal.NonScrollListView;
import atpl.cc.localisys.modal.User;
import atpl.cc.localisys.classes.UserDetailClass;
import de.hdodenhof.circleimageview.CircleImageView;

public class TagPeople extends Fragment {

     ListView tag_list;
    public static NonScrollListView listofPeopleFollow,listofGlobalsearch;

    public static ImageView visibility, back;
    int  tag=1;
    PopupWindow popup;
    ArrayList<UserDetailClass> userDetailClassArrayList=new ArrayList<>();
    ArrayList<String> taguserID =new ArrayList<>();
    ArrayList<String> taguserID1 =new ArrayList<>();
    ArrayList<FollowList> peopleyoufollowarraylist=new ArrayList<>();
    //ArrayList<UserDetailClass> userDetailClassArrayList=new ArrayList<>();

    SharedPreferences sharedPreferences;
    SearchView searchView;
    TagPeopleAdapter tagPeopleAdapter;
   // PeopleYouFollowAdapter peopleYouFollowAdapter;
    View vv;
    boolean[] checked;
    ScrollView sectionScroll;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("User");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    taguserID.add(postSnapShot.getKey());
                    UserDetailClass userDetailClass=postSnapShot.getValue(UserDetailClass.class);
                    userDetailClassArrayList.add(userDetailClass);
                }
                if (AddPostFragment.checked==null){
                    AddPostFragment.checked = new boolean[userDetailClassArrayList.size()];
                }
                tagPeopleAdapter=new TagPeopleAdapter(getActivity(),userDetailClassArrayList,taguserID,AddPostFragment.checked);
                listofGlobalsearch.setAdapter(tagPeopleAdapter);
                tag_list.setAdapter(tagPeopleAdapter);
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_tag_people,container,false);
        sharedPreferences=getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
         tagPeopleAdapter=new TagPeopleAdapter(getActivity());
        vv=(View)view.findViewById(R.id.vv);
        sectionScroll=view.findViewById(R.id.sectionScroll);

        listofGlobalsearch=view.findViewById(R.id.listofGlobalsearch);
        listofPeopleFollow=view.findViewById(R.id.listofPeopleFollow);

        searchView=(SearchView)view.findViewById(R.id.search_people);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vv.setVisibility(View.GONE);
                    sectionScroll.setVisibility(View.GONE);
                    tag_list.setVisibility(View.VISIBLE);
                }
                else {
                    vv.setVisibility(View.VISIBLE);
                    sectionScroll.setVisibility(View.VISIBLE);
                    tag_list.setVisibility(View.GONE);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tagPeopleAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tagPeopleAdapter.filter(newText);

                return false;
            }
        });
        tag_list=(ListView)view.findViewById(R.id.tag_people_list);
        visibility=(ImageView)view.findViewById(R.id.visibility);
        back=(ImageView)view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPostFragment.checked = TagPeopleAdapter.checked;
                int total = TagPeopleAdapter.count+count;
                if (total == 0){
                    editor.putString("nos","0").apply();
                }else {
                    editor.putString("nos",String.valueOf(total)).apply();
                }

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        if (sharedPreferences.getString("private_post","").equalsIgnoreCase("yes")){
            visibility.setImageResource(R.drawable.ic_visibility_on);
        }
        else {
            visibility.setImageResource(R.drawable.eye_vivible);
        }

        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TagPeopleAdapter.count == 0 && count==0){
                    Constants.Information_Dialog(getActivity(),"To make this post private, you must tag at least one user");
                }else {
                    if (tag == 1) {
                        visibility.setImageResource(R.drawable.ic_visibility_on);
                        displayPopupWindow(v);
                        editor.putString("private_post", "yes");
                        editor.apply();
                        tag = 0;
                    } else {
                        visibility.setImageResource(R.drawable.eye_vivible);
                        editor.putString("private_post", "no");
                        editor.apply();
                        if (popup != null) {
                            popup.dismiss();
                        }
                        tag = 1;
                    }
                }

            }
        });

        displayPopupWindow(visibility);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (popup!=null){
                    popup.dismiss();
                }
            }
        },1500);


        peopleyoufollow(Localisys_HomeActivity.user_id);


        return view;
    }

    public static int count = 0;

    public class followPeopleAdapter extends BaseAdapter{

        ArrayList<FollowList> peopleyoufollowarraylist;
        ArrayList<String> taguserID1;


        public boolean[] checked;


        public followPeopleAdapter(ArrayList<FollowList> peopleyoufollowarraylist,boolean[] checked) {
            this.peopleyoufollowarraylist = peopleyoufollowarraylist;
            this.checked = checked;
        }

        @Override
        public int getCount() {
            return peopleyoufollowarraylist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            tag=0;
            final ViewHolder viewHolder;

            if (convertView==null){
                LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
                convertView= layoutInflater.inflate(R.layout.tag_people_list_item_layout, null);
                viewHolder = new ViewHolder(convertView);
                viewHolder.tick.setImageResource(R.drawable.ic_check_white);
                viewHolder.tick.setTag(position);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder=(ViewHolder)convertView.getTag();
            }

            if (checked[position]){
                viewHolder.tick.setImageResource(R.drawable.tick_blue_sharp);
            }else {
                viewHolder.tick.setImageResource(R.drawable.ic_check_white);
            }


            viewHolder.tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checked[position]){
                        checked[position] = false;
                        viewHolder.tick.setImageResource(R.drawable.ic_check_white);
                        TagPeopleAdapter.map.remove(peopleyoufollowarraylist.get(position).getKey());
                        if (count!=0){
                            count--;
                        }
                    }else {
                        TagPeopleAdapter.map.put(peopleyoufollowarraylist.get(position).getKey(),true);
                        checked[position] = true;
                        count++;
                        viewHolder.tick.setImageResource(R.drawable.tick_blue_sharp);
                    }
                    if (count == 0 && TagPeopleAdapter.count == 0){
                        visibility.setImageResource(R.drawable.eye_vivible);
                    }
                    notifyDataSetChanged();
                }
            });

            Log.d("arrayListTag",peopleyoufollowarraylist.size()+"");
            try {
                if (peopleyoufollowarraylist.size()>0){
                    Fetch(peopleyoufollowarraylist.get(position).getKey(),viewHolder.people_image,viewHolder.people_name);
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
           /* LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            view=layoutInflater.inflate(R.layout.tag_people_list_item_layout,null);
            CircleImageView people_image=view.findViewById(R.id.people_image);
            TextView name=view.findViewById(R.id.people_name);
            return view;*/

        }

        public class ViewHolder {
            ImageView tick,people_image;
            TextView people_name;

            ViewHolder(View v) {
                tick=v.findViewById(R.id.refresh_img2);
                people_image=v.findViewById(R.id.people_image);
                people_name=v.findViewById(R.id.people_name);
            }
        }

        public void Fetch(String user_id, final ImageView imageView,final TextView name){
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");

            mDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);

                    Log.d("User name: ", user.getUsername() + ", email " + user.getEmail());

                    Log.d("imageURL",user.getProfileImageURL()+"");

                    name.setText(user.getUsername());

                    try {
                        if (user != null&&!user.getProfileImageURL().isEmpty()&&!user.getProfileImageURL().equalsIgnoreCase("")){
                            Picasso.with(getActivity()).load(user.getProfileImageURL()).placeholder(R.drawable.profile_dummy)
                                    .error(R.drawable.profile_dummy).into(imageView);
                        }
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {

                    // Failed to read value
                    Log.w("Failed to read value.", error.toException());
                }
            });
        }
    }

    public void peopleyoufollow(String userID){
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Following");

        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        peopleyoufollowarraylist.add(new FollowList(ds.getKey(),false));
                    }

                    if (AddPostFragment.checkedsection==null){
                        AddPostFragment.checkedsection = new boolean[peopleyoufollowarraylist.size()];
                    }

                    listofPeopleFollow.setAdapter(new followPeopleAdapter(peopleyoufollowarraylist,AddPostFragment.checkedsection));

                    Log.d("followarraylistsize",peopleyoufollowarraylist.size()+"");
                }


               /* tagPeopleAdapter=new TagPeopleAdapter(getActivity(),peopleyoufollowarraylist,AddPostFragment.checked);
                listofPeopleFollow.setAdapter(tagPeopleAdapter);*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void displayPopupWindow(View anchorView) {
        popup = new PopupWindow(getActivity());
        View layout = getActivity().getLayoutInflater().inflate(R.layout.popup_content, null);
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        //popup.getContentView().setBackgroundResource(android.R.color.transparent);
        popup.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        //popup.setBackgroundDrawable(android.R.drawable.screen_background_light_transparent);
        // Show anchored to button
        popup.showAtLocation(anchorView, Gravity.TOP|Gravity.RIGHT,0, anchorView.getBottom()+35);

        popup.showAsDropDown(anchorView);
    }

}

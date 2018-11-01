package atpl.cc.localisys.adapter;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atpl.cc.localisys.R;
import atpl.cc.localisys.classes.UserDetailClass;
import atpl.cc.localisys.fragments.TagPeople;
import atpl.cc.localisys.classes.UserDetailClass;

/**
 * Created by android on 30/5/17.
 */

public class TagPeopleAdapter extends BaseAdapter{

    Context context;
    LayoutInflater linf;
    ImageView right_img,left_img;
    int tag=0;
    ArrayList<UserDetailClass> arrayList;
    ArrayList<String> taguserID;
    private ArrayList<UserDetailClass> tempjoblist;
    public static boolean[] checked;
    public static int count = 0;
    public static Map<String,Object> map = new HashMap<>();


    public TagPeopleAdapter(Context applicationContext,ArrayList<UserDetailClass> arrayList,ArrayList<String> taguserID,boolean[] checked) {

        this.context = applicationContext;
        this.arrayList=arrayList;
        this.taguserID = taguserID;
        this.checked = checked;
        this.tempjoblist = new ArrayList<>();
        this.tempjoblist.addAll(arrayList);
        /*checked = new boolean[arrayList.size()];*/
    }
    public TagPeopleAdapter(Context applicationContext) {
        this.context = applicationContext;

    }

    @Override
    public int getCount() {
        return arrayList.size();
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
    public View getView(final int position,  View convertView, ViewGroup parent) {
        tag=0;
        final ViewHolder viewHolder;

        linf = LayoutInflater.from(context);

        if (convertView==null){
            convertView= linf.inflate(R.layout.tag_people_list_item_layout, null);
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
                    map.remove(taguserID.get(position));
                    if (count!=0){
                        count--;
                    }
                }else {
                    map.put(taguserID.get(position),true);
                    checked[position] = true;
                    count++;
                    viewHolder.tick.setImageResource(R.drawable.tick_blue_sharp);
                }
                if (count == 0 && TagPeople.count == 0){
                    TagPeople.visibility.setImageResource(R.drawable.eye_vivible);
                }
                notifyDataSetChanged();
            }
        });
        Log.d("arrayListTag",arrayList.size()+"");
        try {
            if (arrayList.get(position).getProfileImageURL() != null && !arrayList.get(position).getProfileImageURL().equalsIgnoreCase("")) {
                Picasso.with(context).load(arrayList.get(position).getProfileImageURL()).placeholder(R.drawable.profile_dummy).error(R.drawable.profile_dummy).into(viewHolder.people_image);
            }
            if (arrayList.get(position).getUsername() == null) {
                //viewHolder.people_name.setText(arrayList.get(position).getUsername());
            } else {
                viewHolder.people_name.setText(arrayList.get(position).getUsername());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    public static class ViewHolder {
        ImageView tick,people_image;
        TextView people_name;

        ViewHolder(View v) {
            tick=v.findViewById(R.id.refresh_img2);
            people_image=v.findViewById(R.id.people_image);
            people_name=v.findViewById(R.id.people_name);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(tempjoblist);

        }
        else
        {
            for (UserDetailClass wp : tempjoblist)
            {
                if (wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)/*||wp.getFirstName().toLowerCase(Locale.getDefault()).contains(charText)*/)
                {
                    arrayList.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }
}

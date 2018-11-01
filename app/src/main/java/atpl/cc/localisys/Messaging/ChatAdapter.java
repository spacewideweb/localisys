package atpl.cc.localisys.Messaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import atpl.cc.localisys.R;
import atpl.cc.localisys.modal.User;

/**
 * Created by designer on 30/3/18.
 */

public class ChatAdapter extends BaseAdapter {

    Context context;
    ArrayList<Message> mList;
    String user_id = "";
    SharedPreferences sp;

    public ChatAdapter(Context context, ArrayList<Message> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        user_id = sp.getString("user_id","");
        view = LayoutInflater.from(context).inflate(R.layout.custom_chat, viewGroup, false);

        RelativeLayout relSender = view.findViewById(R.id.relSender);
        RelativeLayout relReciever = view.findViewById(R.id.relReciever);

        TextView message_userS=view.findViewById(R.id.message_userS);
        TextView message_textS=view.findViewById(R.id.message_textS);
        TextView message_timeS=view.findViewById(R.id.message_timeS);

        TextView message_userR=view.findViewById(R.id.message_userR);
        TextView message_textR=view.findViewById(R.id.message_textR);
        TextView message_timeR=view.findViewById(R.id.message_timeR);



        if (user_id.equalsIgnoreCase(mList.get(i).getSenderId())) {

            relSender.setVisibility(View.VISIBLE);
            message_textS.setText(mList.get(i).getMessageText());
            message_timeS.setText(parseDateToddMMyyyy(mList.get(i).getTimestamp().replace("T"," ").replace("Z"," ")));
            Show_users(mList.get(i).getSenderId(),message_userS);
        } else{
            relReciever.setVisibility(View.VISIBLE);
            message_textR.setText(mList.get(i).getMessageText());
            message_timeR.setText(mList.get(i).getTimestamp());
            message_timeR.setText(parseDateToddMMyyyy(mList.get(i).getTimestamp().replace("T"," ").replace("Z"," ")));
            Show_users(mList.get(i).getSenderId(),message_userR);
        }

        Log.d("mList",mList.size()+"");

        return view;
    }

    public String parseDateToddMMyyyy(String time) {
        //2018-03-03T17:36:12Z
        String inputPattern = "yyyy-MM-dd HH:mm:ss ";
        String outputPattern = "hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public void Show_users(String id, final TextView user_name) {
        try {

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");

            mDatabase.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);

                    Log.d("User name: ", user.getUsername() + ", email " + user.getEmail());
                    user_name.setText(user.getUsername() + " ");

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Failed to read value.", error.toException());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

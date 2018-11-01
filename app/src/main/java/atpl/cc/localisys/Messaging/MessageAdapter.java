package atpl.cc.localisys.Messaging;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import atpl.cc.localisys.R;
import atpl.cc.localisys.modal.User;

public class MessageAdapter extends FirebaseListAdapter<ChatMessage> {

    private ChatActivity activity;

    public MessageAdapter(ChatActivity activity, Class<ChatMessage> modelClass, int modelLayout, DatabaseReference ref) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;

    }

    @Override
    protected void populateView(View v, ChatMessage model, int position) {
        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);

        messageText.setText(model.getMessageText());
        messageUser.setText(model.getMessageUser());


        // Format the date before showing it
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage = getItem(position);

        /*if (chatMessage.getMessageUserId().equals(activity.getLoggedInUserName())) {
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
            TextView message_user=view.findViewById(R.id.message_user);
            Show_users(ChatActivity.UserID,message_user);
        }
        else{
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);
            TextView message_user=view.findViewById(R.id.message_user);
            Show_users(ChatActivity.postCreatorID,message_user);
        }
        //generating view
        populateView(view, chatMessage, position);*/

        return view;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }

    public void Show_users(String id, final TextView user_name){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Log.d("User name: ", user.getUsername() + ", email " + user.getEmail());
                user_name.setText(user.getUsername()+" ");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });

        /*Query queryRef = mDatabase.orderByKey().equalTo(id);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getValue());
                    User user = dataSnapshot.getValue(User.class);
                    user_name.setText(user.email);
                    if (user.profileImageURL!=null) {
                        Glide.with(context).load(user.profileImageURL).into(userImage);
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}

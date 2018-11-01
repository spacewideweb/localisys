package atpl.cc.localisys.Messaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import atpl.cc.localisys.R;
import atpl.cc.localisys.Constants;
import atpl.cc.localisys.activities.Localisys_HomeActivity;
import atpl.cc.localisys.modal.AddService;

public class ChatActivity extends AppCompatActivity {

    private static final int SIGN_IN_REQUEST_CODE = 111;
    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listView;
    private String loggedInUserName = "";
    TextView noMsg;
    public static String postCreatorID="";
    //public static String postCreatorIDSh="";
    SharedPreferences sharedPreferences;
    SharedPreferences sp;
    static public String UserID="";
    ArrayList<ChatMessage> chatMessageArrayList=new ArrayList<>();
    String postId = "";
    ArrayList<Message> mList = new ArrayList<>();
    ChatAdapter chatAdapter;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        sharedPreferences=getSharedPreferences("clickedPost",MODE_PRIVATE);
        sp=getSharedPreferences("sp",MODE_PRIVATE);
        UserID=sp.getString("user_id","");
        //postCreatorIDSh=sharedPreferences.getString("FrontUID","");


        ImageView back=findViewById(R.id._back);
        noMsg=findViewById(R.id._noMsg);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            postCreatorID=bundle.getString("postCreatorID");
            postId=bundle.getString("postId");
            from = bundle.getString("from");
            Log.d("postCreatorID",postCreatorID+" "+postId+" "+from);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //SendbySender();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final EditText input = (EditText) findViewById(R.id.input);
        listView = (ListView) findViewById(R.id.list);



/*
        chatAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                listView.setSelection(listView.getCount() - 1);
            }
        });*/


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().trim().equals("")) {
                    Toast.makeText(ChatActivity.this, "Please enter some texts!", Toast.LENGTH_SHORT).show();
                } else {

                    if (from.equalsIgnoreCase("provider")) {
                        Send(input.getText().toString(), postCreatorID, UserID);
                    }else if (from.equalsIgnoreCase("requester")){
                        Send(input.getText().toString(), postCreatorID, UserID);
                    }
                    input.setText("");
                }
            }
        });

        getAllMsg();
    }

    public void getAllMsg(){
        mList.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Conversation");
        mDatabase.child(postId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ChildF","Addded");
                if (dataSnapshot.exists()) {
                    mList.clear();
                    listView.setVisibility(View.VISIBLE);
                    noMsg.setVisibility(View.GONE);
                    //getSingleMsg(dataSnapshot.getKey());
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Log.d("AllMsg",dataSnapshot1.getKey());
                        getSingleMsg(dataSnapshot1.getKey());
                    }

                }else {
                    listView.setVisibility(View.GONE);
                    noMsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("ChildF","Changed");
                if (dataSnapshot.exists()) {
                    mList.clear();
                    //getSingleMsg(dataSnapshot.getKey());
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Log.d("ChildFkey",dataSnapshot1.getKey());
                        Log.d("AllMsg",dataSnapshot1.getKey());
                        getSingleMsg(dataSnapshot1.getKey());
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("ChildF","Removed");
                mList.clear();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("ChildF","Moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ChildF","Cancel");
            }
        });
    }

    public void getSingleMsg(String id){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Message");
        mDatabase.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.d("AllMsgS","found");
                    Log.d("AllMsg",dataSnapshot.getValue()+"");
                    Message message = dataSnapshot.getValue(Message.class);
                    mList.add(message);
                    if (mList.size()>0){
                        listView.setVisibility(View.VISIBLE);
                        noMsg.setVisibility(View.GONE);
                    }else {
                        listView.setVisibility(View.GONE);
                        noMsg.setVisibility(View.VISIBLE);
                    }
                    chatAdapter = new ChatAdapter(ChatActivity.this,mList);
                    listView.setAdapter(chatAdapter);
                    //chatAdapter.notifyDataSetChanged();


                }else {
                    Log.d("AllMsgS","notfound");
                    listView.setVisibility(View.GONE);
                    noMsg.setVisibility(View.VISIBLE);;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("AllMsgA","notfound");
            }
        });
    }

    public void Send(String messageText, String recipientId, String senderId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Message");
        String message_id = mDatabase.push().getKey();
        Message msg = new Message(messageText,recipientId,senderId,new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
        mDatabase.child(message_id).setValue(msg, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    Log.d("Messagesend","send");
                    AddToConversation(databaseReference.getKey());
                }else {
                    Log.d("Messagesend","not send");
                }
            }
        });

    }

    public void AddToConversation(String message_id){

        Log.d("Messagesend","call");

        /*[post_id]
        [provider_user_id]
                [message_id]: true (edited)*/
            Map<String,Object> params = new HashMap<>();
            params.put(message_id,true);

            String finalUserId = "";

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Conversation");
            if (from.equalsIgnoreCase("provider")) {
                finalUserId = UserID;
            }else if (from.equalsIgnoreCase("requester")){
                finalUserId = postCreatorID;
            }

            mDatabase.child(postId).child(finalUserId).updateChildren(params, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError==null){
                        Log.d("Conversesend","send");
                    }else {
                        Log.d("Conversesend","not send");
                    }
                }
            });


    }


    public void SendbySender() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Message");
        String message_id = mDatabase.push().getKey();
        Message msg = new Message("i m sender",UserID,"fZsA5ucxvHaQ9QTPMY9rXgLAbz63",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
        mDatabase.child(message_id).setValue(msg, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    Log.d("Messagesend","send");
                    AddToConversationSender(databaseReference.getKey());
                }else {
                    Log.d("Messagesend","not send");
                }
            }
        });

    }

    public void AddToConversationSender(String message_id){

        Log.d("Messagesend","call");
        Map<String,Object> params = new HashMap<>();
        params.put(message_id,true);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Conversation");

        mDatabase.child(postId).updateChildren(params, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!=null){
                    Log.d("Conversesend","send");
                }else {
                    Log.d("Conversesend","not send");
                }
            }
        });


    }


}

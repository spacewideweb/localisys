package atpl.cc.localisys.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import atpl.cc.localisys.R;

import atpl.cc.localisys.activities.SkillsActivity;


    public class MySkillRecyclerAdapter extends RecyclerView.Adapter<MySkillRecyclerAdapter.ViewHolder> {

        ArrayList<String> arr;
        Activity context;



        public MySkillRecyclerAdapter(Activity c, ArrayList<String> arrayList) {
            this.arr = arrayList;
            this.context = c;
        }

        @Override
        public MySkillRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recycler_layout, parent, false);
            return new MySkillRecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MySkillRecyclerAdapter.ViewHolder holder, final int position) {

            holder.myTextView.setText(arr.get(position));
            holder.myTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((SkillsActivity)context).add_add(arr.get(position));
                    arr.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arr.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            Button myTextView;
            public ViewHolder(View itemView) {
                super(itemView);
                myTextView = (Button) itemView.findViewById(R.id.flex_button);
            }

        }
    }
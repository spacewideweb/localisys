package atpl.cc.localisys.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import atpl.cc.localisys.R;
import atpl.cc.localisys.activities.InterestActivity;

/**
 * Created by user5 on 29/6/17.
 */

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.Holder>{

    Activity context;
    ArrayList<String> list;

    public InterestAdapter(Activity context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_round,null);
        return new InterestAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (position%2==0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 30, 0, 0);
            holder.itemView.setLayoutParams(params);
        }else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 30);
            holder.itemView.setLayoutParams(params);
        }
        holder.tv1text.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InterestActivity)context).add(list.get(position));
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tv1text;
        public Holder(View itemView) {
            super(itemView);
            tv1text = (TextView)itemView.findViewById(R.id.tv1text);
        }
    }
}

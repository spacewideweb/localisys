package atpl.cc.localisys.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tubb.smrv.SwipeHorizontalMenuLayout;

import atpl.cc.localisys.R;
import atpl.cc.localisys.Constants;

/**
 * Created by user5 on 7/6/17.
 */

public class MyAdapter extends BaseAdapter {

    static final int VIEW_TYPE_ENABLE = 0;
    static final int VIEW_TYPE_DISABLE = 1;
    Context mContext;
    int aData = 1;
    Fragment fragment;

    public MyAdapter(Context mContext, Fragment fragment) {
        this.mContext = mContext;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return 3;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_simple, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        viewHolder.btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // must close normal

            }
        });
        viewHolder.btn_hideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
                    if (aData == 0) {
                        viewHolder.txtFirstDescription.setMaxLines(2);
                        viewHolder.lin_bottom.setVisibility(View.GONE);
                        viewHolder.lin_head.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));
                        viewHolder.lin_bottom.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));

                        viewHolder.img_up.setImageResource(R.drawable.dropdown_arrow_white);
                        viewHolder.tv_sh.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));

                        viewHolder.tv_sh.setText("More");
                        viewHolder.btn_hideshow.setBackgroundResource(R.drawable.button_style);
                        //btnShowHide.setBackgroundResource(R.drawable.arr_down);
                        aData = 1;
                    } else {
                        viewHolder.txtFirstDescription.setMaxLines(40);
                        viewHolder.lin_bottom.setVisibility(View.VISIBLE);
                        viewHolder.lin_head.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.bg_send, null));
                        viewHolder.lin_bottom.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.fade_blue, null));
                        viewHolder.img_up.setImageResource(R.drawable.up_arrow_blue);
                        viewHolder.tv_sh.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.text_color_blue, null));
                        viewHolder.tv_sh.setText("Hide");
                        viewHolder.btn_hideshow.setBackgroundResource(R.drawable.edit_style_switch);
                        //btnShowHide.setBackgroundResource(R.drawable.arr_up);
                        aData = 0;
                    }
                }
            }
        });
        viewHolder.img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.btn_contract.setVisibility(View.VISIBLE);
                viewHolder.btn_accept.setVisibility(View.GONE);
                viewHolder.btn_reject.setVisibility(View.GONE);
            }
        });
        viewHolder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.btn_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.showingDialog(mContext);

             // ((SendProposalFragmentP)fragment).show_dailog(mContext);
            }
        });




        boolean swipeEnable = swipeEnableByViewType(getItemViewType(position));
        //viewHolder.tvSwipeEnable.setText(swipeEnable ? "swipe on" : "swipe off");
        viewHolder.sml.setSwipeEnable(swipeEnable);
        return convertView;
    }



    boolean swipeEnableByViewType(int viewType) {
        if (viewType == VIEW_TYPE_ENABLE)
            return true;
        else
            return viewType != VIEW_TYPE_DISABLE;
    }

    class ViewHolder {
        View btOpen;
        View btDelete;
        View btn_hideshow;
        SwipeHorizontalMenuLayout sml;
        TextView txtFirstDescription;
        View lin_bottom;
        View lin_head;
        TextView tv_sh;
        ImageView img_up;
        ImageView img_send;
        Button btn_contract,btn_reject,btn_accept;

        ViewHolder(View itemView) {
            btn_hideshow = itemView.findViewById(R.id.btn_hideshow);
            btOpen = itemView.findViewById(R.id.btOpen);
            btDelete = itemView.findViewById(R.id.btDelete);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml1);
            txtFirstDescription = (TextView)itemView.findViewById(R.id.txtFirstDescription);
            lin_bottom = itemView.findViewById(R.id.lin_bottom);
            lin_head = itemView.findViewById(R.id.lin_head);
            tv_sh = (TextView)itemView.findViewById(R.id.tv_sh);
            img_up = (ImageView) itemView.findViewById(R.id.img_up);
            img_send = (ImageView)itemView.findViewById(R.id.img_sendbtn);
            btn_contract = (Button)itemView.findViewById(R.id.btn_contract);
            btn_reject = (Button)itemView.findViewById(R.id.btn_reject);
            btn_accept = (Button)itemView.findViewById(R.id.btn_accept);

        }
    }
}

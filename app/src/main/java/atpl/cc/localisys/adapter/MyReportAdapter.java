package atpl.cc.localisys.activities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import atpl.cc.localisys.R;

/**
 * Created by android on 20/7/17.
 */

public class MyReportAdapter extends BaseAdapter {
    Context c;
    String[] cat;
    public MyReportAdapter(Context context, String[] filterCat) {
        this.c = context;
        this.cat = filterCat;
    }

    public MyReportAdapter() {
    }

    @Override
    public int getCount() {
        return cat.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater =LayoutInflater.from(c);
        convertView=layoutInflater.inflate(R.layout.reason_layout,null);

        TextView txt_report = (TextView) convertView.findViewById(R.id.reason_text);
        txt_report.setText(cat[position]);
        return convertView;
    }
}

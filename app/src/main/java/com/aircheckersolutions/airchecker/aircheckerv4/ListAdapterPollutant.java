package com.aircheckersolutions.airchecker.aircheckerv4;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListAdapterPollutant extends BaseAdapter{

    Context context;
    Pollutant[] data;
    private static LayoutInflater inflater = null;

    public ListAdapterPollutant(Context context, Pollutant[] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderPollutant holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.listitem_pollutant, null);
            holder = new ViewHolderPollutant();
            holder.txtname = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtvalue = (TextView) convertView.findViewById(R.id.txt_currentValue);
            holder.txtmax = (TextView) convertView.findViewById(R.id.txt_maxValue);
            holder.prgbar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.myView = (View) convertView.findViewById(R.id.myRectangleView);
            holder.imgview = (ImageView) convertView.findViewById(R.id.iv_item);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolderPollutant) convertView.getTag();
        }

        TextView text = (TextView) convertView.findViewById(R.id.txt_currentValue);
        holder.txtname.setText(data[position].name);
        holder.txtvalue.setText(Float.toString(data[position].currentValue));
        String pollutantType = (String) holder.txtname.getText();
        Drawable background;
        GradientDrawable gradientDrawable;

        background = holder.myView.getBackground();
        gradientDrawable = (GradientDrawable) background;

        switch (data[position].status){
            case 1:
                gradientDrawable.setColor(ContextCompat.getColor(context,R.color.aqiGood));
                break;
            case 2:
                gradientDrawable.setColor(ContextCompat.getColor(context,R.color.aqiMedium));
                break;
            case 3:
                gradientDrawable.setColor(ContextCompat.getColor(context,R.color.aqiBad));
                break;
            case 4:
                gradientDrawable.setColor(ContextCompat.getColor(context,R.color.aqiHazard));
                break;
        }

        holder.prgbar.setMax((int)data[position].max);
        holder.prgbar.setProgress((int)data[position].currentValue);
        holder.imgview.setImageResource(data[position].resID);
        holder.txtmax.setText("Max : " + Float.toString(data[position].max) + " µg/m³");
        //convertView.setBackgroundResource(R.drawable.smiley_happy);



        return convertView;
    }

}

class ViewHolderPollutant{
    View myView;
    ImageView imgview;
    TextView txtname;
    TextView txtvalue;
    TextView txtmax;
    ProgressBar prgbar;
}

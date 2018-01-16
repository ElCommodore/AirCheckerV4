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

public class ListAdapterPollen extends BaseAdapter{

    Context context;
    Pollen[] data;
    private static LayoutInflater inflater = null;

    public ListAdapterPollen(Context context, Pollen[] data) {
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
        ViewHolderPollen holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.listitem_pollen, null);
            holder = new ViewHolderPollen();
            holder.txtname = (TextView) convertView.findViewById(R.id.txt_name_Pollen);
            holder.txtvalue = (TextView) convertView.findViewById(R.id.txt_currentValue_Pollen);
            holder.prgbar = (ProgressBar) convertView.findViewById(R.id.progressBar_Pollen);
            holder.myView = (View) convertView.findViewById(R.id.myRectangleView_Pollen);
            holder.imgview = (ImageView) convertView.findViewById(R.id.iv_item_Pollen);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolderPollen) convertView.getTag();
        }

        Drawable background;
        GradientDrawable gradientDrawable;

        background = holder.myView.getBackground();
        gradientDrawable = (GradientDrawable) background;

        switch (data[position].status){
            case 0:
                gradientDrawable.setColor(ContextCompat.getColor(context,R.color.aqiGood));
                break;
            case 30:
                gradientDrawable.setColor(ContextCompat.getColor(context,R.color.aqiMedium));
                break;
            case 60:
                gradientDrawable.setColor(ContextCompat.getColor(context,R.color.aqiBad));
                break;
            case 90:
                gradientDrawable.setColor(ContextCompat.getColor(context,R.color.aqiHazard));
                break;
        }

        holder.txtname.setText(data[position].name);
        holder.txtvalue.setText(data[position].currentValue);
        holder.prgbar.setMax(90);
        holder.prgbar.setProgress(data[position].status);
        holder.imgview.setImageResource(data[position].resID);
        //convertView.setBackgroundResource(R.drawable.smiley_happy);
        return convertView;
    }

}

class ViewHolderPollen{
    View myView;
    ImageView imgview;
    TextView txtname;
    TextView txtvalue;
    ProgressBar prgbar;
}

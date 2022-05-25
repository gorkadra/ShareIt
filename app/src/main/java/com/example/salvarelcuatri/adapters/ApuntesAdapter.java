package com.example.salvarelcuatri.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.salvarelcuatri.R;
import com.example.salvarelcuatri.objetos.Apuntes;

import java.util.ArrayList;

public class ApuntesAdapter extends ArrayAdapter<Apuntes> {
    Activity activity;
    int layoutResourceId;
    ArrayList<Apuntes> data=new ArrayList<Apuntes>();
    Apuntes apuntes;

    public ApuntesAdapter(Activity activity, int layoutResourceId, ArrayList<Apuntes> data) {
        super(activity, layoutResourceId, data);
        this.activity=activity;
        this.layoutResourceId=layoutResourceId;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        apunteHolder holder=null;
        if(row==null)
        {
            LayoutInflater inflater=LayoutInflater.from(activity);
            row=inflater.inflate(layoutResourceId,parent,false);
            holder=new apunteHolder();
            holder.textViewName= (TextView) row.findViewById(R.id.textViewName);
            holder.textViewEmail= (TextView) row.findViewById(R.id.textViewEmail);
            holder.textViewDesc= (TextView) row.findViewById(R.id.textViewDescricion);
            holder.textViewDegree= (TextView) row.findViewById(R.id.textViewDegree);
            holder.textViewAsignatura= (TextView) row.findViewById(R.id.textViewAsignatura);

            //holder.textViewUrl= (TextView) row.findViewById(R.id.textViewUrl);
            row.setTag(holder);
        }
        else
        {
            holder= (apunteHolder) row.getTag();
        }

        apuntes = data.get(position);
        holder.textViewName.setText(apuntes.getTitulo());
        holder.textViewEmail.setText(apuntes.getEmail());
        holder.textViewDesc.setText(apuntes.getDescripcion());
        holder.textViewDegree.setText(apuntes.getDegree());
        holder.textViewAsignatura.setText(apuntes.getAsignatura());
        return row;
    }

    class apunteHolder
    {
        TextView textViewName, textViewEmail, textViewDesc, textViewDegree, textViewAsignatura,textViewUrl;
    }
}

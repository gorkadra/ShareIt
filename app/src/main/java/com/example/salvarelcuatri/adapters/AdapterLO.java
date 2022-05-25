package com.example.salvarelcuatri.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.salvarelcuatri.R;
import com.example.salvarelcuatri.objetos.LostOBJ;
import com.example.salvarelcuatri.objetos.VolleySingleton;

import java.util.List;


public class AdapterLO extends BaseAdapter {

    private List<LostOBJ> objetos;
    Context contexto;
    LayoutInflater inflater;
    public AdapterLO(Context contexto, List<LostOBJ> obj) {
        this.contexto = contexto;
        this.inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.objetos = obj;
    }

    @Override
    public int getCount() {
        return objetos.size();
    }

    @Override
    public LostOBJ getItem(int i) {
        return objetos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.lo_item ,null);
        TextView tvNombre= (TextView) view.findViewById(R.id.nombreObj);
        TextView tvDesc=(TextView) view.findViewById(R.id.descObj);
        TextView tvCorreo= (TextView) view.findViewById(R.id.correo);
        ImageView ivFoto= (ImageView) view.findViewById(R.id.idImagen);
        tvNombre.setText(objetos.get(i).getNombreLO());
        tvCorreo.setText(objetos.get(i).getCorreoLO());
        tvDesc.setText(objetos.get(i).getDescLO());

        if (objetos.get(i).getRutaImagenLO()!=null){
            //
            cargarImagenWebService(objetos.get(i).getRutaImagenLO(), view);
        }else{
            ivFoto.setImageResource(R.drawable.ic_sum);
        }

        return view;
    }

    private void cargarImagenWebService(String rutaImagen, View view) {



        String urlImagen=rutaImagen;
        urlImagen= urlImagen.replace("\\/","/");
        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ImageView ivFoto= (ImageView) view.findViewById(R.id.idImagen);
                //ivFoto.setImageResource(R.drawable.ic_sum);
                ivFoto.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Url consultada: ", error.toString());
            }
        });
        //request.add(imageRequest);
        VolleySingleton.getIntanciaVolley(contexto).addToRequestQueue(imageRequest);
    }

}
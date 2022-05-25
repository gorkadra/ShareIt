package com.example.salvarelcuatri.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.salvarelcuatri.R;
import com.example.salvarelcuatri.objetos.VolleySingleton;
import com.google.android.material.navigation.NavigationView;

public class MenuDelDiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_menudeldia);
        cargarImagenWebService(getCurrentFocus());

        navigationDrawer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.MainMenuActivity.class);
            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.nav_activity_menudeldia);
        navigationDrawer();
        //cargarImagenWebService();
    }

    private void cargarImagenWebService(View view) {

        Context contexto;
        contexto = getApplicationContext();

        String urlImagen="https://firebasestorage.googleapis.com/v0/b/dasupverse.appspot.com/o/Menu%2Fmenu-del-dia.jpg?alt=media&token=4f256ca6-758e-4fa3-8dfe-26be49d53de9";
        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ImageView ivFoto= (ImageView) findViewById(R.id.imageView);
                ivFoto.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(contexto.getApplicationContext(),"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
            }
        });
        //request.add(imageRequest);
        VolleySingleton.getIntanciaVolley(contexto).addToRequestQueue(imageRequest);
    }

    private void navigationDrawer() {
        //Navigation drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_menu) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.MainMenuActivity.class);
                    startActivity(intent);
                } if (item.getItemId() == R.id.nav_apuntes) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.ApuntesActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_lost) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.LostObjectsActivitylv.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_menudeldia) {
                    //finish();
                    //Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.MenuDelDiaActivity.class);
                    //startActivity(intent);
                } else if (item.getItemId() == R.id.nav_logout) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.LoginActivity.class);
                    startActivity(intent);
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}

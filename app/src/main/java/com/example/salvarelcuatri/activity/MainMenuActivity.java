package com.example.salvarelcuatri.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.salvarelcuatri.R;
import com.example.salvarelcuatri.objetos.VolleySingleton;
import com.google.android.material.navigation.NavigationView;

public class MainMenuActivity extends AppCompatActivity {

    ImageButton btnOP, btnMenu, btnTeam, btnapuntes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_main_menu);

        btnOP = findViewById(R.id.ibop);
        btnMenu= findViewById(R.id.ibmenu);
        //btnTeam = findViewById(R.id.ibsumop);
        btnapuntes = findViewById(R.id.ibap);

        cargarImagenWebService(getCurrentFocus());

        btnOP.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.LostObjectsActivitylv.class);
            startActivity(intent);
        });

        btnMenu.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.MenuDelDiaActivity.class);
            startActivity(intent);
        });

        /*btnTeam.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.SumarObjetoActivity.class);
            startActivity(intent);
        });*/

        btnapuntes.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.ApuntesActivity.class);
            startActivity(intent);
        });

        navigationDrawer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea salir al login?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();

        btnOP.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.LostObjectsActivitylv.class);
            startActivity(intent);
        });

        btnMenu.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.MenuDelDiaActivity.class);
            startActivity(intent);
        });

        /*btnTeam.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.SumarObjetoActivity.class);
            startActivity(intent);
        });*/

        btnapuntes.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.ApuntesActivity.class);
            startActivity(intent);
        });
    }

    private void cargarImagenWebService(View view) {
        String opRuta="https://firebasestorage.googleapis.com/v0/b/dasupverse.appspot.com/o/main_menu%2Fbaulobjetosperdidos.jpg?alt=media&token=720a080f-5216-4dfe-9161-4b7b6f3e9273";

        ImageRequest opRequest=new ImageRequest(opRuta, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                btnOP.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
            }
        });

        String menuRuta="https://firebasestorage.googleapis.com/v0/b/dasupverse.appspot.com/o/main_menu%2Ftenedor%20y%20cuchillo.jpg?alt=media&token=d1b2fe1f-99d3-4389-8707-c73d3a472132";

        ImageRequest menuRequest=new ImageRequest(menuRuta, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                btnMenu.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
            }
        });

        String apuntesRuta="https://firebasestorage.googleapis.com/v0/b/dasupverse.appspot.com/o/main_menu%2Fapuntes.jpg?alt=media&token=110512a8-7c77-4cef-88d1-742dd10ebb70";

        ImageRequest apuntesRequest=new ImageRequest(apuntesRuta, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                btnapuntes.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
            }
        });

        String equipoRuta="https://firebasestorage.googleapis.com/v0/b/dasupverse.appspot.com/o/main_menu%2Fteam.jpg?alt=media&token=b91aed90-dcba-4ce8-9de7-ea4801a083a6";

        ImageRequest equipoRequest=new ImageRequest(equipoRuta, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                //btnTeam.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
            }
        });

        //request.add(imageRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(opRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(menuRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(apuntesRequest);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(equipoRequest);
    }

    private void navigationDrawer() {
        //Navigation drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_menu) {
                    //finish();
                    //Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.MainMenuActivity.class);
                    //startActivity(intent);
                } if (item.getItemId() == R.id.nav_apuntes) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.ApuntesActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_lost) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.LostObjectsActivitylv.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_menudeldia) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.MenuDelDiaActivity.class);
                    startActivity(intent);
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

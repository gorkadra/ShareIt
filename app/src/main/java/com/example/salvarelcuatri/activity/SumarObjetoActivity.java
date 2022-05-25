package com.example.salvarelcuatri.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.salvarelcuatri.R;
import com.example.salvarelcuatri.workers.SumarOP;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SumarObjetoActivity extends AppCompatActivity {

    private TextView nombretv, correotv, desctv;
    private ProgressDialog progreso ;
    private Spinner spin_pe;
    private ImageView imgiv;
    Uri imagenUri = null;
    String imgURL = null;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_sumar_op);

        nombretv = (TextView) findViewById(R.id.editTextTextPersonName);
        correotv = (TextView) findViewById(R.id.editTextTextEmailAddress);
        desctv = (TextView) findViewById(R.id.editTextTextMultiLine);
        spin_pe = (Spinner) findViewById(R.id.spinner);
        imgiv = (ImageView) findViewById(R.id.imageView2);
        bt = (Button) findViewById(R.id.button);

        imgiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirImagen();
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombretv.getText().toString();
                subirImagen(nombre);
            }
        });

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

    private void elegirImagen() {
        if (ContextCompat.checkSelfPermission(SumarObjetoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            seleccionImagenGaleria();
        } else {
            solicitudPermisosGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void seleccionImagenGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriaActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> galeriaActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Gestionamos el resultado de nuestro intent
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Obtener URI de la imagen
                        Intent data = result.getData();
                        imagenUri = data.getData();

                        // Settear l aimagen seleccionada
                        imgiv.setImageURI(imagenUri);
                    } else {
                        Toast.makeText(SumarObjetoActivity.this, "Cancelado por el Usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<String> solicitudPermisosGaleria =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if (isGranted) {
                    // isGranted --> Es concedido el permiso
                    seleccionImagenGaleria();
                } else {
                    Toast.makeText(SumarObjetoActivity.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    public void sumar(View view, String url) {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Sumando objeto al servidor...");
        progreso.show();

        String nombre = nombretv.getText().toString();
        String correo = correotv.getText().toString();
        String desc = desctv.getText().toString();
        String urlIMG;

        nombre = nombre.replace(" ","%20");
        desc = desc.replace(" ","%20");

        //subirImagen(nombre);
        StringBuilder urlTf = new StringBuilder(url);
        urlTf.deleteCharAt(0);
        urlIMG = urlTf.toString();
        Log.d("url de la imagen ", urlIMG);

        if (correo.equals("")){
            correo="no hay email disponible";
        }

        if (nombre.equals("")){
            nombre="no hay titulo disponible";
        }

        if (desc.equals("")){
            desc="no hay descripcion disponible";
        }

        nombre = nombre.replace(" ","%20");
        correo = correo.replace(" ","%20");
        desc = desc.replace(" ","%20");

        Data datos = new Data.Builder()
                .putString("nombre", nombre)
                .putString("correo", correo)
                .putString("descripcion", desc)
                .putString("ep", "perdido")
                .putString("imgURl", urlIMG)
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(SumarOP.class).setInputData(datos).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
                                if (workInfo.getOutputData().getString("datos").equals("error")) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "No se ha podido insertar", Toast.LENGTH_SHORT);
                                    toast.show();

                                    nombretv.setText("");
                                    correotv.setText("");
                                    desctv.setText("");
                                    //imgiv.setImageBitmap(R.drawable.ic_sum);
                                    progreso.hide();
                                    progreso.dismiss();
                                } else if (workInfo.getOutputData().getString("datos").equals("noInsertada")) { //Si los datos son correctos hacer login
                                    Toast toast = Toast.makeText(getApplicationContext(), "Su objeto no ha podido ser insertado, intentelo más tarde", Toast.LENGTH_SHORT);
                                    toast.show();

                                    nombretv.setText("");
                                    correotv.setText("");
                                    desctv.setText("");
                                    //imgiv.setImageBitmap(R.drawable.ic_sum);
                                    progreso.hide();
                                    progreso.dismiss();
                                } else if (workInfo.getOutputData().getString("datos").equals("insertada")) { //Si los datos son incorrectos no hacer nada
                                    Toast toast = Toast.makeText(getApplicationContext(), "su objeto ha sido insertado con éxito", Toast.LENGTH_SHORT);
                                    toast.show();

                                    nombretv.setText("");
                                    correotv.setText("");
                                    desctv.setText("");
                                    //imgiv.setImageBitmap(R.drawable.ic_sum);
                                    progreso.hide();
                                    progreso.dismiss();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Algo ha ido mal :(", Toast.LENGTH_SHORT);
                                    toast.show();

                                    nombretv.setText("");
                                    correotv.setText("");
                                    desctv.setText("");
                                    //imgiv.setImageBitmap(R.drawable.ic_sum);
                                    progreso.hide();
                                    progreso.dismiss();
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "No esta en comprobacion de datos", Toast.LENGTH_SHORT);
                                toast.show();

                                nombretv.setText("");
                                correotv.setText("");
                                desctv.setText("");
                                //imgiv.setImageBitmap(R.drawable.ic_sum);
                                progreso.hide();
                                progreso.dismiss();
                            }
                        }
                    }
                });

        WorkManager.getInstance(this).enqueue(otwr);
    }

    private void subirImagen(String nombre) {
        String carpetaImagenes = "op/"; // Aqui almacenamos todas las imagenes de los usuarios
        String nombreImagen = carpetaImagenes + nombre;

        if (imagenUri != null) {
            StorageReference reference = FirebaseStorage.getInstance().getReference(nombreImagen);
            reference.putFile(imagenUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            String uriImagen = " " + uriTask.getResult(); // Obtenemos la uri que se ha subido al Storage
                            //Enviamos la uri a la base de datos
                            imgURL = uriImagen;
                            Log.d("url respuesta ", imgURL);
                            sumar(getCurrentFocus(), uriImagen);
                        }
                    });
        } else {
            sumar(getCurrentFocus(), "  ");
        }
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
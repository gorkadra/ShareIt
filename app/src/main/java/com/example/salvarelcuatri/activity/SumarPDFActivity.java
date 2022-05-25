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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.salvarelcuatri.R;
import com.example.salvarelcuatri.adapters.ApuntesAdapter;
import com.example.salvarelcuatri.objetos.Apuntes;
import com.example.salvarelcuatri.workers.SumarOP;
import com.example.salvarelcuatri.workers.SumarPDF;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class SumarPDFActivity extends AppCompatActivity implements View.OnClickListener {

    //Botones
    private ImageView buttonChoose;
    private Button buttonUpload;

    private EditText etmail, ettit, etdesc, etdegre, etasig;

    //Solicitud de imagen
    private int PICK_PDF_REQUEST = 1;
    //Permiso de almacenamiento
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_subir_apuntes);

        requestStoragePermission();

        etmail = (EditText) findViewById(R.id.sa_Email);
        ettit = (EditText) findViewById(R.id.sa_titulo);
        etdesc = (EditText) findViewById(R.id.sa_desc);
        etdegre = (EditText) findViewById(R.id.sa_desgree);
        etasig = (EditText) findViewById(R.id.sa_asignatura);

        buttonUpload = (Button) findViewById(R.id.btsa);
        buttonChoose = (ImageView) findViewById(R.id.ivsa);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        navigationDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_subir_apuntes);
        requestStoragePermission();
        etmail = (EditText) findViewById(R.id.sa_Email);
        ettit = (EditText) findViewById(R.id.sa_titulo);
        etdesc = (EditText) findViewById(R.id.sa_desc);
        etdegre = (EditText) findViewById(R.id.sa_desgree);
        etasig = (EditText) findViewById(R.id.sa_asignatura);
        buttonUpload = (Button) findViewById(R.id.btsa);
        buttonChoose = (ImageView) findViewById(R.id.ivsa);
        buttonUpload.setOnClickListener(this);
        buttonChoose.setOnClickListener(this);
    }

    public void sumarBD(View view, String path) {
        String titulo = ettit.getText().toString();
        String descr = etdesc.getText().toString();
        String email = etmail.getText().toString();
        Log.d("email: ", titulo.toUpperCase(Locale.ROOT));
        String url = path;
        String degree = etdegre.getText().toString();
        String asignatura = etasig.getText().toString();

        if (titulo.equals("")){
            titulo="no hay email disponible";
        }

        if (descr.equals("")){
            descr="no hay titulo disponible";
        }

        if (email.equals("")){
            email="no hay descripcion disponible";
        }

        if (url.equals("")){
            url="http://164.90.169.73:3000/";
        }

        if (degree.equals("")){
            degree="no hay titulo disponible";
        }

        if (asignatura.equals("")){
            asignatura="no hay descripcion disponible";
        }

        titulo = titulo.replace(" ","%20");
        descr = descr.replace(" ","%20");
        email = email.replace(" ","%20");
        degree = degree.replace(" ","%20");
        asignatura = asignatura.replace(" ","%20");

        Log.d("url respuesta ", url);

        Data datos = new Data.Builder()
                .putString("nombre", titulo)
                .putString("correo", email)
                .putString("descripcion", descr)
                .putString("degree", degree)
                .putString("asignatura", asignatura)
                .putString("imgURl", url)
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(SumarPDF.class).setInputData(datos).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
                                if (workInfo.getOutputData().getString("datos").equals("error")) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "No se ha podido insertar", Toast.LENGTH_SHORT);
                                    toast.show();

                                    ettit.setText("");
                                    etdesc.setText("");
                                    etmail.setText("");
                                    etasig.setText("");
                                    etdegre.setText("");
                                } else if (workInfo.getOutputData().getString("datos").equals("noInsertada")) { //Si los datos son correctos hacer login
                                    Toast toast = Toast.makeText(getApplicationContext(), "Su objeto no ha podido ser insertado, intentelo más tarde", Toast.LENGTH_SHORT);
                                    toast.show();

                                    ettit.setText("");
                                    etdesc.setText("");
                                    etmail.setText("");
                                    etasig.setText("");
                                    etdegre.setText("");
                                } else if (workInfo.getOutputData().getString("datos").equals("insertada")) { //Si los datos son incorrectos no hacer nada
                                    Toast toast = Toast.makeText(getApplicationContext(), "su objeto ha sido insertado con éxito", Toast.LENGTH_SHORT);
                                    toast.show();

                                    ettit.setText("");
                                    etdesc.setText("");
                                    etmail.setText("");
                                    etasig.setText("");
                                    etdegre.setText("");
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Algo ha ido mal :(", Toast.LENGTH_SHORT);
                                    toast.show();

                                    ettit.setText("");
                                    etdesc.setText("");
                                    etmail.setText("");
                                    etasig.setText("");
                                    etdegre.setText("");
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "No esta en comprobacion de datos", Toast.LENGTH_SHORT);
                                toast.show();

                                ettit.setText("");
                                etdesc.setText("");
                                etmail.setText("");
                                etasig.setText("");
                                etdegre.setText("");
                            }
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }

    private void seleccionPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        pdfActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> pdfActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Gestionamos el resultado de nuestro intent
                    if (result.getResultCode() == Activity.RESULT_OK){
                        // Obtener URI de la imagen
                        Intent data = result.getData();
                        filePath = data.getData();
                        Log.d("Url consultada: ", filePath.getPath());
                    }else {
                        Toast.makeText(SumarPDFActivity.this, "Cancelado por el Usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void subirPDF(String nombre) {
        String carpetaImagenes = "pdf/"; // Aqui almacenamos todas las imagenes de los usuarios
        String nombreImagen = carpetaImagenes + nombre;

        if (filePath != null) {
            Log.d("path: ", filePath.getPath());
            StorageReference reference = FirebaseStorage.getInstance().getReference(nombreImagen);
            reference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            String uriImagen = " " + uriTask.getResult(); // Obtenemos la uri que se ha subido al Storage
                            //Enviamos la uri a la base de datos
                            //imgURL = uriImagen;
                            Log.d("url respuesta ", uriImagen);
                            sumarBD(getCurrentFocus(), uriImagen);
                        }
                    });
        } else {
            sumarBD(getCurrentFocus(), "");
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Necesito el permiso, por favor.", Toast.LENGTH_LONG).show();
        }

        //Pedir el permiso
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //Se llamará a este método cuando el usuario haga clic en permitir o denegar

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido ahora puedes leer el almacenamiento", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Vaya, acabas de denegar el permiso.", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.ApuntesActivity.class);
            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            seleccionPDF();
        }

        if (v == buttonUpload) {
            subirPDF(ettit.getText().toString());
        }

    }

    private void navigationDrawer() {
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


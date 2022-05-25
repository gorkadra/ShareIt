package com.example.salvarelcuatri.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.salvarelcuatri.InfoDialog;
import com.example.salvarelcuatri.LanguageDialog;
import com.example.salvarelcuatri.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    Button btnInfo, btnLang, btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInfo = findViewById(R.id.btn_info);

        btnInfo.setOnClickListener(view -> {
            DialogFragment infoDialog = new InfoDialog();
            infoDialog.show(getSupportFragmentManager(), "etiqueta");
        });

        btnLang = findViewById(R.id.btn_lang);

        btnLang.setOnClickListener(view -> {
            DialogFragment langDialog = new LanguageDialog();
            langDialog.show(getSupportFragmentManager(), "etiqueta");
        });

        btnStart = findViewById(R.id.btn_start);

        btnStart.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), com.example.salvarelcuatri.activity.LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea salir de ShareIt?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

}
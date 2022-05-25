package com.example.salvarelcuatri;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class LanguageDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.lang_select);
        final int[] currentLangIndex = {getCurrentLanguageIndex()};
        final CharSequence[] opciones = { getResources().getString(R.string.lang_en), getResources().getString(R.string.lang_es), getResources().getString(R.string.lang_eu) };
        builder.setSingleChoiceItems(opciones, currentLangIndex[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                currentLangIndex[0] = i;
            }
        });
        builder.setPositiveButton(R.string.gotit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (currentLangIndex[0] == 0) {
                    setLocale("en");
                } else if (currentLangIndex[0] == 1) {
                    setLocale("es");
                } else if (currentLangIndex[0] == 2) {
                    setLocale("eu");
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }

    private int getCurrentLanguageIndex() {
        String currentLang = Locale.getDefault().getLanguage();
        int langIndex = -1;
        switch (currentLang) {
            case "en":
                langIndex = 0;
                break;
            case "es":
                langIndex = 1;
                break;
            case "eu":
                langIndex = 2;
                break;
        }
        return langIndex;
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = getActivity().getBaseContext().getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        Context context = getActivity().getBaseContext().createConfigurationContext(configuration);
        getActivity().getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }
}

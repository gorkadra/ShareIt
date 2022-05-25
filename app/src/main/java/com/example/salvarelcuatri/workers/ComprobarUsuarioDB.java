package com.example.salvarelcuatri.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ComprobarUsuarioDB extends Worker {
    public ComprobarUsuarioDB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/gdelrio004/WEB/Shareit/ComprobarUsuario.php?";
        HttpURLConnection urlConnection = null;

        //Parametros que se van a enviar en la conexion
        String nombre = getInputData().getString("nombre");
        String parametros = "nombre=" + nombre;
        try {
            //Preparar datos de la conexion
            URL destino = new URL(direccion+"nombre=" + nombre);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Log.d("Url consultada: ", urlConnection.toString());
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();

            //LLamada al servicio web
            int statusCode = urlConnection.getResponseCode();
            Log.d("Url consultada: ", urlConnection.toString());
            if (statusCode == 200) { //Si va bien
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                inputStream.close();

                Data resultados = new Data.Builder()
                        .putString("datos",result)
                        .build();
                return Result.success(resultados);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }
}

package com.example.salvarelcuatri.objetos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class LostOBJ {

    private String correoLO;
    private String nombreLO;
    private String descLO;
    private String datoLO;
    private Bitmap imagenLO;
    private String rutaImagenLO;

    public LostOBJ(String correo, String nombre, String desc, String imagen){
        this.correoLO = correo;
        this.nombreLO = nombre;
        this.descLO = desc;
        this.rutaImagenLO = imagen;
    }
    public LostOBJ(String[] resultEmail, String[] resultTitulo, String[] resultDesc, String[] resultURL){
    }

    public void setDatoLO(String dato) {
        this.datoLO = dato;

        try {
            byte[] byteCode= Base64.decode(dato,Base64.DEFAULT);

            int alto=100;//alto en pixeles
            int ancho=150;//ancho en pixeles

            Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagenLO=Bitmap.createScaledBitmap(foto,alto,ancho,true);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setCorreoLO(String correoLO) {
        this.correoLO = correoLO;
    }

    public void setNombreLO(String nombreLO) {
        this.nombreLO = nombreLO;
    }

    public void setDescLO(String descLO) {
        this.descLO = descLO;
    }

    public void setImagenLO(Bitmap imagenLO) {
        this.imagenLO = imagenLO;
    }

    public void setRutaImagenLO(String rutaImagenLO) {
        this.rutaImagenLO = rutaImagenLO;
    }

    public String getCorreoLO() {
        return correoLO;
    }

    public String getNombreLO() {
        return nombreLO;
    }

    public String getDescLO() {
        return descLO;
    }

    public String getDatoLO() {
        return datoLO;
    }

    public Bitmap getImagenLO() {
        return imagenLO;
    }

    public String getRutaImagenLO() {
        return rutaImagenLO;
    }
}

package com.rysolf.netstore.casos_uso;

import android.app.Activity;
import android.content.Intent;

import com.rysolf.netstore.MapsActivity;


public class CasosUsoActividades {

   protected Activity actividad;

   public CasosUsoActividades(Activity actividad) {
      this.actividad = actividad;
   }



   public void lanzarMapa() {
      actividad.startActivity(
              new Intent(actividad, MapsActivity.class));
   }
}
package com.example.guardardatos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BundleActivity extends Activity {
	
	private String msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*		
  		//Si restaura el estado aquí, debe verificar si el bundle no es null
		if(savedInstanceState != null) {
			//si comenta esta línea, no se recupera el estado guardado
			msg = savedInstanceState.getString("MSG");
		}
		*/
	}

	public void guardaMensaje(View v) {
		EditText et = (EditText) findViewById(R.id.msg);
		msg = et.getText().toString();
	}

	public void recuperaMensaje(View v) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/*
	//estos dos métodos permiten guardar y recuperar el estado temporal de una vista
	//pruebe en un dispositivo físico cambiando la orientación de la pantalla
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);//Siempre llame a super (guarda estado de Views)
		outState.putString("MSG", msg);
	}

	//Solo se invoca si el bundle no es null
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);//Siempre llame a super (restaura estado de Views)
		msg = savedInstanceState.getString("MSG");
	}
	*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
	    switch (item.getItemId()) {
	        case R.id.menu_bundle:
	        	intent = new Intent(this, BundleActivity.class);
	            break;
	        case R.id.menu_prefs:
	        	intent = new Intent(this, PreferencesActivity.class);
	            break;	            
	        case R.id.menu_file:
	        	intent = new Intent(this, FileActivity.class);
	            break;
	    }
    	intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    	startActivity(intent);
    	return true;
	}	
	
}

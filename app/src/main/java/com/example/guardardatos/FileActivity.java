package com.example.guardardatos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);
	}

	public void guardaMensaje(View v) {
		EditText edit = (EditText) findViewById(R.id.msg);
		String msg = edit.getText().toString();
		
		try {
			//interno, privado a la aplicación y de acceso protegido
			File f = new File(getFilesDir(), "myfile.txt");
			
			//externo, privado a la aplicación, pero NO protegido
			//null puede reemplazarse por una constante en: Environment.DIRECTORY_...
//			File f = new File(getExternalFilesDir(null), "myfile.txt");

			//externo y público. Acceso NO protegido
			//el parámetro debe ser una constante en: Environment.DIRECTORY_...
//			File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "myfile.txt");			
			
			Toast.makeText(this, f.getPath(), Toast.LENGTH_LONG).show();
			
	        FileOutputStream fos = new FileOutputStream(f);
			fos.write(msg.getBytes());
			fos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void recuperaMensaje(View v) {
		try {
			//interno, privado a la aplicación y de acceso protegido
			File f = new File(getFilesDir(), "myfile.txt");
			
			//externo, privado a la aplicación, pero NO protegido
			//null puede reemplazarse por una constante en: Environment.DIRECTORY_...
//			File f = new File(getExternalFilesDir(null), "myfile.txt");

			//externo y público. Acceso NO protegido
			//el parámetro debe ser una constante en: Environment.DIRECTORY_...
//			File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "myfile.txt");	

			FileInputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[(int)f.length()];
			fis.read(buffer);
			fis.close();

			Toast.makeText(this, new String(buffer), Toast.LENGTH_LONG).show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
	public void espacioDisco(View view) {
		long tot_interno = getFilesDir().getTotalSpace() / (1024 * 1024);
		long tot_externo = getExternalFilesDir(null).getTotalSpace() / (1024 * 1024);
		long tot_publico = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getTotalSpace() / (1024 * 1024);
		
		long free_interno = getFilesDir().getFreeSpace() / (1024 * 1024);
		long free_externo = getExternalFilesDir(null).getFreeSpace() / (1024 * 1024);
		long free_publico = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getFreeSpace() / (1024 * 1024);
		
		String res = String.format("Interno Total: %d; Libre: %d\nExterno Total: %d; Libre: %d\nPublico Total: %d; Libre: %d",
				tot_interno, tot_externo, tot_publico, free_interno, free_externo, free_publico);
				
		Toast.makeText(this, res, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
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

package com.example.guardardatos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileActivity extends Activity {

	final int GUARDA = 1;
	final int RECUPERA = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);
	}

	public void guardaPrivInt(View v) {
		EditText edit = (EditText) findViewById(R.id.msg);
		String msg = edit.getText().toString();
		try {
			//interno, privado a la aplicación y de acceso protegido
			File f = new File(getFilesDir(), "privInt.txt");
			Toast.makeText(this, f.getPath(), Toast.LENGTH_LONG).show();
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(msg.getBytes());
			fos.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void guardaPrivExt(View v) {
		EditText edit = (EditText) findViewById(R.id.msg);
		String msg = edit.getText().toString();
		try {
			//externo, privado a la aplicación, pero NO protegido
			//null puede reemplazarse por una constante en: Environment.DIRECTORY_...
			File f = new File(getExternalFilesDir(null), "privExt.txt");
			Toast.makeText(this, f.getPath(), Toast.LENGTH_LONG).show();
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(msg.getBytes());
			fos.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void guardaPubExt(View v) {
		EditText edit = (EditText) findViewById(R.id.msg);
		String msg = edit.getText().toString();
		try {
			//externo y público. Acceso NO protegido
			Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);//muestra solo contenido que se puede "abrir" (archivos)
			intent.setType("text/plain");//crea archivo con el tipo MIME indicado
			intent.putExtra(Intent.EXTRA_TITLE, "pubExt.txt");//esto lo puede cambiar el usuario en el cuadro de diálogo
			startActivityForResult(intent, GUARDA);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void recuperaPrivInt(View v) {
		EditText edit = (EditText) findViewById(R.id.msg);
		String msg = edit.getText().toString();
		try {
			//interno, privado a la aplicación y de acceso protegido
			File f = new File(getFilesDir(), "privInt.txt");
			FileInputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[(int)f.length()];
			fis.read(buffer);
			fis.close();
			Toast.makeText(this, new String(buffer), Toast.LENGTH_LONG).show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void recuperaPrivExt(View v) {
		EditText edit = (EditText) findViewById(R.id.msg);
		String msg = edit.getText().toString();
		try {
			//externo, privado a la aplicación, pero NO protegido
			//null puede reemplazarse por una constante en: Environment.DIRECTORY_...
			File f = new File(getExternalFilesDir(null), "privExt.txt");
			FileInputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[(int)f.length()];
			fis.read(buffer);
			fis.close();
			Toast.makeText(this, new String(buffer), Toast.LENGTH_LONG).show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void recuperaPubExt(View v) {
		EditText edit = (EditText) findViewById(R.id.msg);
		String msg = edit.getText().toString();
		try {
			//externo y público. Acceso NO protegido
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);//muestra solo contenido que se puede "abrir" (archivos)
			intent.setType("text/plain");//muestra solo archivos con el tipo MIME indicado
			startActivityForResult(intent, RECUPERA);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == Activity.RESULT_CANCELED) {
			Log.e("RESULT_CANCELED", "El usuario canceló: NO otorga permiso");
			return;
		}
		if(requestCode == GUARDA) {
			try {
				EditText edit = (EditText) findViewById(R.id.msg);
				String msg = edit.getText().toString();

				FileDescriptor f = getContentResolver().openFileDescriptor(data.getData(), "w").getFileDescriptor();
				PrintWriter pw = new PrintWriter(new FileOutputStream(f), true);
				pw.println(msg);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(requestCode == RECUPERA) {
		try {
			String msg = "";
			FileDescriptor f = getContentResolver().openFileDescriptor(data.getData(), "r").getFileDescriptor();
			Scanner scan = new Scanner(new FileInputStream(f));
			while (scan.hasNextLine()) {
				msg += scan.nextLine();
			}
			scan.close();
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

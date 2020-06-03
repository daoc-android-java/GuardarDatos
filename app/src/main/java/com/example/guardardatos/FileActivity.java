package com.example.guardardatos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);
		pidePermiso();
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
			File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "pubExt.txt");
			Toast.makeText(this, f.getPath(), Toast.LENGTH_LONG).show();
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(msg.getBytes());
			fos.close();
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
			File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "pubExt.txt");
			FileInputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[(int)f.length()];
			fis.read(buffer);
			fis.close();
			Toast.makeText(this, new String(buffer), Toast.LENGTH_LONG).show();
		}catch(Exception e) {
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

	private void pidePermiso() {
		int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
			//OK. Ya tiene el permiso
		} else if(permissionCheck == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},1);
			//Dependerá... si el usuario da o no el permiso
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode == 1) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "Permiso concedido!!!", Toast.LENGTH_LONG).show();
				//OK. El usuario SÍ dio el permiso
			} else {
				Toast.makeText(this, "Permiso negado XXX", Toast.LENGTH_LONG).show();
				//NOK. El usuario NO dio el permiso
			}
		} else {
			Toast.makeText(this, "Pedido desconocido", Toast.LENGTH_LONG).show();
		}
	}

}
package dordonez.guardardatos;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void guardaMensaje(View v) {
        EditText edit = (EditText) findViewById(R.id.msg);
        String msg = edit.getText().toString();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("MSG", msg);
        editor.commit();

        //sharedPref.edit().putString("MSG", msg).commit();
    }

    public void recuperaMensaje(View v) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String guardado = sharedPref.getString("MSG", "No hay mensaje todavia");
        Toast.makeText(this, guardado, Toast.LENGTH_LONG).show();
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
}
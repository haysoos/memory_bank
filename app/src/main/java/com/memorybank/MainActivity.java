package com.memorybank;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.memorybank.database.MemoriesDatabase;
import com.memorybank.managers.MemoryLocationManager;
import com.memorybank.model.Memory;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";
    private TextView mSaveButton;
    private EditText mMemoryValueEditText;
    private TextView mMemoriesListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDatabase();
        initLocationManager();
        setContentView(R.layout.activity_main);

        mMemoryValueEditText = (EditText) findViewById(R.id.etMemoryValue);
        mMemoriesListTextView = (TextView) findViewById(R.id.tvMemoriesList);
        mSaveButton = (TextView) findViewById(R.id.tvSave);

        initListeners();
    }

    @Override
    protected void onStart() {
        MemoryLocationManager.getInstance().startListeningForUpdates();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MemoryLocationManager.getInstance().stopListeningForUpdates();
    }

    private void initLocationManager() {
        MemoryLocationManager.init(this);
    }

    private void initListeners() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = mMemoryValueEditText.getText().toString();
                Location location = MemoryLocationManager.getInstance().getLocation();
                Memory memory = null;
                if (location != null) {
                    memory = new Memory(System.currentTimeMillis(), location.getLatitude(), location.getLongitude(), value);
                } else {
                    memory = new Memory(System.currentTimeMillis(), 0, 0, value);
                }

                MemoriesDatabase.getInstance().saveMemory(memory);
                mMemoryValueEditText.setText("");
                Toast.makeText(MainActivity.this, "Saved memory to database", Toast.LENGTH_SHORT).show();
            }
        });

        mMemoriesListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MemoriesListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void initDatabase() {
        MemoriesDatabase.initialize(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.memorybank.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.memorybank.R;
import com.memorybank.database.MemoriesDatabase;
import com.memorybank.managers.MemoryLocationManager;
import com.memorybank.model.Memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";
    private TextView mSaveButton;
    private EditText mMemoryValueEditText;
    private TextView mMemoriesListTextView;
    private TextView mTagsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDatabase();
        initLocationManager();
        setContentView(R.layout.activity_main);

        mMemoryValueEditText = (EditText) findViewById(R.id.etMemoryValue);
        mMemoriesListTextView = (TextView) findViewById(R.id.tvMemoriesList);
        mSaveButton = (TextView) findViewById(R.id.tvSave);
        mTagsTextView = (TextView) findViewById(R.id.tvTags);

        initListeners();
        MemoryLocationManager.getInstance().startListeningForUpdates();
    }

    @Override
    protected void onStart() {
        //MemoryLocationManager.getInstance().startListeningForUpdates();
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

        mTagsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TagsActivity.class);
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

        switch (id) {
            case R.id.action_settings:

                return true;
            case R.id.action_save_db:
                try {
                    writeToSD();
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getBaseContext(), "Saved database to sdcard", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        String DB_PATH = this.getDatabasePath("memories").toString();
        String backupDBPath = "memories.db";
        File currentDB = new File(DB_PATH);
        File backupDB = new File(sd, backupDBPath);

        if (currentDB.exists()) {
            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        } else {
            Log.e(TAG, "Current db does not exist\n" + currentDB.getAbsoluteFile() + "\n" + backupDB.getAbsoluteFile());
        }
    }

}

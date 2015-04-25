package com.memorybank.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.memorybank.R;
import com.memorybank.adapters.MemoriesAdapter;
import com.memorybank.database.MemoriesDatabase;
import com.memorybank.managers.MemoryLocationManager;
import com.memorybank.model.Memory;
import com.memorybank.utils.IOUtils;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";
    private TextView mSaveButton;
    private EditText mMemoryValueEditText;
    private TextView mMemoriesListTextView;
    private TextView mTagsTextView;
    private ViewGroup mViewGroupPlaceholder;
    private View mInsertMemoryView;
    private EditText mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewGroupPlaceholder = (ViewGroup) findViewById(R.id.viewGroupPlaceHolder);
        mInsertMemoryView = getLayoutInflater().inflate(R.layout.view_insert_memory, mViewGroupPlaceholder, false);

        mViewGroupPlaceholder.addView(mInsertMemoryView);

        mMemoryValueEditText = (EditText) findViewById(R.id.etMemoryValue);
        mMemoriesListTextView = (TextView) findViewById(R.id.tvMemoriesList);
        mSaveButton = (TextView) findViewById(R.id.tvSave);
        mTagsTextView = (TextView) findViewById(R.id.tvTags);
        mSearchEditText = (EditText) findViewById(R.id.etMemorySearch);

        initListeners();
        MemoryLocationManager.getInstance().startListeningForUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MemoryLocationManager.getInstance().stopListeningForUpdates();
    }

    private void initListeners() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = mMemoryValueEditText.getText().toString();
                if (value == null || value.isEmpty()) {
                    return;
                }

                Location location = MemoryLocationManager.getInstance().getLocation();
                Memory memory;
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

        mSearchEditText.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = mSearchEditText.getRight()
                            - mSearchEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        mSearchEditText.setText("");
                        return false;
                    }
                }

                return false;
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewGroupPlaceholder.removeAllViews();

                if (s.toString().isEmpty()) {
                    mViewGroupPlaceholder.addView(mInsertMemoryView);
                    return;
                }

                ListView lvSearchResults = new ListView(MainActivity.this);
                lvSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, TagsActivity.class);
                        intent.putExtra(TagsActivity.EXTRA_MEMORY_ID, id);
                        MainActivity.this.startActivity(intent);
                    }
                });

                String searchQuery;
                if (mSearchEditText != null && mSearchEditText.getText() != null) {
                    searchQuery = mSearchEditText.getText().toString();
                } else {
                    return;
                }

                MemoriesAdapter adapter = new MemoriesAdapter(MainActivity.this, MemoriesDatabase.getInstance().searchMemories(searchQuery));
                lvSearchResults.setAdapter(adapter);
                mViewGroupPlaceholder.addView(lvSearchResults);
            }
        });
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
                    IOUtils.writeToSD(this);
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getBaseContext(), "Saved database to sdcard", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_use_backup_db:
                try {
                    IOUtils.copySDcardDatabase(this);
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getBaseContext(), "Using backup database from sdcard", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

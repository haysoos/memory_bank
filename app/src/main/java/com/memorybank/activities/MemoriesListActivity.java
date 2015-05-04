package com.memorybank.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.memorybank.R;
import com.memorybank.adapters.MemoriesAdapter;
import com.memorybank.database.MemoriesDatabase;
import com.memorybank.utils.IOUtils;
import com.memorybank.views.InsertMemoryView;
import com.memorybank.views.InsertMemoryView.OnCancelClickListener;
import com.memorybank.views.InsertMemoryView.OnSaveClickListener;

import java.io.IOException;


public class MemoriesListActivity extends ActionBarActivity {

    private ListView mMemoriesListView;
    private MemoriesAdapter mMemoriesAdapter;
    private ImageButton mFloatingActionButton;
    private ViewGroup mViewGroupPlaceholder;
    private InsertMemoryView mInsertMemoryView;
    private SearchView mMemorySearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories_list);
        mViewGroupPlaceholder = (ViewGroup) findViewById(R.id.viewGroupPlaceHolder);
        mInsertMemoryView = new InsertMemoryView(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_color)));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.primary_color_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.primary_color));
        }

        mFloatingActionButton = (ImageButton) findViewById(R.id.ibAddNewMemory);
        mFloatingActionButton.bringToFront();

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewGroupPlaceholder.removeAllViews();
                mViewGroupPlaceholder.addView(mInsertMemoryView);
                ObjectAnimator.ofFloat(mFloatingActionButton, View.ALPHA, 0.0f).setDuration(500).start();
                ObjectAnimator.ofFloat(mMemorySearchView, View.ALPHA, 0.0f).setDuration(500).start();
            }
        });

        mMemoriesListView = new ListView(this);
        mMemoriesAdapter = new MemoriesAdapter(this, MemoriesDatabase.getInstance().getMemories());
        mMemoriesListView.setAdapter(mMemoriesAdapter);
        mViewGroupPlaceholder.addView(mMemoriesListView);

        mMemoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MemoriesListActivity.this, TagsActivity.class);
                intent.putExtra(TagsActivity.EXTRA_MEMORY_ID, id);
                MemoriesListActivity.this.startActivity(intent);
            }
        });

        mMemoriesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MemoriesListActivity.this, EditMemoryActivity.class);
                intent.putExtra(TagsActivity.EXTRA_MEMORY_ID, id);
                MemoriesListActivity.this.startActivity(intent);
                return true;
            }
        });

        mInsertMemoryView.setOnSaveClickListener(new OnSaveClickListener() {

            @Override
            public void onSaveClick() {
                mViewGroupPlaceholder.removeAllViews();
                mViewGroupPlaceholder.addView(mMemoriesListView);
                mMemoriesAdapter.changeCursor(MemoriesDatabase.getInstance().getMemories());
                ObjectAnimator.ofFloat(mFloatingActionButton, View.ALPHA, 1.0f).setDuration(500).start();
                ObjectAnimator.ofFloat(mMemorySearchView, View.ALPHA, 1.0f).setDuration(500).start();
            }
        });

        mInsertMemoryView.setOnCancelClickListener(new OnCancelClickListener() {
            @Override
            public void onCancelClick() {
                mViewGroupPlaceholder.removeAllViews();
                mViewGroupPlaceholder.addView(mMemoriesListView);
                ObjectAnimator.ofFloat(mFloatingActionButton, View.ALPHA, 1.0f).setDuration(500).start();
                ObjectAnimator.ofFloat(mMemorySearchView, View.ALPHA, 1.0f).setDuration(500).start();
            }
        });

        mMemorySearchView = (SearchView) findViewById(R.id.svSearchMemories);
        mMemorySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                Cursor cursor;
                if (searchQuery == null || searchQuery.isEmpty()) {
                    cursor = MemoriesDatabase.getInstance().getMemories();
                } else {
                    cursor = MemoriesDatabase.getInstance().searchMemories(searchQuery);
                }
                mMemoriesAdapter.changeCursor(cursor);
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mMemoriesAdapter.changeCursor(MemoriesDatabase.getInstance().getMemories());
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
                mMemoriesAdapter.changeCursor(MemoriesDatabase.getInstance().getMemories());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

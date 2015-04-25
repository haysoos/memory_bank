package com.memorybank.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.memorybank.R;
import com.memorybank.adapters.MemoriesAdapter;
import com.memorybank.database.MemoriesDatabase;


public class MemoriesListActivity extends ActionBarActivity {

    private ListView mMemoriesListView;
    private MemoriesAdapter mMemoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories_list);

        mMemoriesListView = (ListView) findViewById(R.id.lvMemories);
        mMemoriesAdapter = new MemoriesAdapter(this, MemoriesDatabase.getInstance().getMemories());
        mMemoriesListView.setAdapter(mMemoriesAdapter);

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        mMemoriesAdapter.changeCursor(MemoriesDatabase.getInstance().getMemories());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memories_list, menu);
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

package com.memorybank.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.memorybank.R;
import com.memorybank.adapters.TagsAdapter;
import com.memorybank.database.MemoriesDatabase;
import com.memorybank.model.MemoryTag;

public class TagsActivity extends ActionBarActivity {

    public static final String EXTRA_MEMORY_ID = "extra_memory_id";
    public static final int DEFAULT_VALUE = -1;
    private static final String TAG = "TagsActivity";
    private ListView mTagsListView;
    private TagsAdapter mTagsAdapter;
    private ImageButton mAddNewTagImageButton;
    private boolean isSelectionMode = false;
    private long mMemoryId = DEFAULT_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_color)));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.primary_color_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.primary_color));
        }

        mTagsListView = (ListView) findViewById(R.id.lvTags);
        mTagsAdapter = new TagsAdapter(this);
        mTagsListView.setAdapter(mTagsAdapter);
        mAddNewTagImageButton = (ImageButton) findViewById(R.id.ibAddNewTagButton);
        mAddNewTagImageButton.bringToFront();

        Intent intent = getIntent();
        if (intent != null) {
            mMemoryId = intent.getLongExtra(EXTRA_MEMORY_ID, DEFAULT_VALUE);

            if (mMemoryId > DEFAULT_VALUE) {
                isSelectionMode = true;
                Cursor selectedTagsCursor = MemoriesDatabase.getInstance().getMatchingTagsIds(mMemoryId);
                mTagsAdapter.setSelectedTags(selectedTagsCursor);

                mTagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mTagsAdapter.onItemClick(view, id);
                    }
                });
                mAddNewTagImageButton.setVisibility(View.GONE);
                return;
            }
        }

        mTagsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MemoryTag tag = mTagsAdapter.getMemoryTag(position);
                Intent intent = new Intent(TagsActivity.this, EditTagActivity.class);
                intent.putExtra(EditTagActivity.EXTRA_TAG_NAME, tag.getName());
                intent.putExtra(EditTagActivity.EXTRA_TAG_DESCRIPTION, tag.getDescription());
                intent.putExtra(EditTagActivity.EXTRA_TAG_IS_PRIVATE, tag.isPrivate());
                intent.putExtra(EditTagActivity.EXTRA_TAG_TIMESTAMP, tag.getTimestamp());
                intent.putExtra(EditTagActivity.EXTRA_TAG_ID, tag.getId());
                TagsActivity.this.startActivity(intent);
                return true;
            }
        });
        
        mAddNewTagImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagsActivity.this, EditTagActivity.class);
                TagsActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTagsAdapter.changeCursor(MemoriesDatabase.getInstance().getTags());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tags, menu);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        if (isSelectionMode) {
            MemoriesDatabase.getInstance().saveTagsForMemory(mMemoryId, mTagsAdapter.getSelectedTags(), mTagsAdapter.getUnselectedTags());
        }
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

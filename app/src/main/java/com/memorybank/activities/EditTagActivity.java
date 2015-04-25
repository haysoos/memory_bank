package com.memorybank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.memorybank.R;
import com.memorybank.database.MemoriesDatabase;
import com.memorybank.model.MemoryTag;

public class EditTagActivity extends ActionBarActivity {

    private static final int DEFAULT_VALUE = -1;
    public static final String EXTRA_TAG_ID = "EXTRA_TAG_ID";
    public static final String EXTRA_TAG_NAME = "EXTRA_TAG_NAME";
    public static final String EXTRA_TAG_DESCRIPTION = "EXTRA_TAG_DESCRIPTION";
    public static final String EXTRA_TAG_TIMESTAMP = "EXTRA_TAG_TIMESTAMP";
    public static final String EXTRA_TAG_IS_PRIVATE = "EXTRA_TAG_IS_PRIVATE";
    private TextView tvSaveButton;
    private EditText etTagName;
    private EditText etTagDescription;
    private Switch mPrivateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tag);

        etTagName = (EditText) findViewById(R.id.etTagName);
        etTagDescription = (EditText) findViewById(R.id.etDescription);
        tvSaveButton = (TextView) findViewById(R.id.tvSave);
        mPrivateSwitch = (Switch) findViewById(R.id.privateSwitch);

        Intent intent = getIntent();
        MemoryTag memoryTag = null;
        if (intent != null) {
            long id = intent.getLongExtra(EXTRA_TAG_ID, MemoryTag.DEFAULT_ID);
            if (id > MemoryTag.DEFAULT_ID) {
                String name = intent.getStringExtra(EXTRA_TAG_NAME);
                String description = intent.getStringExtra(EXTRA_TAG_DESCRIPTION);
                long timestamp = intent.getLongExtra(EXTRA_TAG_TIMESTAMP, DEFAULT_VALUE);
                boolean privacyTag = intent.getBooleanExtra(EXTRA_TAG_IS_PRIVATE, false);
                memoryTag = new MemoryTag(id, name, description, privacyTag, timestamp);

                etTagName.setText(memoryTag.getName());
                etTagDescription.setText(memoryTag.getDescription());
                mPrivateSwitch.setChecked(memoryTag.isPrivate());
            }
        }

        final MemoryTag finalMemoryTag = memoryTag;
        tvSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemoryTag tag = finalMemoryTag;
                String tagName = etTagName.getText().toString();
                String tagDescription = etTagDescription.getText().toString();
                boolean isTagPrivate = mPrivateSwitch.isChecked();

                if (tag != null) {
                    tag.setName(tagName);
                    tag.setDescription(tagDescription);
                    tag.setIsPrivate(isTagPrivate);
                    MemoriesDatabase.getInstance().updateTag(tag);
                } else {
                    long timestamp = System.currentTimeMillis();
                    tag = new MemoryTag(tagName, tagDescription, isTagPrivate, timestamp);
                    MemoriesDatabase.getInstance().saveTag(tag);
                }

                EditTagActivity.this.finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_tag, menu);
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

package com.memorybank.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.memorybank.R;
import com.memorybank.database.MemoriesDatabase;
import com.memorybank.model.MemoryTag;


public class CreateTagActivity extends ActionBarActivity {

    private TextView tvCreateTextView;
    private EditText etTagName;
    private EditText etTagDescription;
    private Switch mPrivateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tag);

        etTagName = (EditText) findViewById(R.id.etTagName);
        etTagDescription = (EditText) findViewById(R.id.etDescription);
        tvCreateTextView = (TextView) findViewById(R.id.tvCreate);
        mPrivateSwitch = (Switch) findViewById(R.id.privateSwitch);

        tvCreateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timestamp = System.currentTimeMillis();
                String tagName = etTagName.getText().toString();
                String tagDescription = etTagDescription.getText().toString();
                boolean isTagPrivate = mPrivateSwitch.isChecked();

                MemoryTag tag = new MemoryTag(tagName, tagDescription, isTagPrivate, timestamp);
                MemoriesDatabase.getInstance().saveTag(tag);
                CreateTagActivity.this.finish();
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

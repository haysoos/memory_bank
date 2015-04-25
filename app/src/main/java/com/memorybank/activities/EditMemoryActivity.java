package com.memorybank.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.memorybank.R;
import com.memorybank.database.MemoriesDatabase;
import com.memorybank.model.Memory;

/**
 * Created by Jesus Medrano on 4/24/15.
 */
public class EditMemoryActivity extends Activity {

    public static final String EXTRA_MEMORY_ID = "extra_memory_id";
    private static final long DEFAULT_VALUE = -1;
    private long mMemoryId;
    private EditText mMemoryEditText;
    private TextView mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memory);
        mMemoryEditText = (EditText) findViewById(R.id.etMemoryValue);
        mSaveButton = (TextView) findViewById(R.id.tvSaveButton);

        Intent intent = getIntent();
        if (intent != null) {
            mMemoryId = intent.getLongExtra(EXTRA_MEMORY_ID, DEFAULT_VALUE);
            final Memory memory = MemoriesDatabase.getInstance().getMemory(mMemoryId);
            mMemoryEditText.setText(memory.getValue());

            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memory.setValue(mMemoryEditText.getText().toString());
                    MemoriesDatabase.getInstance().updateMemory(memory);
                    EditMemoryActivity.this.finish();
                }
            });
        }

    }

}

package com.memorybank.views;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.memorybank.R;
import com.memorybank.activities.TagsActivity;
import com.memorybank.database.MemoriesDatabase;
import com.memorybank.managers.MemoryLocationManager;
import com.memorybank.model.Memory;

/**
 * Created by Jesus Medrano on 5/3/15.
 */
public class InsertMemoryView extends RelativeLayout {


    private EditText mMemoryValueEditText;
    private TextView mSaveButton;
    private TextView mCancelButton;
    private TextView mTagsTextView;
    private OnSaveClickListener mOnSaveClickListener;
    private OnCancelClickListener mOnCancelClickListener;

    public interface OnSaveClickListener {
        public void onSaveClick();
    }

    public interface OnCancelClickListener {
        public void onCancelClick();
    }

    public InsertMemoryView(Context context) {
        super(context);
        init(context);
    }

    public InsertMemoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InsertMemoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_insert_memory, this, true);
        mMemoryValueEditText = (EditText) findViewById(R.id.etMemoryValue);
        mSaveButton = (TextView) findViewById(R.id.tvSave);
        mTagsTextView = (TextView) findViewById(R.id.tvTags);
        mCancelButton = (TextView) findViewById(R.id.tvCancel);

        initListeners();
    }

    private void initListeners() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                persistMemory();
            }
        });

        mTagsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TagsActivity.class);
                getContext().startActivity(intent);
            }
        });

        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMemoryValueEditText.setText("");
                hideKeyboard();

                if (mOnCancelClickListener != null) {
                    mOnCancelClickListener.onCancelClick();
                }
            }
        });

        mMemoryValueEditText.setImeActionLabel("Save", KeyEvent.KEYCODE_ENTER);
        mMemoryValueEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    hideKeyboard();
                    persistMemory();
                    return true;
                }
                return false;
            }
        });
    }

    private void persistMemory() {
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
        Toast.makeText(getContext(), "Saved memory to database", Toast.LENGTH_SHORT).show();
        if (mOnSaveClickListener != null) {
            mOnSaveClickListener.onSaveClick();
        }
    }

    public void setOnSaveClickListener(OnSaveClickListener listener) {
        mOnSaveClickListener = listener;
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        mOnCancelClickListener = listener;
    }

    private void hideKeyboard() {
        InputMethodManager mImMan = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mImMan.hideSoftInputFromWindow(mMemoryValueEditText.getWindowToken(), 0);
    }

}

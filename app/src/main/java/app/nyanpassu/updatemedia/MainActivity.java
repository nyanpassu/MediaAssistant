package app.nyanpassu.updatemedia;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 丢猫 on 2015/1/18.
 */
public class MainActivity extends Activity {

    @InjectView(R.id.path_text)
    EditText mEditText;

    @InjectView(R.id.update)
    Button mButton;

    int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = mEditText.getText().toString();
                File file = new File(path);
                if (file.exists()) {
                    if (file.isDirectory()) {
                        File[] files = file.listFiles();
                        int count = files.length;
                        String[] filenames = new String[count];
                        for (int i = 0; i < count; i++) {
                            filenames[i] = files[i].getAbsolutePath();
                        }
                        update(filenames);
                    } else {
                        update(file.getAbsolutePath());
                    }
                } else {
                    mEditText.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    private void update(String[] filenames) {
        mButton.setEnabled(false);
        mCount = filenames.length;
        mButton.setText("0/"+mCount);
        MediaScannerConnection.scanFile(this,
                filenames, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    int count = 0;

                    public void onScanCompleted(String path, Uri uri) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                mButton.setText(count + "/" + mCount);
                                if (count == mCount) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mButton.setEnabled(true);
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
    }

    private void update(String filename) {
        mButton.setEnabled(false);
        mCount = 1;
        mButton.setText("0/1");
        MediaScannerConnection.scanFile(this,
                new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    int count = 0;

                    public void onScanCompleted(String path, Uri uri) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count ++;
                                mButton.setText(count+"/"+mCount);

                                if (count == mCount) {
                                    mButton.setEnabled(true);
                                }
                            }
                        });
                    }
                });

    }
}

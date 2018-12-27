package com.skripsi.androidstegano;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.skripsi.algo.BouncyCastleProvider_AES_CBC;
import com.skripsi.explorer.Filechoice;
import com.skripsi.explorer.FilechoiceSisip;
import com.skripsi.stegano.SteganoInformation;
import com.skripsi.stegano.steganografi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DekripActivity extends AppCompatActivity {

    private static final int REQUEST_PATH = 1;

    EditText txtBrowse;
    Button decodeFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dekrip_fragment);
        final BouncyCastleProvider_AES_CBC AESncrypt = new BouncyCastleProvider_AES_CBC();

        txtBrowse = (EditText) findViewById(R.id.txtBrowse);
        decodeFile = (Button) findViewById(R.id.btndecode);
        txtBrowse.setEnabled(false);

        ImageButton load = (ImageButton) findViewById(R.id.btncari);
        load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getfile();
                // TODO Auto-generated method stub


            }
        });

        decodeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SteganoInformation steg;
                    steganografi stego = null;

                    File fileDecode = new File(txtBrowse.getText().toString());
                    File dir = new File("/sdcard" + File.separator + "SKRIPSI_DECRYPT");

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    steg = new SteganoInformation(fileDecode);

                    stego.decodeFile(steg, false);


                    InputStream inputStream = new FileInputStream(stego.getFile());
                    String file_out = fileDecode.getName().substring(0, fileDecode.getName().length() - 4);
                    OutputStream outputStream = new FileOutputStream(dir + File.separator + file_out);
//
                    AESncrypt.CBCDecrypt(inputStream, outputStream);

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Berhasil Dekrip data !!");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();

                } catch (Exception e) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText(e.getMessage());

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();

                    e.printStackTrace();
                }
            }
        });


//        set nama bar layout
//   getSupportActionBar().setTitle("Encrypt File");
//        ini buat warna bar
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A4C639")));

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new EnkripActivity.PlaceholderFragment()).commit();
//        }
    }

    public void getfile() {
        Intent intent1 = new Intent(this, Filechoice.class);
        startActivityForResult(intent1, REQUEST_PATH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode);
        if (requestCode == REQUEST_PATH) {
            if (resultCode == RESULT_OK) {

                if (data.getExtras().containsKey("GetFileName")) {
                    String namafile = data.getStringExtra("GetFileName");
                    txtBrowse.setText(namafile);
                    MediaController mediaController = new MediaController(this);

                    Uri uri = Uri.parse(txtBrowse.getText().toString());
                    VideoView simpleVideoView = (VideoView) findViewById(R.id.videoView); // initiate a video view
                    simpleVideoView.setVideoURI(uri);
                    simpleVideoView.setMediaController(mediaController);
                    simpleVideoView.start();
                    simpleVideoView.canPause();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.decryp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dekrip_fragment,
                    container, false);
            return rootView;
        }
    }

}

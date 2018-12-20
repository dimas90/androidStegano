package com.skripsi.androidstegano;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;

import com.skripsi.algo.BouncyCastleProvider_AES_CBC;
import com.skripsi.explorer.Filechoice;
import com.skripsi.explorer.FilechoiceSisip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class EnkripActivity extends AppCompatActivity {

    private static final int REQUEST_PATH = 1;
    String ambil;
    String sisip;
    File filedata;
    File filesisip;
    String namafile;
    String namasisip;
    EditText editfile;
    EditText editpassword;
    EditText edithasil;
    EditText editsisip;
    Button Encode;
    Button Encrypt;
    private ImageButton load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final BouncyCastleProvider_AES_CBC AESncrypt = new BouncyCastleProvider_AES_CBC();
        setContentView(R.layout.enkrip_fragment);

        editfile = (EditText) findViewById(R.id.filedapat);
        edithasil = (EditText) findViewById(R.id.hasilenkrip);
        editsisip = (EditText) findViewById(R.id.filetambah);
        Encode = (Button) findViewById(R.id.btnencode);
        Encrypt = (Button) findViewById(R.id.btnenkrip);
        Encode.setEnabled(false);

        editfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getfile();
                // TODO Auto-generated method stub


            }
        });

        editsisip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getsisip();
                // TODO Auto-generated method stub


            }
        });

        Encrypt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              try {
                  AESncrypt.InitCiphers();
                  File f = new File("/sdcard"+File.separator+"data"+File.separator+"output.enc");
                  File dir = new File("/sdcard"+File.separator+"data");
                    System.out.println(f.getAbsolutePath());

                      if(!dir.exists()){
                          dir.mkdirs();
                      }

                      f.createNewFile();

                  OutputStream OS = new FileOutputStream(
                          new File("/sdcard"+File.separator+"data"+File.separator+"output.enc"));


                 InputStream IS = new FileInputStream(new File(editfile.getText().toString()));

                 AESncrypt.CBCEncrypt(IS,OS);


              }catch (Exception e){
                  e.printStackTrace();
              }
            }
        });

        ImageButton load = (ImageButton) findViewById(R.id.btncari);
        load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getfile();
                // TODO Auto-generated method stub


            }
        });
        ImageButton sisip = (ImageButton) findViewById(R.id.btnsisip);
        sisip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getsisip();
                // TODO Auto-generated method stub


            }
        });
    }

    public void getfile() {
        Intent intent1 = new Intent(this, Filechoice.class);
        startActivityForResult(intent1, REQUEST_PATH);
    }

    public void getsisip() {
        Intent intent2 = new Intent(this, FilechoiceSisip.class);
        startActivityForResult(intent2, REQUEST_PATH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode);
        if (requestCode == REQUEST_PATH) {
            if (resultCode == RESULT_OK) {

                if (data.getExtras().containsKey("GetFileName")) {
                    namafile = data.getStringExtra("GetFileName");
                    editfile.setText(namafile);
                    editfile.setEnabled(false);
                } else if (data.getExtras().containsKey("GetFileName1")) {
                    namasisip = data.getStringExtra("GetFileName1");
                    editsisip.setText(namasisip);
                    editsisip.setEnabled(false);


                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.encryp, menu);
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
            View rootView = inflater.inflate(R.layout.enkrip_fragment,
                    container, false);
            return rootView;
        }
    }

}

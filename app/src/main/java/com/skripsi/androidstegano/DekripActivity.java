package com.skripsi.androidstegano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.skripsi.explorer.Filechoice;
import com.skripsi.explorer.FilechoiceSisip;

import java.io.File;

public class DekripActivity extends AppCompatActivity{

    String ambil;
    String sisip;
    private static final int REQUEST_PATH = 1;
    File filedata;
    File filesisip;
    String namafile;
    String namasisip;
    EditText editfile;
    EditText edithasil;
    EditText editsisip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dekrip_fragment);

            editfile = (EditText) findViewById(R.id.filedapat);
            editsisip = (EditText) findViewById(R.id.fileenkrip);
            edithasil = (EditText) findViewById(R.id.filehasil);

        ImageButton load = (ImageButton) findViewById(R.id.btncari);
        load.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getfile();
                // TODO Auto-generated method stub


            }
        });
        ImageButton sisip = (ImageButton) findViewById(R.id.btnfe);
        sisip.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getsisip();
                // TODO Auto-generated method stub


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
        Intent intent1 = new Intent (this, Filechoice.class);
        startActivityForResult(intent1, REQUEST_PATH);
    }
    public void getsisip() {
        Intent intent2 = new Intent (this, FilechoiceSisip.class);
        startActivityForResult(intent2, REQUEST_PATH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode);
        if (requestCode == REQUEST_PATH){
            if(resultCode == RESULT_OK){

                if(data.getExtras().containsKey("GetFileName")){
                    namafile = data.getStringExtra("GetFileName");
                    editfile.setText(namafile);
                }else if(data.getExtras().containsKey("GetFileName1")) {
                    namasisip = data.getStringExtra("GetFileName1");
                    editsisip.setText(namasisip);


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

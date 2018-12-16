package com.skripsi.androidstegano;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    static private Context context;
    public static Context getAppContext() {
        return MainActivity.context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    public void onclick(View view) {
        if (view.getId() == R.id.enkrip)
            startActivity(new Intent(getApplicationContext(),EnkripActivity.class));
        if (view.getId() == R.id.enkrip1)
            startActivity(new Intent(getApplicationContext(),EnkripActivity.class));
        if (view.getId() == R.id.dekrip)
            startActivity(new Intent(getApplicationContext(),DekripActivity.class));
        if (view.getId() == R.id.dekrip1)
            startActivity(new Intent(getApplicationContext(),DekripActivity.class));
        if (view.getId() == R.id.bantuan)
            startActivity(new Intent(getApplicationContext(),HelpActivity.class));
        if (view.getId() == R.id.bantuan1)
            startActivity(new Intent(getApplicationContext(),HelpActivity.class));
        if (view.getId() == R.id.profile)
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        if (view.getId() == R.id.profile1)
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        if (view.getId() == R.id.keluar)
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setMessage("Apakah Anda Benar-Benar ingin keluar?");
            ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // closeDialog.this.finish();
                    Intent exit = new Intent(Intent.ACTION_MAIN);
                    exit.addCategory(Intent.CATEGORY_HOME);
                    exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(exit);
                }
            });
            ad.setNegativeButton("Tidak",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            ad.show();
        }
        if (view.getId() == R.id.keluar1)
        //startActivity(new Intent(getApplicationContext(),exit.class));
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setMessage("Apakah Anda Benar-Benar ingin keluar?");
            ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // closeDialog.this.finish();
                    Intent exit = new Intent(Intent.ACTION_MAIN);
                    exit.addCategory(Intent.CATEGORY_HOME);
                    exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(exit);
                }
            });
            ad.setNegativeButton("Tidak",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            ad.show();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            keluarDenganBackKey();
            //moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void keluarDenganBackKey() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("Apakah Anda Benar-Benar ingin keluar?");
        ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // closeDialog.this.finish();
                Intent exit = new Intent(Intent.ACTION_MAIN);
                exit.addCategory(Intent.CATEGORY_HOME);
                exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exit);
            }
        });
        ad.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        ad.show();

    }
}

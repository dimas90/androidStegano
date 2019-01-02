//package com.skripsi.explorer;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//
//import com.skripsi.androidstegano.R;
//
//public class FileExplorer extends Activity {
//
//    private static final int REQUEST_PATH = 1;
//
//    String curFileName;
//
//    EditText edittext;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fileexplorer);
//        edittext = (EditText) findViewById(R.id.editText);
//    }
//
//    public void getfile(View view) {
//        Intent intent1 = new Intent(this, Filechoice.class);
////    	ArrayList<String> extensions = new ArrayList<String>();
////    	extensions.add(".pdf");
////    	extensions.add(".xls");
////    	extensions.add(".xlsx");
////    	intent1.putStringArrayListExtra("filterFileExtension", extensions);
//        startActivityForResult(intent1, REQUEST_PATH);
//    }
//
//    // Listen for results.
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // See which child activity is calling us back.
//        if (requestCode == REQUEST_PATH) {
//            if (resultCode == RESULT_OK) {
//                curFileName = data.getStringExtra("GetFileName");
//                edittext.setText(curFileName);
//            }
//        }
//    }
//}

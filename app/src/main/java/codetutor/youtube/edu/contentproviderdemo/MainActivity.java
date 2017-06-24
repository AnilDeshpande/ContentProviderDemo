package codetutor.youtube.edu.contentproviderdemo;


import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import android.support.design.widget.Snackbar;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = "ContentProviderDemo";

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS=20;

    private boolean firstTimeLoaded=false;

    private TextView textViewQueryResult;
    private Button buttonLoadData;

    private String[] mColumnProjection = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.HAS_PHONE_NUMBER};

    private String mSelectionCluse = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = ?";

    private String[] mSelectionArguments = new String[]{"Ajay"};

    private String mOrderBy = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewQueryResult = (TextView) findViewById(R.id.textViewQueryResult);

        buttonLoadData = (Button) findViewById(R.id.buttonLoadData);
        buttonLoadData.setOnClickListener(this);

        /*ContentResolver contentResolver=getContentResolver();
        Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                mColumnProjection,
                null,
                null,
                null);

        if(cursor!=null && cursor.getCount()>0){
            StringBuilder stringBuilderQueryResult=new StringBuilder("");
            while (cursor.moveToNext()){
                stringBuilderQueryResult.append(cursor.getString(0)+" , "+cursor.getString(1)+" , "+cursor.getString(2)+"\n");
            }
            textViewQueryResult.setText(stringBuilderQueryResult.toString());
        }else{
            textViewQueryResult.setText("No Contacts in device");
        }*/


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == 1) {
            return new CursorLoader(MainActivity.this, ContactsContract.Contacts.CONTENT_URI,
                    mColumnProjection, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder stringBuilderQueryResult = new StringBuilder("");
            //cursor.moveToFirst();
            while (cursor.moveToNext()) {
                stringBuilderQueryResult.append(cursor.getString(0) + " , " + cursor.getString(1) + " , " + cursor.getString(2) + "\n");
            }
            textViewQueryResult.setText(stringBuilderQueryResult.toString());
        } else {
            textViewQueryResult.setText("No Contacts in device");
        }
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLoadData: if(firstTimeLoaded==false){
                getLoaderManager().initLoader(1, null, this);
                firstTimeLoaded=true;
            }else{
                getLoaderManager().restartLoader(1,null,this);
            }

                break;
            default:
                break;
        }
    }

}

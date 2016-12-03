package codetutor.youtube.edu.contentproviderdemo;


import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "ContentProviderDemo";

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS=20;
    private int MY_PERMISSION_REQUEST_WRITE_CONTACTS=30;

    private boolean firstTimeLoaded=false;

    private TextView textViewQueryResult;
    private Button buttonLoadData, buttonAddContact,buttonRemoveContact,buttonUpdateContact;

    private ContentResolver contentResolver;

    private EditText editTextContactName;
    private CursorLoader mContactsLoader;

    private String[] mColumnProjection = new String[]{
            ContactsContract.Contacts._ID,
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

        editTextContactName=(EditText)findViewById(R.id.editTextContactName);

        buttonLoadData = (Button) findViewById(R.id.buttonLoadData);

        buttonAddContact=(Button)findViewById(R.id.buttonAddContact);
        buttonRemoveContact=(Button)findViewById(R.id.buttonRemoveContact);
        buttonUpdateContact=(Button)findViewById(R.id.buttonUpdateContact);


        buttonLoadData.setOnClickListener(this);

        buttonAddContact.setOnClickListener(this);
        buttonRemoveContact.setOnClickListener(this);
        buttonUpdateContact.setOnClickListener(this);


        contentResolver=getContentResolver();

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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if(i==1){
            return  new CursorLoader(this,ContactsContract.Contacts.CONTENT_URI,mColumnProjection, null,null,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor!=null && cursor.getCount()>0){
            StringBuilder stringBuilderQueryResult=new StringBuilder("");
            while (cursor.moveToNext()){
                stringBuilderQueryResult.append(cursor.getString(0)+" , "+cursor.getString(1)+" , "+cursor.getString(2)+" , "+cursor.getString(3)+"\n");
            }
            textViewQueryResult.setText(stringBuilderQueryResult.toString());
        }else{
            textViewQueryResult.setText("No Contacts in device");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLoadData:loadContacts();
                break;
            case R.id.buttonAddContact: addContact();
                break;
            case R.id.buttonRemoveContact:deleteContact();
                break;
            case R.id.buttonUpdateContact: modifyCotact();
                break;
            default:
                break;
        }
   }

    private void insertContacts(){

       String newName=editTextContactName.getText().toString();
        if(newName!=null && !newName.equals("") && newName.length()!=0){
            ArrayList<ContentProviderOperation> cops=new ArrayList<ContentProviderOperation>();

            cops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,"accountname@gmail.com")
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "com.google")
                    .build());
            cops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, editTextContactName.getText().toString())
                    .build());

            try{
                getContentResolver().applyBatch(ContactsContract.AUTHORITY,cops);
            }catch (Exception exception){
                Log.i(TAG,exception.getMessage());
                Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addContact() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            insertContacts();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Needs Contacts write permission",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS},MY_PERMISSION_REQUEST_WRITE_CONTACTS);
                            }
                        }).show();
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        MY_PERMISSION_REQUEST_WRITE_CONTACTS);
            }
        }
    }


    private void updateContact(){

        String [] updateValue=editTextContactName.getText().toString().split(" ");
        ContentProviderResult[] result=null;

        String targetString=null;
        String newString=null;
        if(updateValue.length==2){
            targetString=updateValue[0];
            newString=updateValue[1];
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                if(newString!=null && !newString.equals("") && newString.length()!=0)
                {
                    String where= ContactsContract.RawContacts._ID + " = ? ";
                    String [] params= new String[] {targetString};
                    ContentResolver contentResolver=getContentResolver();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,newString);
                    // UPDATE <table_name> SET column1 = value1, column2 = value2 where column3 = selection_value
                    contentResolver.update(ContactsContract.RawContacts.CONTENT_URI,contentValues, where,params);
                }
            }
        }
    }

    private void modifyCotact(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            updateContact();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Needs Contacts write permission",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS},MY_PERMISSION_REQUEST_WRITE_CONTACTS);
                            }
                        }).show();
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        MY_PERMISSION_REQUEST_WRITE_CONTACTS);
            }
        }
    }

    private void removeContacts(){
        String newName=editTextContactName.getText().toString();
        if(newName!=null && !newName.equals("") && newName.length()!=0){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                //display_name = '<entered_value>'
                String whereClause=ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY+ " = '"+editTextContactName.getText().toString()+"'";
                //DELETE FROM <table_name> where column1 = selection_value
                getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI,whereClause,null);
            }
        }

    }

    private void deleteContact(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            removeContacts();
        }else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Needs Contacts write permission",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS},MY_PERMISSION_REQUEST_WRITE_CONTACTS);
                            }
                        }).show();
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        MY_PERMISSION_REQUEST_WRITE_CONTACTS);
            }
        }
    }

    private void addContactsViaIntents(){
        String tempContactText=editTextContactName.getText().toString();
        if(tempContactText!=null && !tempContactText.equals("") && tempContactText.length()>0 ){
            Intent intent=new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME,tempContactText);
            startActivity(intent);
        }
    }


    private void loadContacts() {

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            Log.i(TAG,"Permisssion is granted");
            if (firstTimeLoaded == false) {
                getLoaderManager().initLoader(1, null, this);
                firstTimeLoaded = true;
            } else {
                getLoaderManager().restartLoader(1, null, this);
            }
        }else{
            Log.i(TAG,"Permisssion is not granted");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
                Log.i(TAG,"Permisssion is not granted, hence showing rationale");
                Snackbar.make(findViewById(android.R.id.content),"Need permission for loading data",Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        }).show();
            }else {
                Log.i(TAG,"Permisssion being requested for first time");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }
}
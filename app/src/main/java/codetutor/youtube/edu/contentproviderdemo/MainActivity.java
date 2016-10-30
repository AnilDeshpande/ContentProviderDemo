package codetutor.youtube.edu.contentproviderdemo;


import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

    private static final String TAG="ContentProviderDemo";

    private TextView textViewQueryResult;
    private Button buttonLoadData;

    private String [] mColumnProjection=new String[]{
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.HAS_PHONE_NUMBER};

    private String mSelectionCluse=ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+ " = ?";

    private String [] mSelectionArguments = new String [] {"Ajay"};

    private String mOrderBy=ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewQueryResult=(TextView)findViewById(R.id.textViewQueryResult);

        buttonLoadData=(Button)findViewById(R.id.buttonLoadData);
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

        if(id==1){
            return  new CursorLoader(MainActivity.this,ContactsContract.Contacts.CONTENT_URI,
                    mColumnProjection,null,null,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor!=null && cursor.getCount()>0){
            StringBuilder stringBuilderQueryResult=new StringBuilder("");
            while (cursor.moveToNext()){
                stringBuilderQueryResult.append(cursor.getString(0)+" , "+cursor.getString(1)+" , "+cursor.getString(2)+"\n");
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
        switch (view.getId()){
            case R.id.buttonLoadData: getLoaderManager().initLoader(1,null,this);
                break;
            default:break;
        }
    }
}

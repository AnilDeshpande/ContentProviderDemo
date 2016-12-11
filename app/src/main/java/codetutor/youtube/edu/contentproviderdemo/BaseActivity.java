package codetutor.youtube.edu.contentproviderdemo;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anildeshpande on 12/11/16.
 */

public class BaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected int MY_PERMISSIONS_REQUEST_READ_CONTACTS=20;
    protected int MY_PERMISSION_REQUEST_WRITE_CONTACTS=30;
    protected int MY_PHOTO_TAGGING_PERMISSIONS =40;

    protected final String [] permissionsNeededForPhotoTagging =new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION};


    protected void requestRunTimePermissions(final Activity activity, final String [] permissions, final int customPermissionConstant){
        if(permissions.length==1){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[0])){

                Snackbar.make(findViewById(android.R.id.content),"App needs permission to work",Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(activity,permissions,customPermissionConstant);
                            }
                        }).show();
            }else {
                ActivityCompat.requestPermissions(this,new String[]{permissions[0]},customPermissionConstant);
            }
        }else if(permissions.length>1 && customPermissionConstant== MY_PHOTO_TAGGING_PERMISSIONS){
            final List<String> deniedPermissions=new ArrayList<String>();

            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(this,permission)== PackageManager.PERMISSION_DENIED){
                    deniedPermissions.add(permission);
                }
            }
            if(deniedPermissions.size()==1){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,deniedPermissions.get(0))){

                    Snackbar.make(findViewById(android.R.id.content),"App needs permission to work",Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String [] temp=deniedPermissions.toArray(new String[deniedPermissions.size()]);
                                    ActivityCompat.requestPermissions(activity,temp,customPermissionConstant);
                                }
                            }).show();
                }else {

                    String [] temp=deniedPermissions.toArray(new String[deniedPermissions.size()]);
                    ActivityCompat.requestPermissions(activity,temp,customPermissionConstant);
                }
            }else if(deniedPermissions.size()>1){
                final String [] temp=deniedPermissions.toArray(new String[deniedPermissions.size()]);
                if(isFirstTimeAskForPhotoTaggingPermission()){
                    ActivityCompat.requestPermissions(activity,temp,customPermissionConstant);
                }else{
                    Snackbar.make(findViewById(android.R.id.content),"This functionality needs multiple app permissions",Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(activity,temp,customPermissionConstant);
                                }
                            }).show();
                }

            }
        }
    }

    protected boolean isFirstTimeAskForPhotoTaggingPermission(){
        SharedPreferences sharedPreferences=getSharedPreferences("permissionasks",MODE_PRIVATE);
        boolean isFirstTime=sharedPreferences.getBoolean("PHOTO_FIRST_PERMISSION",true);
        if(isFirstTime){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("PHOTO_FIRST_PERMISSION",false);
            editor.commit();
        }
        return isFirstTime;
    }

    protected boolean checkWhetherAllPermissionsPresentForPhotoTagging(){
        for(String permission: permissionsNeededForPhotoTagging){
            if(ActivityCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    protected String[] getDeniedPermissionsAmongPhototaggingPermissions(){
        String [] deniedPermissionsArray;
        final List<String> deniedPermissions=new ArrayList<String>();
        for(String permission: permissionsNeededForPhotoTagging){
            if(ActivityCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(permission);
            }
        }
        deniedPermissionsArray=deniedPermissions.toArray(new String[deniedPermissions.size()]);
        return deniedPermissionsArray;
    }
}

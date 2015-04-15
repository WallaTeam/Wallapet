package akiniyalocts.imgur.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.File;

import akiniyalocts.imgur.imgurmodel.ImageResponse;
import akiniyalocts.imgur.imgurmodel.Upload;
import akiniyalocts.imgur.services.OnImageUploadedListener;
import akiniyalocts.imgur.services.UploadService;
import akiniyalocts.imgur.utils.aLog;

public class MainActivity extends ActionBarActivity implements OnImageUploadedListener{
  public final static String TAG = MainActivity.class.getSimpleName();

  /*
    These annotations are for ButterKnife by Jake Wharton

    https://github.com/JakeWharton/butterknife

   */
  public String imageURL;
  private Upload upload; // Upload object containging image and meta data

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public void uploadImage(File chosenFile){
    /*
      Create the @Upload object
     */
    createUpload(chosenFile);

    /*
      Start upload
     */
    new UploadService(upload, this).execute();
  }

  @Override public void onImageUploaded(ImageResponse response) {
    /*
      Logging the response from the image upload.
     */
    aLog.w(TAG, response.toString());
    imageURL = response.data.link;
  }

  private void createUpload(File image){
    upload = new Upload();

    upload.image = image;
  }
}

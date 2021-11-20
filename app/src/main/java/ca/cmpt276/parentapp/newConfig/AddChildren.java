package ca.cmpt276.parentapp.newConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.ChildManager;


/**Function that allows user to add child**/
public class AddChildren extends AppCompatActivity{
    final ChildManager manager = ChildManager.getInstance();
    ImageView imageView = null;
    String photoPath = null;
    public static final String SHARED_PREFERENCE = "Shared Preference";
    public static final String CHILD_LIST = "Child List";
    private static final int EMPTY_CHILD_LIST = -999;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting up toolbar
        setContentView(R.layout.activity_add_children);
        Toolbar myToolbar = findViewById(R.id.addToolbar);
        setSupportActionBar(myToolbar);
        addImage();
    }

    @Override
    public void onBackPressed() {
        //confirm if user wanted to exit
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.backPressedWarning).setPositiveButton
                (R.string.yes_edit_child, (dialog, which) -> {
                    super.onBackPressed();
                }).setNegativeButton
                    (R.string.no_edit_child, (dialog, which) -> {
                    /*do nothing*/
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //implementation by Dhaval URL: https://github.com/Dhaval2404/ImagePicker
    private void addImage() {
        imageView = findViewById(R.id.addPortrait);
        imageView.setImageResource(R.drawable.add_icon);
        imageView.setOnClickListener(View -> ImagePicker.with(this)
                .cropSquare()
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start());
    }
    //implementation by Dhaval URL: https://github.com/Dhaval2404/ImagePicker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            //Getting URI and converting it to bitmap
            Uri fileUri = data.getData();
            try {
                if(fileUri !=null){
                    photoPath = saveAndReturnPhotoDir(
                            MediaStore.Images.Media.getBitmap(this.getContentResolver() , fileUri), /* obtain captured file**/
                            getNewChildPosition());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            //setting bitmap to imageview and child's portrait variable
            imageView.setImageBitmap(BitmapFactory.decodeFile(photoPath));

            //error handling
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


    }

    String saveAndReturnPhotoDir(Bitmap bitmap,int position) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        String fileName = "portraitChild"+position+time();
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory, fileName + ".jpg");
        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return file.getPath();
    }

    /**ONLY FOR saveAndReturnPhotoDir. NOT TO BE USED USED FOR ANYTHING ELSE**/
    int getNewChildPosition() {
        if(manager.getChildPosition() == EMPTY_CHILD_LIST){
            return 0;
        }
        else {
            return manager.getChildPosition()+1;
        }
    }
    String time() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }

    //configure save button
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.add_toolbar_icons,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save){
            EditText addText = findViewById(R.id.childNameAdd);
            String newName = addText.getText().toString();
            if(!TextUtils.isEmpty(addText.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.confirm_add_child, newName))
                        .setPositiveButton(R.string.yes_add_child, (dialog, which) -> {

                            manager.addChildren(newName,photoPath, this.getResources());
                            saveData();
                            finish();

                        })
                        .setNegativeButton(R.string.no_add_child, (dialog, which) -> {
                        }/*do nothing*/);
                AlertDialog alert = builder.create();
                alert.show();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.empty_value)
                        .setNeutralButton(R.string.OK, (dialog, which) ->{} /*do nothing*/);
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Convert gridManager to json format
        Gson gson = new Gson();
        String json = gson.toJson(manager);

        //Save the json
        editor.putString(CHILD_LIST,json);
        editor.apply();
    }
}


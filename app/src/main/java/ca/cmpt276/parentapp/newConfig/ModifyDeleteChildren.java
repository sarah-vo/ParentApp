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
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;

/**Function that allows user to edit/delete child and their name/picture**/
public class ModifyDeleteChildren extends AppCompatActivity {
    final ChildManager manager = ChildManager.getInstance();
    Child child;
    int position;
    ImageView portraitImageView;
    String photoPath;
    EditText nameEditText;
    public static final String SHARED_PREFERENCE = "Shared Preference";
    public static final String CHILD_LIST = "Child List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting up toolbar
        setContentView(R.layout.activity_modify_delete_children);
        Toolbar myToolbar = findViewById(R.id.modifyToolBar);
        setSupportActionBar(myToolbar);
        fillPositionAndChild();
        fillPortraitAndNameField();
        editImage();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.backPressedWarning)
                .setPositiveButton(R.string.yes_edit_child, (dialog, which) -> {
                    super.onBackPressed();
                })
                .setNegativeButton(R.string.no_edit_child, (dialog, which) -> {
                    /*do nothing*/
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void fillPositionAndChild() {
        Intent intent = getIntent();
        position = intent.getIntExtra("Child Position", -1);
        if (position == -1) {
            throw new IllegalArgumentException("Error in passing child position from configActivity!");
        }
        child = manager.getChild(position);
        Log.d("Child position", String.valueOf(position));
    }

    private void fillPortraitAndNameField() {
        portraitImageView = findViewById(R.id.modifyPortrait);
        if(child.getPortrait() != null){
            portraitImageView.setImageBitmap(child.getPortrait());
        }
        else{
            portraitImageView.setImageResource(R.drawable.add_icon);
        }
        nameEditText = findViewById(R.id.modifyChildName);
        nameEditText.setText(child.getChildName());
    }

    //implementation by Dhaval URL: https://github.com/Dhaval2404/ImagePicker
    /**Allows user to capture/select new image and change the old photo path to a new one**/
    private void editImage() {
        if(child.getPortrait() != null){
            portraitImageView.setImageBitmap(child.getPortrait());
        }
        else{
            portraitImageView.setImageResource(R.drawable.add_icon);
        }
        portraitImageView.setOnClickListener(View -> ImagePicker.with(this)
                .cropSquare()
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
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
                if(  fileUri !=null   ){
                    photoPath = saveAndReturnPhotoDir(
                            MediaStore.Images.Media.getBitmap(this.getContentResolver() , fileUri), /* obtain captured file**/
                            manager.getChildPosition());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            //setting bitmap to imageview and child's portrait variable
            portraitImageView.setImageBitmap(BitmapFactory.decodeFile(photoPath));
            //error handling
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


    }

    String saveAndReturnPhotoDir(Bitmap bitmap, int position){
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

    String time(){
        Date date = new Date();
        return String.valueOf(date.getTime());
    }

    //configure save/delete button
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.modify_toolbar_icons,menu);
        return super.onCreateOptionsMenu(menu);
    }@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //SAVE BUTTON CONFIG
        if (item.getItemId() == R.id.action_save){
            String newName = nameEditText.getText().toString();
            String oldName = child.getChildName();

            if (!TextUtils.isEmpty(nameEditText.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.confirm_edit_child, oldName, newName))
                        .setPositiveButton(R.string.yes_edit_child, (dialog, which) -> {
                            editChildInfo(newName);
                        })
                        .setNegativeButton(R.string.no_edit_child, (dialog, which) -> {
                            /*do nothing*/
                        });

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
        //DELETE BUTTON
        else if(item.getItemId() == R.id.action_delete){
            String name = child.getChildName();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(getString(R.string.confirm_delete_child, name))
                    .setPositiveButton(R.string.yes_delete_child, (dialog, which) -> {
                        deleteChildInfo();
                    })
                    .setNegativeButton(R.string.no_delete_child, (dialog, which) -> {
                        /*do nothing*/
                    });

            AlertDialog alert = builder.create();
            alert.show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteChildInfo() {
        manager.removeChildren(position);
        deletePhoto();
        saveData();
        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }

    private void editChildInfo(String newName) {
        manager.editChildrenName(newName, position);
        saveData();
        manager.editChildrenPortraitPath(photoPath,position);
        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }

    private void deletePhoto() {
        if(child.getPortraitPath() != null) {
            File fdelete = new File(child.getPortraitPath());
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + photoPath);
                } else {
                    System.out.println("file not Deleted :" + photoPath);
                }
            }
        }
    }

    void saveData(){
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
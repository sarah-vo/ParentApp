package ca.cmpt276.parentapp.child_config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;


/**Function that allows user to add child**/
public class AddChildren extends AppCompatActivity{
    final ChildManager manager = ChildManager.getInstance();
    ImageView imageView = null;

    public static final String SHARED_PREFERENCE = "Shared Preference";
    public static final String CHILD_LIST = "Child List";

    Child child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting up toolbar
        setContentView(R.layout.activity_add_children);
        child = new Child();

        addImage();
    }

    @Override
    public void onBackPressed() {
        //confirm if user wanted to exit
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.backPressedWarning).setPositiveButton
                (R.string.yes_edit_child, (dialog, which) -> super.onBackPressed()).setNegativeButton
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
                    child.saveBitmapString(MediaStore.Images.Media.getBitmap(this.getContentResolver() , fileUri));
                    imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver() , fileUri));
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            //error handling
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


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
                            child.setName(newName);
                            manager.addChildren(child);
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


package ca.cmpt276.parentapp.newConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.appcompat.widget.Toolbar;


import com.github.dhaval2404.imagepicker.ImagePicker;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.childManager;

public class modifyDeleteChildren extends AppCompatActivity {
    childManager manager = childManager.getInstance();
    int position;
    Child child;
    ImageView imageview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_delete_children);

        //set up toolbar
        Toolbar myToolbar = findViewById(R.id.modifyToolBar);
        setSupportActionBar(myToolbar);

        //retrieve position from previous activity
        Intent intent = getIntent();
        position = intent.getIntExtra("Child Position",-1);
        if(position == -1){
            throw new IllegalArgumentException("Error in passing child position from configActivity!");
        }
        child = manager.getChild(position);

        changeImage();

    }


    //implementation by Dhaval URL: https://github.com/Dhaval2404/ImagePicker
    private void changeImage() {
        imageview = findViewById(R.id.modifyPortrait);
        if(child.getPortrait() != null){
            imageview.setImageBitmap(child.getPortrait());
        }
        else{
            imageview.setImageResource(R.drawable.add_icon);
        }
        imageview.setOnClickListener(View -> ImagePicker.with(this)
                .cropSquare()
                .start()
        );
    }
    //implementation by Dhaval URL: https://github.com/Dhaval2404/ImagePicker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            //Getting URI and converting it to bitmap
            Uri fileUri = data.getData();
            Bitmap newPortrait = null;
            try {
                if(  fileUri !=null   ){
                    newPortrait = MediaStore.Images.Media.getBitmap(this.getContentResolver() , fileUri);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            //setting bitmap to imageview and child's portrait variable
            child.setPortrait(newPortrait);
            imageview.setImageBitmap(newPortrait);

            //error handling
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
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
            EditText editText = findViewById(R.id.modifyChildName);
            String newName = editText.getText().toString();
            String oldName = child.getName();

            if (!TextUtils.isEmpty(editText.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.confirm_edit_child, oldName, newName))
                        .setPositiveButton(R.string.yes_edit_child, (dialog, which) -> {
                            manager.editChildrenName(newName, position);
                            //Image changed by saveImage() function above
                            Intent intent = new Intent(this, configActivity.class);
                            startActivity(intent);
                        })
                        .setNegativeButton(R.string.no_edit_child, (dialog, which) -> {
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
        //DELETE BUTTON
        else if(item.getItemId() == R.id.action_delete){
            String name = child.getName();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.confirm_delete_child, name))
                    .setPositiveButton(R.string.yes_delete_child, (dialog, which) -> {
                        manager.removeChildren(position);
                        Intent intent = new Intent(this, configActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton(R.string.no_delete_child, (dialog, which) -> {
                    }/*do nothing*/);
            AlertDialog alert = builder.create();
            alert.show();

        }
        return super.onOptionsItemSelected(item);
    }
}
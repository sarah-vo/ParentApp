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
import ca.cmpt276.parentapp.model.childManager;



public class addChildren extends AppCompatActivity {
    childManager manager = childManager.getInstance();
    Bitmap newPortrait = null;
    ImageView imageview = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //setting up toolbar
        setContentView(R.layout.activity_add_children);
        Toolbar myToolbar = findViewById(R.id.addToolbar);
        setSupportActionBar(myToolbar);

        addImage();


    }

    //implementation by Dhaval URL: https://github.com/Dhaval2404/ImagePicker
    private void addImage() {
        imageview = findViewById(R.id.addPortrait);
        imageview.setImageResource(R.drawable.add_icon);
        imageview.setOnClickListener(View -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .start();
        });
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
                    newPortrait = MediaStore.Images.Media.getBitmap(this.getContentResolver() , fileUri);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            //setting bitmap to imageview and child's portrait variable
            imageview.setImageBitmap(newPortrait);

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
    }@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save){
            EditText addText = findViewById(R.id.childNameAdd);
            String newName = addText.getText().toString();
            if(!TextUtils.isEmpty(addText.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.confirm_add_child, newName))
                        .setPositiveButton(R.string.yes_add_child, (dialog, which) -> {
                            manager.addChildren(newName,newPortrait);
                            Intent intent = new Intent(this, configActivity.class);
                            startActivity(intent);;
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
}


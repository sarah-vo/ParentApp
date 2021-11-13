package ca.cmpt276.parentapp.newConfig;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.canhub.cropper.PickImageContract;
import com.canhub.cropper.PickImageContractOptions;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.childManager;
import com.canhub.cropper.*;



public class addChildren extends AppCompatActivity {
    childManager manager = childManager.getInstance();

    private final ActivityResultLauncher<PickImageContractOptions> pickImage =
            registerForActivityResult(new PickImageContract(), presenter::onPickImageResult);

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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.save_icon,menu);
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
                            manager.addChildren(newName);
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

private void addImage() {
        ImageView imageview = findViewById(R.id.addPortrait);
    imageview.setOnClickListener(View ->{
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    });

}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap resultUri = result.getOriginalBitmap();
                ImageView imageView = findViewById(R.id.portrait);
                imageView.setImageBitmap(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}


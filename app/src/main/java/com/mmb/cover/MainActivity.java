package com.mmb.cover;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {

    ImageView imgPic;
    TextView txtMain;
    EditText editText;
    Button btnEdit;
    SeekBar seekBar;
    ConstraintLayout constraintLayout;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraintLayout);
        imgPic = findViewById(R.id.imgPic);
        txtMain = findViewById(R.id.txtMain);
        editText = findViewById(R.id.editText);
        btnEdit = findViewById(R.id.btnEdit);
        seekBar = findViewById(R.id.seekBar);


        if (Build.VERSION.SDK_INT >= 23) {

//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//
//
//            } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//            }

        } //else {
//            Intent intent = new Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, PICK_IMAGE);
//
//        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtMain.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                constraintLayout.setDrawingCacheEnabled(true);

                constraintLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                constraintLayout.layout(0, 0, constraintLayout.getMeasuredWidth(), constraintLayout.getMeasuredHeight());

                constraintLayout.buildDrawingCache(true);
                Bitmap bitmap = Bitmap.createBitmap(constraintLayout.getDrawingCache());
                constraintLayout.setDrawingCacheEnabled(false);

                try {
                    String root = Environment.getExternalStorageDirectory().toString();
                    File mydire = new File(root + "/Download/0instacover");
                    if (!mydire.exists()) {
                        mydire.mkdirs();
                    }

                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "cover" + n + ".jpg";
                    File f = new File(mydire, fname);
                    if (f.exists())
                        f.delete();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        imgPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Crop.pickImage(MainActivity.this);
//
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0)
                    txtMain.setText(editText.getText().toString());
                else
                    txtMain.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(100,155).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            imgPic.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }



//        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
//
//            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
//            Crop.of(data.getData(), destination).asSquare().start(this);
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                imgPic.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else if (requestCode == Crop.REQUEST_CROP) {
//            if (resultCode == RESULT_OK) {
//                imgPic.setImageURI(Crop.getOutput(data));
//            }
//        }

//        if (requestCode == 123) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//
//                        imgPic.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}

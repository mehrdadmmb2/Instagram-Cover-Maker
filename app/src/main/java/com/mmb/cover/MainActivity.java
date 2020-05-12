package com.mmb.cover;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.labters.documentscanner.ImageCropActivity;
import com.labters.documentscanner.helpers.ScannerConstants;

import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView imgPic;
    TextView txtMain;
    EditText editText;
    Button btnEdit;
    SeekBar seekBar;
    ConstraintLayout constraintLayout;
    private Bitmap btimap2;


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

        LoadingDialog.show(this);
        RetrofitRequest.login(new RequestHandler() {
            @Override
            public void onResponse(Call<Object> cal, Response<Object> response) {
                LoadingDialog.dismis();
                LoginModel model = GsonHandler.login(response.body().toString(), "login");

                Log.d("mehrdad", "onResponse: " + model.getToken());
            }

            @Override
            public void onFail(Call<Object> cal, Throwable t) {
                Log.d("mehrdad", "onFail: " + t.getMessage());
                LoadingDialog.dismis();
            }
        });

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
                LoadingDialog.show(MainActivity.this);
                RetrofitRequest.login2(new RequestHandler() {
                    @Override
                    public void onResponse(Call<Object> cal, Response<Object> response) {
                        LoadingDialog.dismis();
                        LoginModel model = GsonHandler.login(response.body().toString(), "login2");
                        Toast.makeText(MainActivity.this, model.getStatus().toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFail(Call<Object> cal, Throwable t) {
                        LoadingDialog.dismis();
                    }
                });

                constraintLayout.setDrawingCacheEnabled(true);

                constraintLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                constraintLayout.layout(0, 0, constraintLayout.getMeasuredWidth(), constraintLayout.getMeasuredHeight());

                constraintLayout.buildDrawingCache(true);
                Bitmap bitmap = Bitmap.createBitmap(constraintLayout.getDrawingCache());
                constraintLayout.setDrawingCacheEnabled(false);

                try {
                    String root = Environment.getExternalStorageDirectory().toString();
                    File mydire = new File(root + "/Download");
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
                Toast.makeText(MainActivity.this, "در فولدر Download ذخیره شد", Toast.LENGTH_LONG).show();

            }
        });

        imgPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {

                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        Crop.pickImage(MainActivity.this);
//                        openCamera();
                        btimap2 = ScannerConstants.selectedImageBitmap;
                        startActivityForResult(new Intent(MainActivity.this, ImageCropActivity.class), 333);
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

                    }
                } else {
                    btimap2 = ScannerConstants.selectedImageBitmap;
                    startActivityForResult(new Intent(MainActivity.this, ImageCropActivity.class), 333);
//                    Crop.pickImage(MainActivity.this);
//                    openCamera();
                }

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

//    public void openCamera() {
//        int REQUEST_CODE = 99;
//        int preference = ScanConstants.OPEN_CAMERA;
//        Intent intent = new Intent(this, ScanActivity.class);
//        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
//        startActivityForResult(intent, REQUEST_CODE);
//    }
//
//    public void openGallery(View v) {
//        int REQUEST_CODE = 99;
//        int preference = ScanConstants.OPEN_MEDIA;
//        Intent intent = new Intent(this, ScanActivity.class);
//        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
//        startActivityForResult(intent, REQUEST_CODE);
//    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(100, 155).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            imgPic.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Crop.pickImage(MainActivity.this);
//                openCamera();
            } else {
                Toast.makeText(this, "لطفا دسرتسی را تایید کنید", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                getContentResolver().delete(uri, null, null);
//
//                imgPic.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

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

        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                        btimap2 = ScannerConstants.selectedImageBitmap;
                        startActivityForResult(new Intent(MainActivity.this, ImageCropActivity.class), 333);
//                        imgPic.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 333 && resultCode == Activity.RESULT_OK) {
            if (ScannerConstants.selectedImageBitmap != null)
                imgPic.setImageBitmap(ScannerConstants.selectedImageBitmap);

        }
    }
}

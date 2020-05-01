package com.maseko.root.absen1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.maseko.root.absen1.SharePreference.SaveSharedPreference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_problem)
    EditText txtProblem;

    @BindView(R.id.imageButton1)
    ImageButton mImgProblem1;

    @BindView(R.id.imageButton2)
    ImageButton mImgProblem2;

    @BindView(R.id.imageButton3)
    ImageButton mImgProblem3;

    @BindView(R.id.button_next)
    Button mBtnNext;

    @BindView(R.id.imageButtonClose1)
    ImageButton mBtnCloseImg1;

    @BindView(R.id.imageButtonClose2)
    ImageButton mBtnCloseImg2;

    @BindView(R.id.imageButtonClose3)
    ImageButton mBtnCloseImg3;

    @BindDrawable(R.drawable.ic_add_blue)
    Drawable icon_add;

    private static final int CHOOSE_GALLERY = 1002;
    private static final int ADD_IMAGE_1 = 0;
    private static final int ADD_IMAGE_2 = 1;
    private static final int ADD_IMAGE_3 = 2;
    private int SAVE_IMAGE = -1;
    private static final String IMAGE_DIRECTORY = "/BugDamartana";
    private Bitmap[] bitmaps = new Bitmap[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
    }

    private void openGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(pickIntent, "Choose from gallery");

        startActivityForResult(chooserIntent, CHOOSE_GALLERY);
    }

    private void refreshButton() {
        int i = 0;
        for (Bitmap photo : bitmaps) {
            ImageButton image = null;
            ImageButton close = null;

            switch (i) {
                case 0:
                    image = mImgProblem1;
                    close = mBtnCloseImg1;
                    break;
                case 1:
                    image = mImgProblem2;
                    close = mBtnCloseImg2;
                    break;
                case 2:
                    image = mImgProblem3;
                    close = mBtnCloseImg3;
            }

            if (image != null && close != null) {
                if (photo != null) {
                    image.setImageBitmap(photo);
                    close.setVisibility(View.VISIBLE);
                } else {
                    image.setImageBitmap(null);
                    image.setImageDrawable(icon_add);
                    close.setVisibility(View.GONE);
                }
            }

            i++;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Uri contentURI = data.getData();
            try {
                Bitmap rBitmap = MediaStore.Images.Media.getBitmap(ContactUsActivity.this.getContentResolver(), contentURI);
                if (SAVE_IMAGE > -1) bitmaps[SAVE_IMAGE] = rBitmap;

                refreshButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(ContactUsActivity.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @OnClick({R.id.imageButton1, R.id.imageButton2, R.id.imageButton3})
    public void LoadPhoto(View button) {
        int id = button.getId();
        switch (id) {
            case R.id.imageButton1:
                SAVE_IMAGE = ADD_IMAGE_1;
                break;
            case R.id.imageButton2:
                SAVE_IMAGE = ADD_IMAGE_2;
                break;
            case R.id.imageButton3:
                SAVE_IMAGE = ADD_IMAGE_3;
        }
        openGallery();
    }

    @OnClick({R.id.imageButtonClose1, R.id.imageButtonClose2, R.id.imageButtonClose3})
    public void RemoveImg(View button) {
        int order = -1;
        int id = button.getId();
        switch (id) {
            case R.id.imageButtonClose1:
                order = 0;
                break;
            case R.id.imageButtonClose2:
                order = 1;
                break;
            case R.id.imageButtonClose3:
                order = 2;
        }

        bitmaps[order] = null;
        refreshButton();
    }

    @OnClick(R.id.button_next)
    public void sendEmail() {
        if (txtProblem.getText().toString().trim().length() >= 10) {
            final Intent emailIntent = new Intent(
                    android.content.Intent.ACTION_SEND_MULTIPLE);

            String address = "ekoaewes2006@gmail.com";
            String subject = "Feedback/Question [Android Apps]";
            String emailText = "\n" + txtProblem.getText() + "\n\n\n" +
                    "--[[Support Info]]--\n" +
                    "NIP: " + SaveSharedPreference.getNipUser(ContactUsActivity.this) + "\n" +
                    "Nama: " + SaveSharedPreference.getNamaUser(ContactUsActivity.this) + "\n" +
                    "Manufacture: " + Build.MANUFACTURER + "\n" +
                    "Model: " + Build.MODEL + "\n" +
                    "Device: " + Build.DEVICE + "\n" +
                    "OS: " + Build.VERSION.RELEASE + "\n" +
                    "Version: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")\n" +

                    emailIntent.setType("message/rfc822");

            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                    new String[]{address});

            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

            ArrayList<Uri> uriList = new ArrayList<>();
            for (Bitmap photo : bitmaps) {
                if (photo != null) {
                    String path = saveImage(photo);
                    // uriList.add(Uri.parse("content://" + path));
                }
            }

            if (uriList.size() > 0) {
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            }

            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailText);

            this.startActivity(Intent.createChooser(emailIntent, "Send via email"));
        } else {
            Toast.makeText(ContactUsActivity.this, "Harap masukan lebih dari 10 huruf!", Toast.LENGTH_SHORT).show();

        }
    }

}

package com.example.frpinkl.fotocamgaleria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class email extends ActionBarActivity implements OnClickListener {

    EditText editTextEmail, editTextSubject, editTextMessage;
    Button btnSend, btnAttachment;
    String codeFoto2, email, subject, message, attachmentFile;
    Uri URI = null;
    private static final int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    int columnIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        editTextEmail = (EditText) findViewById(R.id.editTextTo);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        btnAttachment = (Button) findViewById(R.id.buttonAttachment);
        btnSend = (Button) findViewById(R.id.buttonSend);

        btnSend.setOnClickListener(this);
        btnAttachment.setOnClickListener(this);


        //mirar esto bien
        Intent intentFoto2 = getIntent();
        codeFoto2 = (String) intentFoto2.getSerializableExtra("ahivalafoto");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            attachmentFile = cursor.getString(columnIndex);
            Log.e("Attachment Path:", attachmentFile);
            URI = Uri.parse("file://" + attachmentFile);
            cursor.close();*/


        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);

            Log.e("Attachment Path:", imgDecodableString);
            URI = Uri.parse("file://" + imgDecodableString);

            cursor.close();
            //ImageView imgView = (ImageView) findViewById(R.id.imgView);
            // Set the Image in ImageView after decoding the String
            // imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

        } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onClick(View v) {

        if (v == btnAttachment) {
            loadImagefromGallery();

        }
        if (v == btnSend) {
            try {
                email = editTextEmail.getText().toString();
                subject = editTextSubject.getText().toString();
                message = editTextMessage.getText().toString();

                final Intent emailIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { email });
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        subject);
                if (URI != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                }
                emailIntent
                        .putExtra(android.content.Intent.EXTRA_TEXT, message);
                this.startActivity(Intent.createChooser(emailIntent,
                        "Sending email..."));

            } catch (Throwable t) {
                Toast.makeText(this,
                        "Request failed try again: " + t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    /*public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),PICK_FROM_GALLERY);

    }*/



    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image*//*");
         galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        /*
        el Action_get_content va mejor que el

         */


        /*
         el Action_get_content mejor que el action_pick. el action_pic
         es específico para imágenes y el get_content para cualquier tipo
        entonces no dejaba explorar los archivo en general, sólo podía adjuntar fotos de la galería de imágenes
         Start the Intent
         */


        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

}
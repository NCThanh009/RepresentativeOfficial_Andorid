package com.example.btquatrinh_04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfficialsActivity_photo extends AppCompatActivity {
    TextView txtNormalizedInputPhoTo, txtNamealPhoto, txtNameesPhoto;
    ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officials_photo);
        finViewbyID();
        Intent intent = getIntent();
        String input = intent.getStringExtra("inputphoto");
        String name = intent.getStringExtra("namephoto");
        String offi = intent.getStringExtra("officialphoto");
        String img = intent.getStringExtra("image");
        txtNamealPhoto.setText(offi);
        txtNameesPhoto.setText(name);
        txtNormalizedInputPhoTo.setText(input);
        Picasso.get().load(img)
                .error(R.drawable.img_error)
                .placeholder(R.drawable.img_load)
                .into(imgPhoto);
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(OfficialsActivity_photo.this,OfficialsActivity.class);
                startActivity(intent);
            }
        });
    }
    public void finViewbyID(){
        txtNormalizedInputPhoTo= findViewById(R.id.txtNormalizedInputPhoTo);
        txtNamealPhoto= findViewById(R.id.txtNamealPhoto);
        txtNameesPhoto= findViewById(R.id.txtNameesPhoto);
        imgPhoto= findViewById(R.id.imgPhoto);
    }
}
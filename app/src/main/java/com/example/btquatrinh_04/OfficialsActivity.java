package com.example.btquatrinh_04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.btquatrinh_04.model.representatives;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OfficialsActivity extends AppCompatActivity {
    TextView txtName, txtOff, txtDemorac, txtAddres, txtPhone, txtEmail,txtWeb, txtInput;
    LinearLayout linear;
    ImageView img, imgFace, imgGoo, imgYou, imgTwitt;
    String strOffi,strName, strInput, strImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officials);

        finViewbyID();
        GetKey();
        strInput= (String) txtInput.getText();
        strName= (String) txtName.getText();
        strOffi= (String) txtOff.getText();
    }
    public void finViewbyID(){
        linear= findViewById(R.id.lnl);
        img= findViewById(R.id.img);
        imgFace= findViewById(R.id.imgFacebook);
        imgYou= findViewById(R.id.imgYoutube);
        imgTwitt= findViewById(R.id.imgTwitter);
        imgGoo= findViewById(R.id.imgGoogle);
        txtInput= findViewById(R.id.txtNormalizedInput2);
        txtName= findViewById(R.id.txtNamees);
        txtOff= findViewById(R.id.txtNameal);
        txtDemorac= findViewById(R.id.txtDemoractic);
        txtAddres= findViewById(R.id.txtAddress);
        txtPhone= findViewById(R.id.txtphone);
        txtEmail= findViewById(R.id.txtemail);
        txtWeb= findViewById(R.id.txtWebsite);
    }
    public void GetKey(){
        Intent intent = getIntent();
        String input = intent.getStringExtra("input");
        String name = intent.getStringExtra("name");
        String offi = intent.getStringExtra("official");
        String key = intent.getStringExtra("key");
        String zip = intent.getStringExtra("zip");
        txtInput.setText(input);
        txtName.setText(name);
        txtOff.setText(offi);
        GetData(zip,key);
    }
    //tách ký tự lấy chuỗi số
    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }
    //lấy dữ liệu API
    public void GetData(String zip,String key){
        String url= "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyDCPKvUshMurSIjVjUbJj-7afr_Xx7Noq0&address="+zip;
        RequestQueue reque= Volley.newRequestQueue(OfficialsActivity.this);
        StringRequest strReque= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json= new JSONObject(response);
                    JSONArray jsonArOfficials= json.getJSONArray("officials");
                    JSONObject jsonOBOfficials= jsonArOfficials.getJSONObject(Integer.parseInt(key));

                    String party= jsonOBOfficials.getString("party");
                    txtDemorac.setText(party);

                    //Ảnh
                    if(jsonOBOfficials.isNull("photoUrl")){
                        Picasso.get().load(R.drawable.img_not)
                                .error(R.drawable.img_error)
                                .placeholder(R.drawable.img_load)
                                .into(img);
                    }else {
                        String photoUrl= jsonOBOfficials.getString("photoUrl");
                        strImg= photoUrl;
                        Picasso picasso = new Picasso.Builder(OfficialsActivity.this).listener(new Picasso.Listener() { @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {

                            final String changedUrl = photoUrl.replace("http:", "https:");

                            picasso.load(changedUrl)
                                    .error(R.drawable.img_error)
                                    .placeholder(R.drawable.img_load)
                                    .into(img);
                        }
                        }).build();
                        picasso.load(photoUrl)
                                .error(R.drawable.img_error)
                                .placeholder(R.drawable.img_load)
                                .into(img);
                    }
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent= new Intent(OfficialsActivity.this, OfficialsActivity_photo.class);
                            intent.putExtra("namephoto", strName);
                            intent.putExtra("officialphoto", strOffi);
                            intent.putExtra("inputphoto", strInput);
                            intent.putExtra("image", strImg);
                            startActivity(intent);
                        }
                    });
                    //check background
                    if(jsonOBOfficials.isNull("party")){
                        linear.setBackgroundColor(Color.BLACK);
                    }else {
                        if(party.equals("Republican Party")){
                            linear.setBackgroundColor(Color.RED);
                        }else {
                            linear.setBackgroundColor(Color.BLUE);
                        }
                    }
                    //*check data
                    //địa chì
                    if(jsonOBOfficials.isNull("address")){
                        txtAddres.setText("No data provided");
                    }else {
                        JSONArray jsonAddress= jsonOBOfficials.getJSONArray("address");
                        JSONObject jsonAdr= jsonAddress.getJSONObject(0);
                        String line1= jsonAdr.getString("line1");
                        String city= jsonAdr.getString("city");
                        String state= jsonAdr.getString("state");
                        String zip= jsonAdr.getString("zip");
                        txtAddres.setText(line1+", "+city+", "+state+", "+zip);
                        txtAddres.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String adr=line1+", "+city+", "+state+", "+zip;
                                Intent inte= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/place/"+adr));
                                startActivity(inte);
                            }
                        });
                    }
                    //số điện thoại
                    if(jsonOBOfficials.isNull("phones")){
                        txtPhone.setText("No data provided");
                    }else {
                        JSONArray jsonPhone = jsonOBOfficials.getJSONArray("phones");
                        txtPhone.setText((CharSequence) jsonPhone.get(0));
                        txtPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent inte= new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getOnlyDigits((String) jsonPhone.get(0))));
                                    startActivity(inte);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    //email
                    if(jsonOBOfficials.isNull("emails")){
                        txtEmail.setText("No data provided");
                    }else {
                        JSONArray jsonEmail = jsonOBOfficials.getJSONArray("emails");
                        txtEmail.setText((CharSequence) jsonEmail.get(0));
                        txtEmail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent inte= new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+jsonEmail.get(0)));
                                    startActivity(inte);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    //website
                    if(jsonOBOfficials.isNull("urls")){
                        txtWeb.setText("No data provided");
                    }else {
                        JSONArray jsonWeb = jsonOBOfficials.getJSONArray("urls");
                        txtWeb.setText((CharSequence) jsonWeb.get(0));
                        txtWeb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent inte= new Intent(Intent.ACTION_VIEW, Uri.parse(""+jsonWeb.get(0)));
                                    startActivity(inte);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    //kênh mạng xã hội
                    JSONArray jsonChannels = jsonOBOfficials.getJSONArray("channels");
                    for(int i=0; i<jsonChannels.length(); i++){
                        JSONObject jsonOBChannels= jsonChannels.getJSONObject(i);
                        switch ( jsonOBChannels.getString("type") ) {
                            case  "GooglePlus":
                                imgGoo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String urlToUse= null;
                                        try {
                                            urlToUse = "https://plus.google.com/" + jsonOBChannels.getString("id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(urlToUse));
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case  "Facebook":
                                imgFace.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String urlToUse= null;
                                        try {
                                            urlToUse = "https://www.facebook.com/" + jsonOBChannels.getString("id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(urlToUse));
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case  "Twitter":
                                imgTwitt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String urlToUse= null;
                                        try {
                                            urlToUse = "https://twitter.com/" + jsonOBChannels.getString("id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(urlToUse));
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case  "YouTube":
                                imgYou.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String urlToUse= null;
                                        try {
                                            urlToUse = "https://www.youtube.com/" + jsonOBChannels.getString("id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(urlToUse));
                                        startActivity(intent);
                                    }
                                });
                                break;
                            default:
                                Toast.makeText(OfficialsActivity.this, "No data provided", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                   e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        reque.add(strReque);
    }
}
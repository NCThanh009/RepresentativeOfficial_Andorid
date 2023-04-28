package com.example.btquatrinh_04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.btquatrinh_04.model.OnItemClickListener;
import com.example.btquatrinh_04.model.representatives;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView revRep;
    private List<representatives> repList;
    private representativesAdapter repAdapter;

    TextView txtNorma;
    String zip="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNorma= findViewById(R.id.txtNormalizedInput);
        revRep= findViewById(R.id.revNormalized);
        repList= new ArrayList<>();

        VirtualData((ArrayList) repList);

        repAdapter= new representativesAdapter(repList, this);
        LinearLayoutManager linear= new LinearLayoutManager(this);
        revRep.setLayoutManager(linear);
        RecyclerView.ItemDecoration deco= new DividerItemDecoration(this, DividerItemDecoration.VERTICAL); //tạo đường kẻ
        revRep.addItemDecoration(deco);
        revRep.setAdapter(repAdapter);
    }
    //dữ liệu ảo
    public void VirtualData(ArrayList list){
        txtNorma.setText("NCiThanh");
        representatives rep= new representatives("Thanh","Đồng Tháp");
        representatives rep1= new representatives("Cong","Tp Cao Lãnh");
        representatives rep2= new representatives("Nguyen","TPHCM");
        representatives rep3= new representatives("ToiDo","Tân Phú, TPHCM");
        representatives rep4= new representatives("ToiDo","Bình Tân, TPHCM");
        list.add(rep);
        list.add(rep1);
        list.add(rep2);
        list.add(rep3);
        list.add(rep4);
    }
    public void GetData(String zip){
        String url= "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyDCPKvUshMurSIjVjUbJj-7afr_Xx7Noq0&address="+zip;
        RequestQueue reque= Volley.newRequestQueue(MainActivity.this);
        StringRequest strReque= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json= new JSONObject(response);
                    JSONObject jsonNormalized= json.getJSONObject("normalizedInput");

                    String city= jsonNormalized.getString("city");
                    String state= jsonNormalized.getString("state");
                    String zip= jsonNormalized.getString("zip");
                    txtNorma.setText(city+", "+state+" "+zip);

                    JSONArray jsonArOffices= json.getJSONArray("offices");
                    JSONArray jsonArOfficials= json.getJSONArray("officials");


                    for (int i=0; i<jsonArOffices.length(); i++){
                        JSONObject jsonOBOffices= jsonArOffices.getJSONObject(i);
                        String nameOffices= jsonOBOffices.getString("name");

                        JSONObject jsonOBOfficials= jsonArOfficials.getJSONObject(i);
                        String nameOfficials= jsonOBOfficials.getString("name");

                        JSONArray jsonAr= jsonOBOffices.getJSONArray("officialIndices");
                        representatives rep= new representatives(nameOfficials,nameOffices);
                        rep.setOfficials((Integer) jsonAr.get(0));
                        repList.add(rep);
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

    public void SendData(representatives rep){
        Intent intent= new Intent(MainActivity.this, OfficialsActivity.class);
        intent.putExtra("name", rep.getName());
        intent.putExtra("official", rep.getOffice());
        intent.putExtra("key", ""+rep.getOfficials());
        intent.putExtra("input", txtNorma.getText());
        intent.putExtra("zip",zip);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.itemAbout:
                Intent inte= new Intent(MainActivity.this, About.class);
                startActivity(inte);
                return true;
            case R.id.itemSearch:
                DialogSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //xử lý sữ kiện search
    private void DialogSearch(){
        Dialog dia= new Dialog(this);
        dia.setContentView(R.layout.search_dialog);
        dia.setCanceledOnTouchOutside(false);

        repList.clear();

        EditText edtSearch= dia.findViewById(R.id.edtSearch);
        TextView txtOk= dia.findViewById(R.id.txtOK);
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, edtSearch.getText(),Toast.LENGTH_LONG).show();
                GetData(edtSearch.getText().toString());
                zip=edtSearch.getText().toString();
                dia.dismiss();
            }
        });
        dia.show();
    }

    @Override
    public void OnItemClick(representatives rep) {
        Toast.makeText(this,"Click "+rep.getName(),Toast.LENGTH_LONG).show();
        SendData(rep);

    }
}
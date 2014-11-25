package com.orange_money.enmusubi.activity;



import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orange_money.enmusubi.R;
import com.orange_money.enmusubi.UserData;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SellTextFragment extends Fragment {

    /** REQUEST:画像選択 */
    private static final int REQUEST_IMAGE_SELECT = 1;

    private UserData userData;

//    private File uploadFile;
    private byte[] imageFile = new byte[1];
    private EditText editTextName;
    private EditText editClassName;
    private EditText editTeacherName;
    private EditText editPrice;
    private EditText editContent;
    private TextView photoNameView;
    private Button photoButton;
    private ImageView textResist;



    public SellTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sell_text, container, false);


        userData = (UserData)getArguments().getSerializable("user_data");
        editTextName = (EditText)v.findViewById(R.id.editTextName);
        editClassName = (EditText)v.findViewById(R.id.editClassName);
        editTeacherName = (EditText)v.findViewById(R.id.editTeacherName);
        editPrice = (EditText)v.findViewById(R.id.editPrice);
        editContent = (EditText)v.findViewById(R.id.editContent);

        photoNameView = (TextView)v.findViewById(R.id.photoName);
        photoButton = (Button)v.findViewById(R.id.selectPhoto);


        textResist = (ImageView)v.findViewById(R.id.textResist);


        //写真を選択する際の処理
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"select"), REQUEST_IMAGE_SELECT);
            }

        });

        //新規登録ボタンを押した際の処理
        textResist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //テキスト登録リクエストを送る
                AsyncHttpClient client = new AsyncHttpClient();
                String url = getString(R.string.local) + "texts";

                //jsonでpost
                JSONObject params = new JSONObject();
                try {
                    params.put("user_id",userData.getmUserId());
                    params.put("lecture_name",editClassName.getText().toString());
                    params.put("textbook_name",editTextName.getText().toString());
                    params.put("teacher",editTeacherName.getText().toString());
                    params.put("price",editPrice.getText().toString());
                    params.put("comment",editContent.getText().toString());
                    //デバッグ用 education_listから大学情報を抽出する処理が必要
                    params.put("univ",userData.getmUniv());

                    //todo画像投稿に対応

                    StringEntity entity = new StringEntity(params.toString(),HTTP.UTF_8);
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    client.post(view.getContext(),url,entity,"application/json",new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            ContentResolver cr = getActivity().getContentResolver();
            String[] columns = {MediaStore.Images.Media.DATA};
            if(data != null) {
                String fileName = "";
                Cursor c = cr.query(data.getData(), columns, null, null, null);
                c.moveToFirst();
                fileName = c.getString(0);
                FileInputStream fis = new FileInputStream(fileName);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (fis.read(imageFile) > 0) {
                    baos.write(imageFile);
                }
                baos.close();
                fis.close();
                imageFile = baos.toByteArray();
                photoNameView.setText(fileName);
            }
        }catch (Exception e){
            Log.e("SellTextFragment","no image");
        }
    }
}

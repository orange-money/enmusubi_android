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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orange_money.enmusubi.R;
import com.orange_money.enmusubi.data.UserData;

import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SellTextFragment extends Fragment {

    /** REQUEST:画像選択 */
    private static final int REQUEST_IMAGE_SELECT = 1;

    private UserData userData;

    private File uploadFile;
    private byte[] imageFile = new byte[1];
    private EditText editTextName;
    private EditText editClassName;
    private EditText editTeacherName;
    private EditText editPrice;
    private EditText editContent;
    private TextView photoNameView;
    private ImageView photoButton;
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
        photoButton = (ImageView)v.findViewById(R.id.selectPhoto);


        textResist = (ImageView)v.findViewById(R.id.textResist);


        //写真を選択する際の処理
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"select"), REQUEST_IMAGE_SELECT);
            }

        });

        //新規登録ボタンを押した際の処理
        textResist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View view) {
                //テキスト登録リクエストを送る
                AsyncHttpClient client = new AsyncHttpClient();
//                String url = getString(R.string.local) + "texts";
                String url = getString(R.string.aws) + "texts";

                //jsonでpost
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entity.setCharset(Charset.forName("UTF-8"));


                try {
                    if(uploadFile != null) {
                        entity.addBinaryBody("text[file]", uploadFile, ContentType.create("image/jpeg"), photoNameView.getText().toString());
                    }
                    ContentType textContentType = ContentType.APPLICATION_JSON;
                    entity.addTextBody("text[user_id]",userData.getmUserId().toString(),textContentType);
                    entity.addTextBody("text[lecture_name]",editClassName.getText().toString(),textContentType);
                    entity.addTextBody("text[textbook_name]",editTextName.getText().toString(),textContentType);
                    entity.addTextBody("text[teacher]",editTeacherName.getText().toString(),textContentType);
                    entity.addTextBody("text[price]",editPrice.getText().toString(),textContentType);
                    entity.addTextBody("text[comment]",editContent.getText().toString(),textContentType);
                    entity.addTextBody("text[univ]",userData.getmUniv(),textContentType);

                    client.post(view.getContext(), url, entity.build(), "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            Toast.makeText(view.getContext(), "教科書が投稿されました！", Toast.LENGTH_LONG).show();

                            editTextName.setText("");
                            editClassName.setText("");
                            editPrice.setText("");
                            editContent.setText("");
                            editTeacherName.setText("");
                            photoNameView.setText("");
                            imageFile = new byte[1];
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            Toast.makeText(view.getContext(), "投稿が失敗しました．．．", Toast.LENGTH_LONG).show();

                        }
                    });
                }  catch (Exception e) {
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
                uploadFile = new File(fileName);
                photoNameView.setText(fileName);
            }
        }catch (Exception e){
            Log.e("SellTextFragment","no image");
        }
    }
}

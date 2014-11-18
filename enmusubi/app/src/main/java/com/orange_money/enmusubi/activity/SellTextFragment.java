package com.orange_money.enmusubi.activity;



import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orange_money.enmusubi.R;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SellTextFragment extends Fragment {

    /** REQUEST:画像選択 */
    private static final int REQUEST_IMAGE_SELECT = 1;

    private File uploadFile;
    private TextView photoNameView;
    private Button photoButton;



    public SellTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sell_text, container, false);

        photoNameView = (TextView)v.findViewById(R.id.photoName);
        photoButton = (Button)v.findViewById(R.id.selectPhoto);
        //写真を選択する際の処理
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"select"), REQUEST_IMAGE_SELECT);
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
                uploadFile = new File(fileName);
                photoNameView.setText(fileName);
            }
        }catch (Exception e){

        }
    }
}

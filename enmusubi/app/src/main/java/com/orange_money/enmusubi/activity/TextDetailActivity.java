package com.orange_money.enmusubi.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;
import com.orange_money.enmusubi.R;
import com.orange_money.enmusubi.adaptaer.ImageGetTask;
import com.orange_money.enmusubi.data.TextData;

import org.w3c.dom.Text;


public class TextDetailActivity extends Activity {

    private final static String TAG = "TextDetailActivity";

    private TextData mTextData;
    private TextView mTextInfo;
    private TextView mTextClass;
    private TextView mTextTeacher;
    private TextView mTextPrice;
    private ImageView mTextImage;
    private TextView mTextComment;
    private ImageView mWantImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_detail);

        mTextInfo = (TextView)findViewById(R.id.text_info);
        mTextClass = (TextView)findViewById(R.id.text_class);
        mTextTeacher = (TextView)findViewById(R.id.text_teacher);
        mTextImage = (ImageView)findViewById(R.id.text_image);
        mTextComment = (TextView)findViewById(R.id.text_comment);
        mTextPrice = (TextView)findViewById(R.id.text_price);
        mWantImage = (ImageView)findViewById(R.id.want_button);

        Intent intent = getIntent();
        if(intent.getSerializableExtra("text_data") != null){
            mTextData = (TextData)getIntent().getSerializableExtra("text_data");
        }

        //画像取得スレッド起動
        ImageGetTask task = new ImageGetTask(mTextImage);
        task.execute(mTextData.getFileName());

        mTextInfo.setText(mTextData.getTextTitle());
        mTextClass.setText(mTextData.getClassName());
        mTextTeacher.setText(mTextData.getTeacherName());
        mTextComment.setText(mTextData.getComment());
        mTextPrice.setText(mTextData.getTextPrice());

        mWantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Facebookページへ遷移する処理を暗黙的intentで書く
                try {
                    getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTextData.getLink()));
                    startActivity(linkIntent);
                } catch (Exception e) {
                    Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTextData.getLink()));
                    startActivity(linkIntent);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout){
            Log.d(TAG, "logout.");
            Session session = Session.getActiveSession();
            // セッションとトークン情報を廃棄する。
            session.closeAndClearTokenInformation();
            Intent intent = new Intent(this,FBLoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

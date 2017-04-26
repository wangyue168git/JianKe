package com.example.ubuntu.market.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ubuntu.market.DynamicModel;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.dialog.DialogBuilder;
import com.example.ubuntu.market.entity.DynamicItem;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.entity.UserLocal;
import com.example.ubuntu.market.impl.JKModelImpl;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import jp.wasabeef.richeditor.RichEditor;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class SendArticleActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 0x20;
    @Bind(R.id.editor)
    RichEditor mEditor;
    @Bind(R.id.cancel)
    TextView back;
    @Bind(R.id.article_send)
    TextView send;

    private Dialog mLoadingDialog;
    private Dialog mLoadingFinishDialog;
    int x = 0; //表示是否上传成功
    private List<BmobFile> imagelist = new ArrayList<>();
    private TextView mPreview;
    private UserModel userModel = new UserModel();
    private DynamicModel dynamicModel = new DynamicModel();
    private UserLocal userLocal;
    private User user;
    private DynamicItem dynamicItem = new DynamicItem();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_article);
        ButterKnife.bind(this);
        init();
        mLoadingDialog = DialogBuilder.createLoadingDialog(SendArticleActivity.this, "正在上传");
        mLoadingFinishDialog = DialogBuilder.createLoadingfinishDialog(SendArticleActivity.this, "上传完成");
    }

    private void init() {
        userLocal = new UserLocal();
        userLocal = userModel.getUserLocal();
        userModel.getUser(userLocal.getObjectId(), new makemodelimpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                user = (User) o ;
                dynamicItem.setWriter(user);
            }
            @Override
            public void getFailure() {
            }
        });
        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        mEditor.setEditorHeight(200);

       // mEditor.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setHtml("    <style>\n" +
                "     \n" +
                "    img{\n" +
                "     max-width:98%;\n" +
                "     height:auto;\n" +
                "    }\n" +
                "     \n" +
                "    </style>");
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
       // mEditor.setInputEnabled(false);
        mPreview = (TextView) findViewById(R.id.preview);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {
                mPreview.setText(text);
            }
        });
        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                PhotoPickerIntent intent = new PhotoPickerIntent(getApplicationContext());
                intent.setPhotoCount(6);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_CODE);
                //mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
                  //      "dachshund");
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                mEditor.insertLink("https://wangyue168git.github.io/", "https://wangyue168git.github.io/");

            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertTodo();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(x ==0) {
                            mLoadingDialog.dismiss();
                            ToastUtil.showLong(getApplicationContext(), "网络问题，请稍后再试");
                        }
                        x = 0;
                    }
                }, 20000);
                send.setEnabled(false);
                dynamicItem.setDynamicContent(mPreview.getText().toString());
                dynamicItem.setPhotoList(imagelist);
                dynamicItem.setType("文章");
                dynamicModel.sendDynamicItem(dynamicItem, new JKModelImpl.BaseListener() {
                    @Override
                    public void getSuccess(Object o) {
                        mLoadingDialog.dismiss();
                        mLoadingFinishDialog.show();
                        x = 1 ;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mLoadingFinishDialog.dismiss();
                                finish();
                            }
                        }, 500);
                    }
                    @Override
                    public void getFailure() {
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择结果回调
        if (requestCode == REQUEST_CODE && data != null) {
            List<String> pathList = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            for(int i = 0; i<pathList.size();i++) {
                mEditor.insertImage("file:"+pathList.get(i),"image");
                imagelist.add(new BmobFile(new File(pathList.get(i))));
            }
        }
    }
}

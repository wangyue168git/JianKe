package com.example.ubuntu.market.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ubuntu.market.DynamicModel;
import com.example.ubuntu.market.FixedGridView;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.RoundImageView;
import com.example.ubuntu.market.UserModel;
import com.example.ubuntu.market.application.BaseApplication;
import com.example.ubuntu.market.entity.Comment;
import com.example.ubuntu.market.entity.DynamicItem;
import com.example.ubuntu.market.entity.User;
import com.example.ubuntu.market.entity.UserLocal;
import com.example.ubuntu.market.fragment.CommentAdapter;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleByUserDetailActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;


    @Bind(R.id.wz_write_photo)
    RoundImageView writePhoto;
    @Bind(R.id.wz_write_name)
    TextView writeName;
    @Bind(R.id.wz_write_date)
    TextView writeDate;
    @Bind(R.id.webview)
    WebView article;

    @Bind(R.id.wz_comment)
    ListView comment_add;
    @Bind(R.id.wz_comment_edit)
    EditText comment_edit;
    @Bind(R.id.wz_comment_sub)
    Button comment_sub;
    @Bind(R.id.wz_delete_dy)
    ImageView delete_dy;


    private User user;
    private UserLocal mUserLocal;
    private UserModel mUserModel = new UserModel();
    private DynamicModel dynamicModel = new DynamicModel();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    CommentAdapter comentAdapter;
    List<String> commentList = new ArrayList<>();
    List<Comment> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_article_by_user_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        imageLoader.init(ImageLoaderConfiguration.createDefault(ArticleByUserDetailActivity.this));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.xiaolian)
                .showImageForEmptyUri(R.drawable.xiaolian)
                .showImageOnFail(R.drawable.xiaolian).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
        final DynamicItem dynamicItem = (DynamicItem) getIntent().getSerializableExtra("DYNAMIC");
        mUserModel.getUser(dynamicItem.getWriter().getObjectId(), new makemodelimpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                User user = (User) o;
                imageLoader.displayImage(user.getPhoto().getUrl(), writePhoto, options);
                writeName.setText(user.getName());
            }

            @Override
            public void getFailure() {

            }
        });
        mUserLocal = mUserModel.getUserLocal();
        mUserModel.getUser(mUserLocal.getObjectId(), new makemodelimpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                user = (User) o;
            }
            @Override
            public void getFailure() {
            }
        });

        comment_add.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(dynamicItem.getWriter().getObjectId().equals(mUserLocal.getObjectId())
                        || list.get(i).getCommentUserName().equals(mUserLocal.getName())) {
                    showCommentDeleteDialog(list.get(i), i);
                }
                return false;
            }
        });

        if(dynamicItem.getWriter().getObjectId().equals(mUserLocal.getObjectId())){
            delete_dy.setVisibility(View.VISIBLE);
        }
        delete_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(dynamicItem);
            }
        });


        final Drawable drawable = comment_sub.getBackground();
        Resources resources = getResources();
        final Drawable drawable1 = resources.getDrawable(R.drawable.sub_enable) ;

        if(TextUtils.isEmpty(comment_edit.getText())){
            comment_sub.setEnabled(false);
            comment_sub.setBackground(drawable1);
        }
        comment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(comment_edit.getText())){
                    comment_sub.setEnabled(false);
                    comment_sub.setBackground(drawable1);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(comment_edit.getText())){
                    comment_sub.setEnabled(true);
                    comment_sub.setBackground(drawable);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(comment_edit.getText())){
                    comment_sub.setEnabled(false);
                    comment_sub.setBackground(drawable1);
                }
            }
        });
        comment_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Comment comment = new Comment();
                if(!TextUtils.isEmpty(comment_edit.getText())) {
                    comment.setCommentUserName(user.getName());
                    comment.setDynamicItem(dynamicItem);
                    comment.setCommentContent(comment_edit.getText().toString());
                    list.add(comment);
                    dynamicModel.sendComment(comment, new makemodelimpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            ToastUtil.showLong(BaseApplication.getmyContext(), "发表成功");
                            String comments = comment.getCommentUserName()+"："+comment.getCommentContent();
                            commentList.add(comments);
                            setListViewHeightBasedOnChildren(comment_add);
                            comentAdapter.notifyDataSetChanged();
                            comment_edit.setText("");
                            comment_edit.clearFocus();
                        }

                        @Override
                        public void getFailure() {
                        }
                    });
                }else {
                    ToastUtil.showShort(getApplicationContext(),"评论不能为空：）");
                }
            }
        });
        comment_add.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String a[] = commentList.get(i).split("：");
                comment_edit.setText("@"+a[0]+" ");
                //edit获取焦点到指定位置
                comment_edit.setFocusable(true);
                comment_edit.setFocusableInTouchMode(true);
                comment_edit.requestFocus();
                comment_edit.setSelection(a[0].length()+2);
                //显示软键盘
                InputMethodManager imm = (InputMethodManager)ArticleByUserDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        writeDate.setText(dynamicItem.getCreatedAt());


        article.loadDataWithBaseURL("article",  getNewContent(dynamicItem), "text/html", "utf-8", null);


        article.getSettings().setJavaScriptEnabled(true);
        // article.setWebChromeClient(new WebChromeClient());

        dynamicModel.getComment(dynamicItem, new makemodelimpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                list = (List<Comment>) o;
                com(list);
            }
            @Override
            public void getFailure() {
            }
        });
        comentAdapter = new CommentAdapter(this, R.layout.comment_item,commentList) ;
        comment_add.setAdapter(comentAdapter);
    }
    public static String getNewContent(DynamicItem dynamicItem){

        String [] urls = new String[dynamicItem.getPhotoList().size()];
        for (int i = 0; i < dynamicItem.getPhotoList().size(); i++) {
            urls[i] = dynamicItem.getPhotoList().get(i).getUrl();
            Log.i("path", "sendDynamicItem: " + urls[i] + " " + dynamicItem.getPhotoList().size());
        }
        String htmltext = dynamicItem.getDynamicContent();
        Document doc= Jsoup.parse(htmltext);
        Elements elements=doc.getElementsByTag("img");
        for (int i = 0 ; i<dynamicItem.getPhotoList().size();i++){
            Element element = elements.get(i);
            element.attr("src",urls[i]);
        }
        return doc.toString();
    }


    /**
     * 刷新评论区
     * @param list
     */
    private void com(List<Comment> list) {
        commentList.clear();
        for (int i = 0;i< list.size();i++) {
            Comment comments = list.get(i);
            String comment = comments.getCommentUserName()+"："+comments.getCommentContent();
            commentList.add(comment);
        }
        setListViewHeightBasedOnChildren(comment_add);
        comentAdapter.notifyDataSetChanged();
    }
    /**
     * 动态改变listview高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }



    public  void showDeleteDialog(final DynamicItem dynamicItem){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ArticleByUserDetailActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("删除动态");
        normalDialog.setMessage("确定删除吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dynamicModel.deleteDynamic(dynamicItem, new makemodelimpl.BaseListener() {
                            @Override
                            public void getSuccess(Object o) {
                                ToastUtil.showShort(ArticleByUserDetailActivity.this,"删除成功");
                                Intent intent = new Intent();
                                intent.setAction("MSG_DY");
                                sendBroadcast(intent);
                                finish();
                            }
                            @Override
                            public void getFailure() {
                            }
                        });

                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }
    public  void showCommentDeleteDialog(final Comment comment, final int i){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ArticleByUserDetailActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("删除评论");
        normalDialog.setMessage("确定删除吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dynamicModel.deleteComment(comment, new makemodelimpl.BaseListener() {
                            @Override
                            public void getSuccess(Object o) {
                                ToastUtil.showShort(ArticleByUserDetailActivity.this,"删除成功");
                                list.remove(i);
                                commentList.remove(i);
                                setListViewHeightBasedOnChildren(comment_add);
                                comentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void getFailure() {
                                ToastUtil.showShort(ArticleByUserDetailActivity.this,"删除shibai");
                            }
                        });


                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}

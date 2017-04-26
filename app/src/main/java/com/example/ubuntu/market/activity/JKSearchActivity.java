package com.example.ubuntu.market.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.ubuntu.market.JKModel;
import com.example.ubuntu.market.R;
import com.example.ubuntu.market.SharedpreferencesHelper;
import com.example.ubuntu.market.entity.JKArticle;
import com.example.ubuntu.market.fragment.SearchHIstoryAdapter;
import com.example.ubuntu.market.impl.makemodelimpl;
import com.example.ubuntu.market.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JKSearchActivity extends AppCompatActivity {
    @Bind(R.id.search_back)
    ImageView searchBack;
    @Bind(R.id.search_text)
    EditText searchText;
    @Bind(R.id.search_btn)
    ImageView searchButton;
    @Bind(R.id.search_history_lv)
    ListView searchHistoryLv;
    @Bind(R.id.clear_history_btn)
    Button clearHistoryBtn;


    private SearchHIstoryAdapter mAdapter;
    private final String SEARCHRESULT = "searchresult";
    private SharedpreferencesHelper mHelper;
    int page = 1;
    String search_q;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jksearch);
        ButterKnife.bind(this);
        init();
    }
    public void init(){
        mHelper = new SharedpreferencesHelper(JKSearchActivity.this);
        List<String> history_arr = mHelper.returnhistroydata();
        if(history_arr.size()>0){
            mAdapter = new SearchHIstoryAdapter(this,R.layout.searchhistory_listview_item,history_arr);
            searchHistoryLv.setAdapter(mAdapter);
            searchHistoryLv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, final long l) {
                    page = 1;
                    search_q = mAdapter.getItem(i);
                    new JKModel().onSearchJK(mAdapter.getItem(i), page, "note", new makemodelimpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            List<JKArticle> list = (List<JKArticle>) o;
                            Intent intent = new Intent(JKSearchActivity.this, JKSearchResultActivity.class);
                            intent.putExtra(SEARCHRESULT, (Serializable) list);
                            intent.putExtra("search_q",search_q);
                            startActivity(intent);
                        }
                        @Override
                        public void getFailure() {

                        }
                    });
                }
            });
        }else{
            searchHistoryLv.setVisibility(View.GONE);
            clearHistoryBtn.setVisibility(View.GONE);
        }


    }
    @OnClick({R.id.search_back, R.id.search_btn, R.id.clear_history_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.search_btn:
                mHelper.save(searchText.getText().toString());
                if (!TextUtils.isEmpty(searchText.getText().toString())) {
                    page = 1;
                    new JKModel().onSearchJK(searchText.getText().toString(), page, "note", new makemodelimpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            mHelper.save(searchText.getText().toString());
                            List<JKArticle> list = (List<JKArticle>) o;
                            Intent intent = new Intent(JKSearchActivity.this, JKSearchResultActivity.class);
                            intent.putExtra(SEARCHRESULT, (Serializable) list);
                            intent.putExtra("search_q",searchText.getText().toString());
                            startActivity(intent);
                        }
                        @Override
                        public void getFailure() {

                        }
                    });
                } else {
                    ToastUtil.showLong(JKSearchActivity.this, "搜索名不能为空");
                }
                break;
            case R.id.clear_history_btn:
                mHelper.cleanHistory();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                searchHistoryLv.setVisibility(View.GONE);
                break;
        }
    }

}

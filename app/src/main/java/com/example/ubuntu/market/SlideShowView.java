package com.example.ubuntu.market;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ubuntu.market.activity.JKArticleDetailActivity;
import com.example.ubuntu.market.entity.ArticleDetail;
import com.example.ubuntu.market.impl.JKModelImpl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 作者： wangyue on 17-4-5 10:42
 * 邮箱：973459080@qq.com
 */
public class SlideShowView extends FrameLayout {

    private static ImageLoader imageLoader = ImageLoader.getInstance();
    private static DisplayImageOptions options;
    private List<SliderShowViewItem> list;
    // 轮播图图片数量
    private final static int IMAGE_COUNT = 3;
    // 自动轮播的时间间隔
    private final static int TIME_INTERVAL = 7;
    // 自动轮播启用开关
    private final static boolean isAutoPlay = true;
    // 自定义轮播图的资源
    private List<String> imageUrls;
    private List<ImageView> imageViewsList;

    // 放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    // 当前轮播页
    private int currentItem = 0;
    // 定时任务
    private ScheduledExecutorService scheduledExecutorService;

    final JKModel jkModel = new JKModel();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };
    /**
     *
     * @param context
     *
     *
     * public View (Context context)是在java代码创建视图的时候被调用，如果是从xml填充的视图，就不会调用这个
    public View (Context context, AttributeSet attrs)这个是在xml创建但是没有指定style的时候被调用
    public View (Context context, AttributeSet attrs, int defStyle)这个也是在xml创建指定style的时候被调用
     */
    public SlideShowView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initImageLoader(context);
    }
    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, TIME_INTERVAL,
                TimeUnit.SECONDS);
    }

    /**
     * 停止轮播图切换
     */
    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    private void initData(List<SliderShowViewItem> list){
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
        imageUrls = new ArrayList<String>();
        for(SliderShowViewItem item:list){
            imageUrls.add(item.getImgLink());
        }
    }
    /**
     * 初始化Views等UI
     */
    public void initUI(Context context, final List<SliderShowViewItem> list) {
        this.list = list;
        initData(list);
        if (imageUrls == null || imageUrls.size() == 0)
            return;
        LayoutInflater.from(context).inflate(R.layout.slideshow, this,
                true);
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();

        // 热点个数与图片特殊相等
        for (int i = 0; i < 3; i++) {
            final int index = i;
            ImageView view = new ImageView(context);
            view.setTag(imageUrls.get(i));
            if (i == 0)// 给一个默认图
                view.setBackgroundResource(R.drawable.xiaolian);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(view);
            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    25, 25);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());

        if (isAutoPlay) {
            startPlay();
        }
    }
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            // ((ViewPag.er)container).removeView((View)object);
            ((ViewPager) container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, final int position) {
            ImageView imageView = imageViewsList.get(position);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SliderShowViewItem jkitem =list.get(position);
                    String link = jkitem.getLink().substring(25,37);
                    jkModel.getJKArticleDetailItem(getContext(),link, new JKModelImpl.BaseListener() {
                        @Override
                        public void getSuccess(Object o) {
                            Intent intent = new Intent(getContext(),JKArticleDetailActivity.class);
                            intent.putExtra("article", (ArticleDetail) o);
                            getContext().startActivity(intent);
                        }
                        @Override
                        public void getFailure() {
                        }
                    });
                }
            });
            imageLoader.displayImage(imageView.getTag() + "", imageView,
                    options);
            ((ViewPager) container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {
        }

    }
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter()
                            .getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                        // foodnametextview.setText(foodnameList.get(0));
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager
                                .setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub
            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    ((View) dotViewsList.get(pos))
                            .setBackgroundResource(R.drawable.dot_focus);
                } else {
                    ((View) dotViewsList.get(i))
                            .setBackgroundResource(R.drawable.dot_blur);
                }
            }
        }

    }

    /**
     * 执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     *
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                // 解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }

    /**
     * ImageLoader 图片组件初始化
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.xiaolian)
                .showImageForEmptyUri(R.drawable.xiaolian)
                .showImageOnFail(R.drawable.xiaolian).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }

    public static class SliderShowViewItem implements Serializable {


        private String link;
        private String imgLink;

        public String getImgLink() {
            return imgLink;
        }

        public void setImgLink(String imgLink) {
            this.imgLink = imgLink;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
        public SliderShowViewItem() {
        }
    }
}


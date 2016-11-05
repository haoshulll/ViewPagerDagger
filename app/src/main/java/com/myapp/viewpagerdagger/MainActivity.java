package com.myapp.viewpagerdagger;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;// 视图页
    private TextView mDesTv;// 图片的描述的文本控件
    private LinearLayout mDotLly;// 存放圆点的容器布局
    private Context context;
    View v;

    private String[] mImageDes;// 图片描述文本
    private int[] mImageResIds;// 图片的资源id数组
    private List<ImageView> mData;
    private int mPreviosPosition=0;//保存圆点的位置

    private Handler myHandler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            //切换图片
            mViewPager.setCurrentItem((mViewPager.getCurrentItem()+1)%mData.size());//设置当前的视图项
            switchView();

        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1. 初始化视图
        initView();
        // 2.初始化数据
        initData();
        /**
         * 3. 对ViewPager装配数据 ，
         *   好比ListView  （1. 设置初始数据  2. 列表项的布局 3. 设置适配器 4. 监听列表项）
         */
        mViewPager.setAdapter(new BannerAdapter());

        //4. ViewPager视图项的页面改变监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 页面改变的监听
             * position：当前展示的页面在容器中的位置 ，索引号
             */
            @Override
            public void onPageSelected(int position) {
                //设置当前图片描述的文本
                mDesTv.setText(mImageDes[position]);
                //改圆点的状态
                //找到要改变状态的圆点
                mDotLly.getChildAt(position).setEnabled(true);
                mDotLly.getChildAt(mPreviosPosition).setEnabled(false);
                mPreviosPosition=position;




            }
            //页面滚动，则回调
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }
            //页面滚动状态改变，则回调
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 切换页面
        switchView();

    }

    private void switchView() {
        //定时切换页面
//		myHandler.postDelayed(r, delayMillis);
//		myHandler.sendMessageDelayed(msg, 500);// 延迟0.5秒发送消息
        myHandler.sendEmptyMessageDelayed(0, 2000);// 延迟0.5秒发送空消息

    }

    private void initData() {
        // 2.1 图片的描述的文本数据
        mImageDes = new String[] { "巩俐不低俗，我们就不低俗", "朴树又回来了，再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级", "乐视网TV大派送", "热血屌丝的反杀" };
        mDesTv.setText(mImageDes[0]);//设置初始值
        // 2.2 初始化图片数据，即要加载的图片
        mImageResIds = new int[] { R.drawable.a, R.drawable.b, R.drawable.c,
                R.drawable.d, R.drawable.e };
        //2.3 初始化圆点
        for(int i=0;i<mImageDes.length;i++){
            View dotView=new View(context);
            //设置背景
            dotView.setBackgroundResource(R.drawable.dot_select);//选择器

            /**
             * 要设置布局参数
             * width:宽度的规格
             * height:高度的规格
             */
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(getPx(8),
                    getPx(8));
            if(i!=0){//不是第一个点，则有左边的外边距
                layoutParams.leftMargin=getPx(5);//像素值
                dotView.setEnabled(false);
            }
            dotView.setLayoutParams(layoutParams);
            mDotLly.addView(dotView);
        }
        //2.4:初始化ViewPager要装配的数据 ,即要装配的五个视图页
        mData=new ArrayList<ImageView>();
        for(int i=0;i<mImageResIds.length;i++){
            ImageView imageView=new ImageView(context);
            imageView.setBackgroundResource(mImageResIds[i]);//设置背景
            mData.add(imageView);
        }


    }
    //把dp转换为px
    private int getPx(int dp) {
        return (int) (getResources().getDisplayMetrics().density*dp); // density:1.5*8=12
    }

    private void initView() {
        context = this;
        mViewPager = (ViewPager) findViewById(R.id.banner_vp);
        mDesTv = (TextView) findViewById(R.id.des_tv);
        mDotLly = (LinearLayout) findViewById(R.id.dot_lly);
    }

    //自定义ViewPager的适配器
    private class BannerAdapter extends PagerAdapter {
        //取得要装配的视图页的数量
        @Override
        public int getCount() {
            return mData==null?0:mData.size();
        }
        // 当前要展示的视图页对象是否来自缓存中存储的对象
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        /**初始化ViewPager的视图项，（列表项）
         * container: ViewPager
         * position:当前要初始化的视图项在容器（ViewPager）中的位置
         * return： 初始化后的视图项对象
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("instantiateItem--->"+position);
            // 把视图项添加到容器中
            container.addView(mData.get(position));

            return mData.get(position);
        }
        /**销毁ViewPager的视图
         * container： 容器 ViewPager
         * position： 当前要初始化的视图项在容器（ViewPager）中的位置
         * object：要销毁的对象
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            System.out.println("destroyItem--->"+position);
            mViewPager.removeView((View)object);
        }

    }
}


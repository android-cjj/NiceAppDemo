package com.shine.niceapp.ui.fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.extras.viewpager.PullToRefreshViewPager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.shine.niceapp.R;
import com.shine.niceapp.bean.Card;
import com.shine.niceapp.control.IRhythmItemListener;
import com.shine.niceapp.control.RhythmAdapter;
import com.shine.niceapp.control.RhythmLayout;
import com.shine.niceapp.control.ViewPagerScroller;
import com.shine.niceapp.ui.adapter.CardPagerAdapter;
import com.shine.niceapp.ui.widget.ProgressHUD;
import com.shine.niceapp.utils.AnimatorUtils;
import com.shine.niceapp.utils.HexUtils;
import com.shine.niceapp.utils.NetWorkHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * User: shine
 * Date: 2014-12-13
 * Time: 19:45
 * Description:
 */
public class CardViewPagerFragment extends AbsBaseFragment implements PullToRefreshBase.OnRefreshListener<ViewPager> {

    private TextView mTimeFirstText;
    private TextView mTimeSecondText;

    /**
     * 最外层的View，为了设置背景颜色而使用
     */
    private View mMainView;
    private ImageButton mRocketToHeadBtn;
    private Button mSideMenuOrBackBtn;
    /**
     * 钢琴布局
     */
    private RhythmLayout mRhythmLayout;
    /**
     * 可以侧拉刷新的ViewPager，其实是一个LinearLayout控件
     */
    private PullToRefreshViewPager mPullToRefreshViewPager;
    /**
     * 接收PullToRefreshViewPager中的ViewPager控件
     */
    private ViewPager mViewPager;
    /**
     * ViewPager的适配器
     */
    private CardPagerAdapter mCardPagerAdapter;
    /**
     * 记录上一个选项卡的颜色值
     */
    private int mPreColor;

    private boolean mHasNext = true;

    private boolean mIsRequesting;

    private boolean isAdapterUpdated;

    private int mCurrentViewPagerPage;


    private List<Card> mCardList;

    private ProgressHUD mProgressHUD;

    /**
     * 钢琴布局的适配器
     */
    private RhythmAdapter mRhythmAdapter;

    private static CardViewPagerFragment mFragment;

    /**
     * 自定义钢琴控件的监听器
     */
    private IRhythmItemListener rhythmItemListener = new IRhythmItemListener() {
        public void onRhythmItemChanged(int paramInt) {
        }

        public void onSelected(final int paramInt) {
            CardViewPagerFragment.this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    mViewPager.setCurrentItem(paramInt);
                }
            }, 100L);
        }

        public void onStartSwipe() {
        }
    };

//    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
//        public void onPageScrollStateChanged(int paramInt) {
//        }
//
//        public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
//            int childCount = mViewPager.getChildCount();
//            int width = mViewPager.getChildAt(0).getWidth();
//            int padding = (mViewPager.getWidth() - width) / 2;
//
//            for (int j = 0; j < childCount; j++) {
//                View v = mViewPager.getChildAt(j);
//                float rate = 0;
//                if (v.getLeft() <= padding) {
//                    if (v.getLeft() >= padding - v.getWidth()) {
//                        rate = (padding - v.getLeft()) * 1f / v.getWidth();
//                    } else {
//                        rate = 1;
//                    }
//                    v.setScaleY(1 - rate * 0.1f);
//                    v.setScaleX(1 - rate * 0.1f);
//
//                    Log.i("cjj", "rate-------->" + rate);
//                    Log.i("cjj", "paramInt1-------->" + paramInt1);
//                    Log.i("cjj", "paramFloat-------->" + paramFloat);
//                    Log.i("cjj", "paramInt2-------->" + paramInt2);
//                } else {
//                    if (v.getLeft() <= mViewPager.getWidth() - padding) {
//                        rate = (mViewPager.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
//                    }
//                    v.setScaleY(0.9f + rate * 0.1f);
//                    v.setScaleX(0.9f + rate * 0.1f);
//                }
//            }
//
//
//        }
//
//        public void onPageSelected(int position) {
//            onAppPagerChange(position);
//            if (mHasNext && (position > -10 + mCardList.size()) && !mIsRequesting && NetWorkHelper.isWifiDataEnable(getActivity())) {
//                fetchData();
//            }
//        }
//    };

    public static CardViewPagerFragment getInstance() {
        if (mFragment == null) {
            mFragment = new CardViewPagerFragment();
        }
        return mFragment;
    }


    @Override
    protected View initViews(LayoutInflater inflater) {
        //初始化控件
        View view = inflater.inflate(R.layout.fragment_niceapp, null);
        mTimeFirstText = (TextView) view.findViewById(R.id.text_time_first);
        mTimeSecondText = (TextView) view.findViewById(R.id.text_time_second);
        mMainView = view.findViewById(R.id.main_view);
        mRocketToHeadBtn = (ImageButton) view.findViewById(R.id.btn_rocket_to_head);
        mSideMenuOrBackBtn = (Button) view.findViewById(R.id.btn_side_menu_or_back);
        mRhythmLayout = (RhythmLayout) view.findViewById(R.id.box_rhythm);
        mPullToRefreshViewPager = (PullToRefreshViewPager) view.findViewById(R.id.pager);
        mViewPager = mPullToRefreshViewPager.getRefreshableView();

        //设置ViewPager的滚动速度
        setViewPagerScrollSpeed(mViewPager, 400);
        //设置ScrollView滚动动画延迟执行的时间
        mRhythmLayout.setScrollRhythmStartDelayTime(400);
        //设置钢琴布局的高度 高度为钢琴布局item的宽度+10dp
        int height = (int) mRhythmLayout.getRhythmItemWidth() + (int) TypedValue.applyDimension(1, 10.0F, getResources().getDisplayMetrics());
        mRhythmLayout.getLayoutParams().height = height;
        ((RelativeLayout.LayoutParams) mPullToRefreshViewPager.getLayoutParams()).bottomMargin = height;

        mTimeSecondText.setText("12月\n星期六");

        return view;
    }


    @Override
    protected void initActions(View paramView) {
        //设置控件的监听
        mRhythmLayout.setRhythmListener(rhythmItemListener);
        mPullToRefreshViewPager.setOnRefreshListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onAppPagerChange(position);
            if (mHasNext && (position > -10 + mCardList.size()) && !mIsRequesting && NetWorkHelper.isWifiDataEnable(getActivity())) {
                fetchData();
            }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRocketToHeadBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                CardViewPagerFragment.this.mViewPager.setCurrentItem(0, true);
            }
        });

    }

    @Override
    protected void initData() {
        mCardList = new ArrayList<>();
    }

    /**
     * 设置ViewPager的滚动速度，即每个选项卡的切换速度
     *
     * @param viewPager ViewPager控件
     * @param speed     滚动速度，毫秒为单位
     */
    private void setViewPagerScrollSpeed(ViewPager viewPager, int speed) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(viewPager.getContext(), new OvershootInterpolator(0.6F));
            field.set(viewPager, viewPagerScroller);
            viewPagerScroller.setDuration(speed);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变当前选中钢琴按钮
     *
     * @param position viewPager的位置
     */
    private void onAppPagerChange(int position) {
        //执行动画，改变升起的钢琴按钮
        mRhythmLayout.showRhythmAtPosition(position);
        toggleRocketBtn(position);
        Card post = this.mCardList.get(position);
        //得到当前的背景颜色
        int currColor = HexUtils.getHexColor(post.getBackgroundColor());
        //执行颜色转换动画
        AnimatorUtils.showBackgroundColorAnimation(this.mMainView, mPreColor, currColor, 400);
        mPreColor = currColor;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchData();
        onAppPagerChange(0);
    }

    /**
     * 加载数据
     */
    private void fetchData() {
        ArrayList<Card> cardList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int m = i % 10;
            Card card = addData(m);
            cardList.add(card);
        }
        mPreColor = HexUtils.getHexColor(cardList.get(0).getBackgroundColor());
        updateAppAdapter(cardList);
    }


    private void updateAppAdapter(List<Card> cardList) {
        if ((getActivity() == null) || (getActivity().isFinishing())) {
            return;
        }
        if (mProgressHUD != null && mProgressHUD.isShowing()) {
            this.mProgressHUD.dismiss();
            this.isAdapterUpdated = true;
        }
        if (cardList.isEmpty()) {
            this.mMainView.setBackgroundColor(this.mPreColor);
            return;
        }
        int size = mCardList.size();

        if (mCardPagerAdapter == null) {
            mCurrentViewPagerPage = 0;
            mCardPagerAdapter = new CardPagerAdapter(getActivity().getSupportFragmentManager(), cardList);
            mViewPager.setAdapter(mCardPagerAdapter);
        } else {
            mCardPagerAdapter.addCardList(cardList);
            mCardPagerAdapter.notifyDataSetChanged();
        }
        addCardIconsToDock(cardList);

        this.mCardList = mCardPagerAdapter.getCardList();

        if (mViewPager.getCurrentItem() == size - 1)
            mViewPager.setCurrentItem(1 + mViewPager.getCurrentItem(), true);
    }

    private void addCardIconsToDock(final List<Card> cardList) {
        if (mRhythmAdapter == null) {
            resetRhythmLayout(cardList);
            return;
        }
        mRhythmAdapter.addCardList(cardList);
        mRhythmAdapter.notifyDataSetChanged();
    }

    //重置钢琴控件数据源
    private void resetRhythmLayout(List<Card> cardList) {
        if (getActivity() == null)
            return;
        if (cardList == null)
            cardList = new ArrayList<>();
        mRhythmAdapter = new RhythmAdapter(getActivity(), mRhythmLayout, cardList);
        mRhythmLayout.setAdapter(mRhythmAdapter);
    }

    /**
     * viewPager刷新或加载更多监听
     *
     * @param pullToRefreshBase
     */
    public void onRefresh(PullToRefreshBase<ViewPager> pullToRefreshBase) {
        if (this.mIsRequesting)
            return;
        if (pullToRefreshBase.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {//最右
            mIsRequesting = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchData();
                    mPullToRefreshViewPager.onRefreshComplete();
                    mIsRequesting = false;
                }
            }, 2000);

        } else if (pullToRefreshBase.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {//最左
            mIsRequesting = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPullToRefreshViewPager.onRefreshComplete();
                    mIsRequesting = false;
                }
            }, 2000);
        }
    }

    /**
     * 根据当前viewPager的位置决定右上方的火箭图案是否显示
     *
     * @param position
     */
    private void toggleRocketBtn(int position) {
        if (position > 1) {
            if (mRocketToHeadBtn.getVisibility() == View.GONE) {
                mRocketToHeadBtn.setVisibility(View.VISIBLE);
                AnimatorUtils.animViewFadeIn(this.mRocketToHeadBtn);
            }
        } else if (this.mRocketToHeadBtn.getVisibility() == View.VISIBLE) {
            AnimatorUtils.animViewFadeOut(this.mRocketToHeadBtn).addListener(new Animator.AnimatorListener() {
                public void onAnimationCancel(Animator paramAnimator) {
                }

                public void onAnimationEnd(Animator paramAnimator) {
                    CardViewPagerFragment.this.mRocketToHeadBtn.setVisibility(View.GONE);
                }

                public void onAnimationRepeat(Animator paramAnimator) {
                }

                public void onAnimationStart(Animator paramAnimator) {
                }
            });
        }
        mTimeFirstText.setText((position + 1) + "");
    }


    private Card addData(int i) {
        Card card = new Card();
        switch (i) {
            case 0:
                card.setTitle("代码家");
                card.setSubTitle("daimajia");
                card.setDigest("A student in mainland China. Welcome to offer me an internship. If you have any new idea about this project, feel free to contact me");
                card.setAuthorName("cjj");
                card.setUpNum(6300);
                card.setBackgroundColor("#00aac6");
                card.setCoverImgerUrl("https://avatars1.githubusercontent.com/u/2503423?v=3&s=400");
                card.setIconUrl("https://avatars3.githubusercontent.com/u/2503423?v=3&s=192");
                break;
            case 1:
                card.setTitle("Huqiu Liao"
                       );
                card.setSubTitle("liaohuqiu");
                card.setDigest("Ultra Pull to Refresh for Android. Support all the views");
                card.setUpNum(2400);
                card.setAuthorName("cjj");
                card.setBackgroundColor("#dc4e97");
                card.setCoverImgerUrl("https://avatars0.githubusercontent.com/u/4088573?v=3&s=400");
                card.setIconUrl("https://avatars2.githubusercontent.com/u/4088573?v=3&s=192");
                break;
            case 2:
                card.setTitle("drakeet");
                card.setSubTitle("drakeet");
                card.setDigest("I am a student in China, I love reading pure literature, love Japanese culture and Hongkong music. At the same time, I am also obsessed with writing code. If you have any questions or want to make friends with me, you can write to me: drakeet.me@gmail.com");
                card.setUpNum(995);
                card.setAuthorName("cjj");
                card.setBackgroundColor("#00aac6");
                card.setCoverImgerUrl("https://avatars3.githubusercontent.com/u/5214214?v=3&s=460");
                card.setIconUrl("https://avatars3.githubusercontent.com/u/5214214?v=3&s=192");
                break;
            case 3:
                card.setTitle("Mr.Bao");
                card.setSubTitle("baoyongzhang");
                card.setDigest("A swipe menu for ListView.");
                card.setUpNum(560);
                card.setAuthorName("cjj");
                card.setBackgroundColor("#e76153");
                card.setCoverImgerUrl("https://avatars1.githubusercontent.com/u/4636761?v=3&s=460");
                card.setIconUrl("https://avatars1.githubusercontent.com/u/4636761?v=3&s=192");
                break;
            case 4:
                card.setTitle("xuyisheng");
                card.setSubTitle("xuyisheng");
                card.setDigest("Android Design Support Library Demo");
                card.setUpNum(268);
                card.setAuthorName("cjj");
                card.setBackgroundColor("#9a6dbb");
                card.setCoverImgerUrl("https://avatars3.githubusercontent.com/u/7419202?v=3&s=460");
                card.setIconUrl("https://avatars3.githubusercontent.com/u/7419202?v=3&s=192");
                break;
            case 5:
                card.setTitle("轻微");
                card.setSubTitle("zzz40500");
                card.setDigest("一个富有动感的Sheet(选择器)");
                card.setUpNum(305);
                card.setAuthorName("cjj");
                card.setBackgroundColor("#51aa53");
                card.setCoverImgerUrl("https://avatars2.githubusercontent.com/u/5378647?v=3&s=460");
                card.setIconUrl("https://avatars2.githubusercontent.com/u/5378647?v=3&s=192");
                break;
            case 6:
                card.setTitle("陈继军");
                card.setSubTitle("cjj");
                card.setDigest("我不生产轮子，我只是轮子的搬运工");
                card.setUpNum(293);
                card.setAuthorName("cjj");
                card.setBackgroundColor("#ea5272");
                card.setCoverImgerUrl("https://avatars1.githubusercontent.com/u/7941050?v=3&s=460");
                card.setIconUrl("https://avatars1.githubusercontent.com/u/7941050?v=3&s=460");
                break;
            case 7:
                card.setTitle("程序亦非猿" );
                card.setSubTitle("AlanCheen");
                card.setDigest("个人练习项目,记录成长之路");
                card.setUpNum(94);
                card.setAuthorName("cjj");
                card.setBackgroundColor("#e76153");
                card.setCoverImgerUrl("https://avatars2.githubusercontent.com/u/6982439?v=3&s=460");
                card.setIconUrl("https://avatars2.githubusercontent.com/u/6982439?v=3&s=460");
                break;
            case 8:
                card.setTitle("仔仔" );
                card.setSubTitle("SeniorZhai");
                card.setDigest("SeniorZhai:developer.zhaitao@gmail.com");
                card.setUpNum(74);
                card.setAuthorName("cjj");
                card.setBackgroundColor("#e76153");
                card.setCoverImgerUrl("https://avatars3.githubusercontent.com/u/5416585?v=3&s=460");
                card.setIconUrl("https://avatars3.githubusercontent.com/u/5416585?v=3&s=460");
                break;

            case 9:
                card.setTitle("Bob1993_Dev" );
                card.setSubTitle("Bob1993");
                card.setDigest("今天为它起一个霸气点的名字——挖掘机 哈哈，大家快来快来star!!");
                card.setUpNum(17);
                card.setAuthorName("cjj");
                card.setBackgroundColor("#e76153");
                card.setCoverImgerUrl("https://avatars0.githubusercontent.com/u/8554661?v=3&s=460");
                card.setIconUrl("https://avatars0.githubusercontent.com/u/8554661?v=3&s=192");
                break;
        }
        return card;
    }
}

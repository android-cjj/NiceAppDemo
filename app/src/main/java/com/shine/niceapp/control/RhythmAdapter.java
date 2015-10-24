package com.shine.niceapp.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.shine.niceapp.R;
import com.shine.niceapp.bean.Card;
import com.shine.niceapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class RhythmAdapter extends BaseAdapter {

    /**
     * item的宽度
     */
    private float itemWidth;
    /**
     * 数据源
     */
    private List<Card> mCardList;

    private LayoutInflater mInflater;
    private Context mContext;
    private RhythmLayout mRhythmLayout;

    public RhythmAdapter(Context context, RhythmLayout rhythmLayout, List<Card> cardList) {
        this.mContext = context;
        this.mRhythmLayout = rhythmLayout;
        this.mCardList = new ArrayList();
        this.mCardList.addAll(cardList);
        if (context != null)
            this.mInflater = LayoutInflater.from(context);
    }

    public List<Card> getCardList() {
        return this.mCardList;
    }

    public void addCardList(List<Card> cardList) {
        mCardList.addAll(cardList);
    }

    public int getCount() {
        return this.mCardList.size();
    }

    public Object getItem(int position) {
        return this.mCardList.get(position);
    }

    public long getItemId(int paramInt) {
        return (this.mCardList.get(paramInt)).getUid();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout relativeLayout = (RelativeLayout) this.mInflater.inflate(R.layout.adapter_rhythm_icon, null);
        //设置item布局的大小以及Y轴的位置
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams((int) itemWidth, mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height)));
        relativeLayout.setTranslationY(itemWidth);

        //设置第二层RelativeLayout布局的宽和高
        RelativeLayout childRelativeLayout = (RelativeLayout) relativeLayout.getChildAt(0);
        int relativeLayoutWidth = (int) itemWidth - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin);
        childRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayoutWidth, mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height) - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin)));

        ImageView imageIcon = (ImageView) relativeLayout.findViewById(R.id.image_icon);
        //计算ImageView的大小
        int iconSize = (relativeLayoutWidth - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin));
        ViewGroup.LayoutParams iconParams = imageIcon.getLayoutParams();
        iconParams.width = iconSize;
        iconParams.height = iconSize;
        imageIcon.setLayoutParams(iconParams);
        //设置背景图片
        Glide.with(mContext).load(mCardList.get(position).getIconUrl()).fitCenter().into(imageIcon);
        return relativeLayout;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.mRhythmLayout.invalidateData();
    }

    public void setCardList(List<Card> paramList) {
        this.mCardList = paramList;
    }

    /**
     * 设置每个item的宽度
     */
    public void setItemWidth(float width) {
        this.itemWidth = width;
    }
}
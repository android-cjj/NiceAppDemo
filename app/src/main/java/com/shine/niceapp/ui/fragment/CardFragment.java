package com.shine.niceapp.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shine.niceapp.R;
import com.shine.niceapp.bean.Card;
import com.shine.niceapp.ui.widget.HtmlTextView;
import com.shine.niceapp.utils.AppUtils;

public class CardFragment extends AbsBaseFragment {
    protected Card mCard;


    protected TextView mAuthorText;
    protected ImageView mBottomEdgeImageView;
    protected TextView mBravoNumText;
    protected RelativeLayout mCardLayout;
    protected ImageView mCoverImageView;
    protected HtmlTextView mDigestText;
    protected TextView mSubTitleText;
    protected TextView mTitleText;

    public static CardFragment getInstance(Card card) {
        CardFragment localCardFragment = new CardFragment();
        Bundle localBundle = new Bundle();
        localBundle.putSerializable("card", card);
        localCardFragment.setArguments(localBundle);
        return localCardFragment;
    }


    protected View initViews(LayoutInflater paramLayoutInflater) {
        View view = paramLayoutInflater.inflate(R.layout.fragment_card, null);
        mCardLayout = ((RelativeLayout) view.findViewById(R.id.box_card));
        mBottomEdgeImageView = ((ImageView) view.findViewById(R.id.image_bottom_edge));
        mCoverImageView = ((ImageView) view.findViewById(R.id.image_cover));
        mTitleText = ((TextView) view.findViewById(R.id.text_title));
        mSubTitleText = ((TextView) view.findViewById(R.id.text_subtitle));
        mDigestText = ((HtmlTextView) view.findViewById(R.id.text_digest));
        mAuthorText = ((TextView) view.findViewById(R.id.text_author));
        mBravoNumText = ((TextView) view.findViewById(R.id.text_bravos));

        mTitleText.setText(this.mCard.getTitle());
        mSubTitleText.setText(this.mCard.getSubTitle());
        this.mBravoNumText.setText("  " + this.mCard.getUpNum());
        this.mDigestText.setTextViewHtml(mCard.getDigest());
        this.mAuthorText.setText(Html.fromHtml("<B>" + this.mCard.getAuthorName() + "</B>"));
        initAndDisplayCoverImage();

        return view;
    }

    protected void initAndDisplayCoverImage() {
        int coverWidth = AppUtils.getScreenDisplayMetrics(getActivity()).widthPixels - 2 * getResources().getDimensionPixelSize(R.dimen.card_margin);
        int coverHeight = (int) (180.0F * (coverWidth / 320.0F));
        ViewGroup.LayoutParams localLayoutParams = this.mCoverImageView.getLayoutParams();
        localLayoutParams.height = Float.valueOf(coverHeight).intValue();

        Glide.with(getActivity()).load(mCard.getCoverImgerUrl()).centerCrop().into(mCoverImageView);
    }

    protected void initData() {
        this.mCard = (Card) getArguments().getSerializable("card");
    }

    protected void initActions(View paramView) {
    }

    public void onDestroy() {
        this.mCoverImageView.setImageBitmap(null);
        super.onDestroy();
    }

    public void onDestroyView() {
        this.mCoverImageView.setImageBitmap(null);
        super.onDestroyView();
    }
}
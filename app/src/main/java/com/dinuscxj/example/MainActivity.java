package com.dinuscxj.example;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.EditText;

import com.dinuscxj.ellipsize.EllipsizeTextView;
import com.dinuscxj.ellipsizeexample.R;

public class MainActivity extends AppCompatActivity {
    private EllipsizeTextView mTvEllipsize0;
    private EllipsizeTextView mTvEllipsize1;
    private EllipsizeTextView mTvEllipsize2;
    private EllipsizeTextView mTvEllipsize3;
    private EllipsizeTextView mTvEllipsize4;
    private EllipsizeTextView mTvEllipsize5;
    private EllipsizeTextView mTvEllipsize6;
    private EllipsizeTextView mTvEllipsize7;

    private EditText mEtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvEllipsize0 = (EllipsizeTextView) findViewById(R.id.tv_ellipsize0);
        mTvEllipsize1 = (EllipsizeTextView) findViewById(R.id.tv_ellipsize1);
        mTvEllipsize2 = (EllipsizeTextView) findViewById(R.id.tv_ellipsize2);
        mTvEllipsize3 = (EllipsizeTextView) findViewById(R.id.tv_ellipsize3);
        mTvEllipsize4 = (EllipsizeTextView) findViewById(R.id.tv_ellipsize4);
        mTvEllipsize5 = (EllipsizeTextView) findViewById(R.id.tv_ellipsize5);
        mTvEllipsize6 = (EllipsizeTextView) findViewById(R.id.tv_ellipsize6);
        mTvEllipsize7 = (EllipsizeTextView) findViewById(R.id.tv_ellipsize7);

        mEtInput = (EditText) findViewById(R.id.input_text);

        setUpTvEllipsize0();
        setUpTvEllipsize1();
        setUpTvEllipsize2();
        setUpTvEllipsize3();
        setUpTvEllipsize4();
        setUpTvEllipsize5();
        setUpTvEllipsize6();
        setUpTvEllipsize7();
    }

    private void setUpTvEllipsize0() {
        SpannableString moreText = new SpannableString("...");
        moreText.setSpan(new ForegroundColorSpan(Color.RED), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvEllipsize0.setEllipsizeText(moreText, 0);
        mTvEllipsize0.setText(R.string.long_text);
    }

    private void setUpTvEllipsize1() {
        SpannableString moreText = new SpannableString("...");
        moreText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvEllipsize1.setEllipsizeText(moreText, 0);
        mTvEllipsize1.setText(R.string.long_text);
    }

    private void setUpTvEllipsize2() {
        SpannableString moreText = new SpannableString("***");
        moreText.setSpan(new ForegroundColorSpan(Color.CYAN), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvEllipsize2.setEllipsizeText(moreText, 0);
        mTvEllipsize2.setText(R.string.long_text);
    }

    private void setUpTvEllipsize3() {
        SpannableString moreText = new SpannableString("***");
        moreText.setSpan(new ForegroundColorSpan(Color.GREEN), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvEllipsize3.setEllipsizeText(moreText, 8);
        mTvEllipsize3.setText(R.string.long_text);
    }

    private void setUpTvEllipsize4() {
        final String timeText = " 1 minute ago";
        final SpannableString timeLongText = new SpannableString(getString(R.string.long_text) + timeText);
        timeLongText.setSpan(new TextAppearanceSpan(this, R.style.time_style),
                timeLongText.length() - timeText.length(), timeLongText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString moreText = new SpannableString("...more");
        moreText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                mTvEllipsize4.setText(timeLongText);
                mTvEllipsize4.setMaxLines(Integer.MAX_VALUE);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 3, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        moreText.setSpan(new TextAppearanceSpan(this, R.style.link_style),
                3, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvEllipsize4.setMovementMethod(LinkMovementMethod.getInstance());
        mTvEllipsize4.setText(timeLongText);
        mTvEllipsize4.setEllipsizeText(moreText, timeText.length());
    }

    private void setUpTvEllipsize5() {
        int colors[] = new int[] {Color.GRAY, Color.MAGENTA, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE};
        SpannableString longNumberText = new SpannableString(getString(R.string.long_number_text));
        for (int i = 0; i < longNumberText.length(); i += 10) {
            longNumberText.setSpan(new ForegroundColorSpan(colors[i / 10 % colors.length]),
                    i, i + 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mTvEllipsize5.setText(longNumberText);
    }

    private void setUpTvEllipsize6() {
        SpannableString moreText = new SpannableString("...");
        moreText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvEllipsize6.setEllipsizeText(moreText, 0);
        mTvEllipsize6.setText(R.string.long_text);
    }

    private void setUpTvEllipsize7() {
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvEllipsize7.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

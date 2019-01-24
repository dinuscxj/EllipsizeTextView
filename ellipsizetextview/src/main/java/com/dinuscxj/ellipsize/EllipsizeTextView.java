package com.dinuscxj.ellipsize;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;

public class EllipsizeTextView extends AppCompatTextView {
    private static final String DEFAULT_ELLIPSIZE_TEXT = "...";

    private CharSequence mEllipsizeText;
    private CharSequence mOriginText;

    private int mEllipsizeIndex;
    private int mMaxLines;

    private boolean mIsExactlyMode;
    private boolean mEnableUpdateOriginText = true;

    public EllipsizeTextView(Context context) {
        this(context, null);
    }

    public EllipsizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EllipsizeTextView);
        mEllipsizeIndex = ta.getInt(R.styleable.EllipsizeTextView_ellipsize_index, 0);
        mEllipsizeText = ta.getText(R.styleable.EllipsizeTextView_ellipsize_text);

        if (mEllipsizeText == null) {
            mEllipsizeText = DEFAULT_ELLIPSIZE_TEXT;
        }
        ta.recycle();
    }


    @Override
    public void setMaxLines(int maxLines) {
        if (mMaxLines != maxLines) {
            super.setMaxLines(maxLines);
            this.mMaxLines = maxLines;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setText(mOriginText);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            mIsExactlyMode = View.MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY;
            final Layout layout = getLayout();
            if (layout != null) {
                if (isExceedMaxLine(layout) || isOutOfBounds(layout)) {
                    adjustEllipsizeEndText(layout);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (mEnableUpdateOriginText) {
            mOriginText = text;
        }

        super.setText(text, type);

        if (mIsExactlyMode) {
            requestLayout();
        }
    }

    private boolean isExceedMaxLine(Layout layout) {
        return layout.getLineCount() > mMaxLines && mMaxLines > 0;
    }

    private boolean isOutOfBounds(Layout layout) {
        return layout.getHeight() > getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
    }

    private void adjustEllipsizeEndText(Layout layout) {
        final CharSequence originText = mOriginText;
        final CharSequence restSuffixText = originText.subSequence(
                originText.length() - mEllipsizeIndex, originText.length());

        final int width = layout.getWidth() - getPaddingLeft() - getPaddingRight();
        final int maxLineCount = Math.max(1, computeMaxLineCount(layout));
        final int lastLineWidth = (int) layout.getLineWidth(maxLineCount - 1);
        int mLastCharacterIndex = layout.getLineEnd(maxLineCount - 1);
        while (originText.toString().charAt(mLastCharacterIndex-1) == '\n') {
            // remove any \n that are present, because the read more link will not be shown otherwise
            mLastCharacterIndex = mLastCharacterIndex - 1;
        }

        final int suffixWidth = (int) (Layout.getDesiredWidth(mEllipsizeText, getPaint()) +
                Layout.getDesiredWidth(restSuffixText, getPaint())) + 1;

        mEnableUpdateOriginText = false;
        if (lastLineWidth + suffixWidth > width) {
            final int widthDiff = lastLineWidth + suffixWidth - width;

            final int removedCharacterCount = computeRemovedEllipsizeEndCharacterCount(widthDiff,
                    originText.subSequence(0, mLastCharacterIndex));

            setText(originText.subSequence(0, mLastCharacterIndex - removedCharacterCount));
            append(mEllipsizeText);
            append(restSuffixText);
        } else {
            setText(originText.subSequence(0, mLastCharacterIndex));
            append(mEllipsizeText);
            append(restSuffixText);
        }

        mEnableUpdateOriginText = true;
    }

    private int computeMaxLineCount(Layout layout) {
        int availableHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        for (int i = 0; i < layout.getLineCount(); i++) {
            if (availableHeight < layout.getLineBottom(i)) {
                return i;
            }
        }

        return layout.getLineCount();
    }

    private int computeRemovedEllipsizeEndCharacterCount(final int widthDiff, final CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }

        final List<Range<Integer>> characterStyleRanges = computeCharacterStyleRanges(text);
        final String textStr = text.toString();

        // prevent the subString from containing messy code when the given string contains emotion
        int characterIndex = text.length();
        int codePointIndex = textStr.codePointCount(0, text.length());
        int currentRemovedWidth = 0;

        while (codePointIndex > 0 && widthDiff > currentRemovedWidth) {
            codePointIndex--;
            characterIndex = textStr.offsetByCodePoints(0, codePointIndex);

            // prevent the subString from containing messy code when the given string contains CharacterStyle
            Range<Integer> characterStyleRange = computeCharacterStyleRange(characterStyleRanges, characterIndex);
            if (characterStyleRange != null) {
                characterIndex = characterStyleRange.getLower();
                codePointIndex = textStr.codePointCount(0, characterIndex);
            }

            currentRemovedWidth = (int) Layout.getDesiredWidth(
                    text.subSequence(characterIndex, text.length()),
                    getPaint());
        }

        return text.length() - textStr.offsetByCodePoints(0, codePointIndex);
    }

    private Range<Integer> computeCharacterStyleRange(List<Range<Integer>> characterStyleRanges, int index) {
        if (characterStyleRanges == null || characterStyleRanges.isEmpty()) {
            return null;
        }

        for (Range<Integer> characterStyleRange : characterStyleRanges) {
            if (characterStyleRange.contains(index)) {
                return characterStyleRange;
            }
        }

        return null;
    }

    private List<Range<Integer>> computeCharacterStyleRanges(CharSequence text) {
        final SpannableStringBuilder ssb = SpannableStringBuilder.valueOf(text);
        final CharacterStyle[] characterStyles = ssb.getSpans(0, ssb.length(), CharacterStyle.class);

        if (characterStyles == null || characterStyles.length == 0) {
            return Collections.EMPTY_LIST;
        }

        List<Range<Integer>> ranges = new ArrayList<>();
        for (CharacterStyle characterStyle : characterStyles) {
            ranges.add(new Range<>(ssb.getSpanStart(characterStyle), ssb.getSpanEnd(characterStyle)));
        }

        return ranges;
    }

    /**
     * @param ellipsizeText  causes words in the text that are longer than the view is wide
     *                       to be ellipsized by used the text instead of broken in the middle.
     * @param ellipsizeIndex the index of the ellipsizeText will be inserted in the reverse order.
     */
    public void setEllipsizeText(CharSequence ellipsizeText, int ellipsizeIndex) {
        this.mEllipsizeText = ellipsizeText;
        this.mEllipsizeIndex = ellipsizeIndex;
    }

    public static final class Range<T extends Comparable<? super T>> {

        private final T mLower;
        private final T mUpper;

        public Range(final T lower, final T upper) {
            mLower = lower;
            mUpper = upper;

            if (lower.compareTo(upper) > 0) {
                throw new IllegalArgumentException("lower must be less than or equal to upper");
            }
        }

        public T getLower() {
            return mLower;
        }

        public T getUpper() {
            return mUpper;
        }

        public boolean contains(T value) {

            boolean gteLower = value.compareTo(mLower) >= 0;
            boolean lteUpper = value.compareTo(mUpper) < 0;

            return gteLower && lteUpper;
        }
    }
}

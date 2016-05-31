package com.matteo.cc.ui.view;

import com.matteo.cc.model.XString;
import com.matteo.cc.model.XString.SearchResult;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class XTextView extends TextView {

	public XTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setText(XString text) {
		super.setText(text.toString());
	}

	public void setText(XString text, boolean isSearchResult) {
		if (isSearchResult && text.getSearchResult() != null) {
			this.setText(text.toString(), text.getSearchResult());
		} else {
			super.setText(text.toString());
		}
	}

	public void setText(String text, SearchResult result) {

		SpannableString ss = new SpannableString(text);

		ss.setSpan(new ForegroundColorSpan(Color.RED), result.getStart(),
				result.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		super.setText(ss);
	}
}

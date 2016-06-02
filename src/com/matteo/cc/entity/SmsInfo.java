package com.matteo.cc.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.utils.ContactUtil;
import com.matteo.cc.utils.XTimeUtils;
import android.annotation.SuppressLint;
import android.provider.Telephony.Sms;
import android.text.TextUtils;

@SuppressLint("InlinedApi")
public class SmsInfo {
	public static final int MESSAGE_TYPE_INBOX = Sms.MESSAGE_TYPE_INBOX;
	public static final int MESSAGE_TYPE_SENT =  Sms.MESSAGE_TYPE_SENT;
	public static final int MESSAGE_TYPE_DRAFT =  Sms.MESSAGE_TYPE_DRAFT;
	public static final int MESSAGE_TYPE_OUTBOX = Sms.MESSAGE_TYPE_OUTBOX;
	public int mId;
	public int mThreadId;
	private String mNumber;
	// public String mPersonName;
	private int mContactId;
	private boolean mReadContact = false;
	public Date mDate;
	public boolean mRead;
	public int mStatus;
	public int mType;
	public String mBody;
	private String mDisplayDate;
	private ContactInfo mContact;

	public String getDisplayDate() {
		if (TextUtils.isEmpty(this.mDisplayDate)) {
			this.mDisplayDate = XTimeUtils.formatDateWithWeekDay(this.mDate);
		}
		return this.mDisplayDate;
	}

	public void setNumber(String number) {
		this.mNumber = number.replace(" ", "");
	}

	public String getNumber() {
		return this.mNumber;
	}

	public String getDisplayName() {
		if (this.getContact() == null) {
			return mNumber;
		}
		return this.getContact().mName.toString();
	}

	public void setContactId(int contactId) {
		this.mContactId = contactId;
	}
	
	public boolean isRecv(){
		return this.mType==MESSAGE_TYPE_INBOX;
	}

	public ContactInfo getContact() {
		if (!this.mReadContact) {
			List<ContactInfo> contacts = ContentManager.get().getContacts();
			this.mContact = ContactUtil.getContactById(this.mContactId,
					contacts);
			if (this.mContact == null) {
				this.mContact = ContactUtil.getContactByNumber(this.mNumber,
						contacts);
			}
			this.mReadContact = true;
		}
		return this.mContact;
	}

	public static class SmsThreadInfo extends ArrayList<SmsInfo> implements
			Comparable<SmsThreadInfo> {

		private static final long serialVersionUID = 1L;
		public int mId;
		public SmsInfo mNewestSms = null;

		public String getDisplayName() {
			return this.mNewestSms.getDisplayName();
		}

		public ContactInfo getContact() {
			return this.mNewestSms.getContact();
		}

		@Override
		public boolean add(SmsInfo sms) {
			if (this.mNewestSms == null) {
				this.mNewestSms = sms;
			} else {
				if (sms.mDate.after(this.mNewestSms.mDate)) {
					this.mNewestSms = sms;
				}
			}
			return super.add(sms);
		}

		@Override
		public int compareTo(SmsThreadInfo another) {
			return another.mNewestSms.mDate.compareTo(this.mNewestSms.mDate);
		}
	}
}

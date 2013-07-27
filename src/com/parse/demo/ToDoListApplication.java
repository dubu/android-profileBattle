package com.parse.demo;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ToDoListApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "jIMnY07BUyA2xgkCjmnXqAvEDPjtBpDoLqoS2qCF", "Y1OHobydfhRtD23n8RXc30nzFSKrb83Y4F2oUAP1");
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access.
		// defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
	}
}

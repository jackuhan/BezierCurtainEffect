package com.example.curtaineffect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import com.example.curtaineffect.view.OutCurtainLayout;

public class SecondActivity extends Activity {

	private OutCurtainLayout outCurtainLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		outCurtainLayout = (OutCurtainLayout)findViewById(R.id.out_curtain_layout);
		initView();
	}

	private void initView() {
		// TODO: 16/7/26
		//outCurtainLayout.setPromotionHeader();
		//outCurtainLayout.setCurtainGoodsLayout();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

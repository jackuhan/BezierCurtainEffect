package com.example.curtaineffect.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.curtaineffect.R;

/**
 * 窗帘拉开自定义布局
 *
 * created by Hanjiahu
 */
public class OutCurtainLayout extends RelativeLayout implements CurtainDownView.OnPullDownOffsetListener {
  private static String TAG = "CurtainView";
  private Context mContext;
  private ImageView header;
  private CurtainDownView curtainView;
  private CurtainTopView curtainTopView;
  //private RelativeLayout goodsLayout;

  public OutCurtainLayout(Context context) {
    super(context);
    init(context, null);
  }

  public OutCurtainLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  public OutCurtainLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  /** 初始化 */
  private void init(Context context, AttributeSet attrs) {
    this.mContext = context;
    if (null != attrs) {
    } else {
    }

    // 背景设置成透明
    this.setBackgroundColor(Color.argb(0, 0, 0, 0));
    final View view = LayoutInflater.from(mContext).inflate(R.layout.out_curtain, null);
    header = (ImageView) view.findViewById(R.id.header);
    curtainView = (CurtainDownView) view.findViewById(R.id.curtain_view);
    curtainTopView = (CurtainTopView) view.findViewById(R.id.curtain_top_view);
    addView(view);
    curtainView.setOnPullDownOffsetListener(this);
    curtainTopView.setOnOutCurtainListener(new CurtainTopView.OnOutCurtainListener() {
      @Override public void onPullDownOffset(float offset) {

      }

      @Override public void isOpen(boolean bOpen) {
        Log.w("上层窗帘回调","isOpen= "+bOpen);
        if(bOpen){
          //header.setVisibility(GONE);
          //curtainTopView.openCurtain(10);
        } else {
          //header.setVisibility(VISIBLE);
          curtainView.closeCurtain(10);
        }
      }
    });
    view.findViewById(R.id.header).setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        curtainTopView.openCurtain(500);
      }
    });
  }

  public void onPullDownOffset(float offset) {
    Log.e("onPullDownOffset","offset= "+offset);
    //if (offset >= CurtainView.touchScrollHeight) {
    //  header.setVisibility(GONE);
    //  goodsLayout.setVisibility(VISIBLE);
    //  header.setAlpha(255);
    //} else if (offset <= 0) {
    //  header.setVisibility(VISIBLE);
    //  goodsLayout.setVisibility(GONE);
    //  header.setAlpha(255);
    //} else {
    //  //header.setVisibility(VISIBLE);
    //  //goodsLayout.setVisibility(GONE);
    //  //header.setAlpha((int)(255*offset/CurtainView.touchScrollHeight));
    //}
  }

  public void isOpen(boolean bOpen){
    Log.e("下层窗帘回调","isOpen= "+bOpen);
    if(bOpen){
      //header.setVisibility(GONE);
      curtainTopView.openCurtain(10);
    } else {
      //header.setVisibility(VISIBLE);
    }
  }
}

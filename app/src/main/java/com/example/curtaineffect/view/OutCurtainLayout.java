package com.example.curtaineffect.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
  private View promotionHeader;
  private CurtainDownView curtainView;
  private CurtainTopView curtainTopView;

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
    promotionHeader = (View) view.findViewById(R.id.promotion_header);
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
        } else {
          curtainView.closeCurtain(10);
        }
      }
    });
  }

  //设置底层头部内容
  public void setCurtainGoodsLayout(View curtainGoodsLayout) {
    curtainTopView.setCurtainGoodsLayout(curtainGoodsLayout);
  }

  //设置上层内容区域
  public void setPromotionHeader(View promotionHeader) {
    this.promotionHeader = promotionHeader;
  }

  public void onPullDownOffset(float offset) {
    Log.e("onPullDownOffset","offset= "+offset+" alpha="+(255- 255*offset/CurtainDownView.touchScrollHeight));
    if (offset >= CurtainDownView.touchScrollHeight) {
      promotionHeader.setVisibility(GONE);
      promotionHeader.setAlpha(1f);
    } else if (offset <= 0) {
      promotionHeader.setVisibility(VISIBLE);
      promotionHeader.setAlpha(1f);
    } else {
      promotionHeader.setVisibility(VISIBLE);
      promotionHeader.setAlpha((float) (255- 255*offset/CurtainDownView.touchScrollHeight)/255);
    }
  }

  public void isOpen(boolean bOpen){
    Log.e("下层窗帘回调","isOpen= "+bOpen);
    if(bOpen){
      curtainTopView.openCurtain(10);
      promotionHeader.setVisibility(VISIBLE);
      promotionHeader.setAlpha(255);
    } else {
      promotionHeader.setVisibility(VISIBLE);
      promotionHeader.setAlpha(255);
    }
  }
}

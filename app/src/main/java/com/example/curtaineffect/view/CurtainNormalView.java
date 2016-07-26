package com.example.curtaineffect.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import com.example.curtaineffect.R;
import com.example.curtaineffect.tools.BaseTools;
import com.example.curtaineffect.tools.Device;

/**
 * 窗帘拉开自定义布局
 *
 * create by Ra blog : http://blog.csdn.net/vipzjyno1/
 * modified by Hanjiahu
 */
public class CurtainNormalView extends RelativeLayout implements OnTouchListener {
  private static String TAG = "CurtainView";
  private Context mContext;
  private Scroller mScroller;
  private int mScreenHeigh = 0;
  private int mScreenWidth = 0;
  /** 点击时候Y的坐标 */
  private int downY = 0;
  /** 拖动时候Y的坐标 */
  private int moveY = 0;
  /** 拖动时候Y的方向距离 */
  private int scrollY = 0;
  /** 松开时候Y的坐标 */
  private int upY = 0;
  /** 广告幕布的高度 */
  private int curtainHeigh = 0;
  /** 是否 打开 */
  private boolean isOpen = false;
  /** 是否在动画 */
  private boolean isMove = false;
  /** 绳子的图片 */
  private ImageView img_curtain_rope;
  private TextView tv_curtain_rope;
  /** 广告的图片 */
  private View img_curtain_ad;
  /** 上升动画时间 */
  private int upDuration = 1000;
  /** 下落动画时间 */
  private int downDuration = 500;
  BezierViewFrameLayout bezierViewFrameLayout;
  private int mJellyColor;
  //拖动150像素有效滑动
  public static float touchScrollHeight = 150;

  public void setTouchScrollHeight(int mTouchScrollHeight) {
    touchScrollHeight = mTouchScrollHeight;
  }

  public CurtainNormalView(Context context) {
    super(context);
    init(context, null);
  }

  public CurtainNormalView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  public CurtainNormalView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  /** 初始化 */
  private void init(Context context, AttributeSet attrs) {
    this.mContext = context;
    if (null != attrs) {
      TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CurtainDownView);
      touchScrollHeight = ta.getDimension(R.styleable.CurtainDownView_touchScrollHeight, Device.sp2px(getResources(), 75));
      mJellyColor = ta.getColor(R.styleable.CurtainDownView_jellyColor, getResources().getColor(android.R.color.white));
    } else {
      touchScrollHeight = Device.sp2px(getResources(), 75);
      mJellyColor = getResources().getColor(android.R.color.white);
    }
    //Interpolator 设置为有反弹效果的  （Bounce：反弹）
    Interpolator interpolator = new BounceInterpolator();
    mScroller = new Scroller(context, interpolator);
    mScreenHeigh = BaseTools.getWindowHeigh(context);
    mScreenWidth = BaseTools.getWindowWidth(context);
    // 背景设置成透明
    this.setBackgroundColor(Color.argb(0, 0, 0, 0));
    final View view = getView();
    img_curtain_ad = (View) view.findViewById(R.id.curtain_gooods_layout);
    img_curtain_rope = (ImageView) view.findViewById(R.id.img_curtain_rope);
    tv_curtain_rope = (TextView) view.findViewById(R.id.tv_curtain_rope);
    bezierViewFrameLayout = (BezierViewFrameLayout) view.findViewById(R.id.bezier_layout);
    addView(view);
    img_curtain_ad.post(new Runnable() {

      @Override public void run() {
        curtainHeigh = img_curtain_ad.getHeight();
        Log.d(TAG, "curtainHeigh= "
            + curtainHeigh
            + " mScreenHeigh="
            + mScreenHeigh
            + " img_curtain_rope.heigeht="
            + img_curtain_rope.getHeight());
        CurtainNormalView.this.scrollTo(0, curtainHeigh);
        bezierViewFrameLayout.setMinimumHeight(tv_curtain_rope.getHeight());
        Log.d("ttttttttt", "" + (tv_curtain_rope.getHeight()));
        bezierViewFrameLayout.setJellyHeight(bezierViewFrameLayout.getHeight());
        bezierViewFrameLayout.setJellyColor(mJellyColor);
        //注意scrollBy和scrollTo的区别
      }
    });
    img_curtain_ad.setOnTouchListener(this);
    bezierViewFrameLayout.setOnTouchListener(this);
  }

  private View getView() {
    return LayoutInflater.from(mContext).inflate(R.layout.curtain_down, null);
  }

  public void closeCurtain(int mDuration) {
    isOpen = false;
    CurtainNormalView.this.startMoveAnim(0, curtainHeigh, mDuration );
  }

  public void openCurtain(int mDuration) {
    isOpen = true;
    CurtainNormalView.this.startMoveAnim(curtainHeigh, -curtainHeigh, mDuration);
  }

  /**
   * 拖动动画
   *
   * @param dy 垂直距离, 滚动的y距离
   * @param duration 时间
   */
  public void startMoveAnim(int startY, int dy, int duration) {
    isMove = true;
    mScroller.startScroll(0, startY, 0, dy, duration);
    invalidate();//通知UI线程的更新
    if (dy > 0) {
      //关闭
      if (null != mOnPullDownOffsetListener) {
        mOnPullDownOffsetListener.onPullDownOffset(touchScrollHeight);
        this.postDelayed(new Runnable() {
          @Override public void run() {
            mOnPullDownOffsetListener.isOpen(false);
          }
        },downDuration);
      }
    } else {
      //打开
      if (null != mOnPullDownOffsetListener) {
        mOnPullDownOffsetListener.onPullDownOffset(touchScrollHeight);
        this.postDelayed(new Runnable() {
          @Override public void run() {
            mOnPullDownOffsetListener.isOpen(true);
          }
        },downDuration);
      }
    }
  }

  /**
   * 点击绳索开关，会展开关闭
   * 在onToch中使用这个中的方法来当点击事件，避免了点击时候响应onTouch的衔接不完美的影响
   */
  public void onRopeClick() {
    if (isOpen) {
      closeCurtain(downDuration);
      beserViewClose();
      isOpen = false;
      Log.d("ttttttttt点击", "" + (tv_curtain_rope.getHeight()));
    } else {
      openCurtain(downDuration);
      Log.d("ttttttttt点击下", "" + (bezierViewFrameLayout.getHeight()));
      beserViewOpen();
      isOpen = true;
    }
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
  }

  @Override public void computeScroll() {
    //判断是否还在滚动，还在滚动为true
    if (mScroller.computeScrollOffset()) {
      scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      //更新界面
      postInvalidate();
      isMove = true;
    } else {
      isMove = false;
    }
    super.computeScroll();
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    if (!isMove) {
      int offViewY = 0;//屏幕顶部和该布局顶部的距离
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          downY = (int) event.getRawY();
          offViewY = downY - (int) event.getX();
          return true;
        case MotionEvent.ACTION_MOVE:
          moveY = (int) event.getRawY();
          scrollY = moveY - downY;
          Log.e("hanjiahu", "scrollY=" + scrollY);
          if (null != mOnPullDownOffsetListener) {
            mOnPullDownOffsetListener.onPullDownOffset(scrollY);
          }
          if (scrollY < 0) {
            // 向上滑动
            if (isOpen) {
              if (Math.abs(scrollY) <= img_curtain_ad.getBottom() - offViewY) {
                scrollTo(0, -scrollY);
                Log.e("hanjiahu", "scrollTo=" + (-scrollY));
              }
            }
          } else {
            // 向下滑动
            if (!isOpen) {
              if (scrollY <= curtainHeigh) {
                scrollTo(0, curtainHeigh - scrollY);
                Log.e("hanjiahu", "scrollTo=" + (curtainHeigh - scrollY));
                if (((int) (img_curtain_rope.getHeight())) >= scrollY) {
                  bezierViewFrameLayout.setMinimumHeight(tv_curtain_rope.getHeight() + (int) scrollY);
                  Log.d("ttttttttt", "" + (tv_curtain_rope.getHeight() + (int) scrollY));
                  bezierViewFrameLayout.invalidate();
                }
              }
            }
          }
          break;
        case MotionEvent.ACTION_UP:
          upY = (int) event.getRawY();
          if (Math.abs(upY - downY) < 10) {
            onRopeClick();
            break;
          }
          if (downY > upY) {
            // 向上滑动
            if (isOpen) {
              if (Math.abs(scrollY) > touchScrollHeight) {
                // 向上滑动超过touchScrollHeight的时候 开启向上消失动画
                startMoveAnim(this.getScrollY(), (curtainHeigh - this.getScrollY()), upDuration);
                isOpen = false;
                beserViewClose();
                Log.d("ttttttttt向上滑动", "" + (tv_curtain_rope.getHeight()));
              } else {
                startMoveAnim(this.getScrollY(), -this.getScrollY(), upDuration);
                isOpen = true;
                beserViewOpen();
                Log.d("ttttttttt向上滑动", "" + (bezierViewFrameLayout.getHeight()));
              }
            }
          } else {
            // 向下滑动
            if (scrollY > touchScrollHeight) {
              // 向下滑动超过touchScrollHeight的时候 开启向下展开动画
              startMoveAnim(this.getScrollY(), -this.getScrollY(), upDuration);
              isOpen = true;
              beserViewOpen();
              Log.d("ttttttttt向下滑动", "" + (bezierViewFrameLayout.getHeight()));
            } else {
              startMoveAnim(this.getScrollY(), (curtainHeigh - this.getScrollY()), upDuration);
              isOpen = false;
              beserViewClose();
              Log.d("ttttttttt向下滑动", "" + (tv_curtain_rope.getHeight()));
            }
          }
          break;
        default:
          break;
      }
    }
    return false;
  }

  private void beserViewOpen() {
    bezierViewFrameLayout.setMinimumHeight(bezierViewFrameLayout.getHeight());
    bezierViewFrameLayout.invalidate();
  }

  private void beserViewClose() {
    bezierViewFrameLayout.setMinimumHeight(tv_curtain_rope.getHeight());
    bezierViewFrameLayout.invalidate();
  }

  public interface OnPullDownOffsetListener {
    void onPullDownOffset(float offset);
    void isOpen(boolean bOpen);
  }

  private OnPullDownOffsetListener mOnPullDownOffsetListener;

  public void setOnPullDownOffsetListener(OnPullDownOffsetListener onPullDownOffsetListener) {
    mOnPullDownOffsetListener = onPullDownOffsetListener;
  }
}

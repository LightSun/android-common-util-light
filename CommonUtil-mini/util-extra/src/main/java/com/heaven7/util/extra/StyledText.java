package com.heaven7.util.extra;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.BOLD_ITALIC;
import static android.graphics.Typeface.ITALIC;


/**
 * Helpers on top of {@link SpannableStringBuilder}
 * 封装文本前景色，背景色，字体。样式等操作: 下划线。删除线。前景色背景色,图片等
 * <p>一般是TextView使用</p>
 * @see Spannable
 * {@link Typeface}
 * @author heaven7
 */
public class StyledText extends SpannableStringBuilder {

	public static final String FAMILY_MONOSPACE="monospace";
	public static final String FAMILY_SERIFE="serif";
	public static final String FAMILY_SANS_SERIF="sans-serif";

    /** same to {@link ImageSpan#ALIGN_BOTTOM} */
    public static final int ALIGN_TYPE_BOTTOM    = 1;
    /** same to {@link ImageSpan#ALIGN_BASELINE} */
    public static final int ALIGN_TYPE_BASELINE  = 2;
    /** unlike {@link ImageSpan#ALIGN_BASELINE} or {@link ImageSpan#ALIGN_BOTTOM} , and align to the center */
    public static final int ALIGN_TYPE_CENTER    = 3;

    @IntDef({ ALIGN_TYPE_BOTTOM,ALIGN_TYPE_BASELINE ,ALIGN_TYPE_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AlignType{
    }

    /*** Append text and span to end of this text
     * @param  text the text
     * @param span  the span
     * @return  this */
    public StyledText append(final CharSequence text, final Object span) {
        if (!TextUtils.isEmpty(text)) {
            append(text);
            if (span != null) {
                final int length = length();
                setSpan(span, length - text.length(), length,
                        SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return this;
    }
    /*** Append text and span to end of this text
     * @param text the text
     * @param spans  the all spans
     * @return  this */
    public StyledText append(final CharSequence text, final Object...spans) {
        if (!TextUtils.isEmpty(text)) {
            append(text);
            if (spans != null && spans.length!=0) {
                final int length = length();
                for(int i=0, size = spans.length ;i<size ;i++){
                    setSpan(spans[i], length - text.length(), length,
                            SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return this;
    }

    @Override
    public StyledText append(char text) {
        super.append(text);
        return this;
    }

    @Override
    public StyledText append(CharSequence text) {
        if (text != null)
            super.append(text);
        return this;
    }

    /**
     * Append text and span to end of this text
     * @param  text the text
     * @param span  the span
     * @return  this
     */
    public StyledText append(final char text, final Object span) {
        append(text);
        if (span != null) {
            final int length = length();
            setSpan(span, length - 1, length, SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    public StyledText appendBold(final CharSequence text) {
        return append(text, new StyleSpan(BOLD));
    }
    public StyledText appendItalic(final CharSequence text) {
        return append(text, new StyleSpan(ITALIC));
    }
    public StyledText appendBoldItalic(final CharSequence text) {
        return append(text, new StyleSpan(BOLD_ITALIC));
    }
    
    //----下划线，删除线 ---//
    public StyledText appendUnderline(final CharSequence text) {
        return append(text, new UnderlineSpan());
    }
    public StyledText appendStrikethrough(final CharSequence text) {
    	return append(text, new StrikethroughSpan());
    }

    public StyledText appendBackground(final CharSequence text, final int color) {
        return append(text, new BackgroundColorSpan(color));
    }
    public StyledText appendBackgroundWithForeground(final CharSequence text, final int backgroundColor,int foregroundColor) {
        return append(text, new BackgroundColorSpan(backgroundColor),new ForegroundColorSpan(foregroundColor));
    }

    public StyledText appendForeground(final CharSequence text, final int color) {
        return append(text, new ForegroundColorSpan(color));
    }

    public StyledText appendForeground(final char text, final int color) {
        return append(text, new ForegroundColorSpan(color));
    }

    //--3种字体---//
    /**
     * Append text in monospace typeface
     * @param  text the text
     * @return  this
     */
    public StyledText appendMonospaceTypeface(final CharSequence text) {
        return append(text, new TypefaceSpan("monospace"));
    }
    public StyledText appendSerifTypeface(final CharSequence text) {
        return append(text, new TypefaceSpan("serif"));
    }
    public StyledText appendSansSerifTypeface(final CharSequence text) {
    	return append(text, new TypefaceSpan("sans-serif"));
    }
    /**
     * 设置url及点击这个url的监听器： text可以是url，电话，邮箱，地图，短信，彩信 等
     * <p>需要TextView.setMovementMethod(LinkMovementMethod.getInstance());响应(一般最后调用) </p>
     * <p>电话eg： msp.setSpan(new URLSpan("tel:4155551212"), 37, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  </p>
        <p>邮件eg： msp.setSpan(new URLSpan("mailto:webmaster@google.com"), 39, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);</p>
        <p>网址eg：msp.setSpan(new URLSpan("http://www.baidu.com"), 41, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     </p>
        <p>短信:msp.setSpan(new URLSpan("sms:4155551212"), 43, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //使用sms:或者smsto</p>
       <p>彩信: msp.setSpan(new URLSpan("mms:4155551212"), 45, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //使用mms:或者mmsto:</p>
       <p>地图: msp.setSpan(new URLSpan("geo:38.899533,-77.036476"), 47, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //地图   </p>
     @param text the context
     @param listener the click listener
        @return this
     */
    public StyledText appendUrl(final CharSequence text,
            final OnClickListener listener) {
        return append(text, new URLSpan(text.toString()) {
            @Override
            public void onClick(View widget) {
                listener.onClick(widget);
                /** 默认用浏览器打开指定的Url
                 *  Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        context.startActivity(intent);
                 */
            }
        });
    }
    //默认用浏览器打开
    public StyledText appendUrl(final CharSequence text) {
        return append(text, new URLSpan(text.toString()));
    }

    /*
     * Append given date in relative time format
     */
   /* public StyledText append(final Date date) {
        final CharSequence time = TimeUtils.getRelativeTime(date);
        // Un-capitalize time string if there is already a prefix.
        // So you get "opened in 5 days" instead of "opened In 5 days".
        final int timeLength = time.length();
        if (length() > 0 && timeLength > 0
                && Character.isUpperCase(time.charAt(0))) {
            append(time.subSequence(0, 1).toString()
                    .toLowerCase(Locale.getDefault()));
            append(time.subSequence(1, timeLength));
        } else
            append(time);

        return this;
    }*/
    
    public StyledText appendPix(CharSequence text,int pixSize){
    	//setSpan(new AbsoluteSizeSpan(20), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    	return append(text, new AbsoluteSizeSpan(pixSize));
    }
    
    public StyledText appendDip(CharSequence text,int dipSize){
    	return append(text, new AbsoluteSizeSpan(dipSize,true));
    }
    /**
     * @param proportion 默认字体大小的比例(eg 0.5f为一半.2f为2倍)
     * @param text the text
     * @return this
     */
    public StyledText appendRelative(CharSequence text, float proportion){
    	return append(text, new RelativeSizeSpan(proportion));
    }
    
    // --上下标-- //
    public StyledText appendSubscript(CharSequence text){
    	return append(text, new SubscriptSpan());
    }
    public StyledText appendSuperscript(CharSequence text){
    	return append(text, new SuperscriptSpan());
    }
    //缩放：x变宽
    public StyledText appendScaleX(CharSequence text,float proportion){
    	return append(text, new ScaleXSpan(proportion));
    }
    
    /**
     * 设置组合样式
     * @param family 3种字体 eg：{@linkplain StyledText#FAMILY_MONOSPACE} 
     * @param style  eg: android.graphics.Typeface.BOLD_ITALIC
     * @param text the text
     * @param size the text size
     * @param color the color
     * @param linkColor the linke color
     *  @return this
     */
    public StyledText appendComplex(CharSequence text,String family,int style,int size,
    		ColorStateList color,ColorStateList linkColor){
    	return append(text, new TextAppearanceSpan(family, style, size, color,linkColor));
    }
    public StyledText appendComplex(CharSequence text,String family,int style,int size,
    		int normalSelectorId,int linkSelectorId,Context context){
    	Resources res = context.getResources();
    	ColorStateList normalColorState = res.getColorStateList(normalSelectorId);
    	ColorStateList linkColorState = res.getColorStateList(linkSelectorId);
    	return append(text, new TextAppearanceSpan(family, style, size, normalColorState,linkColorState));
    }
    /**
     * @param family 3种字体 eg：{@linkplain StyledText#FAMILY_MONOSPACE} 
     * @param style  eg: android.graphics.Typeface.BOLD_ITALIC
     * @param normalColors 颜色数组：包括 正常颜色，选中颜色，按压颜色
     * @param linkColors   颜色数组：包括 正常颜色，选中颜色，按压颜色
     * @param text the text
     * @param size the size
     * @param context the context
     * @return this
     */
    public StyledText appendComplex(CharSequence text,String family,int style,int size,
    		int[] normalColors,int[] linkColors,Context context){
    	if(normalColors.length!=3 || linkColors.length!=3)
    		throw new IllegalArgumentException();
    	
    	ColorStateList normalColorState = SelectorUtil.getSelectorColor(context,
    			normalColors[0], normalColors[1], normalColors[2]);
    	ColorStateList linkColorState = SelectorUtil.getSelectorColor(context,
    			linkColors[0], linkColors[1], linkColors[2]);
    	
    	return append(text, new TextAppearanceSpan(family, style, size, normalColorState,linkColorState));
    }
    //项目符号
    public StyledText setBulletSymbol(int color,int start,int end){
    	setSpan(new BulletSpan(BulletSpan.STANDARD_GAP_WIDTH,color),
    			start,end,SPAN_EXCLUSIVE_EXCLUSIVE);
    	return this;
    }
   /* 无效
    * public StyledText appendBulletSymbol(CharSequence text,int color){
    	return append(text, new BulletSpan(android.text.style.BulletSpan.STANDARD_GAP_WIDTH,color));
    }*/
    
    //图片
    public StyledText setImage(Drawable drawable,int start,int end ,@AlignType int alignType){
    	setSpan(getImageSpan(drawable,alignType), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    	return this;
    }
    public StyledText appendImage(Drawable drawable, @AlignType int alignType){
    	return append("x", getImageSpan(drawable,alignType)); //x为任意一个非空字符序列,站位
    }

    private static ImageSpan getImageSpan(Drawable d,int alignType) {
        switch (alignType){
            case ALIGN_TYPE_BOTTOM :
                return new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
            case ALIGN_TYPE_BASELINE :
                return new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            case ALIGN_TYPE_CENTER :
            default:
                return new CenterImageSpan(d);
        }
    }

    static class CenterImageSpan extends ImageSpan {

        public CenterImageSpan(Context arg0,int resourceId) {
            super(arg0, resourceId);
        }
        public CenterImageSpan(Drawable d) {
            super(d, ALIGN_TYPE_BOTTOM);
        }
        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fm) {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint=paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight=rect.bottom-rect.top;

                int top= drHeight/2 - fontHeight/4;
                int bottom=drHeight/2 + fontHeight/4;

                fm.ascent=-bottom;
                fm.top=-bottom;
                fm.bottom=top;
                fm.descent=top;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            canvas.save();
            int transY = ((bottom-top) - b.getBounds().bottom)/2 + top;
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }
}

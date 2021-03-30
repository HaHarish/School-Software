package com.newletseduvate.utils.rich_text_editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.chinalwb.are.spans.ARE_Clickable_Span;
import com.chinalwb.are.spans.ARE_Span;

public class AreVideoSpan extends ImageSpan implements ARE_Span, ARE_Clickable_Span {
    private final Context mContext;

    private final String mVideoPath;

    private final String mVideoUrl;

    public AreVideoSpan(Context context, Bitmap bitmapDrawable, String videoPath, String videoUrl) {
        super(context, bitmapDrawable);
        this.mContext = context;
        this.mVideoPath = videoPath;
        this.mVideoUrl = videoUrl;
    }

    @Override
    public String getHtml() {
        String path = TextUtils.isEmpty(mVideoUrl) ? mVideoPath : mVideoUrl;
        StringBuffer htmlBuffer;
        if (path.startsWith("https://youtu.be")) {
            htmlBuffer = new StringBuffer("<iframe width=\"300\" height=\"150\" src=\"//www.youtube.com/embed/");
            htmlBuffer.append(path.substring(16));
            htmlBuffer.append("\" allowFullscreen=\"1\"></iframe>");
        } else {
            htmlBuffer = new StringBuffer("<video width=\"300\" height=\"150\" src=\"");
            htmlBuffer.append(path);
            htmlBuffer.append("\" uri=\"");
            htmlBuffer.append(mVideoPath);
            htmlBuffer.append("\" controls=\"controls\">");
            htmlBuffer.append("</video>");
        }
        return htmlBuffer.toString();
    }

    public com.chinalwb.are.spans.AreVideoSpan.VideoType getVideoType() {
        if (!TextUtils.isEmpty(mVideoUrl)) {
            return com.chinalwb.are.spans.AreVideoSpan.VideoType.SERVER;
        }

        if (!TextUtils.isEmpty(mVideoPath)) {
            return com.chinalwb.are.spans.AreVideoSpan.VideoType.LOCAL;
        }

        return com.chinalwb.are.spans.AreVideoSpan.VideoType.UNKNOWN;
    }

    public String getVideoPath() {
        return mVideoPath;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public enum VideoType {
        LOCAL,
        SERVER,
        UNKNOWN,
    }
}

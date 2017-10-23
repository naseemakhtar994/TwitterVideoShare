/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ko.twitter.vplay.core;

import android.content.Context;
import android.util.Log;

import com.ko.twitter.vplay.core.internal.json.JSONException;
import com.ko.twitter.vplay.core.internal.json.JSONObject;
import com.ko.twitter.vplay.core.internal.json.ParseUtil;

/**
 * Represents result of "/1.1/media/upload.json"
 * 
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.2
 */
public final class UploadedMedia implements java.io.Serializable {

    private static final long serialVersionUID = 5393092535610604718L;
    
    private int imageWidth;
    private int imageHeight;
    private String imageType;
    private long mediaId;
    private String mediaKey;
    private long size;
    private String processingState;
    private int processingCheckAfterSecs;
    private static OnCallBackVideoShareListener onCallBackVideoShareListener;
    private Context context;

    /*package*/ UploadedMedia(JSONObject json) throws TwitterException {
        Log.e("TwitterException","init Uploaded Media Enters");
        init(json);
    }

    UploadedMedia(Context context){
        this.context = context;
    }

    public static void setOnCallBackVideoShareListener(OnCallBackVideoShareListener onCallBackVideoListener){
        onCallBackVideoShareListener = onCallBackVideoListener;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public String getImageType() {
        return imageType;
    }

    public long getMediaId() {
        return mediaId;
    }

    public long getSize() {
        return size;
    }
    
    public String getProcessingState() {
    	return processingState;
    }
    
    public int getProcessingCheckAfterSecs() {
    	return processingCheckAfterSecs;
    }

    private void init(JSONObject json) throws TwitterException {
        Log.e("TwitterException","init Uploaded Media");
        mediaId = ParseUtil.getLong("media_id", json);
        size = ParseUtil.getLong("size", json);
        mediaKey = ParseUtil.getRawString("media_key",json);
        try {
            if (!json.isNull("image")) {
                JSONObject image = json.getJSONObject("image");
                imageWidth = ParseUtil.getInt("w", image);
                imageHeight = ParseUtil.getInt("h", image);
                imageType = ParseUtil.getUnescapedString("image_type", image);
            }
            
            if (!json.isNull("processing_info")) {
            	JSONObject processingInfo = json.getJSONObject("processing_info");
            	processingState = ParseUtil.getUnescapedString("state", processingInfo);
            	processingCheckAfterSecs = ParseUtil.getInt("check_after_secs", processingInfo);
            }

            Log.e("TwitterException","init Uploaded Media Success "+json.toString());
            if(processingState != null) {
                if (processingState.equalsIgnoreCase("succeeded")) {
                    if(onCallBackVideoShareListener != null)
                   onCallBackVideoShareListener.onSuccess(mediaId,mediaKey);
                }
            }
            
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UploadedMedia that = (UploadedMedia) o;

        if (imageWidth != that.imageWidth) return false;
        if (imageHeight != that.imageHeight) return false;
        if (imageType != that.imageType) return false;
        if (mediaId != that.mediaId) return false;
        if (size != that.size) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (mediaId ^ (mediaId >>> 32));
        result = 31 * result + imageWidth;
        result = 31 * result + imageHeight;
        result = 31 * result + (imageType != null ? imageType.hashCode() : 0);
        result = 31 * result + (int)(size ^ (size >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "UploadedMedia{" +
                "mediaId=" + mediaId +
                ", imageWidth=" + imageWidth + 
                ", imageHeight=" + imageHeight +
                ", imageType='" + imageType + '\'' +
                ", size=" + size +
                '}';
    }
}
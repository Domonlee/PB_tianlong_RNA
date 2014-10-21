package org.tianlong.rna.utlis;

import android.annotation.SuppressLint;
import android.os.Handler;   
import android.os.Message;   
import android.util.Log;

@SuppressLint({ "HandlerLeak", "UseValueOf" })
public abstract class AdvancedCountdownTimer {   
    private final long mCountdownInterval;   
    protected long mTotalTime;   
    private long mRemainTime;   
       
    public AdvancedCountdownTimer(long millisInFuture, long countDownInterval) {   
        mTotalTime = millisInFuture;   
        mCountdownInterval = countDownInterval;   
        mRemainTime = millisInFuture;   
        Log.w("mRemainTime construt",  "mRemainTime="+ mRemainTime);
    }   
       
    public final void cancel() {   
        mHandler.removeMessages(MSG_RUN);   
        mHandler.removeMessages(MSG_PAUSE);   
    }   
  
    public final void resume() {   
        mHandler.removeMessages(MSG_PAUSE);   
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));   
    }   
  
    public final void pause() {   
        mHandler.removeMessages(MSG_RUN);   
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_PAUSE));   
    }   
  
       
    public synchronized final AdvancedCountdownTimer start() {   
        if (mRemainTime <= 0) {   
            onFinish();   
            return this;   
        }   
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN),   
                mCountdownInterval);   
        return this;   
    }   
  
    public abstract void onTick(long millisUntilFinished, int percent);   
  
       
    public abstract void onFinish();   
  
    private static final int MSG_RUN = 1;   
    private static final int MSG_PAUSE = 2;   
  
	private Handler mHandler = new Handler() {   
  
        @Override  
        public void handleMessage(Message msg) {   
            synchronized (AdvancedCountdownTimer.this) {   
                if (msg.what == MSG_RUN) {   
                	//mCountdownInterval = 1s;
                	Log.w("mRemainTime",  "mRemainTime="+ mRemainTime);
                    mRemainTime = mRemainTime - mCountdownInterval;   
                    if (mRemainTime <= 0) {   
                        onFinish();   
                    } else if (mRemainTime < mCountdownInterval) {   
                        sendMessageDelayed(obtainMessage(MSG_RUN), mRemainTime);   
                    } else {   
                        onTick(mRemainTime, new Long(100  
                                * (mTotalTime - mRemainTime) / mTotalTime)   
                                .intValue());   
                        sendMessageDelayed(obtainMessage(MSG_RUN),   
                                mCountdownInterval);   
                    }   
                } 
                else if (msg.what == MSG_PAUSE) {   
                }   
            }   
        }   
    };   
}  

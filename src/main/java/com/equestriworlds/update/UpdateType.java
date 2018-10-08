/*
 * Decompiled with CFR 0_133.
 */
package com.equestriworlds.update;

import com.equestriworlds.util.UtilTime;
import java.io.PrintStream;

public enum UpdateType {
    MIN_64(3840000L),
    MIN_32(1920000L),
    MIN_16(960000L),
    MIN_10(600000L),
    MIN_08(480000L),
    MIN_05(300000L),
    MIN_04(240000L),
    MIN_02(120000L),
    MIN_01(60000L),
    SLOWEST(32000L),
    SLOWER(16000L),
    SLOW(4000L),
    TWOSEC(2000L),
    SEC(1000L),
    FAST(500L),
    FASTER(250L),
    FASTEST(125L),
    TICK(49L);
    
    private long _time;
    private long _last;
    private long _timeSpent;
    private long _timeCount;

    private UpdateType(long time) {
        this._time = time;
        this._last = System.currentTimeMillis();
    }

    public boolean Elapsed() {
        if (UtilTime.elapsed(this._last, this._time)) {
            this._last = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void StartTime() {
        this._timeCount = System.currentTimeMillis();
    }

    public void StopTime() {
        this._timeSpent += System.currentTimeMillis() - this._timeCount;
    }

    public void PrintAndResetTime() {
        System.out.println(this.name() + " in a second: " + this._timeSpent);
        this._timeSpent = 0L;
    }
}
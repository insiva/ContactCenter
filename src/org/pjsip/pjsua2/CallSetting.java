/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua2;

public class CallSetting {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CallSetting(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CallSetting obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        pjsua2JNI.delete_CallSetting(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setFlag(long value) {
    pjsua2JNI.CallSetting_flag_set(swigCPtr, this, value);
  }

  public long getFlag() {
    return pjsua2JNI.CallSetting_flag_get(swigCPtr, this);
  }

  public void setReqKeyframeMethod(long value) {
    pjsua2JNI.CallSetting_reqKeyframeMethod_set(swigCPtr, this, value);
  }

  public long getReqKeyframeMethod() {
    return pjsua2JNI.CallSetting_reqKeyframeMethod_get(swigCPtr, this);
  }

  public void setAudioCount(long value) {
    pjsua2JNI.CallSetting_audioCount_set(swigCPtr, this, value);
  }

  public long getAudioCount() {
    return pjsua2JNI.CallSetting_audioCount_get(swigCPtr, this);
  }

  public void setVideoCount(long value) {
    pjsua2JNI.CallSetting_videoCount_set(swigCPtr, this, value);
  }

  public long getVideoCount() {
    return pjsua2JNI.CallSetting_videoCount_get(swigCPtr, this);
  }

  public CallSetting(SWIGTYPE_p_pj_bool_t useDefaultValues) {
    this(pjsua2JNI.new_CallSetting__SWIG_0(SWIGTYPE_p_pj_bool_t.getCPtr(useDefaultValues)), true);
  }

  public CallSetting() {
    this(pjsua2JNI.new_CallSetting__SWIG_1(), true);
  }

  public boolean isEmpty() {
    return pjsua2JNI.CallSetting_isEmpty(swigCPtr, this);
  }

}

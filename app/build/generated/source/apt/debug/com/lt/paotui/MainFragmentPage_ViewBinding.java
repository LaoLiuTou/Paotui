// Generated code from Butter Knife. Do not modify!
package com.lt.paotui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.youth.banner.Banner;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainFragmentPage_ViewBinding implements Unbinder {
  private MainFragmentPage target;

  @UiThread
  public MainFragmentPage_ViewBinding(MainFragmentPage target, View source) {
    this.target = target;

    target.banner = Utils.findRequiredViewAsType(source, R.id.banner, "field 'banner'", Banner.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainFragmentPage target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.banner = null;
  }
}

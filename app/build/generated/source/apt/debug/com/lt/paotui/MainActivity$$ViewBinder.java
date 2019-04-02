// Generated code from Butter Knife. Do not modify!
package com.lt.paotui;

import android.support.v4.view.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class MainActivity$$ViewBinder<T extends MainActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(Finder finder, T target, Object source) {
    return new InnerUnbinder<>(target, finder, source);
  }

  protected static class InnerUnbinder<T extends MainActivity> implements Unbinder {
    protected T target;

    protected InnerUnbinder(T target, Finder finder, Object source) {
      this.target = target;

      target.viewPager = finder.findRequiredViewAsType(source, 2131492957, "field 'viewPager'", ViewPager.class);
      target.tabLayoutView = finder.findRequiredViewAsType(source, 2131492958, "field 'tabLayoutView'", TabLayoutView.class);
    }

    @Override
    public void unbind() {
      T target = this.target;
      if (target == null) throw new IllegalStateException("Bindings already cleared.");

      target.viewPager = null;
      target.tabLayoutView = null;

      this.target = null;
    }
  }
}

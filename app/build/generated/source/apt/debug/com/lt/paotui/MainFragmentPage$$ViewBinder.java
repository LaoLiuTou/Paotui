// Generated code from Butter Knife. Do not modify!
package com.lt.paotui;

import butterknife.Unbinder;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import com.youth.banner.Banner;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class MainFragmentPage$$ViewBinder<T extends MainFragmentPage> implements ViewBinder<T> {
  @Override
  public Unbinder bind(Finder finder, T target, Object source) {
    return new InnerUnbinder<>(target, finder, source);
  }

  protected static class InnerUnbinder<T extends MainFragmentPage> implements Unbinder {
    protected T target;

    protected InnerUnbinder(T target, Finder finder, Object source) {
      this.target = target;

      target.banner = finder.findRequiredViewAsType(source, 2131492968, "field 'banner'", Banner.class);
    }

    @Override
    public void unbind() {
      T target = this.target;
      if (target == null) throw new IllegalStateException("Bindings already cleared.");

      target.banner = null;

      this.target = null;
    }
  }
}

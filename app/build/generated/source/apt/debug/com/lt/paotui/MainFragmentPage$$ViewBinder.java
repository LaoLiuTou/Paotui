// Generated code from Butter Knife. Do not modify!
package com.lt.paotui;

import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
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

    private View view2131492969;

    private View view2131492970;

    private View view2131492971;

    private View view2131492972;

    private View view2131492973;

    private View view2131492974;

    private View view2131492975;

    private View view2131492976;

    private View view2131492977;

    protected InnerUnbinder(final T target, Finder finder, Object source) {
      this.target = target;

      View view;
      target.banner = finder.findRequiredViewAsType(source, 2131492967, "field 'banner'", Banner.class);
      target.sroll_text = finder.findRequiredViewAsType(source, 2131492968, "field 'sroll_text'", TextView.class);
      target.top_bar_title = finder.findRequiredViewAsType(source, 2131493009, "field 'top_bar_title'", TextView.class);
      view = finder.findRequiredView(source, 2131492969, "method 'sayHi'");
      view2131492969 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131492970, "method 'sayHi'");
      view2131492970 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131492971, "method 'sayHi'");
      view2131492971 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131492972, "method 'sayHi'");
      view2131492972 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131492973, "method 'sayHi'");
      view2131492973 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131492974, "method 'sayHi'");
      view2131492974 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131492975, "method 'sayHi'");
      view2131492975 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131492976, "method 'sayHi'");
      view2131492976 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131492977, "method 'sayHi'");
      view2131492977 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
    }

    @Override
    public void unbind() {
      T target = this.target;
      if (target == null) throw new IllegalStateException("Bindings already cleared.");

      target.banner = null;
      target.sroll_text = null;
      target.top_bar_title = null;

      view2131492969.setOnClickListener(null);
      view2131492969 = null;
      view2131492970.setOnClickListener(null);
      view2131492970 = null;
      view2131492971.setOnClickListener(null);
      view2131492971 = null;
      view2131492972.setOnClickListener(null);
      view2131492972 = null;
      view2131492973.setOnClickListener(null);
      view2131492973 = null;
      view2131492974.setOnClickListener(null);
      view2131492974 = null;
      view2131492975.setOnClickListener(null);
      view2131492975 = null;
      view2131492976.setOnClickListener(null);
      view2131492976 = null;
      view2131492977.setOnClickListener(null);
      view2131492977 = null;

      this.target = null;
    }
  }
}

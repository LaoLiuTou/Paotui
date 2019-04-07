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

    private View view2131558568;

    private View view2131558529;

    private View view2131558530;

    private View view2131558531;

    private View view2131558532;

    private View view2131558533;

    private View view2131558534;

    private View view2131558535;

    private View view2131558536;

    private View view2131558537;

    protected InnerUnbinder(final T target, Finder finder, Object source) {
      this.target = target;

      View view;
      target.banner = finder.findRequiredViewAsType(source, 2131558527, "field 'banner'", Banner.class);
      target.sroll_text = finder.findRequiredViewAsType(source, 2131558528, "field 'sroll_text'", TextView.class);
      target.top_bar_title = finder.findRequiredViewAsType(source, 2131558569, "field 'top_bar_title'", TextView.class);
      view = finder.findRequiredView(source, 2131558568, "method 'sayHi'");
      view2131558568 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131558529, "method 'sayHi'");
      view2131558529 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131558530, "method 'sayHi'");
      view2131558530 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131558531, "method 'sayHi'");
      view2131558531 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131558532, "method 'sayHi'");
      view2131558532 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131558533, "method 'sayHi'");
      view2131558533 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131558534, "method 'sayHi'");
      view2131558534 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131558535, "method 'sayHi'");
      view2131558535 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131558536, "method 'sayHi'");
      view2131558536 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.sayHi(p0);
        }
      });
      view = finder.findRequiredView(source, 2131558537, "method 'sayHi'");
      view2131558537 = view;
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

      view2131558568.setOnClickListener(null);
      view2131558568 = null;
      view2131558529.setOnClickListener(null);
      view2131558529 = null;
      view2131558530.setOnClickListener(null);
      view2131558530 = null;
      view2131558531.setOnClickListener(null);
      view2131558531 = null;
      view2131558532.setOnClickListener(null);
      view2131558532 = null;
      view2131558533.setOnClickListener(null);
      view2131558533 = null;
      view2131558534.setOnClickListener(null);
      view2131558534 = null;
      view2131558535.setOnClickListener(null);
      view2131558535 = null;
      view2131558536.setOnClickListener(null);
      view2131558536 = null;
      view2131558537.setOnClickListener(null);
      view2131558537 = null;

      this.target = null;
    }
  }
}

package me.tapeline.cuteui.layout;

import me.tapeline.cuteui.components.Container;

public interface Layout {

  void measure(Container c, int maxW, int maxH);
  void layout(Container c);

}
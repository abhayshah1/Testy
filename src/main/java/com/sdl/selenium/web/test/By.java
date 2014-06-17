package com.sdl.selenium.web.test;

import com.sdl.selenium.web.PathBuilder;
import com.sdl.selenium.web.WebLocator;

/**
 * Created with IntelliJ IDEA.
 * User: vculea
 * Date: 6/17/14
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class By extends com.sdl.selenium.web.By<String> {

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void init(PathBuilder pathBuilder, WebLocator builder) {
    }

    public static By icon(String icon) {
        if (icon == null)
            throw new IllegalArgumentException(
                    "Cannot find elements when the icon expression is null.");

        return new ByIcon(icon);
    }

    public static class ByIcon extends By {

        public ByIcon(String icon) {
            setValue(icon);
        }

        public String getPath() {
            return getValue();
        }

        public void init(PathBuilder builder, WebLocator webLocator) {
            PathBuilder2 pathBuilder2 = (PathBuilder2) builder;
            pathBuilder2.setIcon(getValue());
            ((TextField) webLocator).setIcon(getValue());
        }
    }
}

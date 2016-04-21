package com.devefx.crawler.core;

import org.jsoup.nodes.Element;

public interface Resolve<T> {
	T process(Element element);
}

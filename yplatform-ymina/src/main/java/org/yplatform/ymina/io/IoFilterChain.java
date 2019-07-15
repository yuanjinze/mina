package org.yplatform.ymina.io;

import java.util.List;

public interface IoFilterChain {

	IoFilter getChild(String name);
	
	List getChildren();

	List getChildrenReversed();

	void addFirst(String name, IoFilter filter);

	void addLast(String name, IoFilter filter);

	void addBefore(String baseName, String name, IoFilter filter);

	void addAfter(String baseName, String name, IoFilter filter);

	IoFilter remove(String name);

	void clear();
}

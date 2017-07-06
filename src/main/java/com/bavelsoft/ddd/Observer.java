package com.bavelsoft.ddd;

public interface Observer<T, V> {
	V getBeforeChange(T observed);
	void onChange(T observed, V fromBefore);
}

package com.bavelsoft.ddd;

import java.util.Collection;

public interface Initializer<T, I extends T, R> {
	public T initialize(I object, Collection<R> validationResults);
}

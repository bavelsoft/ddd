package com.bavelsoft.ddd;

import java.util.Set;

public interface Initializer<T, I extends T, R> {
	public T initialize(I object, Set<R> validationResults);
}

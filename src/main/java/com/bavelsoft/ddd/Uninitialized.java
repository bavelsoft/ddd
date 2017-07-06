package com.bavelsoft.ddd;

import java.util.Collection;

public interface Uninitialized<T,R> {
	public T validate(Collection<R> validationResults);
}

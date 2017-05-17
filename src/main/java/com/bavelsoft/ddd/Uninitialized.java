package com.bavelsoft.ddd;

import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.function.Consumer;

public interface Uninitialized<T,R> {
	public T validate(Collection<R> validationResults);
}

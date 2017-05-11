package com.bavelsoft.ddd;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Validation<T,R> extends Predicate<T>, Function<T,R> {
}


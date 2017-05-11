package com.bavelsoft.ddd;

import java.util.function.Consumer;
import java.util.function.Function;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Default<T,X> {
	abstract Consumer<X> setter();
	abstract Function<X,T> defaultFunction();

	static <T,X> Default create(Consumer<X> setter, Function<X,T> defaultFunction) {
		return new AutoValue_Default(setter, defaultFunction);
	}
}


package com.bavelsoft.ddd;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class InitializerImpl<T,I extends T,R> implements Initializer<T,I,R> {
	private final List<BiConsumer<I,?>> defaultSetters = new ArrayList<>();
	private final List<Function<T,?>> defaultFunctions = new ArrayList<>();
	private final List<Predicate<T>> validationPredicates = new ArrayList<>();
	private final List<Function<T,R>> validationFunctions  = new ArrayList<>();

	public <F> void addDefault(BiConsumer<I,F> defaultSetter, Function<T,F> defaultFunction) {
		defaultSetters.add(defaultSetter);
		defaultFunctions.add(defaultFunction);
	}

	public void addValidation(Function<T,R> validationFunction, Predicate<T> validationPredicate) {
		validationPredicates.add(validationPredicate);
		validationFunctions.add(validationFunction);
	}

	public T initialize(I object, Set<R> validationResults) {
		assignDefaults(object);
		validate(object, validationResults);
		return object;
	}

	private void assignDefaults(T object) {
		Object[] defaultValues = new Object[defaultFunctions.size()];
		int i=0;
		for (Function<T,?> defaultFunction : defaultFunctions) {
			defaultValues[i++] = defaultFunction.apply(object);
		}
		int j=0;
		for (BiConsumer defaultSetter : defaultSetters) {
			defaultSetter.accept(object, defaultValues[j++]);
		}
	}

	private void validate(T object, Set<R> validationResults) {
		int i=0;
		for (Predicate<T> validationPredicate : validationPredicates) {
			if (validationPredicate.test(object)) {
				R validationResult = validationFunctions.get(i++).apply(object);
				validationResults.add(validationResult);
			}
		}
	}
}



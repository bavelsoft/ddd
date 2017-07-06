package com.bavelsoft.ddd;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class Initializer<T,I extends T,R> {
	private final List<BiConsumer<I,?>> defaultSetters = new ArrayList<>();
	private final List<Function<T,?>> defaultFunctions = new ArrayList<>();
	private final List<Predicate<T>> validationPredicates = new ArrayList<>();
	private final List<Function<T,R>> validationFunctions  = new ArrayList<>();
	private final List<Observer<T,Object>> observers = new ArrayList<>();

	public <F> void addDefault(BiConsumer<I,F> defaultSetter, Function<T,F> defaultFunction) {
		defaultSetters.add(defaultSetter);
		defaultFunctions.add(defaultFunction);
	}

	public void addValidation(Function<T,R> validationFunction, Predicate<T> validationPredicate) {
		validationPredicates.add(validationPredicate);
		validationFunctions.add(validationFunction);
	}

	public void addObserver(Observer<T,Object> observer) {
		observers.add(observer);
	}

	public void notifyObservers(T observed) {
		for (Observer<T,Object> observer : observers) {
			Object beforeValue = observer.getBeforeChange(observed);
			observer.onChange(observed, beforeValue);
		}
	}

	public T initialize(I object, Collection<R> validationResults) {
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

	private void validate(T object, Collection<R> validationResults) {
		int i=0;
		for (Predicate<T> validationPredicate : validationPredicates) {
			if (validationPredicate.test(object)) {
				R validationResult = validationFunctions.get(i++).apply(object);
				validationResults.add(validationResult);
			}
		}
	}
}

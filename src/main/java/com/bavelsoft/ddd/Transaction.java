package com.bavelsoft.ddd;

import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Transaction<T,R> {
	private Collection<R> validationResults;
	private List<Object> inputs;
	private List<Consumer<?>> steps;
	private Object currentInput;

	public Transaction() {
		begin();
	}

	public Transaction<T,R> begin() {
		validationResults = new HashSet<>();
		inputs = new ArrayList<>();
		steps = new ArrayList<>();
		currentInput = null;
		return this;
	}

	public Collection<R> validate() {
		return validationResults;
	}

	public void commit() {
		int i=0;
		for (Consumer step : steps) {
			step.accept(inputs.get(i));
			i++;
		}
	}

	public <T> Transaction<T,R> with(Uninitialized<T,R> uninitialized) {
		currentInput = uninitialized.validate(validationResults);
		return (Transaction<T,R>)this;
	}

	public <T> Transaction<T,R> with(T initialized) {
		currentInput = initialized;
		return (Transaction<T,R>)this;
	}

	public <T> Transaction<T,R> add(Consumer<T> step) {
		inputs.add(currentInput);
		steps.add(step);
		return (Transaction<T,R>)this;
	}

	public <S> Transaction<S,R> nowWith(Uninitialized<S,R> uninitialized, BiConsumer<S,T> step) {
		step.accept((S)uninitialized.validate(validationResults), (T)currentInput);
		currentInput = step;
		return (Transaction<S,R>)this;
	}

	public Transaction<T,R> now(Consumer<T> step) {
		step.accept((T)currentInput);
		return (Transaction<T,R>)this;
	}
}

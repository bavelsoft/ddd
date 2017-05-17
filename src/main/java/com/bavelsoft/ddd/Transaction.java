package com.bavelsoft.ddd;

import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Transaction<R> {
	private Collection<R> validationResults;
	private List<Uninitialized<?,R>> uninitializeds;
	private List<Object> initializeds;
	private List<Consumer<?>> steps;

	public Transaction<R> begin() {
		validationResults = new HashSet<>();
		uninitializeds = new ArrayList<>();
		initializeds = new ArrayList<>();
		steps = new ArrayList<>();
		return this;
	}

	public Collection<R> validate() {
		int i=0;
		for (Uninitialized<?,R> uninitialized : uninitializeds) {
			if (uninitialized != null)
				initializeds.set(i, uninitialized.validate(validationResults));			
			i++;
		}
		return validationResults;
	}

	public void commit() {
		int i=0;
		for (Consumer step : steps) {
			step.accept(initializeds.get(i));
			i++;
		}
	}

	public <T> Transaction<R> add(Uninitialized<T,R> uninitialized, Consumer<T> step) {
		uninitializeds.add(uninitialized);
		initializeds.add(null);
		steps.add(step);
		return this;
	}

	public <T> Transaction<R> add(T initialized, Consumer<T> step) {
		uninitializeds.add(null);
		initializeds.add(initialized);
		steps.add(step);
		return this;
	}
}

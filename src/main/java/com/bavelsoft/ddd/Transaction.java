package com.bavelsoft.ddd;

public class Transaction<T> {
	
/*
 * composition?
 *
 * txn.start().with(x).do(x->repo.add(x)).andWith(y).do(y->otherRepo.add(y));
 * if (txn.isValid())
 *  txn.commit();
 * else
 *  doSomethingWith(txn.getValidationResults());
 */
}

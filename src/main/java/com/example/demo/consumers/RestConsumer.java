package com.example.demo.consumers;

public interface RestConsumer<K,V> {

	public V fetch(K key);
	
}

package net.sourceforge.gjtapi.util;

/*
	Copyright (c) 2002 8x8 Inc. (www.8x8.com) 

	All rights reserved. 

	Permission is hereby granted, free of charge, to any person obtaining a 
	copy of this software and associated documentation files (the 
	"Software"), to deal in the Software without restriction, including 
	without limitation the rights to use, copy, modify, merge, publish, 
	distribute, and/or sell copies of the Software, and to permit persons 
	to whom the Software is furnished to do so, provided that the above 
	copyright notice(s) and this permission notice appear in all copies of 
	the Software and that both the above copyright notice(s) and this 
	permission notice appear in supporting documentation. 

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
	OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT 
	OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
	HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL 
	INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING 
	FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, 
	NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION 
	WITH THE USE OR PERFORMANCE OF THIS SOFTWARE. 

	Except as contained in this notice, the name of a copyright holder 
	shall not be used in advertising or otherwise to promote the sale, use 
	or other dealings in this Software without prior written authorization 
	of the copyright holder.
*/
import java.lang.ref.Reference;
import java.util.*;
/**
 * This is an abstract cache Map of keys to values where the values are held in References which will
 * allow them to be collected by a garbage collector when memory gets low.
 * <P>Concreate subclasses define which type of references are used.
 * Creation date: (2000-06-08 12:16:14)
 * @author: Richard Deadman
 * @param <K>
 * @param <V>
 */
public abstract class Cache<K, V> implements Map<K, V> {
		// The map that does the actual storage
	private HashMap<K, Reference<V>> backingStore = new HashMap<K, Reference<V>>();
/**
 * Clear myself of entries
 */
public void clear() {
	backingStore.clear();
}
/**
 * Do I have an entry under the given key?
 */
public boolean containsKey(Object key) {
	return backingStore.containsKey(key);
}
/**
 * Does the map contain the given value?
 * <P><B>Note that this may not work if two WeakReferences to the same object do not
 * test as equal.
 */
@SuppressWarnings("unchecked")
public boolean containsValue(Object value) {
	try {
		return backingStore.containsValue(this.wrap((V)value));
	} catch (ClassCastException cce) {
		return false;	// values must be of type V
	}
}
/**
 * Return the set of all entries as instances of Map.Entry.
 */
public Set<Map.Entry<K, V>> entrySet() {
	Set<Entry<K, Reference<V>>> referenceSet = backingStore.entrySet();
	Set<Entry<K, V>> set = new HashSet<Entry<K,V>>();
	
	// now dereference entry values -- we extend HashMap whose Map.Entry
	// implementation implements setValue()
	Iterator<Entry<K, Reference<V>>> it = referenceSet.iterator();
	while (it.hasNext()) {
		Map.Entry<K, Reference<V>> referenceEntry = it.next();
		CacheEntry<K, V> entry = new CacheEntry<K, V>();
		entry.setKey(referenceEntry.getKey());
		entry.setValue(referenceEntry.getValue().get());
	}
	return set;
}

/**
 * Get the dereferenced value object, clearing the key if the value is garbage-collected.
 */
public V get(Object key) {
	Reference<V> ref = backingStore.get(key);
	V o = null;
	if (ref != null) {
		o = ref.get();
		if (o == null)
			this.remove(key);
	}
	return o;
}
/**
 * Do I have any entries left
 */
public boolean isEmpty() {
	return backingStore.isEmpty();
}
/**
 * Return the keyset of this map
 */
public Set<K> keySet() {
	return backingStore.keySet();
}
/**
 * put method comment.
 */
public V put(K key, V value) {
	Reference<V> oldValue =  backingStore.put(key, this.wrap(value));
	if(oldValue != null) {
		return oldValue.get();
	} else {
		return null;
	}
}
/**
 * putAll method comment.
 */
@SuppressWarnings("unchecked")
public void putAll(Map<? extends K, ? extends V> m) {
	Set<?> set = m.entrySet();
	
	Iterator<?> it = set.iterator();
	while (it.hasNext()) {
		Object entry = it.next();
		this.put(((Map.Entry<K, V>) entry).getKey(), ((Map.Entry<K, V>) entry).getValue());
	}
}
/**
 * Remove an entry from the map.
 */
public V remove(Object key) {
	Reference<V> oldValue = backingStore.remove(key);
	if(oldValue != null) {
		return oldValue.get();
	} else {
		return null;
	}
}
/**
 * How many entries do I have
 */
public int size() {
	return backingStore.size();
}
/**
 * Describe myself
 * @return a string representation of the receiver
 */
public String toString() {
	return "A HashMap with reference values: " + backingStore.toString();
}
/**
 * values method comment.
 */
public Collection<V> values() {
	Collection<Reference<V>> coll = backingStore.values();
	Collection<V> newColl = new HashSet<V>();

		// copy each item over while dereferencing all references
	Iterator<Reference<V>> it = coll.iterator();
	while (it.hasNext()) {
		newColl.add(it.next().get());
	}
	return newColl;
}
/**
 * If the value is not a SoftReference, wrap it in one.
 */
protected abstract Reference<V> wrap(V value);
}


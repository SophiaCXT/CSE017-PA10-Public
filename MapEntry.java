/**
 * HashMap Entry: Generic class for the pair used in the hashmap 
 */
public class MapEntry<K, V> {
    // data members
	private K key;
	private V value;
    /**
     * Constructor with two parameters
     * @param k the key
     * @param v the value associated with the key
     */
	public MapEntry(K k, V v) {
		key = k;
		value = v;
	}
    /**
     * Method getKey
     * @return the value of the key itself
     */
	public K getKey() { 
        return key; 
    }
    /**
     * Method getValue
     * @return the value associated with the key
     */
	public V getValue() { 
        return value; 
    }
    /**
     * Method setKey
     * @param k the new value of the key itself
     */
	public void setKey(K k) {
		key = k;
	}
    /**
     * Method setValue
     * @param v the new value associated with the key
     */
	public void setValue(V v) {
		value = v;
	}
    /**
     * Method toString()
     * @return formatted string with the values of key and value
     */
	public String toString() {
		return "(" + key + ", " + value + ")";
	}
}
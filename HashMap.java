import java.util.ArrayList;
import java.util.LinkedList;

public abstract class HashMap<K,V> {
    protected double loadFactor;

    protected HashMap (double lf) {
		loadFactor = lf;
    }
    
    /**
    * Method trimToPowerOf2
    * @param c the capacity of the hashtable
    * @return the closest power of 2 to c
	* Time complexity: O(log n)
    */
	protected int trimToPowerOf2(int c) {
		int capacity = 1;
		while (capacity < c)
			capacity  = capacity << 1;
		return capacity;
	}

    /**
     * Method hash
     * @param hashCode
     * @return a valid index in the hashtable
	 * Time complexity: O(1)
     */
    protected abstract int hash(int hashCode);

    /**
     * Method rehash
     * creates a new hashtable with double capacity
     * puts all the entries from the old hashtable into the new table
	 * Time complexity: O(n)
     */
	protected abstract void rehash();

    /**
     * Method isEmpty
     * @return true if the hashtable is empty, false otherwise
	 * Time complexity: O(1)
     */
	public boolean isEmpty() {
		return (size() == 0);
	}

    /**
     * Method contains to search for a key in the hashtable
     * @param key the value of the key being searched for
     * @return true if key was found, false otherwise
	 * Time complexity: O(1)
     */
	public boolean containsKey(K key) {
		if(get(key) != null)
			return true;
		return false;
	}

    public abstract void clear();

    public abstract int size();

    /**
     * Method get to find an entry in the hashtable
     * @param key the value of the key being searched for
     * @return the value associated with the key if key is found, null otherwise
	 * Time complexity: O(1)
     */
	public abstract V get(K key);
    /**
     * Method remove to remote an entry from the hashtable
     * @param key the key to be removed
     * if the key is found, the pair (key and it associated value) will be removed from the hashtable
     * the hashtable is not modified if key is not found
	 * Time complexity: O(1)
     */
	public abstract void remove(K key);
    /**
     * Method put to add a new entry to the hashtable
     * @param key the value of the key of the new entry
     * @param value the value associated with the key
     * @return the old value of the entry if an entry is found for key
     *         or the new value if a new entry was added to the hashtable
	 * Time complexity: O(1) on average, but can reach O(n) if rehashing is required
     */
    public abstract V put(K key, V value);

    /**
     * Method toList
     * @return an arraylist with all the entries in the hashtable
	 * Time complexity: O(n)
     */
	public abstract ArrayList<MapEntry<K,V>> toList();

    public abstract ArrayList<V> values();

    public abstract void printCharacteristics();
}
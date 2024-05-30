import java.util.ArrayList;
//import java.util.LinkedList;
/**
 * Class HashMap: An implementation of the hash table using separate chaining
 */
public class HashMapLP <K, V> extends HashMap<K,V>{
    // data members
	private MapEntry<K,V>[] hashTable;
    public int collisions;
    private int size;
    public static int getIterations, putIterations, removeIterations;
    
    /**
     * Default constructor 
     * default capacity: 100
     * default load factor: 0.9
	 * Time complexity: O(1)
     */
	public HashMapLP() {
		this(100, 0.9);
	}
    /**
     * Constructor with one parameter
     * @param c for the capacity
     * default load factor: 0.9
	 * Time complexity: O(log n)
     */
	public HashMapLP(int c) {
		this(c, 0.9);
	}
    /**
     * Constructor with two parameters
     * @param c for the capacity
     * @param lf for the load factor
	 * Time complexity: O(log n)
     */
	public HashMapLP(int c, double lf) {
        super(lf);
		hashTable = new MapEntry[trimToPowerOf2(c)];
		size = 0;
        
	}
    
    /**
     * Method hash
     * @param hashCode
     * @return a valid index in the hashtable
	 * Time complexity: O(1)
     */
    protected int hash(int hashCode) {
		return hashCode & (hashTable.length-1);
	}
    /**
     * Method size
     * @return the number of pairs (key,value) stored the hashtable
	 * Time complexity: O(1)
     */
	public int size() {
		return size;
	}
    /**
     * Method clear to clear the hashtable
	 * Time complexity: O(n)
     */
	public void clear() {
		size = 0;
		for(int i=0; i<hashTable.length; i++)
			if(hashTable[i] != null)
				hashTable[i] = null;
	}
    /**
     * Method isEmpty
     * @return true if the hashtable is empty, false otherwise
	 * Time complexity: O(1)
     */
	public boolean isEmpty() {
		return (size == 0);
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
    /**
     * Method get to find an entry in the hashtable
     * @param key the value of the key being searched for
     * @return the value associated with the key if key is found, null otherwise
	 * Time complexity: O(1)
     */
	public V get(K key) {
        getIterations = 0;
		int HTIndex = hash(key.hashCode());

		while(hashTable[HTIndex] != null) {
            getIterations++;
            //collisions++;
            if (hashTable[HTIndex].getKey().equals(key)) {
                return hashTable[HTIndex].getValue();
            }
            HTIndex = (HTIndex + 1) % hashTable.length;

            if (HTIndex == size) {
                HTIndex = 0;
            }
		}
		return null;
	}
    /**
     * Method remove to remote an entry from the hashtable
     * @param key the key to be removed
     * if the key is found, the pair (key and it associated value) will be removed from the hashtable
     * the hashtable is not modified if key is not found
	 * Time complexity: O(1)
     */
	public void remove(K key) {
        removeIterations = 0;
        int hash = hash(key.hashCode());
        int index = hash;
        //int clusterStart = -1;
        ArrayList<MapEntry<K,V>> removeEntry = new ArrayList<>();
        
        while (hashTable[index] != null) {            
            removeIterations++;
            if (hashTable[hash].getKey().equals(key)) {
                hashTable[hash] = null;
                size--;
                hash = (hash + 1) % (hashTable.length);
                while (hashTable[hash] != null) {
                    removeIterations++;
                    removeEntry.add(hashTable[hash]);
                    hashTable[hash] = null;
                    size--;
                    hash = (hash + 1) % (hashTable.length);
                    //hash++;
                }
                break;
            }
            hash = (hash + 1) % (hashTable.length);
        } 
        for (MapEntry<K,V> entry : removeEntry) {
            put(entry.getKey(), entry.getValue());
            removeIterations++;
        }
    }
    /**
     * Method put to add a new entry to the hashtable
     * @param key the value of the key of the new entry
     * @param value the value associated with the key
     * @return the old value of the entry if an entry is found for key
     *         or the new value if a new entry was added to the hashtable
	 * Time complexity: O(1) on average, but can reach O(n) if rehashing is required
     */
    public V put(K key, V value) {
        V val = get(key);
        if (val != null) {
            int index = hash(key.hashCode());
            while (hashTable[index] != null) {
                MapEntry<K,V> entry = hashTable[index];
                if (entry.getKey().equals(key)) {
                    V old = entry.getValue();
                    entry.setValue(value);
                    return old;
                }
                index = (index +1) % (hashTable.length -1);
            } 
        }
        if (size >= hashTable.length - loadFactor) {
            //System.out.println("");
            rehash();
        }

        int index = hash(key.hashCode());
        if (hashTable[index] != null ) {
            collisions++;
        }

        while (hashTable[index] != null) {
            index = (index +1) % (hashTable.length -1);
        }
        hashTable[index] = new MapEntry<>(key, value);
        size++;
        return value;
    }
    /**
     * Method rehash
     * creates a new hashtable with double capacity
     * puts all the entries from the old hashtable into the new table
	 * Time complexity: O(n)
     */
	protected void rehash() {
        collisions = 0;
		ArrayList<MapEntry<K,V>> list = toList(); 
		hashTable = new MapEntry[hashTable.length << 1];
		size = 0;
		for(MapEntry<K,V> entry: list) {
            put(entry.getKey(), entry.getValue());
        }

	}
    /**
     * Method toList
     * @return an arraylist with all the entries in the hashtable
	 * Time complexity: O(n)
     */
	public ArrayList<MapEntry<K,V>> toList(){
		ArrayList<MapEntry<K,V>> list = new ArrayList<>();
		for(int i=0; i< hashTable.length; i++) {
            putIterations++;
			if(hashTable[i]!= null) {
				//MapEntry<K,V> ll = hashTable[i];
				for(int j = 0; j < hashTable.length; j++) {
                    //putIterations++;
                    MapEntry<K,V> entry = hashTable[j];
                    list.add(entry);
                }
					
			}
		} 
		return list;
	}
    /**
     * Method toString
     * @return a formatted string with all the entries in the hashtable
	 * Time complexity: O(n)
     */
	public String toString() {
		String out = "[";
		for(int i=0; i<hashTable.length; i++) {
			if(hashTable[i]!=null) {
				for(int j = 0; j< hashTable.length; j++)
					out += hashTable[j].toString();
				out += "\n";
			}
		}
		out += "]"; 
		return out;
	}

    public ArrayList<V> values() {
        //returns an arryalist with all the values from the hashtable
        ArrayList<V> val = new ArrayList<>();

                for (MapEntry<K,V> entry: hashTable) {
                    if (entry != null) {
                        val.add(entry.getValue()); 
                    }
                }

        return val;
    }

    public void printCharacteristics() {
        //prints the capcity, size, total collisions, 
        //number of bukets/clusters, and size of largest and smallest bucket/cluster
        boolean [] visited = new boolean [hashTable.length];
        int numCluster = 0;
        int max = 0;
        int min = hashTable.length;

        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null && !visited[i]) {
                numCluster++;
                int clusterSize = 0;
                int index = i;

                while (hashTable[index] != null) {
                    clusterSize++;
                    visited[index] = true;
                    index = (index + 1) % hashTable.length;
                }
                if (clusterSize > max) {
                    max = clusterSize;
                }
                if (clusterSize < min) {
                    min = clusterSize;
                }
            }
        }
        
        System.out.println("HashTable Capacity: " + hashTable.length);
        System.out.println("HashTable size: " + size);
        System.out.println("Total number of collisions: "+ collisions);
        System.out.println("Number of clusters: " + numCluster);
        System.out.println("Size of the largest cluster: " + max);
        System.out.println("Size of the smallest cluster: " +  min);

    }
}
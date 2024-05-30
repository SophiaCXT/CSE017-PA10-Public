import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class Test{
    public static void main(String[] args){
        ArrayList<MapEntry<Integer, Movie>> movieList = new ArrayList<>(70000);
        // Creating the hashmap
        HashMapLP<Integer, Movie> moviesHMLP = new HashMapLP<>(70000, 0.5);
        HashMapSC<Integer, Movie> moviesHMSC = new HashMapSC<>(70000, 0.9);
        //Comparator <E> comp = new Comparator<>();

        // Populating the array list movieList with the data from the files
        readMovies(moviesHMLP,moviesHMSC, "/home/houdghiri/CSE017/movies.csv");
        System.out.println(moviesHMLP.size() + " movies read from the file");
        readRatings(moviesHMLP, "/home/houdghiri/CSE017/ratings.csv");
        
        // print the characetristics of the two implementations of the hashmap
        System.out.println("\nHash table characteristics (Separate Chaining)");
        moviesHMSC.printCharacteristics();

        System.out.println("\n\nHash table characteristics (Linear Probing)");
        moviesHMLP.printCharacteristics();

        // Test the performance of the get methods in the two implementations of the hashmap
        int ids[] = {1544, 2156, 31349, 3048, 4001, 356, 5672, 6287, 25738, 26};
        testGet(moviesHMLP, moviesHMSC, ids);

        // Test the performance of the remove methods in the two implementations of the hashmap
        testRemove(moviesHMLP, moviesHMSC, ids);


        // print the characetristics of the two implementations of the hashmap after adding more movies
        System.out.println("\nHash table characteristics (Separate Chaining)");
        moviesHMSC.printCharacteristics();

        System.out.println("\n\nHash table characteristics (Linear Probing)");
        moviesHMLP.printCharacteristics();
       
        // sort the movies
        System.out.println("\nSorting the movies from the hashtable with separate chaining");
        //mergeSort((new ArrayList<>(moviesHMSC.values())), null);
        mergeSortMovies(moviesHMSC);

        System.out.println("\nSorting the movies from the hashtable with linear probing");
        //mergeSort((new ArrayList<>(moviesHMLP.values())), null);
        mergeSortMovies(moviesHMLP);
        
        
    }
    /**
     * read the list of movies from filename and adds the pairs (movieid, movie) to the two hash maps
     * @param hm1 the first hash table
     * @param hm2 the second hash table
     * @param filename the name of the file to read
     */
    public static void readMovies(HashMapLP<Integer, Movie> hm1, HashMapSC<Integer, Movie> hm2, String filename){
        try {
            File file = new File(filename);
            Scanner readFile = new Scanner (file);
            while (readFile.hasNextLine()) {
                String s = readFile.nextLine();
                String [] tokens = s.split("\\,"); // change depneding of txt file
                Integer id = Integer.parseInt(tokens[0]);
                String title  = tokens[1];
                String genre = tokens[2];

                Movie grape = new Movie (id, title, genre);
                hm1.put(grape.getID(), grape);
                hm2.put(grape.getID(), grape);
            }
            readFile.close();
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     * read the ratings of the movies from filename and update the number of ratings and the average rating of the movies in hm
     * @param hm the hash table of movies to be updated
     * @param filename the name of the file with the movie ratings
     */
    public static void readRatings(HashMapLP<Integer, Movie> hm, String filename){
        try {
            File file = new File(filename);
            Scanner readFile = new Scanner (file);
            while (readFile.hasNextLine()) {
                // uses movie id to find movie in hm, and updates movie rating using addRating()
                String s = readFile.nextLine();
                String [] tokens = s.split("\\,");
                Integer id = Integer.parseInt(tokens[1]);
                double rating = Double.parseDouble(tokens[2]);
                
                Movie m = hm.get(id);
                if (m != null) {
                    m.addRating(rating);
                }
            }
            readFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     * calls the get method of a list of movie ids and displays the number of iterations for each hash tabel
     * @param hm1 the first hashtable
     * @param hm2 the second hashtable
     * @param ids an array of movie ids to lookup
     */
    public static void testGet(HashMapLP<Integer, Movie> hm1, HashMapSC<Integer, Movie> hm2, int[] ids){
        System.out.println("\nResults of the search operation in the two hashmaps");
        System.out.printf("%-5s\t%-50s\t%-20s\t%-20s\n", 
                          "Id", "Title","Iterations(SC:get)", "Iterations(LP:get)");
        for(int id: ids){
            Movie m = hm1.get(id);
            hm2.get(id);
            if(m == null){
                System.out.println("Movie id not found.");
            }
            else{
                System.out.printf("%-5d\t%-50s\t%-20d\t%-20d\n", 
                                  id, m.getTitle(),HashMapSC.getIterations, HashMapLP.getIterations);
            }
        }
    }
    /**
     * calls the remove method of a list of movie ids and displays the number of iterations for each hashtable
     * @param hm1 the first hashtable
     * @param hm2 the second hashtable
     * @param ids an array of movie ids to remove
     */
    public static void testRemove(HashMapLP<Integer, Movie> hm1, HashMapSC<Integer, Movie> hm2, int[] ids){
        System.out.println("\nResults of the remove operation in the two hashmaps");
        System.out.printf("%-5s\t%-50s\t%-20s\t%-20s\n", 
                          "Id", "Title","Iterations(SC:remove)", "Iterations(LP:remove)");
        for(int id: ids){
            Movie m = hm1.get(id);
            hm1.remove(id);
            hm2.remove(id);
            System.out.printf("%-5d\t%-50s\t%-20d\t%-20d\n", 
                            id, m.getTitle(), HashMapSC.removeIterations, HashMapLP.removeIterations);
        }
    }
    /**
     * sorts the list of movies by ratings first, then select the movies with more than 10,000 ratings and 
     * sort them by the average rating
     * The method uses the merge sort algorithm for the two sortings
     * displays the bottom/top ten rated movies in the hashmap
     */
    public static <E> void mergeSort(List<E> list, 
                                        Comparator<E>comp){
        //create recursive
        if (list.size() > 1) {
            List<E> firstHalf = subList(list, 0, list.size()/2);
            List<E>  secondHalf = subList(list, list.size()/2, list.size());   
            mergeSort(firstHalf, comp);
            mergeSort(secondHalf, comp);
            merge (firstHalf, secondHalf, list, comp);   
        }
    }

    public static void mergeSortMovies (HashMap<Integer, Movie> hm) {
        ArrayList<Movie> movieList1 = hm.values();
        /*ArrayList<Movie> movieList1 = new ArrayList<>();
        for (Movie m : hm.values()) {
            movieList1.add(m);
        }*/
        mergeSort(movieList1, null);
        ArrayList<Movie> movieList2 = new ArrayList<>();
       
        for (Movie m : movieList1) {
            if (m.getRatings() > 10000) {
                movieList2.add(m);
            }
        } 
        //System.out.println(movieList2.get(0).getRating() + "\n" + movieList2.get(1).getRating());

        class ComparatorByRating implements Comparator <Movie> {
             public int compare (Movie m1, Movie m2) {
                Double rate1 = m1.getRating();
                Double rate2 = m2.getRating();
                return rate1.compareTo(rate2);
            }
        }
        mergeSort(movieList2, new ComparatorByRating());
        //System.out.println(movieList2.get(movieList2.size()-1).getRating() + "\n" + movieList2.get(movieList2.size()-2).getRating());

        System.out.println("Bottom ten movies with over 10,000 ratings");
        System.out.printf("%-10s\t%-45s\t%-20s\t%-20s\n", "ID", "Title", "Number of Ratings", "Average Rating");
        
        for (int i = 0; i < 10 && i < movieList2.size(); i++) {
            Movie movie = movieList2.get(i);
            System.out.printf("%-10s\t%-50s\t%-20s\t%-20.1f\n", 
                                movie.getID(), movie.getTitle(),  movie.getRatings(), movie.getRating());
                //System.out.println("$$$$"+ movie.getRating());
        }

        System.out.println("\nTop ten movies with over 10,000 ratings");
        System.out.printf("%-10s\t%-50s\t%-20s\t%-20s\n", "ID", "Title", "Number of Ratings", "Average Rating");
        
        for (int i = movieList2.size()-1; i >= movieList2.size() - 10; i--) {
            Movie movie = movieList2.get(i);
            System.out.printf("%-10s\t%-45s\t%-20s\t%-20.1f\n", 
                            movie.getID(), movie.getTitle(), movie.getRatings(), movie.getRating());
        }
    }

//_________________________________________________________________________
        

    /**
    * subList Method to extract a deep copy of a range of elements
    * @param list to extract from
    * @param start the index at which the extraction starts
    * @param end the index at which the extraction ends
    * throws an IndexOutOfBoundsException if start or end are out of bounds or 
    * if start>end
    * @return a sublist from the element at index start inclusive to end exclusive
    *         an empty list if start equals end
    */
    
    public static <E> List<E> subList(List<E> list, int start, int end){
        if(start < 0 || start >= list.size() || 
        end < 0 || end > list.size() || start > end)
        throw new ArrayIndexOutOfBoundsException();
        
        ArrayList<E> sub = new ArrayList<>();
        if(start == end) return sub;
        for(int i=start; i<end; i++)
            sub.add(list.get(i));
        return sub;
    }
    /**
    * Method merge used by the merge sort method
    * @param list1 the first sorted list
    * @param list2 the second sorted list
    * @param list where list1 and list2 are merged
    */
    public static <E> void merge(List<E> list1, List<E> list2, List<E> list, Comparator<E> comp) {
        int list1Index = 0, list2Index = 0, listIndex = 0;
        while(list1Index < list1.size() && list2Index < list2.size()) {
            int result = 0;
            if (comp == null) {
                result = ((Comparable<E>)list1.get(list1Index)).compareTo(list2.get(list2Index));
            } else { //comparator
                result = comp.compare(list1.get(list1Index),list2.get(list2Index)); //using comp
            }
            
            if (result < 0) {
                //setting the list, get var of list 1
                list.set(listIndex++, list1.get(list1Index++));
            } else {
                // list 2
                list.set(listIndex++, list2.get(list2Index++));
            } 
        }
        // copy the remaining elements from list1 to list if any
        while(list1Index < list1.size()){
        list.set(listIndex++, list1.get(list1Index++));
        }
        // copy the remaining elements from list2 to list if any
        while(list2Index < list2.size()){
            list.set(listIndex++, list2.get(list2Index++));
        }
    }
}



/**
 * Discussion of the results for testing get and remove
 *      The get method for both separate chaining and linear probing
 *      seem to be very efficient as it finds the indicated key
 *      within 1 iteration everytime. The time complexity for both
 *      these methods in SC also has a linear time complexity.
 *      As for the remove method, it is very efficient when used in
 *      separate chaining, being successful after 1 iterations. 
 *      Contrastingly, the remove method yielded mixed results
 *      when used in linear probing, with some iterations being
 *      considerably high. THis indicates that there is more complexity
 *      in the removal process for linear probing.
 * 
 * 
 * Discussion of the characteristics displayed by the method printCharacteristics()
 *      The printCharacteristics() method shows a summarized result of the 
 *      separate chaining and linear probing functions. It can be seen that
 *      separate chaining results in less collisions compared to 
 *      linear probing, but SC has a larger number of buckets as opposed
 *      to the number of clusters in LP
 */
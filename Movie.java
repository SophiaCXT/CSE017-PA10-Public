public class Movie implements Comparable<Movie>{
    private int id;
    private String title;
    private String genre;
    private int ratings;
    private double rating;

    public Movie (int id, String title, String genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    public int getID() {return id;}
    public String getTitle() {return title;}
    public String getGenre() {return genre;} //public Title?
    public int getRatings() {return ratings;}
    public double getRating() {return rating;}

    public void setID(int id) {this.id = id;}
    public void setTitle (String title) {this.title = title;}
    public void setGenre (String genre) {this.genre = genre;}
    public void setRatings (int rs) {this.ratings = rs;}
    
    public void addRating (double r) {
        //increments ratings and updates the average to include the new rating r
        
        //rating = ((rating * (ratings - 1)) + r)/ (double) ratings;
        double totalRating = rating * ratings;
        ratings++;
        rating = (totalRating + r) / ratings;
    }

    public String toString () {
        String out = "";
        out += String.format("%-10d\t%-20s\t%-20s", id, title, genre);
        return out;
    }

    public int compareTo(Movie m) {
        return this.getRatings() - m.getRatings();
    }
}
import java.util.ArrayList;
import java.util.List;

public class Movie extends Video {
    public int duration;
    public ArrayList<Double> rating;

    Movie(final String title, final ArrayList<String> cast,
          final ArrayList<String> genres, final int year,
          final int duration) {
        this.title = title;
        this.cast = cast;
        this.genres = genres;
        this.year = year;
        this.duration = duration;
        this.rating=new ArrayList<>();
    }



    @Override
    public String toString() {
        return "Movie{" +
                "duration=" + duration +
                ", rating=" + rating +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", genres=" + genres +
                ", cast=" + cast +
                '}';
    }
    public double mediaRatingului(){
        Double media=new Double(0);
        if(!rating.isEmpty()){
            for (Double iterator : rating){
                media=new Double(media.doubleValue()+iterator.doubleValue());
            }
            media=new Double(media.doubleValue()/rating.size());
        }
        return media.doubleValue();
    }
    public int numarulAparitiiFavorite(ArrayList<User> users){
        int nrAparitii=0;
        for (User iteratorUsers : users){
            if(iteratorUsers.favoritMovies.contains(this.title))
                nrAparitii++;
        }
        return nrAparitii;
    }
    public int getDuration(){
        return this.duration;
    }

    public int getNumberOfViews(ArrayList<User> users){
        int nov=0;
        for (User iteratorUser : users){
            if(iteratorUser.history.keySet().contains(this.title)){
                nov++;
            }
        }
        return nov;
    }
}

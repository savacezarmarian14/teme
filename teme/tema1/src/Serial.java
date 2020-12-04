import java.util.ArrayList;

public class Serial extends Video {
    public ArrayList<Season> seasons;
    public int numberOfSeansons;

    public Serial(final String title, final ArrayList<String> cast,
                  final ArrayList<String> genres,
                  final int numberOfSeasons, final ArrayList<Season> seasons,
                  final int year){
        this.title=title;
        this.cast=cast;
        this.genres=genres;
        this.numberOfSeansons=numberOfSeasons;
        this.seasons=seasons;
        this.year=year;
    }

    @Override
    public String toString() {
        return "Serial{" +
                "seasons=" + seasons +
                ", numberOfSeansons=" + numberOfSeansons +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", genres=" + genres +
                ", cast=" + cast +
                '}';
    }
    public double mediaRating(){
        double mediaSerial=0;
        int sezonCuRating=0;
        for (Season iteratorSeason : seasons){

            double mediaSeason=0;
            if(!iteratorSeason.ratings.isEmpty()){
                for (Double iteratorRating :iteratorSeason.ratings){
                    mediaSeason+=iteratorRating.doubleValue();
                }
                mediaSeason/=iteratorSeason.ratings.size();
            }
            if(mediaSeason!=0)
                sezonCuRating++;
            //System.out.println(String.valueOf(mediaSeason)+" "+String.valueOf(sezonCuRating));
            mediaSerial+=mediaSeason;
        }
        mediaSerial/=this.seasons.size();
       // System.out.println(mediaSerial+"=================");
        return mediaSerial;
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
        int duration=0;
        for (Season iteratorSeason : this.seasons){
            duration+=iteratorSeason.duration;
        }
        return duration;
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

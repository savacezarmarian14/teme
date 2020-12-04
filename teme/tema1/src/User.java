import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String username;
    public String subscriptionType;
    public ArrayList<String> favoritMovies;
    public Map<String, Integer> history;
    public ArrayList<String>ratedVideos;
    public Map<String,Integer> SeasonsRated;


    public User(final String username, final String subscriptionType,
                final Map<String, Integer> history,
                final ArrayList<String> favoriteMovies){
        this.username=username;
        this.subscriptionType=subscriptionType;
        this.history=history;
        this.favoritMovies=favoriteMovies;
        this.ratedVideos=new ArrayList<>();
        this.SeasonsRated=new HashMap<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", subscriptionType='" + subscriptionType + '\'' +
                ", favoritMovies=" + favoritMovies +
                ", history=" + history +
                '}';
    }

    public int getNumberOfRatings(){
        return this.ratedVideos.size();
    }
}

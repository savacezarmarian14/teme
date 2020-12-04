import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Action {

    public  int actionId;
    public  String actionType;
    public  String type;
    public  String username;
    public String objectType;
    public String sortType;
    public  String criteria;
    public  String title;
    public String genre;
    public int number;
    public double grade;
    public int seasonNumber;
    public List<List<String>> filters = new ArrayList<>();
///Recomandation
    public Action(final int actionId, final String actionType,
                  final String type, final String username, final String genre) {
        this.actionId = actionId;
        this.actionType = actionType;
        this.type = type;
        this.username = username;
        this.genre = genre;
        this.objectType = null;
        this.sortType = null;
        this.criteria = null;
        this.number = 0;
        this.title = null;
        this.grade = 0;
        this.seasonNumber = 0;
    }
///query
    public Action(final int actionId, final String actionType, final String objectType,
                  final String genre, final String sortType, final String criteria,
                  List<List<String>> filters,final int number) {
        this.actionId = actionId;
        this.actionType = actionType;
        this.objectType = objectType;
        this.sortType = sortType;
        this.criteria = criteria;
        this.number = number;
        this.filters=filters;
        this.title = null;
        this.type = null;
        this.username = null;
        this.genre = null;
        this.grade = 0;
        this.seasonNumber = 0;
    }
///command
    public Action(final int actionId, final String actionType, final String type,
                  final String username, final String title, final Double grade,
                  final int seasonNumber) {
        this.actionId = actionId;
        this.actionType = actionType;
        this.type = type;
        this.grade = grade;
        this.username = username;
        this.title = title;
        this.seasonNumber = seasonNumber;
        this.genre = null;
        this.objectType = null;
        this.sortType = null;
        this.criteria = null;
        this.number = 0;
    }

    @Override
    public String toString() {
        return "Action{" +
                "actionId=" + actionId +
                ", actionType='" + actionType + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", objectType='" + objectType + '\'' +
                ", sortType='" + sortType + '\'' +
                ", criteria='" + criteria + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", number=" + number +
                ", grade=" + grade +
                ", seasonNumber=" + seasonNumber +
                ", filters=" + filters +
                '}';
    }
}

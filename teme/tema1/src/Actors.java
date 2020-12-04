
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Actors {
    public String name;
    public String career_description;
    public ArrayList<String> filmography;
    public Map<ActorsAwards,Integer> awards;
    public Map<String,Double>rating;
    public double bestRating;
    public int numberOfAwards;
    public Actors(){
        this.rating=new HashMap<String, Double>();
        this.filmography=new ArrayList<>();
        this.awards=new HashMap<>();
    }
    public Actors(String name, String careerDescription, ArrayList<String> filmography, Map<ActorsAwards, Integer> awards) {
        this.name=name;
        this.career_description=careerDescription;
        this.filmography=filmography;
        this.awards=awards;
        this.rating=new HashMap<String, Double>();
        this.bestRating=0;
        this.numberOfAwards=0;
    }



    @Override
    public String toString() {
        return "Actors{" +
                "name='" + name + '\'' +
                ", career_description='" + career_description + '\'' +
                ", filmography=" + filmography +
                ", awards=" + awards +
                '}';
    }
    public void copy(Actors toCopy){
        this.bestRating=toCopy.bestRating;
        this.rating=toCopy.rating;
        this.name=toCopy.name;
        this.awards=toCopy.awards;
        this.career_description=toCopy.career_description;
        this.filmography=toCopy.filmography;
        this.numberOfAwards=toCopy.numberOfAwards;
    }
    public void swap(Actors toSwapActor){
        Actors auxiliar=new Actors();
        auxiliar.copy(this);
        this.copy(toSwapActor);
        toSwapActor.copy(auxiliar);
    }
    public void setNumberOfAwards(){
        for (ActorsAwards iteratorAwards : this.awards.keySet()){
            this.numberOfAwards+=this.awards.get(iteratorAwards).intValue();
        }
    }
}

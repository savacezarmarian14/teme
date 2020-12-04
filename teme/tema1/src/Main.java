

import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.lang.constant.DynamicConstantDesc;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }



    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */

    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation


        ArrayList<Actors> actors = new ArrayList<>();
        for (ActorInputData actorInputData : input.getActors()) {
            Actors actor = new Actors(actorInputData.getName(),actorInputData.getCareerDescription(),
                    actorInputData.getFilmography(),actorInputData.getAwards());
            actors.add(actor);
        }

        ArrayList<User> users =new ArrayList<>();
        for(UserInputData userInputData : input.getUsers()){
            users.add(new User(userInputData.getUsername(),userInputData.getSubscriptionType(),
            userInputData.getHistory(),userInputData.getFavoriteMovies()));
        }

        ArrayList<Movie> movies=new ArrayList<>();
        for(MovieInputData movieInputData:input.getMovies()){
            movies.add(new Movie(movieInputData.getTitle(),movieInputData.getCast(),
                    movieInputData.getGenres(),movieInputData.getYear(),movieInputData.getDuration()));
        }
        ArrayList<Serial> serials=new ArrayList<>();
        for(SerialInputData serialInputData:input.getSerials()){
            serials.add(new Serial(serialInputData.getTitle(),serialInputData.getCast(),
                    serialInputData.getGenres(),serialInputData.getNumberSeason(),serialInputData.getSeasons(),
                    serialInputData.getYear()));
        }
        ArrayList<Action> actions=new ArrayList<>();
        for(ActionInputData actionInputData: input.getCommands())
        {
            //command
            switch (actionInputData.getActionType()) {
                case "recommendation":
                    actions.add(new Action(actionInputData.getActionId(), actionInputData.getActionType(),
                        actionInputData.getType(), actionInputData.getUsername(), actionInputData.getGenre()));
                    break;
                case "query":
                    actions.add(new Action(actionInputData.getActionId(),actionInputData.getActionType(),
                            actionInputData.getObjectType(),actionInputData.getGenre(),actionInputData.getSortType(),
                            actionInputData.getCriteria(),actionInputData.getFilters(),actionInputData.getNumber()));
                    break;
                case "command" :
                    actions.add(new Action(actionInputData.getActionId(),actionInputData.getActionType(),
                            actionInputData.getType(),actionInputData.getUsername(),actionInputData.getTitle(),
                            actionInputData.getGrade(),actionInputData.getSeasonNumber()));
            }

        }
        System.out.println(filePath1);
        /** ---------------CITIREA DATELOR /|\ ------------------------------**/

        for(Action iterator : actions)
        {
            switch (iterator.actionType){
                case "command":
                    ///Cauta referinta utilizatorului care a actionat comanda
                    User findUser = null;
                    boolean found=false;
                    for (User userIterator : users  ) {
                        if(userIterator.username.equals(iterator.username)){
                            found=true;
                            findUser=userIterator;
                            break;
                        }
                    }
                    switch (iterator.type){
                        case "favorite":
                            if(findUser.history.containsKey(iterator.title)){
                                if(findUser.favoritMovies.contains(iterator.title)){
                                    arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                            "error -> "+iterator.title+" is already in favourite list"));
                                }
                                else
                                {
                                    for (User iteratorUser : users){
                                        if(iteratorUser.username.equals(findUser.username))
                                            iteratorUser.favoritMovies.add(iterator.title);
                                    }
                                    arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                            "success -> "+iterator.title+ " was added as favourite"));
                                }
                            }
                            else
                                arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                        "error -> "+iterator.title+" is not seen"));

                            break;
                        case "view":
                            Integer newNumberOfViews;
                            if(findUser.history.containsKey(iterator.title)) {
                                newNumberOfViews=new Integer(findUser.history.get(iterator.title).intValue()+1);
                                findUser.history.replace(iterator.title,newNumberOfViews);
                            }
                            else {
                                findUser.history.put(iterator.title, new Integer(1));
                                newNumberOfViews=new Integer(1);
                            }
                            arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                    "success -> "+iterator.title+" was viewed with total views of "+
                                    String.valueOf(newNumberOfViews.intValue())));

                            break;
                        case "rating":
                            /**movie*/
                            if (findUser.SeasonsRated.keySet().contains(iterator.title))
                                if(findUser.SeasonsRated.get(iterator.title).intValue()==iterator.seasonNumber) {
                                    arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                            "error -> " + iterator.title + " has been already rated"));
                                    break;
                                }
                            if(findUser.ratedVideos.contains(iterator.title)) {
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                        "error -> " + iterator.title + " has been already rated"));
                                break;
                            }
                            if(!findUser.history.containsKey(iterator.title)){
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                        "error -> " + iterator.title + " is not seen"));
                                break;
                            }
                                Movie findMovieRef=null;
                                for (Movie iteratorMovies : movies){
                                    if(iteratorMovies.title.equals(iterator.title)){
                                        findMovieRef=iteratorMovies;
                                        break;
                                    }
                                }
                                if (findMovieRef!=null){
                                    findMovieRef.rating.add(iterator.grade);
                                    findUser.ratedVideos.add(findMovieRef.title);
                                }
                             /**Serial*/
                                Serial findSerialRef=null;
                                for(Serial iteratorSerial : serials){
                                    if(iteratorSerial.title.equals(iterator.title)){
                                        findSerialRef=iteratorSerial;
                                        break;
                                    }
                                }
                                if(findSerialRef!=null){
                                    findUser.ratedVideos.add(findSerialRef.title);
                                    findSerialRef.seasons.get(iterator.seasonNumber-1).ratings.add(iterator.grade);
                                }
                                arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                        "success -> "+iterator.title+" was rated with "+
                                                String.valueOf(iterator.grade)+" by "+iterator.username));
                            break;
                    }
                    break;
                case "query":
                    ArrayList<String>queryResult=new ArrayList<>();
                    ArrayList<Actors>queryActorsResult=new ArrayList<>();

                    switch (iterator.objectType){
                        case "actors":
                            switch (iterator.criteria){
                                case "average":
                                    for (Actors iteratorActors: actors){
                                        iteratorActors.rating=new HashMap<>();
                                    }
                                    for (Actors iteratorActors: actors){
                                        for (Movie iteratorMovie : movies){
                                            if (iteratorActors.filmography.contains(iteratorMovie.title)){
                                                iteratorActors.rating.put(iteratorMovie.title,
                                                        new Double(iteratorMovie.mediaRatingului()));
                                            }
                                        }
                                        for (Serial iteratorSerial : serials){
                                            if(iteratorActors.filmography.contains(iteratorSerial.title)){
                                                iteratorActors.rating.put(iteratorSerial.title,
                                                        new Double(iteratorSerial.mediaRating()));
                                            }
                                        }
                                    }
                                    for (Actors iteratorActors : actors){
                                        for (String iteratorKey : iteratorActors.rating.keySet()){
                                            if(iteratorActors.rating.get(iteratorKey).doubleValue()>iteratorActors.bestRating){
                                                iteratorActors.bestRating=iteratorActors.rating.get(iteratorKey).doubleValue();
                                            }
                                        }
                                    }
                                    int sortMaker;
                                    switch (iterator.sortType){
                                        case "asc":
                                            sortMaker=1;
                                            break;
                                        case "desc":
                                            sortMaker=-1;
                                            break;
                                        default:
                                            sortMaker=0;
                                    }
                                    for (int i = 0; i < actors.size()-1 ; i++) {
                                        for (int j = i+1; j <actors.size() ; j++) {
                                            if(sortMaker*actors.get(i).bestRating>sortMaker*actors.get(j).bestRating)
                                                actors.get(i).swap(actors.get(j));
                                            if(sortMaker*actors.get(i).bestRating==sortMaker*actors.get(j).bestRating){
                                                if(actors.get(i).name.compareTo(actors.get(j).name)>0 && sortMaker ==1 ||
                                                        actors.get(i).name.compareTo(actors.get(j).name)<0 && sortMaker ==-1)
                                                    actors.get(i).swap(actors.get(j));
                                            }
                                        }
                                    }
                                    int number=iterator.number;
                                    for (int i = 0; i < actors.size()-1 && number!=0; i++){
                                        if (actors.get(i).bestRating!=0) {
                                            queryResult.add(actors.get(i).name);
                                            number--;
                                        }
                                    }
                                    arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                            "Query result: "+String.valueOf(queryResult)));
                                    break;
                                case "awards":
                                    for (Actors iteratorActors : actors) {
                                        iteratorActors.setNumberOfAwards();
                                    }
                                    ArrayList<String> toFindAwards =new ArrayList<>();
                                    for (String iteratorAwards : iterator.filters.get(3)){
                                        toFindAwards.add(iteratorAwards);
                                    }

                                    for (Actors iteratorActors : actors){
                                        ArrayList<String> actorAwards=new ArrayList<>();
                                        for (ActorsAwards iteratorActorAwards: iteratorActors.awards.keySet()){
                                            actorAwards.add(String.valueOf(iteratorActorAwards));
                                        }
                                        boolean hasAllAwards=true;
                                        for (String iteratorAwards : toFindAwards){
                                            if(!actorAwards.contains(iteratorAwards)){
                                                hasAllAwards=false;
                                            }
                                        }
                                        if(hasAllAwards==true)
                                            queryActorsResult.add(iteratorActors);
                                    }
                                    ///sort
                                    int sort=1;
                                    switch (iterator.sortType){
                                        case "asc":
                                            sort=1;
                                            break;
                                        case "desc":
                                            sort=-1;
                                            break;
                                    }
                                    for (int i = 0; i < queryActorsResult.size()-1; i++) {
                                        for (int j = i+1; j <queryActorsResult.size(); j++) {
                                            if(sort*queryActorsResult.get(i).numberOfAwards > sort*queryActorsResult.get(j).numberOfAwards) {
                                                queryActorsResult.get(i).swap(queryActorsResult.get(j));
                                            }
                                            if (sort*queryActorsResult.get(i).numberOfAwards == sort*queryActorsResult.get(j).numberOfAwards){
                                                if(queryActorsResult.get(i).name.compareTo(queryActorsResult.get(j).name)>0 && sort == 1 ||
                                                        queryActorsResult.get(i).name.compareTo(queryActorsResult.get(j).name)<0 && sort == -1 ){
                                                    queryActorsResult.get(i).swap(queryActorsResult.get(j));
                                                }
                                            }
                                        }
                                    }
                                    for (int i = 0; i < queryActorsResult.size(); i++) {
                                        queryResult.add(queryActorsResult.get(i).name);
                                    }
                                    arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                            "Query result: "+String.valueOf(queryResult)));
                                    break;
                                case "filter_description":
                                    queryActorsResult=new ArrayList<>();
                                    ArrayList<String>toFindWords=new ArrayList<>();
                                    for (String iteratorString : iterator.filters.get(2))
                                        toFindWords.add(iteratorString);
                                    for (Actors iteratorActors : actors){
                                        String[] words=iteratorActors.career_description.toLowerCase().split("[, ?.@]+");
                                        ArrayList<String>wrd=new ArrayList<>();
                                            for (int i = 0; i < words.length; i++) {
                                                wrd.add(words[i]);
                                            }
                                        boolean containsAllWords=true;
                                        for (String iteratorString : toFindWords){
                                            if(!wrd.contains(iteratorString))
                                            {
                                                containsAllWords=false;
                                            }

                                        }
                                        if(containsAllWords==true)
                                            queryActorsResult.add(iteratorActors);
                                    }
                                    for (int i = 0; i < queryActorsResult.size()-1; i++) {
                                        for (int j = 0; j <queryActorsResult.size() ; j++) {
                                            if(queryActorsResult.get(i).name.compareTo(queryActorsResult.get(j).name)>0 && iterator.sortType == "asc" ||
                                                    queryActorsResult.get(i).name.compareTo(queryActorsResult.get(j).name)<0 && iterator.sortType == "desc" ){
                                                queryActorsResult.get(i).swap(queryActorsResult.get(j));
                                            }
                                        }
                                    }
                                    for (int i = 0; i < queryActorsResult.size(); i++) {
                                        queryResult.add(queryActorsResult.get(i).name);
                                    }
                                    arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                            "Query result: "+String.valueOf(queryResult)));
                                    break;
                            }
                            break;
                        case "users":
                            ArrayList<String> usernames =new ArrayList<>();
                            ArrayList<Integer>numberOfRating =new ArrayList<>();
                            for (User iteratorUser : users){
                                if(iteratorUser.getNumberOfRatings()!=0){
                                    usernames.add(iteratorUser.username);
                                    numberOfRating.add(Integer.valueOf(iteratorUser.getNumberOfRatings()));
                                }
                            }
                            int usersort=1;
                            switch (iterator.sortType){
                                case "asc":
                                    usersort=1;
                                    break;
                                case "desc":
                                    usersort=-1;
                                    break;
                            }
                            for (int i = 0; i <usernames.size()-1 ; i++) {
                                for (int j = i+1; j <usernames.size() ; j++) {
                                    if (usersort*numberOfRating.get(i).intValue() > usersort*numberOfRating.get(j).intValue()){
                                        Integer aux=Integer.valueOf(numberOfRating.get(i).intValue());
                                        numberOfRating.set(i,Integer.valueOf(numberOfRating.get(j).intValue()));
                                        numberOfRating.set(j,Integer.valueOf(aux.intValue()));
                                        String auxs=String.valueOf(usernames.get(i));
                                        usernames.set(i,String.valueOf(usernames.get(j)));
                                        usernames.set(j,String.valueOf(auxs));
                                    }
                                    if (numberOfRating.get(i).intValue() == numberOfRating.get(j).intValue()){
                                        if (usernames.get(i).compareTo(usernames.get(j)) > 0 && iterator.sortType.compareTo("asc")==0 ||
                                        usernames.get(i).compareTo(usernames.get(j))<0 && iterator.sortType.compareTo("desc")==0){
                                            String auxs=String.valueOf(usernames.get(i));
                                            usernames.set(i,String.valueOf(usernames.get(j)));
                                            usernames.set(j,String.valueOf(auxs));
                                        }
                                    }
                                }
                            }
                            int no=iterator.number;
                            for (int i = 0; i <usernames.size() && no>0 ; i++) {
                                no--;
                                queryResult.add(usernames.get(i));
                            }
                            arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                    "Query result: "+String.valueOf(queryResult)));
                            break;

                    }
                    if(iterator.objectType.contains("movies") || iterator.objectType.contains("shows")){
                        switch (iterator.criteria){
                            case "ratings":
                                ArrayList<String>VideosName=new ArrayList<>();
                                ArrayList<Double>VideosRating=new ArrayList<>();
                                switch (iterator.objectType) {
                                    case "movies":
                                        for (Movie iteratorMovies : movies) {
                                            if (iteratorMovies.mediaRatingului() != 100) {
                                                if(iterator.filters.get(0)!=null){
                                                    if (iterator.filters.get(0).contains(String.valueOf(iteratorMovies.year))) {
                                                        if(iterator.filters.get(1)!=null){
                                                            System.out.println(iteratorMovies.title+" "+iteratorMovies.genres);
                                                            if(iteratorMovies.genres.contains(iterator.filters.get(1).get(0))){
                                                                System.out.println(iterator.actionId);
                                                                VideosName.add(iteratorMovies.title);
                                                                VideosRating.add(Double.valueOf(iteratorMovies.mediaRatingului()));
                                                            }
                                                        }
                                                        else {
                                                            VideosName.add(iteratorMovies.title);
                                                            VideosRating.add(Double.valueOf(iteratorMovies.mediaRatingului()));
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(iterator.filters.get(1)!=null){
                                                        if(iteratorMovies.genres.contains(iterator.filters.get(1).get(0))){
                                                            VideosName.add(iteratorMovies.title);
                                                            VideosRating.add(Double.valueOf(iteratorMovies.mediaRatingului()));
                                                        }
                                                    }
                                                    else {
                                                        VideosName.add(iteratorMovies.title);
                                                        VideosRating.add(Double.valueOf(iteratorMovies.mediaRatingului()));
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case "shows":
                                        for (Serial iteratorSerials : serials) {
                                            if (iteratorSerials.mediaRating() != 0 ){
                                                if(iterator.filters.get(0)!=null && iterator.filters.get(0).get(0)!=null){
                                                    if(String.valueOf(iteratorSerials.year).compareTo(iterator.filters.get(0).get(0))==0){
                                                        if(iterator.filters.get(1)!=null){
                                                            if(iteratorSerials.genres.contains(iterator.filters.get(1).get(0))){
                                                                VideosName.add(iteratorSerials.title);
                                                                VideosRating.add(Double.valueOf(iteratorSerials.mediaRating()));
                                                            }
                                                        }
                                                        else {
                                                            VideosName.add(iteratorSerials.title);
                                                            VideosRating.add(Double.valueOf(iteratorSerials.mediaRating()));
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(iterator.filters.get(1)!=null){
                                                        if(iteratorSerials.genres.contains(iterator.filters.get(1).get(0))){
                                                            VideosName.add(iteratorSerials.title);
                                                            VideosRating.add(Double.valueOf(iteratorSerials.mediaRating()));
                                                        }
                                                    }
                                                    else{
                                                        VideosName.add(iteratorSerials.title);
                                                        VideosRating.add(Double.valueOf(iteratorSerials.mediaRating()));
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    }

                                int sortcrt=1;
                                switch (iterator.sortType){
                                    case "asc":
                                        sortcrt=1;
                                        break;
                                    case "desc":
                                        sortcrt=-1;
                                        break;
                                }
                                for (int i = 0; i < VideosRating.size()-1; i++) {
                                    for (int j = i+1; j < VideosRating.size(); j++) {
                                        if(sortcrt*VideosRating.get(i).doubleValue() > sortcrt*VideosRating.get(j).doubleValue()){
                                            Double aux=Double.valueOf(VideosRating.get(i).doubleValue());
                                            VideosRating.set(i,Double.valueOf(VideosRating.get(j).doubleValue()));
                                            VideosRating.set(j,Double.valueOf(aux.doubleValue()));
                                            String auxs=String.valueOf(VideosName.get(i));
                                            VideosName.set(i,String.valueOf(VideosName.get(j)));
                                            VideosName.set(j,String.valueOf(auxs));
                                        }
                                        if(sortcrt*VideosRating.get(i).doubleValue() == sortcrt*VideosRating.get(j).doubleValue()){
                                            if(VideosName.get(i).compareTo(VideosName.get(j)) > 0 && sortcrt == 1 ||
                                                    VideosName.get(i).compareTo(VideosName.get(j)) < 0 && sortcrt == -1){
                                                String auxs=String.valueOf(VideosName.get(i));
                                                VideosName.set(i,String.valueOf(VideosName.get(j)));
                                                VideosName.set(j,String.valueOf(auxs));
                                            }
                                        }
                                    }
                                }
                                int nr=iterator.number;
                                for (int i = 0; i <VideosName.size() && nr>0 ; i++) {
                                    nr--;
                                    queryResult.add(VideosName.get(i));
                                }
                                arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                        "Query result: "+String.valueOf(queryResult)));

                                break;
                            case "favorite":
                                VideosName=new ArrayList<>();
                                ArrayList<Integer> favoriteVideosViews =new ArrayList<>();
                                switch (iterator.objectType) {
                                    case "movies":
                                        for (Movie iteratorMovies : movies) {
                                            if (iteratorMovies.numarulAparitiiFavorite(users) != 0) {
                                                if(iterator.filters.get(0)!=null){
                                                    if (iterator.filters.get(0).contains(String.valueOf(iteratorMovies.year))) {
                                                        if(!iterator.filters.get(1).isEmpty() && iterator.filters.get(1).get(0)!=null){
                                                            if(iteratorMovies.genres.contains(iterator.filters.get(1).get(0))){
                                                                VideosName.add(iteratorMovies.title);
                                                                favoriteVideosViews.add(Integer.valueOf(iteratorMovies.numarulAparitiiFavorite(users)));
                                                            }
                                                        }
                                                        else {
                                                            VideosName.add(iteratorMovies.title);
                                                            favoriteVideosViews.add(Integer.valueOf(iteratorMovies.numarulAparitiiFavorite(users)));
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(iterator.filters.get(1)!=null){
                                                        if(iteratorMovies.genres.contains(iterator.filters.get(1).get(0))){
                                                            VideosName.add(iteratorMovies.title);
                                                            favoriteVideosViews.add(Integer.valueOf(iteratorMovies.numarulAparitiiFavorite(users)));
                                                        }
                                                    }
                                                    else {
                                                        VideosName.add(iteratorMovies.title);
                                                        favoriteVideosViews.add(Integer.valueOf(iteratorMovies.numarulAparitiiFavorite(users)));
                                                    }
                                                }
                                                if(iterator.filters.get(0).get(0)== null && iterator.filters.get(1).get(0)==null){
                                                    VideosName.add(iteratorMovies.title);
                                                    favoriteVideosViews.add(Integer.valueOf(iteratorMovies.numarulAparitiiFavorite(users)));
                                                }
                                            }
                                        }
                                        break;
                                    case "shows":
                                        for (Serial iteratorSerials : serials) {
                                            if (iteratorSerials.numarulAparitiiFavorite(users) != 0 ){
                                                if(iterator.filters.get(0)!=null && iterator.filters.get(0).get(0)!=null){
                                                    if(String.valueOf(iteratorSerials.year).compareTo(iterator.filters.get(0).get(0))==0){
                                                        if(iterator.filters.get(1)!=null){
                                                            if(iteratorSerials.genres.contains(iterator.filters.get(1).get(0))){
                                                                VideosName.add(iteratorSerials.title);
                                                                favoriteVideosViews.add(Integer.valueOf(iteratorSerials.numarulAparitiiFavorite(users)));
                                                            }
                                                        }
                                                        else {
                                                            VideosName.add(iteratorSerials.title);
                                                            favoriteVideosViews.add(Integer.valueOf(iteratorSerials.numarulAparitiiFavorite(users)));
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(iterator.filters.get(1)!=null){
                                                        if(iteratorSerials.genres.contains(iterator.filters.get(1).get(0))){
                                                            VideosName.add(iteratorSerials.title);
                                                            favoriteVideosViews.add(Integer.valueOf(iteratorSerials.numarulAparitiiFavorite(users)));
                                                        }
                                                    }
                                                    else{
                                                        VideosName.add(iteratorSerials.title);
                                                        favoriteVideosViews.add(Integer.valueOf(iteratorSerials.numarulAparitiiFavorite(users)));
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                }
                                int srt=1;
                                switch (iterator.sortType){
                                    case "asc":
                                        srt=1;
                                        break;
                                    case "desc":
                                        srt=-1;
                                        break;
                                }
                                for (int i = 0; i < VideosName.size()-1; i++) {
                                    for (int j = i+1; j < VideosName.size() ; j++) {
                                        if(srt*favoriteVideosViews.get(i).intValue() > srt*favoriteVideosViews.get(j).intValue()){
                                            Integer aux=new Integer(favoriteVideosViews.get(i).intValue());
                                            favoriteVideosViews.set(i,Integer.valueOf(favoriteVideosViews.get(j).intValue()));
                                            favoriteVideosViews.set(j,Integer.valueOf(aux.intValue()));
                                            String auxs=String.valueOf(VideosName.get(i));
                                            VideosName.set(i,String.valueOf(VideosName.get(j)));
                                            VideosName.set(j,String.valueOf(auxs));
                                        }
                                        if(srt*favoriteVideosViews.get(i).intValue() == srt*favoriteVideosViews.get(j).intValue()){
                                            if(VideosName.get(i).compareTo(VideosName.get(j)) > 0 && srt == 1 ||
                                                    VideosName.get(i).compareTo(VideosName.get(j)) < 0 && srt == -1){
                                                String auxs=String.valueOf(VideosName.get(i));
                                                VideosName.set(i,String.valueOf(VideosName.get(j)));
                                                VideosName.set(j,String.valueOf(auxs));
                                            }
                                        }
                                    }
                                }
                                int nr1=iterator.number;
                                for (int i = 0; i <VideosName.size() && nr1>0 ; i++) {
                                    nr1--;
                                    queryResult.add(VideosName.get(i));
                                }
                                arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                        "Query result: "+String.valueOf(queryResult)));
                                break;
                            case "longest":
                                VideosName=new ArrayList<>();
                                favoriteVideosViews =new ArrayList<>();
                                switch (iterator.objectType) {
                                    case "movies":
                                        for (Movie iteratorMovies : movies) {
                                            if (iteratorMovies.getDuration() != 0) {
                                                if(iterator.filters.get(0)!=null && iterator.filters.get(0).get(0)!=null){
                                                    if (String.valueOf(iteratorMovies.year).compareTo(iterator.filters.get(0).get(0))==0) {
                                                        if(iterator.filters.get(1)!=null){
                                                            if(iteratorMovies.genres.contains(iterator.filters.get(1).get(0))){
                                                                VideosName.add(iteratorMovies.title);
                                                                favoriteVideosViews.add(Integer.valueOf(iteratorMovies.getDuration()));
                                                            }
                                                        }
                                                        else {
                                                            VideosName.add(iteratorMovies.title);
                                                            favoriteVideosViews.add(Integer.valueOf(iteratorMovies.getDuration()));
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(iterator.filters.get(1)!=null){
                                                        if(iteratorMovies.genres.contains(iterator.filters.get(1).get(0))){
                                                            VideosName.add(iteratorMovies.title);
                                                            favoriteVideosViews.add(Integer.valueOf(iteratorMovies.getDuration()));
                                                        }
                                                    }
                                                    else {
                                                        VideosName.add(iteratorMovies.title);
                                                        favoriteVideosViews.add(Integer.valueOf(iteratorMovies.getDuration()));
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case "shows":
                                        for (Serial iteratorSerials : serials) {
                                            if (iteratorSerials.getDuration() != 0 ){
                                                if(iterator.filters.get(0)!=null && iterator.filters.get(0).get(0)!=null){
                                                    if(String.valueOf(iteratorSerials.year).compareTo(iterator.filters.get(0).get(0))==0){
                                                        if(iterator.filters.get(1)!=null){
                                                            if(iteratorSerials.genres.contains(iterator.filters.get(1).get(0))){
                                                                VideosName.add(iteratorSerials.title);
                                                                favoriteVideosViews.add(Integer.valueOf(iteratorSerials.getDuration()));
                                                            }
                                                        }
                                                        else {
                                                            VideosName.add(iteratorSerials.title);
                                                            favoriteVideosViews.add(Integer.valueOf(iteratorSerials.getDuration()));
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(iterator.filters.get(1)!=null){
                                                        if(iteratorSerials.genres.contains(iterator.filters.get(1).get(0))){
                                                            VideosName.add(iteratorSerials.title);
                                                            favoriteVideosViews.add(Integer.valueOf(iteratorSerials.getDuration()));
                                                        }
                                                    }
                                                    else{
                                                        VideosName.add(iteratorSerials.title);
                                                        favoriteVideosViews.add(Integer.valueOf(iteratorSerials.getDuration()));
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                }
                                int srt1=1;
                                switch (iterator.sortType){
                                    case "asc":
                                        srt1=1;
                                        break;
                                    case "desc":
                                        srt1=-1;
                                        break;
                                }
                                for (int i = 0; i < VideosName.size()-1; i++) {
                                    for (int j = i+1; j < VideosName.size() ; j++) {
                                        if(srt1*favoriteVideosViews.get(i).intValue() > srt1*favoriteVideosViews.get(j).intValue()){
                                            Integer aux=new Integer(favoriteVideosViews.get(i).intValue());
                                            favoriteVideosViews.set(i,Integer.valueOf(favoriteVideosViews.get(j).intValue()));
                                            favoriteVideosViews.set(j,Integer.valueOf(aux.intValue()));
                                            String auxs=String.valueOf(VideosName.get(i));
                                            VideosName.set(i,String.valueOf(VideosName.get(j)));
                                            VideosName.set(j,String.valueOf(auxs));
                                        }
                                        if(srt1*favoriteVideosViews.get(i).intValue() == srt1*favoriteVideosViews.get(j).intValue()){
                                            if(VideosName.get(i).compareTo(VideosName.get(j)) > 0 && srt1 == 1 ||
                                                    VideosName.get(i).compareTo(VideosName.get(j)) < 0 && srt1 == -1){
                                                String auxs=String.valueOf(VideosName.get(i));
                                                VideosName.set(i,String.valueOf(VideosName.get(j)));
                                                VideosName.set(j,String.valueOf(auxs));
                                            }
                                        }
                                    }
                                }
                                int nr2=iterator.number;
                                for (int i = 0; i <VideosName.size() && nr2>0 ; i++) {
                                    nr2--;
                                    queryResult.add(VideosName.get(i));
                                }
                                arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                        "Query result: "+String.valueOf(queryResult)));
                                break;
                            case "most_viewed":
                                VideosName=new ArrayList<>();
                                ArrayList<Integer> VideosViews =new ArrayList<Integer>();
                                switch (iterator.objectType) {
                                    case "movies":
                                        for (Movie iteratorMovies : movies) {
                                            if (iteratorMovies.getNumberOfViews(users) != 0) {
                                                if(iterator.filters.get(0)!=null && iterator.filters.get(0).get(0)!=null){
                                                    if (String.valueOf(iteratorMovies.year).compareTo(iterator.filters.get(0).get(0))==0) {
                                                        if(iterator.filters.get(1)!=null){
                                                            if(iteratorMovies.genres.contains(iterator.filters.get(1).get(0))){
                                                                VideosName.add(iteratorMovies.title);
                                                                VideosViews.add(Integer.valueOf(iteratorMovies.getNumberOfViews(users)));
                                                            }
                                                        }
                                                        else {
                                                            VideosName.add(iteratorMovies.title);
                                                            VideosViews.add(Integer.valueOf(iteratorMovies.getNumberOfViews(users)));
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(iterator.filters.get(1)!=null){
                                                        if(iteratorMovies.genres.contains(iterator.filters.get(1).get(0))){
                                                            VideosName.add(iteratorMovies.title);
                                                            VideosViews.add(Integer.valueOf(iteratorMovies.getNumberOfViews(users)));
                                                        }
                                                    }
                                                    else {
                                                        VideosName.add(iteratorMovies.title);
                                                        VideosViews.add(Integer.valueOf(iteratorMovies.getNumberOfViews(users)));
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case "shows":
                                        for (Serial iteratorSerials : serials) {
                                            if (iteratorSerials.getNumberOfViews(users) != 0 ){
                                                if(iterator.filters.get(0)!=null && iterator.filters.get(0).get(0)!=null){
                                                    if(String.valueOf(iteratorSerials.year).compareTo(iterator.filters.get(0).get(0))==0){
                                                        if(iterator.filters.get(1)!=null){
                                                            if(iteratorSerials.genres.contains(iterator.filters.get(1).get(0))){
                                                                VideosName.add(iteratorSerials.title);
                                                                VideosViews.add(Integer.valueOf(iteratorSerials.getNumberOfViews(users)));
                                                            }
                                                        }
                                                        else {
                                                            VideosName.add(iteratorSerials.title);
                                                            VideosViews.add(Integer.valueOf(iteratorSerials.getNumberOfViews(users)));
                                                        }
                                                    }
                                                }
                                                else {
                                                    if(iterator.filters.get(1)!=null){
                                                        if(iteratorSerials.genres.contains(iterator.filters.get(1).get(0))){
                                                            VideosName.add(iteratorSerials.title);
                                                            VideosViews.add(Integer.valueOf(iteratorSerials.getNumberOfViews(users)));
                                                        }
                                                    }
                                                    else{
                                                        VideosName.add(iteratorSerials.title);
                                                        VideosViews.add(Integer.valueOf(iteratorSerials.getNumberOfViews(users)));
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                }

                                int srt2=1;
                                switch (iterator.sortType){
                                    case "asc":
                                        srt2=1;
                                        break;
                                    case "desc":
                                        srt2=-1;
                                        break;
                                }
                                for (int i = 0; i < VideosName.size()-1; i++) {
                                    for (int j = i+1; j < VideosName.size() ; j++) {
                                        if(srt2*VideosViews.get(i).intValue() > srt2*VideosViews.get(j).intValue()){
                                            Integer aux=new Integer(VideosViews.get(i).intValue());
                                            VideosViews.set(i,Integer.valueOf(VideosViews.get(j).intValue()));
                                            VideosViews.set(j,Integer.valueOf(aux.intValue()));
                                            String auxs=String.valueOf(VideosName.get(i));
                                            VideosName.set(i,String.valueOf(VideosName.get(j)));
                                            VideosName.set(j,String.valueOf(auxs));
                                        }
                                        if(srt2*VideosViews.get(i).intValue() == srt2*VideosViews.get(j).intValue()){
                                            if(VideosName.get(i).compareTo(VideosName.get(j)) > 0 && srt2 == 1 ||
                                                    VideosName.get(i).compareTo(VideosName.get(j)) < 0 && srt2 == -1){
                                                String auxs=String.valueOf(VideosName.get(i));
                                                VideosName.set(i,String.valueOf(VideosName.get(j)));
                                                VideosName.set(j,String.valueOf(auxs));
                                            }
                                        }
                                    }
                                }
                                int nr3=iterator.number;
                                for (int i = 0; i <VideosName.size() && nr3>0 ; i++) {
                                    nr3--;
                                    queryResult.add(VideosName.get(i));
                                }
                                arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                        "Query result: "+String.valueOf(queryResult)));
                                break;
                        }

                    }

                    break;
                case "recommendation":
                    ///TODO
                    User interogatedUser=null;
                    for (User iteratorUser : users){
                        if(iteratorUser.username.equals(iterator.username)){
                            interogatedUser=iteratorUser;
                            break;
                        }
                    }
                    ArrayList<Video> videos =new ArrayList<>();
                    for (Movie iteratorMovie : movies){
                        videos.add(iteratorMovie);
                    }
                    for (Serial iteratorSerial : serials){
                        videos.add(iteratorSerial);
                    }
                    switch (iterator.type){
                        case "standard" :
                            String result=new String();
                            for (Video iteratorVideos : videos){
                                if(!interogatedUser.history.keySet().contains(iteratorVideos.title)){
                                    result=new String(iteratorVideos.title);
                                    break;
                                }
                            }
                            if(!result.equals(""))
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null ,
                                      "StandardRecommendation result: "+ result ));
                            else
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null ,
                                        "StandardRecommendation cannot be applied!"));

                            break;

                        case "best_unseen" :
                            String result_best_unseen=new String();
                            ArrayList<String>ratedVideos=new ArrayList<>();
                            for (Movie iteratorMovies : movies){
                                if(iteratorMovies.mediaRatingului()==0)
                                    ratedVideos.add(iteratorMovies.title);
                            }
                            for (Serial iteratorSerials : serials){
                                if(iteratorSerials.mediaRating()==0){
                                    ratedVideos.add(iteratorSerials.title);
                                }
                            }
                            for (String iteratorVideoTitle : ratedVideos){
                                if(!interogatedUser.history.keySet().contains(iteratorVideoTitle)){
                                    result_best_unseen=new String(iteratorVideoTitle);
                                }
                            }
                            if(!result_best_unseen.equals(""))
                                arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                        "BestRatedUnseenRecommendation result: "+result_best_unseen));
                            else
                                arrayResult.add(fileWriter.writeFile(iterator.actionId,null,
                                        "BestRatedUnseenRecommendation cannot be applied!"));

                            break;

                        case "popular":
                            String resultPopular = new String();
                            if(!interogatedUser.subscriptionType.equals("PREMIUM")) {
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                        "PopularRecommendation cannot be applied!"));
                                break;
                            }
                            Map<String, Integer> popularity=new HashMap<>();
                            for (Genre iteratorGenre : Genre.values()){
                                popularity.put(String.valueOf(iteratorGenre),Integer.valueOf(0));
                            }
                           // for (User iteratorUser : users){
                               // if(!iteratorUser.username.equals(interogatedUser.username)){
                                    for(Video iteratorVideo : videos){
                                        if(!interogatedUser.history.keySet().contains(iteratorVideo.title)){
                                            for (String iteratorGenre : iteratorVideo.genres){
                                                if(iteratorGenre.toUpperCase().compareTo("ACTION & ADVENTURE")==0)
                                                    iteratorGenre=new String("ACTION_ADVENTURE");
                                                if(iteratorGenre.toUpperCase().compareTo("SCI-FI & FANTASY")==0)
                                                    iteratorGenre=new String("SCI_FI_FANTASY");
                                                if(iteratorGenre.toUpperCase().compareTo("TV MOVIE")==0)
                                                    iteratorGenre=new String("TV_MOVIE");
                                                if(iteratorGenre.toUpperCase().compareTo("SCIENCE FICTION")==0)
                                                    iteratorGenre=new String("SCIENCE_FICTION");
                                                int views=popularity.get(iteratorGenre.toUpperCase()).intValue();
                                                views++;
                                                popularity.replace(iteratorGenre.toUpperCase(),views);
                                            }
                                        }
                                    }
                             //   }
                            //}
                            ArrayList<String>sortedGenre = new ArrayList<>();
                            while (popularity.size()!=0){
                                int max=-1;
                                String addString=new String();
                                for(String iteratorString : popularity.keySet()){
                                    if(popularity.get(iteratorString).intValue() > max ){
                                        max=popularity.get(iteratorString).intValue();
                                        addString=new String(iteratorString);
                                    }
                                }
                                sortedGenre.add(addString);
                                popularity.remove(addString);
                            }
                            int foundPopualar=0;
                            for(String iteratorString : sortedGenre){
                                for (Video iteratorVideo : videos){
                                    if(!interogatedUser.history.keySet().contains(iteratorVideo.title)){
                                        for (String iteratorGenre : iteratorVideo.genres){
                                            if(iteratorGenre.toUpperCase().compareTo("ACTION & ADVENTURE")==0)
                                                iteratorGenre=new String("ACTION_ADVENTURE");
                                            if(iteratorGenre.toUpperCase().compareTo("SCI-FI & FANTASY")==0)
                                                iteratorGenre=new String("SCI_FI_FANTASY");
                                            if(iteratorGenre.toUpperCase().compareTo("TV MOVIE")==0)
                                                iteratorGenre=new String("TV_MOVIE");
                                            if(iteratorGenre.toUpperCase().compareTo("SCIENCE FICTION")==0)
                                                iteratorGenre=new String("SCIENCE_FICTION");
                                            if(iteratorGenre.toUpperCase().equals(iteratorString) && foundPopualar==0){
                                                resultPopular=new String(iteratorVideo.title);
                                            }
                                        }
                                    }
                                }
                            }
                            if(resultPopular.compareTo("")==0){
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                        "PopularRecommendation cannot be applied!"));
                            }
                            else
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null ,
                                        "PopularRecommendation result: "+ resultPopular ));
                            break;

                        case "favorite":
                            String resultFavorite = null;
                            if(!interogatedUser.subscriptionType.equals("PREMIUM")) {
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                        "FavoriteRecommendation cannot be applied!"));
                                break;
                            }
                            ArrayList<String>favoriteMovies=new ArrayList<>();
                            for (User iteratorUser : users){
                                if(!iteratorUser.username.equals(interogatedUser.username)){
                                    for (String iteratorFavVideos : iteratorUser.favoritMovies){
                                        favoriteMovies.add(iteratorFavVideos);
                                    }
                                }
                            }
                            HashMap<String,Integer> FavFreq = new HashMap<>();
                            for (int i = 0; i < favoriteMovies.size(); i++) {
                                if(!FavFreq.keySet().contains(favoriteMovies.get(i))){
                                    FavFreq.put(favoriteMovies.get(i),new Integer(1));
                                }
                                else{
                                    int newValue=FavFreq.get(favoriteMovies.get(i));
                                    newValue++;
                                    FavFreq.replace(favoriteMovies.get(i),Integer.valueOf(newValue));
                                }
                            }
                           if(FavFreq.isEmpty()){
                               arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                       "FavoriteRecommendation cannot be applied!"));
                               break;
                           }
                           ArrayList<String>sortedFavVideos=new ArrayList<>();
                           for (String iteratorString : FavFreq.keySet())
                               sortedFavVideos.add(iteratorString);
                            for (int i = 0; i < sortedFavVideos.size()-1; i++) {
                                for (int j = i+1; j <sortedFavVideos.size() ; j++) {
                                    if(FavFreq.get(sortedFavVideos.get(i)).intValue() > FavFreq.get(sortedFavVideos.get(j))){
                                        String auxs=String.valueOf(sortedFavVideos.get(i));
                                        sortedFavVideos.set(i,String.valueOf(sortedFavVideos.get(j)));
                                        sortedFavVideos.set(j,String.valueOf(auxs));
                                    }
                                }
                            }
                            double rat=FavFreq.get(sortedFavVideos.get(0)).intValue();

                            ArrayList<String>SortedByRating =new ArrayList<>();
                            for (String iteratorStr : sortedFavVideos){
                                if(FavFreq.get(iteratorStr).intValue()==rat){
                                    SortedByRating.add(iteratorStr);
                                }
                            }
                            double bestrating=-1;
                            for (Movie itMovies : movies){
                                if(SortedByRating.contains(itMovies.title) && bestrating < itMovies.mediaRatingului()){
                                    bestrating=itMovies.mediaRatingului();
                                }
                            }
                            for (Serial itSerial : serials){
                                if(SortedByRating.contains(itSerial.title) && bestrating < itSerial.mediaRating()){
                                    bestrating=itSerial.mediaRating();
                                }
                            }
                            int kk=0;
                            for (Movie itMovies : movies){
                                if(SortedByRating.contains(itMovies.title) && bestrating == itMovies.mediaRatingului() &&
                                kk==0 && !interogatedUser.history.keySet().contains(itMovies.title)){
                                    kk=1;
                                    resultFavorite=itMovies.title;
                                }
                            }
                            for (Serial itSerial : serials){
                                if(SortedByRating.contains(itSerial.title) && bestrating == itSerial.mediaRating()&&
                                kk==0 && !interogatedUser.history.keySet().contains(itSerial.title)){
                                    kk=1;
                                    resultFavorite=itSerial.title;
                                }
                            }

                            ///TODO CAND MA INTORC REPAR TREBUIE SA CAUT RATINGUL NU FRECVENTA


                            if(resultFavorite!=null)
                            arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                    "FavoriteRecommendation result: "+resultFavorite));
                            else
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                        "FavoriteRecommendation cannot be applied!"));

                            break;

                        case "search":
                            String resultSearch;
                            if(!interogatedUser.subscriptionType.equals("PREMIUM")) {
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                        "SearchRecommendation cannot be applied!"));
                                break;
                            }
                            ArrayList<String>searchUnseenVideos=new ArrayList<>();
                            ArrayList<Double>searchUnseenVideosRating=new ArrayList<>();
                            for (Movie iteratorMovies : movies){
                                if(!interogatedUser.history.keySet().contains(iteratorMovies.title) &&
                                iteratorMovies.genres.contains(iterator.genre)){
                                    searchUnseenVideos.add(iteratorMovies.title);
                                    searchUnseenVideosRating.add(iteratorMovies.mediaRatingului());
                                }
                            }
                            for (Serial iteratorSerials : serials){
                                if(!interogatedUser.history.keySet().contains(iteratorSerials.title) &&
                                iteratorSerials.genres.contains(iterator.genre)){
                                    searchUnseenVideos.add(iteratorSerials.title);
                                    searchUnseenVideosRating.add(iteratorSerials.mediaRating());
                                }
                            }
                            for (int i = 0; i < searchUnseenVideos.size()-1; i++) {
                                for (int j = i+1; j < searchUnseenVideos.size() ; j++) {
                                    if(searchUnseenVideosRating.get(i).doubleValue() > searchUnseenVideosRating.get(j).doubleValue()){
                                        Double aux=new Double(searchUnseenVideosRating.get(i).doubleValue());
                                        searchUnseenVideosRating.set(i,Double.valueOf(searchUnseenVideosRating.get(j).doubleValue()));
                                        searchUnseenVideosRating.set(j,Double.valueOf(aux.doubleValue()));
                                        String auxs=String.valueOf(searchUnseenVideos.get(i));
                                        searchUnseenVideos.set(i,String.valueOf(searchUnseenVideos.get(j)));
                                        searchUnseenVideos.set(j,String.valueOf(auxs));
                                    }
                                    if (searchUnseenVideosRating.get(i).doubleValue() == searchUnseenVideosRating.get(j).doubleValue()){
                                        if(searchUnseenVideos.get(i).compareTo(searchUnseenVideos.get(j)) > 0){
                                            String auxs=String.valueOf(searchUnseenVideos.get(i));
                                            searchUnseenVideos.set(i,String.valueOf(searchUnseenVideos.get(j)));
                                            searchUnseenVideos.set(j,String.valueOf(auxs));
                                        }
                                    }
                                }
                            }
                            if(searchUnseenVideos.size()==0){
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                        "SearchRecommendation cannot be applied!"));
                            }
                            else{
                                arrayResult.add(fileWriter.writeFile(iterator.actionId, null,
                                        "SearchRecommendation result: "+searchUnseenVideos));
                            }
                            break;

                    }
                    break;
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}

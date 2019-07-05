package de.hda.fbi.db2.stud;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import de.hda.fbi.db2.stud.entity.Catalog;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.CategoryStatistic;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Gamer;
import de.hda.fbi.db2.stud.entity.Question;
import de.hda.fbi.db2.tools.CsvDataReader;

/**
 * Main Class.
 * @version 0.1.1
 * @since 0.1.0
 * @author Aria Gholami
 * @author Youssef Ebrahimzadeh
 */
public class Main {
    /**
     * Main Method and Entry-Point.
     * @param args Command-Line Arguments.
     */
    public static void main(String[] args) {
        int selection = 0;
        System.out.println("1 = read Additional Data");
        System.out.println("2 = new Game");
        System.out.println("3 = create Mass Data");
        System.out.println("4 = analyze of Gamedata");
        Scanner scan = new Scanner(System.in, "UTF-8");
        System.out.println("Please write your Selection: ");
        selection = scan.nextByte();

        switch (selection) {
            case 1:
                try {
                    System.out.println(getGreeting());
                    //Read default csv
                    final List<String[]> defaultCsvLines = CsvDataReader.read();

                    for (int i = 0; i < defaultCsvLines.size(); i++) {
                        String[] tempArray = defaultCsvLines.get(i);
                    }
                    convertCSVIntoOutput(defaultCsvLines);
                    //Read (if available) additional csv-files and default csv-file
                    List<String> availableFiles = CsvDataReader.getAvailableFiles();
                    for (String availableFile : availableFiles) {
                        final List<String[]> additionalCsvLines = CsvDataReader.read(availableFile);
                    }
                } catch (URISyntaxException use) {
                    System.out.println(use);
                } catch (IOException ioe) {
                    System.out.println(ioe);
                }
                break;
            case 2:
                System.out.println("Game is starting...");
                play();
                break;
            case 3:
                createMassData();
                break;
            case 4:
                analyzerMenu();
                break;
            default:
                System.out.println("Choose another number!");
                break;
        }
    }
    private static Map<String, Category> convertCSVIntoOutput(List<String[]> csvLines) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresPU");

        //DELET EVRYTHING
        /*EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        Query queryTemp = em2.createQuery("DELETE From Question public");
        queryTemp.executeUpdate();
        queryTemp = em2.createQuery("DELETE From Category public");
        queryTemp.executeUpdate();
        queryTemp = em2.createQuery("DELETE From CategoryStatistic public");
        queryTemp.executeUpdate();
        queryTemp = em2.createQuery("DELETE From Catalog public");
        queryTemp.executeUpdate();
        queryTemp = em2.createQuery("DELETE From Game public");
        queryTemp.executeUpdate();
        queryTemp = em2.createQuery("DELETE From Gamer public");
        queryTemp.executeUpdate();
        em2.getTransaction().commit();*/

        Map<String, Category> categories = new HashMap<>();
        Map<String, Boolean> answers = new HashMap<>();
        int questionCounter = 0;
        int categorycounter = 0;
        csvLines.remove(0);
        Catalog catalogClass = new Catalog();
        //einlesen
        for (String[] line : csvLines) {
            Category categoryClass = new Category();

            //create EntityManager
            EntityManager em = emf.createEntityManager();
            questionCounter++;
            // set values of the csv data
            String questionId = line[0];
            String questionString = line[1];
            String answer1String = line[2];
            String answer2String = line[3];
            String answer3String = line[4];
            String answer4String = line[5];
            String correctAnswerString = line[6];
            String categoryNameString = line[7];

            //create objects
            Question questionClass = new Question();

            //Catalogname
            catalogClass.setName("Your personal Catalog");

            //Question
            questionClass.setQuestionId(Integer.parseInt(questionId));
            questionClass.setQuestionString(questionString);
            questionClass.setAnswer(
                    Integer.parseInt(correctAnswerString),
                    answer1String,
                    answer2String,
                    answer3String,
                    answer4String);
            if (questionClass.getAnswer() != null) {
                answers = questionClass.getAnswer();
            }
            //Categories
            if (questionCounter == 1) {
                categoryClass.setCategoryName(categoryNameString);
                categoryClass.setQuestion(questionClass);
            }

            //If Category is not contain in the Map, create a new String inside the map
            if (categories.containsKey(categoryNameString)) {
                categories.get(categoryNameString).setQuestion(questionClass);
                categoryClass = categories.get(categoryNameString);
                questionClass.setCategory(categoryClass);

                //link database
                em.getTransaction().begin();
                em.persist(questionClass);
                em.getTransaction().commit();
                em.close();

            } else {
                categorycounter++;
                categoryClass.setCategoryName(categoryNameString);
                categoryClass.setQuestion(questionClass);
                //Update CategoryStatistic
                //statisticClass.setCategoryList(categoryClass);
                //statisticClass.setCategoryStatFirst(categoryNameString);
                //Update Catalog
                catalogClass.setCategorieList(categoryClass);
                //Update Category
                categoryClass.setCatalogClass(catalogClass);
                //categoryClass.setCategoryStatisticPlay(statisticClass);
                //Update Question
                questionClass.setCategory(categoryClass);
                //link database
                if (questionCounter == 1) {
                    //Update Category
                    categoryClass.setCatalogClass(catalogClass);

                    em.getTransaction().begin();
                    em.persist(questionClass);
                    em.persist(categoryClass);
                    em.persist(catalogClass);
                    //em.persist(statisticClass);
                    em.getTransaction().commit();
                    em.close();
                } else {
                    em.getTransaction().begin();
                    em.persist(questionClass);
                    em.persist(categoryClass);
                    //em.persist(statisticClass);
                    em.getTransaction().commit();
                    em.close();
                }
                categories.put(categoryClass.getCategorieName(), categoryClass);
            }
            String rightAnswer = "";
            if (answers.get(answer1String) == true) {
                rightAnswer = answer1String;
            }
            if (answers.get(answer2String) == true) {
                rightAnswer = answer2String;
            }
            if (answers.get(answer3String) == true) {
                rightAnswer = answer3String;
            }
            if (answers.get(answer4String) == true) {
                rightAnswer = answer4String;
            }
            //Output
            System.out.format("%-130s%10s%20s%30s%40s%50s%60s%n",
                    questionClass.getQuestionString(), categoryClass.getCategorieName(),
                    answer1String, answer2String,
                    answer3String, answer4String, rightAnswer);
        }

        System.out.println("Result of Questions: " + questionCounter);
        System.out.println("Result of Categories: " + categorycounter);

        return categories; }

    private static void play() {
        int selection;
        int rightAnswer = 0;
        String gamerName;
        Scanner scan = new Scanner(System.in, "UTF-8");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresPU");
        EntityManager em = emf.createEntityManager();

        //Create Objects from Database
        Gamer gamerClass;
        Game gameClass = new Game();
        CategoryStatistic statisticClass = new CategoryStatistic();
        Category categoryClass;
        List<String> categoryList = new ArrayList<>();
        List<Category> categoryList1 = new ArrayList<>();
        List<Gamer> gamerList = new ArrayList<>();
        List<CategoryStatistic> statisticList = new ArrayList<>();
        List<Category> chosenCategories;

        //get ALl Data From Database
        Query query = em.createQuery("select public from Gamer public");
        List resultL = query.getResultList();
        for (Iterator i = resultL.iterator(); i.hasNext();) {
            gamerClass = (Gamer) i.next();
            gamerList.add(gamerClass);
        }

        query = em.createQuery("select public from CategoryStatistic public");
        resultL = query.getResultList();
        for (Iterator i = resultL.iterator(); i.hasNext();) {
            CategoryStatistic statisticClass2 = (CategoryStatistic) i.next();
            statisticList.add(statisticClass2);
        }

        query = em.createQuery("select public from Category public");
        resultL = query.getResultList();
        for (Iterator i = resultL.iterator(); i.hasNext();) {
            categoryClass = (Category) i.next();
            categoryList.add(categoryClass.getCategorieName());
            categoryList1.add(categoryClass);
        }

        System.out.println("\nAll Gamer: ");
        for (int i = 0; i < gamerList.size(); i++) {
            List<Game> gameList1 = gamerList.get(i).getGameList();
            for (int j = 0; j < gameList1.size(); j++) {
                if (gamerList.size() > 0 && gameList1.size() != 0) {
                    Date gBegin = gameList1.get(j).getBegin();
                    Date gEnd = gameList1.get(j).getEnd();
                    System.out.println(
                        "Gamername: " + gamerList.get(i).getGamerName() + "\tBegin: " + gBegin
                            + "\tEnd: " + gEnd);
                }
            }
        }

        System.out.println("\nPlayername: ");
        gamerName = scan.next();

        //set Game Player Name
        EntityTransaction tx = em.getTransaction();
        gamerClass = null;
        selection = 0;
        try {
            tx.begin();
            gamerClass = em
                .createNamedQuery("Gamer.findByUsername", Gamer.class)
                .setParameter("gamerName", gamerName)
                .getSingleResult();

            tx.commit();
            gamerClass.setNewGamer(gamerName);
            System.out.println("Welcome back, " + gamerClass.getGamerName());
        } catch (NoResultException ignored) {
            gamerClass = new Gamer();
            gamerClass.setNewGamer(gamerName);
            gamerClass = em.merge(gamerClass);
            em.flush();
            tx.commit();

            System.out.println("Creating a new Player...");
            System.out.println("Hello new user " + gamerClass.getGamerName());
        } catch (Exception other) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, other.getMessage(), other);
            tx.rollback();
            throw other;

        }

        System.out.println("\nAll Games: ");
        for (int i = 0; i < gamerList.size(); i++) {
                List<Game> gameList1 = gamerList.get(i).getGameList();
                for (int j = 0; j < gameList1.size(); j++) {
                    if (gamerList.get(i).getGamerName() == gamerName) {
                        System.out.println(
                            "Gameid: " + gameList1.get(i).getGameID() + "\tGamebegin: " + gameList1
                                .get(i)
                                .getBegin() + "\tGameend: " + gameList1.get(i).getEnd());
                    }
                }
        }

        int counterCategories = 0;
        System.out.println("\nAll Categories: ");
        for (int i = 0; i < categoryList.size(); i++) {
            statisticClass.setCategoryStatFirst(categoryList.get(i));
            System.out.println("Category: " + categoryList.get(i) + "\t" + counterCategories);
            counterCategories++;
        }

        System.out.println(
            "\nPlease choose your categories and select it by writing the positionnumber!"
                + "\nIf you dont like to chose one category, "
                + "you have to write instead of the positionnumber 'finished'!");

        String input = scan.next();

        gameClass.setAllCategories(categoryList);
        while (!input.equals("finished") ||
            gameClass.getChosenCategories().size() <= 2) {

            if (input.equals("finished")) {
                System.out.println("Sie müssen mind. 2 Kategorien wählen.");
            } else if (gameClass.getAllCategories().size() <= Integer
                .parseInt(input)
                || Integer.parseInt(input) < 0) {
                System.out.println("Input Wrong. Please try it again!");
            } else if (gameClass.getChosenCategories().containsKey(
                gameClass.getAllCategories().get(Integer.parseInt(input)))) {
                System.out.println(
                    "Category had been already choosen. Please select another category");
            } else {
                gameClass.addNewCategory (categoryList1.get(Integer.parseInt(input)));
            }
            input = scan.next();
        }

        System.out.println(
            "\nPlease write your maximum questions per category:");
        int maxQuestions = scan.nextInt();
        Random randomGenerator = new Random();

        for (Map.Entry<String, Category> entry : gameClass.getChosenCategories().entrySet()) {
            if (entry.getValue().getQuestion().size()  <= maxQuestions) {
                for (int j = 0; j < entry.getValue().getQuestion().size(); j++) {
                    gameClass.addQuestionToGame(entry.getValue().getQuestion().get(j));

                }
            } else {
                List<Question> selectcopy = entry.getValue().getQuestion();
                int zw;
                for (int l = 0; l < maxQuestions; l++) {
                    zw = randomGenerator.nextInt(
                        1000) % selectcopy.size();
                    gameClass.addQuestionToGame(selectcopy.get(zw));
                    selectcopy.remove(selectcopy.get(zw));
                }
            }
        }

        //shuffle
        List <Question> questionList = new ArrayList<>();
        for (int m = 0; m < gameClass.getChosenQuestions().size(); ++m) {
            int rSm1 = randomGenerator.nextInt(500) % gameClass.getChosenQuestions().size();
            int rSm2 = randomGenerator.nextInt(500) % gameClass.getChosenQuestions().size();

            //swap
            for (Map.Entry<String, Question> entry : gameClass.getChosenQuestions().entrySet()) {
                questionList.add(entry.getValue());
            }
                Question hold = questionList.get(rSm1);
                gameClass.setChosenQuestions(rSm1, questionList.get(rSm2));
                gameClass.setChosenQuestions(rSm2, hold);
        }

        System.out.println("\n\nstart:");
        gameClass.setBegin();

        for (int v = 0; v < gameClass.getChosenQuestions().size(); ++v) {
            int counterAnswer = 0;
            System.out.println(questionList.get(v).getQuestionString());
            Map<String, Boolean> answers =
                questionList.get(v).getAnswer();
            for (Map.Entry<String, Boolean> entry : answers.entrySet()) {
                System.out.println(entry.getKey() + "\t" + counterAnswer +  "\t");
                if (entry.getValue() == true) {
                    rightAnswer = counterAnswer;
                }
                counterAnswer++;
            }

            System.out.println ("Please write your Selection as Numbers:");
            selection = scan.nextInt();
            if (selection == rightAnswer) {
                System.out.println("Right Answer");
                gameClass.setTotalRightAnswer(
                    "Right Answer",
                    questionList.get(v).getQuestionString());
            } else {
                System.out.println("False Answer");
                gameClass.setTotalRightAnswer(
                    "False Answer",
                    questionList.get(v).getQuestionString());
            }
        }

        gameClass.setEnd();
        System.out.println("Game End");
        Map <String, Category> temp = gameClass.getChosenCategories();
        List<Category> categoryTemp = new ArrayList<>();
        for (Map.Entry<String, Category> entry : temp.entrySet()) {
            categoryTemp.add(entry.getValue());
        }
        chosenCategories = categoryTemp;

        em.getTransaction().begin();
        //Update Gamer
        gamerClass.setGame(gameClass);
        gamerClass.addGameToList(gameClass);
        gamerClass.setNewGamer(gamerName);
        //Update Game
        gameClass.putGamerMap(gamerClass);
        gameClass.setGamer(gamerClass);
        for (int i = 0; i < statisticList.size(); i++) {
            Map<String, Integer> statisticsTemp;
            statisticsTemp = statisticList.get(i).getStatistic();
            for (Map.Entry<String, Integer> entry : statisticsTemp.entrySet()) {
                statisticClass.setStatisticNormal(entry.getKey());
            }
        }

        for (int i = 0; i < chosenCategories.size(); i++) {
            //Update Category
            chosenCategories.get(i).setGame(gameClass);
            //Update Statistic
            statisticClass.setStatistic(
                chosenCategories.get(i).getCategorieName());
        }
        Map<String, Integer> statistics = statisticClass.getStatistic();

        Map<String, Integer> counterList = statisticClass.getStatistic();
        for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
            counterList.put(entry.getKey(), 0);
        }
        int counter = 0;
        for (int i = 0; i < statisticList.size(); i++) {
            statistics = statisticList.get(i).getStatistic();
            List<String> counterOff = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
                if (counterOff.contains(entry.getKey()) == false) {
                    counterOff.add(entry.getKey());

                    if (entry.getValue() >= 1){
                        counter = counterList.get(entry.getKey());
                        counter += entry.getValue();
                        counterList.replace(entry.getKey(), counter);
                    }
                }
            }
        }
        for (int i = 0; i < chosenCategories.size(); i++) {
            counter = counterList.get(chosenCategories.get(i).getCategorieName());
            counter++;
            counterList.replace(chosenCategories.get(i).getCategorieName(), counter);
        }
        statisticClass.setCategoryMap(counterList);

        em.persist(gameClass);
        em.persist(gamerClass);
        statisticClass = em.merge(statisticClass);
        //em.persist(statisticClass);
        em.getTransaction().commit();
        int catStatId = statisticClass.getCategoryStatID();
        tx.begin();
        em.createNamedQuery("Category.deleteClass", Category.class)
            .setParameter("id", statisticClass.getCategoryStatID())
            .executeUpdate();
        tx.commit();
        tx.begin();
        em.createNamedQuery("Category.updateID", Category.class)
            .setParameter("statisticPlay", statisticClass)
            .executeUpdate();
        tx.commit();
        tx.begin();
        em.createNamedQuery("CategoryStatistic.deleteAll", CategoryStatistic.class)
            .setParameter("id", statisticClass.getCategoryStatID() - 1)
            .executeUpdate();
        tx.commit();
        System.out.println("\nAll Categories: ");
        for (Map.Entry<String, Integer> entry : counterList.entrySet()) {
            System.out
                .println("Category: " + entry.getKey() + "\tCounter: " + entry.getValue());
        }
        //DELET EVRYTHING
        /*Query queryTemp = em.createQuery("DELETE From CategoryStatistic public");
        queryTemp.executeUpdate();*/
    }

    private static void createMassData() {
        Scanner sc = new Scanner(System.in, "UTF-8");
        CategoryStatistic categoryStatisticClass = null;
        System.out.print("Are you really sure, that you want to create mass data? \n" +
            "(yes / no)\n>> ");
        String confirmation = sc.nextLine();
        if (!confirmation.equals("yes")) {
            return;
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresPU");
        EntityManager em = emf.createEntityManager();


        //List<User> userList = new ArrayList<User>();

        int numberOfUsers = 10000;
        int numberOfGamesPerUser = 100;
        int numberOfQuestionsPerGame = 20;
        Map<String, Integer> categoriesSelected = new HashMap<>();
        Category categoryClass = new Category();
        List<Category> categoryList = new ArrayList<>();

        long numberOfCategorys = (long) em
            .createNamedQuery("Category.countAllCategories", long.class)
            .getSingleResult();


        Query query = em.createQuery("select public from Category public");
        List resultL = query.getResultList();
        for (Iterator i = resultL.iterator(); i.hasNext();) {
            categoryClass = (Category) i.next();
            categoryList.add(categoryClass);
        }
        int firstRound = 1;

        CategoryStatistic statisticClassTemp = new CategoryStatistic();

        for (int gamerCount = 0; gamerCount < numberOfUsers; ++gamerCount) {
            Random r = new Random();
            List<Game> gameList = new ArrayList<Game>();

            CategoryStatistic statisticClass = new CategoryStatistic();
            if (firstRound == 1) {
                for (int z = 0; z < categoryList.size(); ++z) {
                    statisticClass.setCategoryStatFirst(categoryList.get(z).getCategorieName());
                }
                firstRound = 0;
            } else {
                statisticClass.setCategoryMap(statisticClassTemp.getStatistic());
            }
            System.out.println("\033[H\033[2J");
            System.out.flush();
            System.out.println("Progress: " + gamerCount + " / " + numberOfUsers);

            Gamer currentGamer = new Gamer();
            currentGamer.setNewGamer("Gamer" + gamerCount);

            //Loop for Games
            Integer playerGameDifference = r.nextInt(50);
            for (int gameCount = 0; gameCount < playerGameDifference + 50; ++gameCount) {
                Game currentGame = new Game();
                categoriesSelected = statisticClass.getStatistic();
                currentGame.setGamer(currentGamer);
                currentGame.setGamerMap(currentGamer);

                currentGame.setBegin();
                currentGame.setEnd();

                Map<String, Category> categoriesOfGames = new HashMap<>();

                List<Integer> categoriesInGameAsInt = new ArrayList<Integer>();
                int numeberOfCategorys = r.nextInt() & Integer.MAX_VALUE;
                numberOfCategorys = (numeberOfCategorys % 5) + 2;

                for (int i = 0; i < numberOfCategorys; ++i) {
                    int categoryNumber = r.nextInt() & Integer.MAX_VALUE;
                    categoryNumber = (categoryNumber % 51) + 1;
                    categoriesInGameAsInt.add(categoryNumber);
                    categoryClass = (Category) em
                        .createNamedQuery("Category.getCategoryByID", Category.class)
                        .setParameter("categoryID", categoryNumber)
                        .getSingleResult();
                    categoryClass.setGame(currentGame);

                    categoriesOfGames.put(categoryClass.getCategorieName(), categoryClass);

                    currentGame.addAllCategories(categoryClass.getCategorieName());

                    int counter = categoriesSelected.get(categoryClass.getCategorieName());
                    counter++;
                    categoriesSelected.replace(categoryClass.getCategorieName(), counter);

                    categoryClass = em.createNamedQuery("Category.getCategoryByID", Category.class)
                        .setParameter("categoryID", categoryNumber)
                        .getSingleResult();

                    categoryClass.setGame(currentGame);
                    categoryClass.setCategoryName(
                        em.createNamedQuery("Category.getCategoryByID", Category.class)
                            .setParameter("categoryID", categoryNumber)
                            .getSingleResult().getCategorieName()
                    );
                    categoryClass.setCategoryStatisticPlay(statisticClass);

                    statisticClass.setCategoryList(categoryClass);
                }
                currentGame.setChosenCategories(categoriesOfGames);

                List<Question> questionsInCategories = (List<Question>) em
                    .createNamedQuery("Question.QuestionID", Question.class)
                    .setParameter("categories", categoriesInGameAsInt)
                    .getResultList();

                //Start Random Play of Games
                int questionCounter = 0;
                for (; questionCounter < numberOfQuestionsPerGame; ++questionCounter) {
                    ++questionCounter;

                    Question currentQuestion = questionsInCategories
                        .get(r.nextInt(questionsInCategories.size()));
                    Map<String, Boolean> questionAnswers = currentQuestion.getAnswer();


                    currentGame.addQuestionToGame(currentQuestion);
                    Integer randomAnswerSelection = r.nextInt() & Integer.MAX_VALUE;
                    randomAnswerSelection = randomAnswerSelection % 4;
                    int answerCounter = 0;
                    for (Map.Entry<String, Boolean> entry : questionAnswers.entrySet()) {
                        if (answerCounter == randomAnswerSelection) {
                            if (entry.getValue().equals(true)) {
                                currentGame.addRightQuestion(currentQuestion);
                            }
                        }
                        answerCounter++;
                    }
                }

                System.out.print(" User: " + (gameCount + 1) + " / " + numberOfUsers
                    + " Game: " + (gameCount + 1) + " / " + numberOfGamesPerUser + "\n");


                currentGamer.setGame(currentGame);
                currentGamer.addGameToList(currentGame);

                gameList.add(currentGame);

                categoryStatisticClass = statisticClass;
                statisticClassTemp = statisticClass;
            }

            em.getTransaction().begin();

            em.persist(currentGamer);
            for (Game g : gameList){
                em.persist(g);
            }

            if (gamerCount % 100 == 0){
                System.out.println("Flushed at User " + gamerCount);
                em.flush();
                em.clear();
            }
            em.getTransaction().commit();
        }
        em.getTransaction().begin();
        categoryStatisticClass.setCategoryMap(categoriesSelected);
        em.persist(categoryStatisticClass);
        em.getTransaction().commit();

    }

    private static void analyzerMenu() {
        Scanner sc = new Scanner(System.in, "UTF-8");

        System.out.print("What data do you want to get?\n" +
            "1. Ausgabe aller Spieler in einem Zeitraum\n" +
            "2. Ausgabe aller Infos eines Spielers\n" +
            "3. Ausgabe aller Spieler Sortiert nach Anzahl absteigend\n" +
            "4. Ausgabe der beliebtesten Kategorie absteigend sortiert\n");


        Integer userSelection = sc.nextInt();

        switch (userSelection){
            case 1:
                analyzePlayersInTimeSpan();
                break;
            case 2:
                analyzePlayer();
                break;
            case 3:
                analyzeAllPlayersSortNumGames();
                break;
            case 4:
                analyzeFavouriteCategory();
                break;
            default:
                System.out.println("NOT A VALID CHOICE!");
                return;
        }
    }







    private static void analyzeFavouriteCategory() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresPU");
        EntityManager em = emf.createEntityManager();

        Vector<CategoryStatistic> gamesByUser = (Vector<CategoryStatistic>) em
            .createNamedQuery("CategoryStatistic.getStatistic", CategoryStatistic.class)
            .getResultList();

        for (CategoryStatistic cs : gamesByUser) {
            Map<String, Integer> statisticTemp = sortByValues(cs.getStatistic());
            for (Map.Entry<String, Integer> entry : statisticTemp.entrySet()) {
                System.out.println("Category: " + entry.getKey());
            }
            break;
        }
    }

    private static void analyzeAllPlayersSortNumGames() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresPU");
        EntityManager em = emf.createEntityManager();

        Vector<Object[]> allUsersSortedByGames = (Vector<Object[]>) em
            .createNamedQuery("Gamer.sortedByGames", Object[].class)
            .getResultList();

        for (Object[] u : allUsersSortedByGames) {
            System.out.println("Player " + ((Gamer) u[0]).getGamerName() + " played " + (int) u[1] +
                " games");
        }
    }

    private static void analyzePlayer() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresPU");
        EntityManager em = emf.createEntityManager();

        Scanner sc = new Scanner(System.in, "UTF-8");

        System.out.print("Write the name of the Gamer\n>>");
        String username = sc.nextLine();



        Vector<Game> gamesByUser = (Vector<Game>) em
            .createNamedQuery("Game.getGameByUser", Game.class)
            .setParameter("userID", username)
            .getResultList();

        for (Game g : gamesByUser) {
            long total = 0;
            System.out.println("Game id: " + g.getGameID() +
                " Gamebegin: " + g.getBegin() +
                " Gameend: " + g.getEnd() +
                " RightQuestion " + g.getSizeOfAllRightAnswers() +
                " Total Question: " + g.getSizeOfQuestions());
        }

    }


    private static Date inputDate() {
        int month, day, year;
        Scanner sc = new Scanner(System.in, "UTF-8");

        System.out.print("Please insert the Date like:\n" +
            "(Month)XX (Day)XX (Year)XX\n" +
            ">> ");

        month = sc.nextInt();
        day = sc.nextInt();
        year = sc.nextInt();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); //-1 because January in the ENUM is 0, Feb is 1 ...
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    private static void analyzePlayersInTimeSpan() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresPU");
        EntityManager em = emf.createEntityManager();

        Date beginDate = inputDate();
        Date endDate = inputDate();

        Vector<Gamer> activeUsers = (Vector<Gamer>) em
            .createNamedQuery("Game.playersInDateRange", Gamer.class)
            .setParameter("beginDate", beginDate)
            .setParameter("endDate", endDate)
            .getResultList();

        for (Gamer u : activeUsers) {
            System.out.println("User: " + u.getGamerName() + "ID: " + u.getGamerID());
        }
    }

    private static Map sortByValues(Map map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                    .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        Map sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }


    public static String getGreeting() {
        return "Welcome to our Quiz. " +
                "We appreciate, you are testing our new game. " +
                "We hope you enjoy it(:";
    }
}

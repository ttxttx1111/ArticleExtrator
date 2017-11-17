
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.*;
import java.util.*;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Created by ttx on 2017/11/9.
 */
public class Main {
    public static void makeStatistics(Properties prop){
        String mongoURI = prop.getProperty("mongoURI");
        String mongoDatabase =  prop.getProperty("mongoDatabase");
        String mongoArticleCollection =  prop.getProperty("mongoArticleCollection");
        System.out.println(mongoArticleCollection);

        MongoClientURI connectionString = new MongoClientURI(mongoURI);
        MongoClient mongoClient= new MongoClient(connectionString);

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase(mongoDatabase).withCodecRegistry(pojoCodecRegistry);

        final MongoCollection<Document> articleCollection = database.getCollection(mongoArticleCollection);

        Gson gson = new Gson();
//        List<Document> documents = (List<Document>) articleCollection.find().into(new ArrayList<Document>());
        Map<String, Integer>  map = new HashMap<>();

        MongoCursor<Document> cursor = articleCollection.find().iterator();

//        int documentNum = documents.size();
//        System.out.println("documents num:" + documentNum);

        int catCount = 0;
//        for(Document document:documents){
        while(cursor.hasNext()){
//            String s = document.toJson();
            String s = cursor.next().toJson();
            Article article = gson.fromJson(s, Article.class);
            List<List<Category>> catgory = article.getCategory();
            try {
                String cat = "";
                for(List<Category> categories:catgory){
//                    System.out.println(categories.get(categories.size() - 1).getName());
                    if((categories.get(categories.size() - 1).getName()).equals("手机新浪网")){
                        cat = categories.get(categories.size() - 2).getName();
                        catCount++;
                        break;
                    };
                }


                if(map.containsKey(cat)){
                    map.put(cat, map.get(cat) + 1);
                }else{
                    map.put(cat, 1);
                }
            }catch (Exception e){
                System.out.println("Category error");
            }
            if(catCount % 1000 == 0){
                System.out.println("catCount:" + catCount);
            }
        }

        String reportString = "";
        reportString += "map size:" + map.size() + "\n";
        reportString += "catCount:" + catCount + "\n";
        for(Map.Entry<String, Integer> entry:map.entrySet()){
            reportString += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        try {
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(new File("Category statistic")));
            fw.write(reportString);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 1. connect to mongodb and get cursor
     * 2. Make a map<Category_name, List<Article>>, every category with 100 articles
     * 3. Remove repeated articles in category
     * 4. According to map size, extract 1000 articles
     * 5. insert into mongodb
     */
    public static void extractArticle(Properties prop){
        MongoCursor<Document> cursor = readFromMongo(prop);
        Map<String, List<Article>> categoryToArticleMap = makeMap(cursor,300);

        categoryToArticleMap = cleanCategory(categoryToArticleMap, 100);
        List<Article> articleList = extractArticleFromMap(categoryToArticleMap, 1000);
        saveToMongo(articleList, prop);
        reportArticleList(categoryToArticleMap);
    }

    private static void reportArticleList(Map<String, List<Article>> categoryToArticleMap) {
        String reportString = "";
        for(Map.Entry<String, List<Article>> entry:categoryToArticleMap.entrySet()){
            reportString += entry.getKey() + ": " + entry.getValue().size() + "\n";
        }
        try {
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(new File("Extracted_article")));
            fw.write(reportString);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void saveToMongo(List<Article> articleList, Properties prop){
        System.out.println("Saving extracted articles");
        String mongoURI = prop.getProperty("mongoURI");
        String mongoDatabase =  prop.getProperty("mongoDatabase2");
        String mongoArticleCollection =  prop.getProperty("mongoArticleCollection2");
        System.out.println("database:" + mongoDatabase);
        System.out.println("collection:" + mongoArticleCollection);

        MongoClientURI connectionString = new MongoClientURI(mongoURI);
        MongoClient mongoClient= new MongoClient(connectionString);

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase(mongoDatabase).withCodecRegistry(pojoCodecRegistry);

        final MongoCollection<Document> articleCollection = database.getCollection(mongoArticleCollection);

//        final MongoCollection<Article> articleCollection = database.getCollection(mongoArticleCollection, Article.class);
        Gson gson = new Gson();
        List<Document> articleDocuments = new ArrayList<>();
        for(Article article:articleList){
            String jsonString = gson.toJson(article);
            Document document = Document.parse(jsonString);
            articleDocuments.add(document);
        }
//        articleCollection.insertMany(articles);
        articleCollection.insertMany(articleDocuments);


    }


    private static List<Article> extractArticleFromMap(Map<String, List<Article>> categoryToArticleMap, int totalNum) {
        System.out.println("Start to extract articles");
        int mapSize = categoryToArticleMap.size();
        int numOfEachCategory = totalNum / mapSize;
        List<Article> articleList = new ArrayList();
        for(Map.Entry<String, List<Article>> entry:categoryToArticleMap.entrySet()){
            if(numOfEachCategory > entry.getValue().size()){
                System.out.println("extract num is unreasonable,");
                System.out.println("numOfEachCategory:" + numOfEachCategory);
                System.exit(1);
            }
            articleList.addAll(entry.getValue().subList(0, numOfEachCategory));
        }
        return articleList;
    }

    private static Map<String, List<Article>> cleanCategory(Map<String, List<Article>> categoryToArticleMap, int thresh) {
        categoryToArticleMap.remove("");
        int mapSize = 0;
        List<String> keyList = new ArrayList<>();
        for(Map.Entry<String, List<Article>> entry:categoryToArticleMap.entrySet()){
            List<Article> articleList = entry.getValue();
            Set<String> articleSet = new HashSet<>();

            for(int i = articleList.size() - 1;i>=0;i--){
                if(articleSet.contains(articleList.get(i).getContent())){
                    articleList.remove(i);
                }else{
                    articleSet.add(articleList.get(i).getContent());
                }
            }

            if(articleList.size() < thresh){
                keyList.add(entry.getKey());
            }
        }
        for(String i:keyList){
            categoryToArticleMap.remove(i);
        }
        return categoryToArticleMap;
    }

    private static Map<String,List<Article>> makeMap(MongoCursor<Document> cursor, int thresh) {
        System.out.println("Start to process articles");
        Map<String, List<Article>> categoryToArticleMap = new HashMap<>();
        Gson gson = new Gson();
        int count = 0;
        while(cursor.hasNext()){
            String s = cursor.next().toJson();
            Article article = gson.fromJson(s, Article.class);
            List<List<Category>> catgoriesList = article.getCategory();
            String category = "";

            for(List<Category> categories:catgoriesList){
//                    System.out.println(categories.get(categories.size() - 1).getName());
                if((categories.get(categories.size() - 1).getName()).equals("手机新浪网")){
                    category = categories.get(categories.size() - 2).getName();
                    break;
                };
            }

            if(categoryToArticleMap.containsKey(category)){
                List<Article> articleList = categoryToArticleMap.get(category);
                if(articleList.size() < thresh){
                    articleList.add(article);
                }
            }else{
                List<Article> articleList = new ArrayList<>();
                articleList.add(article);
                categoryToArticleMap.put(category, articleList);
            }
            count++;
            if(count % 1000 == 0){
                System.out.println("Article processed:" + count);
            }
        }

        return categoryToArticleMap;

    }

    private static MongoCursor<Document> readFromMongo(Properties prop) {
        String mongoURI = prop.getProperty("mongoURI");
        String mongoDatabase =  prop.getProperty("mongoDatabase");
        String mongoArticleCollection =  prop.getProperty("mongoArticleCollection");
        System.out.println(mongoArticleCollection);

        MongoClientURI connectionString = new MongoClientURI(mongoURI);
        MongoClient mongoClient= new MongoClient(connectionString);

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase(mongoDatabase).withCodecRegistry(pojoCodecRegistry);

        final MongoCollection<Document> articleCollection = database.getCollection(mongoArticleCollection);

        MongoCursor<Document> cursor = articleCollection.find().iterator();
        return cursor;
    }

    public static Properties readProp(String path){
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(path);
            prop.load(fis);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }


    public static void main(String[] args) {
        Properties prop = readProp("settings.properties");
//        makeStatistics(prop);
        extractArticle(prop);
    }


}

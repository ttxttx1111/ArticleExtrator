
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
    public static void readFromMongo(Properties prop){
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
        readFromMongo(prop);
    }
}

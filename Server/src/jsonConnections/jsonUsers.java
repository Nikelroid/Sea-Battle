package jsonConnections;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import objects.userObject;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class jsonUsers {
    

    public jsonUsers() {
    }

    public jsonUsers(List<userObject> users) {





                Gson userObj = new GsonBuilder().setPrettyPrinting().create();
                try {
                    Writer writer = Files.newBufferedWriter(Paths.get("Users.json"));
                    userObj.toJson(users, writer);
                    writer.close();
                } catch (Exception ex) {
                }



    }
    List<userObject> users;


    public List<userObject> get() {

                Gson userObj = new GsonBuilder().setPrettyPrinting().create();
                try {
                    Gson gson = new Gson();
                    Reader reader = Files.newBufferedReader(Paths.get("Users.json"));
                    users = new Gson().fromJson(
                            reader, new TypeToken<List<userObject>>() {
                            }.getType());
                    reader.close();
                } catch (IOException ignored) {
                }


        return users;
    }
}

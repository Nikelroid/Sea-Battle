package jsonConnections;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import objects.gameObject;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class jsonGame {

    public jsonGame() {
    }
    public jsonGame(List<gameObject> users) {

        Gson userObj = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("Games.json"));
            userObj.toJson(users, writer);
            writer.close();
        } catch (Exception ex) {

        }
    }
    List<gameObject> users;



    public List<gameObject> get() {

            try {
                Gson gson = new Gson();
                Reader reader = Files.newBufferedReader(Paths.get("Games.json"));
                users = new Gson().fromJson(
                        reader, new TypeToken<List<gameObject>>() {
                        }.getType());
                reader.close();
            } catch (IOException ignored) {
            }

        return users;
    }
}

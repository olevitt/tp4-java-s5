package fr.rathesh.springbook.springbooktest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    private OkHttpClient client = new OkHttpClient();
    private Map<String, Pokemon> cachePokemon = new HashMap<>();

    @RequestMapping("/")
    public String index() {
        return "Hello World !";
    }
    @RequestMapping("/espece/{pokemon}")
    public Pokemon getInfos(@PathVariable String pokemon) throws IOException {
        if ( cachePokemon.get(pokemon) != null)
        {
            return cachePokemon.get(pokemon);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Request request = new Request.Builder()
                .url("https://pokeapi.co/api/v2/pokemon/"+pokemon)
                .build();
        Response response = client.newCall(request).execute();

        Pokemon mappedPokemon = objectMapper.readValue(response.body().string(), Pokemon.class);
        cachePokemon.put(pokemon,mappedPokemon);

        return mappedPokemon;
    }
}

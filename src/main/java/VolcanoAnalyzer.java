import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VolcanoAnalyzer {
    private List<Volcano> volcanos;

    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try{
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch(Exception e){
            throw(e);
        }
    }

    public Integer numbVolcanoes(){
        return volcanos.size();
    }

    //add methods here to meet the requirements in README.md

    public Integer eruptedInEighties(){
        long a =  volcanos.stream().filter(n -> n.getYear() > 1979 && n.getYear() < 1990).count();
        return Math.toIntExact(a);
    }

    public double causedTsunami(String type){
        return volcanos.stream().filter(n -> n.getAgent().equals(type)).count();
    }

    public String mostCommonType(){
        return volcanos.stream()
                .collect(Collectors.groupingBy(Volcano::getType, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No volcanoes");
    }

    public double averageElevation(){
        return volcanos.stream().mapToDouble(n->n.getElevation()).average().getAsDouble();
    }

   public String[] elevatedVolcanoes(Integer num){
    return volcanos.stream().filter(n -> n.getElevation() > num).map(Volcano::getName).toArray(String[]::new);
   }

}

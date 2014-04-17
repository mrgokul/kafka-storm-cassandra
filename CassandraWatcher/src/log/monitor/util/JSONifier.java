package log.monitor.util;

import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class JSONifier {

    /**
       Converts map of form '{a:[1,2],b:[3,4]}' to JSON '{"a":[1,2],"b":[3,4]}'
    **/

    public static String JSONify(Map<String, List<Integer>> map){
        JsonObjectBuilder logObject = Json.createObjectBuilder();
        for(Map.Entry<String, List<Integer>> entry : map.entrySet()){
            String key = entry.getKey();
            List<Integer> countList = entry.getValue();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (int i = 0; i < countList.size(); i++){
                arrayBuilder.add(countList.get(i));
            }
            logObject.add(key,arrayBuilder.build());
    }
    
        return logObject.build().toString();
        
        
    }
}

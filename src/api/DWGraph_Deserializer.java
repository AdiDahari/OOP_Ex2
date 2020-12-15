package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;

public class DWGraph_Deserializer implements JsonDeserializer {
    @Override
    public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jObj = jsonElement.getAsJsonObject();
        HashMap<Integer, node_data> nMap = new HashMap<>();
        HashMap<Integer, HashMap<Integer, edge_data>> eMap = new HashMap<>();
        JsonArray nodes = jObj.get("Nodes").getAsJsonArray();
        for(JsonElement n : nodes){
            String pos = n.getAsJsonObject().get("pos").getAsString();
            String[] s = pos.split(",");
            double x = Double.parseDouble(s[0]);
            double y = Double.parseDouble(s[1]);
            double z = Double.parseDouble(s[2]);
            int id = n.getAsJsonObject().get("id").getAsInt();
            nMap.put(id, new NodeData(id, new GeoLocation(x,y,z)));
        }
        DWGraph_DS dwg = new DWGraph_DS(nMap);
//        JsonObject edges = jObj.get("Edges").getAsJsonObject();
        JsonArray eArr = jObj.get("Edges").getAsJsonArray();
        for(JsonElement elem : eArr){
            int currSrc = elem.getAsJsonObject().get("src").getAsInt();
            int currDest = elem.getAsJsonObject().get("dest").getAsInt();
            double currWeight = elem.getAsJsonObject().get("w").getAsDouble();
            dwg.connect(currSrc,currDest,currWeight);
//        for(Map.Entry<String, JsonElement> edgeMap : edges.entrySet()){
//            JsonObject currEdges = edgeMap.getValue().getAsJsonObject();
//            HashMap<Integer, edge_data> eMapIn = new HashMap<>();
//            for(Map.Entry<String, JsonElement> edge : currEdges.entrySet()) {
//                JsonElement currEdge = edge.getValue();
//                int currSrc = currEdge.getAsJsonObject().get("src").getAsInt();
//                int currDest = currEdge.getAsJsonObject().get("dest").getAsInt();
//                double currWeight = currEdge.getAsJsonObject().get("w").getAsDouble();
//                String currInfo = currEdge.getAsJsonObject().get("_info").getAsString();
//                int currTag = currEdge.getAsJsonObject().get("_tag").getAsInt();
//                eMapIn.put(currDest, new DWGraph_DS.EdgeData(currSrc,currDest,currWeight,currTag,currInfo));
//            }
//            eMap.put(Integer.parseInt(edgeMap.getKey()), eMapIn);
        }
//        int eCount = jObj.get("edgeCount").getAsInt();
//        int mCount = jObj.get("modeCount").getAsInt();
//        DWGraph_DS dwgraph = new DWGraph_DS(nMap,eMap,eCount,mCount);
        return dwg;
    }

    //    @Override
//    public DWGraph_DS deserialize(JsonElement jElem, Type type, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject jObj = jElem.getAsJsonObject();
//        HashMap<Integer, node_data> nMap = new HashMap<>();
//        HashMap<Integer, HashMap<Integer, edge_data>> eMap = new HashMap<>();
//        JsonObject nodes = jObj.get("Nodes").getAsJsonObject();
//        for(Map.Entry<String, JsonElement> node : nodes.entrySet()){
//            JsonElement currNode = node.getValue();
//            int currKey = currNode.getAsJsonObject().get("id").getAsInt();
//            String currInfo = currNode.getAsJsonObject().get("_info").getAsString();
//            int currTag = currNode.getAsJsonObject().get("_tag").getAsInt();
//            double currWeight = currNode.getAsJsonObject().get("_weight").getAsDouble();
//            JsonObject gloc = currNode.getAsJsonObject().get("pos").getAsJsonObject();
//            double x = gloc.get("_x").getAsDouble();
//            double y = gloc.get("_y").getAsDouble();
//            double z = gloc.get("_z").getAsDouble();
//            nMap.put(currKey, new DWGraph_DS.NodeData(currKey,currInfo,currTag,currWeight,new DWGraph_DS.GeoLocation(x,y,z)));
//        }
//        JsonObject edges = jObj.get("Edges").getAsJsonObject();
//        for(Map.Entry<String, JsonElement> edgeMap : edges.entrySet()){
//            JsonObject currEdges = edgeMap.getValue().getAsJsonObject();
//            HashMap<Integer, edge_data> eMapIn = new HashMap<>();
//            for(Map.Entry<String, JsonElement> edge : currEdges.entrySet()) {
//                JsonElement currEdge = edge.getValue();
//                int currSrc = currEdge.getAsJsonObject().get("src").getAsInt();
//                int currDest = currEdge.getAsJsonObject().get("dest").getAsInt();
//                double currWeight = currEdge.getAsJsonObject().get("w").getAsDouble();
//                String currInfo = currEdge.getAsJsonObject().get("_info").getAsString();
//                int currTag = currEdge.getAsJsonObject().get("_tag").getAsInt();
//                eMapIn.put(currDest, new DWGraph_DS.EdgeData(currSrc,currDest,currWeight,currTag,currInfo));
//            }
//            eMap.put(Integer.parseInt(edgeMap.getKey()), eMapIn);
//        }
//        int eCount = jObj.get("edgeCount").getAsInt();
//        int mCount = jObj.get("modeCount").getAsInt();
//        DWGraph_DS dwgraph = new DWGraph_DS(nMap,eMap,eCount,mCount);
//        return dwgraph;
//    }

}

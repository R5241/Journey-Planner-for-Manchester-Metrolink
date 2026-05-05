import java.util.*;

public class RouteFindingFunction {
     public static void shortestTimeRoute_Dijkstra(
        String start, String end, HashMap<String, ArrayList<Connection>> graph
    ){
        HashMap<String,info> INFO = new HashMap<>();
        HashSet<String> unvistedState = new HashSet<>();

        // Initialise the HashMap at the beginning
        Set<String> allStations = graph.keySet();
        for (String station : allStations){
            // get any possible state of this station.
            ArrayList<Connection> connections = graph.get(station);

            // loop all the states/connection of this station.
            for (int i = 0; i < connections.size(); i++) {
                // Get one connection and concatenated station and line together.
                Connection c = connections.get(i);
                String key = station + "**" + c.line;

                //eg. station1 both on red and yellow line
                // station1**red, station1**yellow

                // if this station is not exist, then initialise it
                if (!INFO.containsKey(key)) {
                    // assume that all station takes infinte time to reach
                    // and we don't know how and which line it is on.
                    INFO.put(key, new info(Double.MAX_VALUE, null, c.line));
                    unvistedState.add(key);
                }
            }
        }


        String startSta = start + "**START";
        // Initialise, it takes 0 min from start to start
        // And we don't know which line we are going to.
        INFO.put(startSta, new info(0.0, null, null));
        unvistedState.add(startSta);

        String bestEndSta = null;
        double bestTime = Double.MAX_VALUE;

        while(!unvistedState.isEmpty()){
            String currentkey = null;
            double shortestTime = Double.MAX_VALUE;

            for (String state : unvistedState){
                if (INFO.get(state).time < shortestTime){
                    shortestTime = INFO.get(state).time;
                    currentkey = state;
                }
            }

            if (currentkey == null){
                break;
                //Break the loop when there is no unvisted stations
            }

            // This state is now visted, need to be removed
            unvistedState.remove(currentkey);
            // split it and store them into string array.
            String[] cParts = currentkey.split("\\*\\*",-1);
            String cStation = cParts[0];
            String cLine = cParts[1];
            
            // if cLine is START, means the current line is NULL.
            if (cLine.equals("START")){
                cLine = null;
            }

            if (cStation.equals(end)){
                if(INFO.get(currentkey).time < bestTime){
                    bestTime = INFO.get(currentkey).time;
                    bestEndSta = currentkey;
                }
                continue;
                // Compare and check the best route.
            }

            // Loop through all adjacen stations
            ArrayList<Connection> adjacentStations = graph.get(cStation);

            for (int i = 0; i < adjacentStations.size(); i++){
                Connection cc = adjacentStations.get(i);

                double extraTime = cc.time;
                // check last line is equals to the current line.
                // if not, add 2 mins.
                if (cLine != null && !cLine.equals(cc.line)) {
                    extraTime += 2.0;
                }   

                double newTime = INFO.get(currentkey).time + extraTime;

                String nextState = cc.to + "**" + cc.line;

                // If we found current time taken is lesser than before
                // overwrite it.
                if(newTime < INFO.get(nextState).time){
                    INFO.put(nextState, new info(newTime, currentkey, cc.line));
                }
            }
        }
        if (bestEndSta == null){
            System.out.println("This station is impossible to reach.");
            return;
        }

        ArrayList<String> route = new ArrayList<>();
        String cPointer = bestEndSta;

        while (cPointer!=null){
            // add stations to beginning of the array
            // eg, 1 -> 3 -> 6, stations
            //[6],[3,6],[1,3,6]
            // this is how it insert, every time, insert into position 0.
            route.add(0, cPointer);
            cPointer = INFO.get(cPointer).previous;
        }

        
        System.out.println("*** Minimal Time Route ***");
        int changes = 0;
        String LastLine = null;
        String LastStation = null;
        
        for (int i = 0; i < route.size(); i++){
            String keyStation = route.get(i);
            String[] parts = keyStation.split("\\*\\*",-1);

            String Line = null;

            if (i == 0 && route.size() > 1) {
                Line = INFO.get(route.get(1)).line;
            } else {
                Line = INFO.get(keyStation).line;
            }

            // Out put message when u need to switch line.
            if (LastLine != null && Line != null && !LastLine.equals(Line)) {
                System.out.println("** Change Line to " + Line + " line ***");
                changes += 1;
                System.out.println(LastStation + " on " + Line + " line");
            }

            System.out.println(parts[0] + " on " + Line + " line");
            LastLine = Line; // let last line = current line.
            LastStation = parts[0];
        }
        System.out.println();
        System.out.println("Overall Journey Time (mins) = " + bestTime);
        System.out.println("Number of Changes = " + changes + "\n");
    }

    public static void fewestChangeRoute_Dijkstra(
        String start, String end, HashMap<String, ArrayList<Connection>> graph
    ){
        // This hash map is used to track number of changes.
        HashMap<String, Integer> changeINFO = new HashMap<>();

        HashMap<String,info> INFO = new HashMap<>();
        HashSet<String> unvistedState = new HashSet<>();

        // Initialise the HashMap at the beginning
        Set<String> allStations = graph.keySet();
        for (String station : allStations){
            // get any possible state of this station.
            ArrayList<Connection> connections = graph.get(station);

            // loop all the states/connection of this station.
            for (int i = 0; i < connections.size(); i++) {
                // Get one connection and concatenated station and line together.
                Connection c = connections.get(i);
                String key = station + "**" + c.line;

                //eg. station1 both on red and yellow line
                // station1**red, station1**yellow

                // if this station is not exist, then initialise it
                if (!INFO.containsKey(key)) {
                    // assume that all station takes infinte time to reach
                    // and all station takes infinte changes.
                    // and we don't know how and which line it is on.
                    INFO.put(key, new info(Double.MAX_VALUE, null, c.line));
                    changeINFO.put(key, Integer.MAX_VALUE);
                    unvistedState.add(key);
                }
            }
        }


        String startSta = start + "**START";
        // Initialise, it takes 0 min from start to start
        // And we don't know which line we are going to.
        INFO.put(startSta, new info(0.0, null, null));
        changeINFO.put(startSta, 0);
        unvistedState.add(startSta);

        String bestEndSta = null;
        double bestTime = Double.MAX_VALUE;
        int bestChange = Integer.MAX_VALUE;

        while(!unvistedState.isEmpty()){
            String currentkey = null;
            double shortestTime = Double.MAX_VALUE;
            int shortestChange = Integer.MAX_VALUE;

            for (String state : unvistedState){
                // store current time an changes
                // if number of changes of two states are same
                // take the route with shortest change.
                int stateChange = changeINFO.get(state);
                double stateTime = INFO.get(state).time;

                if (stateChange < shortestChange ||(stateChange == shortestChange && stateTime < shortestTime)){
                    shortestChange = stateChange;
                    shortestTime = stateTime;
                    currentkey = state;
                }
            }

            if (currentkey == null){
                break;
                //Break the loop when there is no unvisted stations
            }

            // This state is now visted, need to be removed
            unvistedState.remove(currentkey);
            // split it and store them into string array.
            String[] cParts = currentkey.split("\\*\\*",-1);
            String cStation = cParts[0];
            String cLine = cParts[1];
            
            // if cLine is START, means the current line is NULL.
            if (cLine.equals("START")){
                cLine = null;
            }

            if (cStation.equals(end)){
                // when end, check weather it is the route with shortest
                // changes or not. if num of change are equal, check time
                // taken then.
                if (
                    changeINFO.get(currentkey) < bestChange || (
                        changeINFO.get(currentkey) == bestChange && 
                        INFO.get(currentkey).time < bestTime
                    )
                ){
                    bestChange = changeINFO.get(currentkey);
                    bestTime = INFO.get(currentkey).time;
                    bestEndSta = currentkey;
                }
                continue;
                // Compare and check the best route.
            }

            // Loop through all adjacen stations
            ArrayList<Connection> adjacentStations = graph.get(cStation);

            for (int i = 0; i < adjacentStations.size(); i++){
                Connection cc = adjacentStations.get(i);

                double extraTime = cc.time;
                int change = 0;
                // check last line is equals to the current line.
                // if not, add 2 mins.
                if (cLine != null && !cLine.equals(cc.line)) {
                    extraTime += 2.0;
                    change += 1;
                }   

                double newTime = INFO.get(currentkey).time + extraTime;
                int newChange = changeINFO.get(currentkey) + change;

                String nextState = cc.to + "**" + cc.line;

                // If we found current time taken is lesser than before
                // overwrite it.
                if(
                    newChange < changeINFO.get(nextState) || (
                        newChange == changeINFO.get(nextState) 
                        && newTime < INFO.get(nextState).time
                    )
                ){
                    INFO.put(nextState, new info(newTime, currentkey, cc.line));
                    changeINFO.put(nextState, newChange);
                }
            }
        }
        if (bestEndSta == null){
            System.out.println("This station is impossible to reach.");
            return;
        }

        ArrayList<String> route = new ArrayList<>();
        String cPointer = bestEndSta;

        while (cPointer!=null){
            // add stations to beginning of the array
            // eg, 1 -> 3 -> 6, stations
            //[6],[3,6],[1,3,6]
            // this is how it insert, every time, insert into position 0.
            route.add(0, cPointer);
            cPointer = INFO.get(cPointer).previous;
        }

        
        System.out.println("*** Route with Fewest Changes ***");
        String LastLine = null;
        String LastStation = null;
        
        for (int i = 0; i < route.size(); i++){
            String keyStation = route.get(i);
            String[] parts = keyStation.split("\\*\\*",-1);

            String Line = null;

            if (i == 0 && route.size() > 1) {
                Line = INFO.get(route.get(1)).line;
            } else {
                Line = INFO.get(keyStation).line;
            }

            // Out put message when u need to switch line.
            if (LastLine != null && Line != null && !LastLine.equals(Line)) {
                System.out.println("** Change Line to " + Line + " line ***");
                System.out.println(LastStation + " on " + Line + " line");
            }

            System.out.println(parts[0] + " on " + Line + " line");
            LastLine = Line; // let last line = current line.
            LastStation = parts[0];
        }
        System.out.println();
        System.out.println("Time (mins): " + bestTime);
        System.out.println("Total Changes: " + bestChange + "\n");

    }

}

import java.io.*;
import java.util.*;

public class MetroPlan {
    //This class is used to store variables reads from CSV file
    static class Connection {
        String from;
        String to;
        //From which station to another station.
        double time;
        String line;
        //Time needed and which line is "from" located.

        public Connection(String from, String to, double time, String line) {
            this.from = from;
            this.to = to;
            this.time = time;
            this.line = line;
        }
    }

    // info class is used to store informations like:
    // time: time taken from start to here
    // previous: what is last station before this?
    // used for Dijkstra, easy to search backwards
    // line: which line is this station on.
    static class info {
        double time;
        String previous;
        String line;

        public info(double time, String pervious, String line){
            this.time = time;
            this.previous = previous;
            this.line = line;
        }
    }

    public static void shortestTimeRoute_Dijkstra(
        String start, String end, HashMap<String, ArrayList<Connection>> graph
    ){
        HashMap<String,info> INFO = new HashMap<>();
        HashSet<String> unvistedStation = new HashSet<>();

        // Initialise the HashMap at the beginning
        // assume that all station takes infinte time to reach
        // and we don't know how and which line it is on.
        Set<String> allStations = graph.keySet();
        for (String station : allStations){
            INFO.put(station, new info(Double.MAX_VALUE, null, null));
            unvistedStation.add(station);
        }

        // Initialise, it takes 0 min from start to start
        // And we don't know which line we are going to.
        INFO.put(start, new info(0.0, null, null));

        while(!unvistedStation.isEmpty()){

        }
    }

    public static void main(String[] args){
        String fName = "Metrolink_times_linecolour.csv";
        //store every things into Array for easy output
        ArrayList<Connection> links = new ArrayList<>();
        //store stations into a hash map for easy check user input.
        HashSet<String> stations = new HashSet<>();

        try{
            BufferedReader p = new BufferedReader(new FileReader(fName));
            String currentLine = null;
            String row;

            p.readLine();
            row = p.readLine();
            //skip the first Row of CSV file
            while(row != null){
                row = row.trim();

                if (row.equals("")){
                    continue;
                }
                String[] parts = row.split(",",-1);
                //Split current row into parts, split by ","
                //Store as an array of String
                
                String p1 = parts[0].trim();
                String p2 = parts[1].trim();
                String p3 = parts[2].trim();
                
                //if only first parts contain value, which means it is a Line catagory.
                if (!p1.equals("") && p2.equals("") && p3.equals("")){
                    currentLine = p1;
                    //eg, currentLine = Red;
                }
                //if all of them have variable, it is a line of data.
                else if (!p1.equals("") && !p2.equals("") && !p3.equals("")){
                    //Store those parts into Connection Class, then append into array.
                    Connection c = new Connection(p1, p2, Double.parseDouble(p3), currentLine);
                    links.add(c);
                }
                row = p.readLine();
            }
            p.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        //Out put how many connections are readed from the file, and print them.
        System.out.println("Total connections: " + links.size());
        
        //loop the array and store name of stations into a hash map.
        for (int i = 0; i<links.size(); i++){
            Connection obj = links.get(i);
            stations.add(obj.from);
        }

        //A graph which store station in a way of (A: B,C,D).
        HashMap<String, ArrayList<Connection>> graph = new HashMap<>();

        //initialise HASHMAP - graph
        for(int i = 0;i<links.size();i++){
            Connection temp = links.get(i);

            //If the station exists (and is not null)
            //Graph remains unchanged. If absent, the new value is inserted.
            //Used to prevent NullpointerException
            graph.putIfAbsent(temp.from, new ArrayList<>());
            graph.putIfAbsent(temp.to, new ArrayList<>());

            //forward
            graph.get(temp.from).add(temp);

            //backward should take same time
            graph.get(temp.to).add(
                new Connection(temp.to, temp.from, temp.time, temp.line)
            );
        }


        Scanner input = new Scanner(System.in);
        String from;
        String to;

        do {
            System.out.print("Please Enter start location of your journey: ");
            from = input.nextLine().trim();
            if(!stations.contains(from)){
                System.out.print("Location inputed is invalid\n");
            }
        } while (!stations.contains(from));

        do {
            System.out.print("Please Enter end location of your journey: ");
            to = input.nextLine().trim();
            if(!stations.contains(to)){
                System.out.print("Location inputed is invalid\n");
            }
        } while (!stations.contains(to));

        input.close();
        System.out.print("Start: "+ from + "\n");
        System.out.print("End: "+ to + "\n");
    }
}
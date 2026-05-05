public class Connection {
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

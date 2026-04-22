import java.io.*;
import java.util.*;

public class MetroPlan {

    static class Node{
        String station; //station Name.
        String line; //This station is on what line
    }

    static class Edge{
        Node to;//Form current station to xxx station
        double time; //Time taken from current station to another.
        boolean changeLine; //T for change, F for no need to change
    }

    
}
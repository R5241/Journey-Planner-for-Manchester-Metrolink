import java.util.*;
import javax.swing.*;
import java.awt.GridLayout;

public class MetroGUI {
    public static void routeGui(HashMap<String, ArrayList<Connection>> graph, HashSet<String> stations){
        JFrame frame = new JFrame("MteroLink Planner");
        
        JLabel startLabel = new JLabel("Start location:");
        JTextField startField = new JTextField();

        JLabel endLabel = new JLabel("End location:");
        JTextField endField = new JTextField();

        JLabel routeOptionLabel = new JLabel("Route option:");
        String[] routeOptions = {"Shortest Time", "Fewest Changes"};
        JComboBox<String> optionBox = new JComboBox<>(routeOptions);

        JButton exeuteButton = new JButton("Find Route");

        frame.setLayout(new GridLayout(4, 2, 10, 10));

        frame.add(startLabel);
        frame.add(startField);

        frame.add(endLabel);
        frame.add(endField);

        frame.add(routeOptionLabel);
        frame.add(optionBox);

        frame.add(new JLabel(""));
        frame.add(exeuteButton);


        exeuteButton.addActionListener( e -> 
            {
                String from = startField.getText().trim();
                String to = endField.getText().trim();
                String option = (String) optionBox.getSelectedItem();
                
                if(!stations.contains(from)){
                    JOptionPane.showMessageDialog(frame, "Start location is invalid.");
                    return;
                }

                if (!stations.contains(to)){
                    JOptionPane.showMessageDialog(frame, "End location is invalid.");
                    return;
                }

                System.out.println("Start: " + from);
                System.out.println("End: " + to);

                if (option.equals("Shortest Time")){
                    RouteFindingFunction.shortestTimeRoute_Dijkstra(from, to, graph);
                }
                else if(option.equals("Fewest Changes")){
                    RouteFindingFunction.fewestChangeRoute_Dijkstra(from, to, graph);
                }

            }
        );

        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}

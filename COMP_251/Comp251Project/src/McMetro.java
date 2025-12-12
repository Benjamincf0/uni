import java.util.*;
import java.lang.Math.*;

public class McMetro {
    protected Track[] tracks;
    protected HashMap<BuildingID, Building> buildingTable = new HashMap<>();
    private HashMap<BuildingID, ArrayList<Track>> adjacencyList = new HashMap<>();

    // You may initialize anything you need in the constructor
    McMetro(Track[] tracks, Building[] buildings) {
       this.tracks = tracks;

       // Populate buildings table
       for (Building building : buildings) {
           buildingTable.putIfAbsent(building.id(), building);
           adjacencyList.putIfAbsent(building.id(), new ArrayList<>());
       }

       // Populate adjacency lists
       for (Track track: tracks) {
            BuildingID buildingID1 = track.startBuildingId();
            // BuildingID buildingID2 = track.endBuildingId();

            adjacencyList.get(buildingID1).add(track);
            // adjacencyList.get(buildingID2).add(track);
       }
    }

    // Maximum number of passengers that can be transported from start to end
    int maxPassengers(BuildingID start, BuildingID end) {
        // TODO: your implementation here
        List<Track> startBuildingTracks = adjacencyList.get(start);
        for (Track track : startBuildingTracks) {
            if (track.endBuildingId().equals(end) || track.startBuildingId().equals(end)) {
                return Math.min(buildingTable.get(start).occupants(), Math.min(buildingTable.get(end).occupants(), track.capacity()));
            }
        }
        return 0;
    }

    // Returns a list of trackIDs that connect to every building maximizing total network capacity taking cost into account
    TrackID[] bestMetroSystem() {
        // TODO: your implementation here
        return new TrackID[0];
    }

    // Adds a passenger to the system
    void addPassenger(String name) {
        // TODO: your implementation here
    }

    // Do not change this
    void addPassengers(String[] names) {
        for (String s : names) {
            addPassenger(s);
        }
    }

    // Returns all passengers in the system whose names start with firstLetters
    ArrayList<String> searchForPassengers(String firstLetters) {
        // TODO: your implementation here
        return new ArrayList<>();
    }

    // Return how many ticket checkers will be hired
    static int hireTicketCheckers(int[][] schedule) {
        // TODO: your implementation here
        return 0;
    }
}
import java.util.*;

import javax.swing.plaf.basic.BasicTreeUI.TreeIncrementAction;

import java.lang.Math.*;

public class McMetro {
    protected Track[] tracks;
    protected HashMap<BuildingID, Building> buildingTable = new HashMap<>();
    private HashMap<BuildingID, ArrayList<Track>> adjacencyList = new HashMap<>();
    private Trie passengerTrie = new Trie();

    private record goodTrack(double goodness, Track track) implements Comparable<goodTrack> {
        @Override
        public int compareTo(goodTrack other) {
            return Double.compare(goodness, other.goodness);
        }
    };

    private class Trie {
        private TrieNode rootNode;
        private class TrieNode {
            public Map<Character, TrieNode> children = new HashMap<>();
            public boolean endNode = false;
            public char symbol;

            TrieNode(char symbol) {
                this.symbol = symbol;
            }
        }

        Trie() {
            this.rootNode = new TrieNode('|');
        }

        public void insert(String s) {
            TrieNode current = this.rootNode;

            for (char c : s.toLowerCase().toCharArray()) {
                current.children.putIfAbsent(c, new TrieNode(c));
                current = current.children.get(c);
            }
            current.endNode = true;
        }

        private TrieNode search(String s) {
            TrieNode current = this.rootNode;

            for (char c : s.toCharArray()) {
                current = current.children.get(c);
                if (current == null) {
                    return null;
                }
            }
            return current;
        }

        public ArrayList<String> searchAll(String s) {
            ArrayList<String> out = new ArrayList<>();
            TrieNode lastNode = this.search(s);
            if (lastNode == null) return out;

            // now do dfs to get everything else from here
            ArrayList<TrieNode> stack = new ArrayList<>();
            stack.add(lastNode);

            while (stack.size() > 0) {
                TrieNode currNode = stack.removeLast();
                
            }

        }

    }

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
        if (!buildingTable.containsKey(start) || !buildingTable.containsKey(end)) return 0; // input validation...

        List<Track> startBuildingTracks = adjacencyList.get(start);
        int maxTrackCap = 0;
        for (Track track : startBuildingTracks) {
            if (track.endBuildingId().equals(end)) {
                maxTrackCap =  Math.max(maxTrackCap, track.capacity());
            }
        }

        int occupantsStart = buildingTable.get(start).occupants();
        int occupantsEnd = buildingTable.get(end).occupants();

        int minOccupants = Math.min(occupantsStart, occupantsEnd);

        return Math.min(minOccupants, maxTrackCap);
    }

    // Returns a list of trackIDs that connect to every building maximizing total network capacity taking cost into account
    TrackID[] bestMetroSystem() {
        // TODO: your implementation here
        // first i need to compute the "goodness" of every track.
        // then i sort the tracks in order of their goodness.
        // then i init a disjoint set, where each building is in its own set.
        // i init a trackID arrayList that will serve as my return.
        // then i need to loop through each track from most to least good.
        //      for each track, i check if their start and end nodes are part of the same set.
        //          if yes: i dont choose this track
        //          otherwise: i add the trackID to my array of tracks
        goodTrack[] goodTracks = new goodTrack[tracks.length];

        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];
            double goodness = getGoodness(track);
            goodTracks[i] = new goodTrack(goodness, track);
        }

        NaiveDisjointSet<BuildingID> buildingDisjointSet = new NaiveDisjointSet<>();

        for (Building b : buildingTable.values()) {
            buildingDisjointSet.add(b.id());
        }

        Arrays.sort(goodTracks);
        TrackID[] selectedTracks = new TrackID[buildingTable.size() - 1];
        
        int selectedCount = 0;
        for (int i = tracks.length - 1; i >= 0 && selectedCount < selectedTracks.length; i--) {
            Track track = goodTracks[i].track();
            BuildingID b1 = track.startBuildingId();
            BuildingID b2 = track.endBuildingId();

            if (!buildingDisjointSet.find(b1).equals(buildingDisjointSet.find(b2))) {
                buildingDisjointSet.union(b1, b2);
                selectedTracks[selectedCount++] = track.id();
            }
        }

        return selectedTracks;
    }

    // Adds a passenger to the system
    void addPassenger(String name) {
        // TODO: your implementation here
        this.passengerTrie.insert(name);
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

    private double getGoodness(Track track) {
        return (track.cost() == 0) ? Double.MAX_VALUE : Math.min(track.capacity(),
                                                        Math.min(
                                                            buildingTable.get(track.startBuildingId()).occupants(),
                                                            buildingTable.get(track.endBuildingId()).occupants()
                                                        )) / ((double) track.cost());
    }
}
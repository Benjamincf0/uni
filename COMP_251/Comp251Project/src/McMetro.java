import java.util.*;
// import java.lang.Math.*;

public class McMetro {
    protected Track[] tracks;
    protected HashMap<BuildingID, Building> buildingTable = new HashMap<>();
    private Trie passengerTrie = new Trie();

    private record goodTrack(double goodness, Track track) implements Comparable<goodTrack> {
        @Override
        public int compareTo(goodTrack other) {
            return Double.compare(goodness, other.goodness);
        }
    };

    private class Trie {
        private TrieNode rootNode = new TrieNode();
        private class TrieNode {
            public TrieNode[] children = new TrieNode[26]; // just 26 letters
            public boolean endNode = false;
        }

        public void insert(String s) {
            TrieNode current = this.rootNode;

            for (char letter : s.toLowerCase().toCharArray()) {
                if (letter -'a' > 25 || letter-'a' < 0) System.err.println("uhmmm thats not a normal letter");
                if (current.children[letter-'a'] == null) {
                    current.children[letter-'a'] = new TrieNode();
                }
                current = current.children[letter-'a'];
            }
            current.endNode = true;
        }

        private TrieNode search(String s) {
            TrieNode current = this.rootNode;

            for (char letter : s.toLowerCase().toCharArray()) {
                if (letter -'a' > 25 || letter-'a' < 0) System.err.println("uhmmm thats not a normal letter 2");
                current = current.children[letter-'a'];
                if (current == null) return null;
            }
            return current;
        }

        public ArrayList<String> searchAll(String s) {
            ArrayList<String> out = new ArrayList<>();
            if (s == null) return out;
            TrieNode lastNode = this.search(s);
            if (lastNode == null) return out; // if theres no match just return empty list

            String prefix = (s.length() == 0)? "" : s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase(); // if length is 0, all results...

            // now do dfs to get every match to the search prefix
            this.dfs(lastNode, prefix, out);
            return out;
        }

        private void dfs(TrieNode node, String prefix, ArrayList<String> out) {
            if (node.endNode) out.add(prefix);

            // iterate for each child
            for (char letter = 'a'; letter <= 'z'; letter++) {
                if (node.children[letter-'a'] == null) continue;
                
                // combine each childs array of wordss
                String newPrefix = prefix.length() < 1 ? (""+letter).toUpperCase() : prefix + letter;
                this.dfs(node.children[letter-'a'], newPrefix, out);
            }
        }
    }

    // You may initialize anything you need in the constructor
    McMetro(Track[] tracks, Building[] buildings) {
        // this.tracks = tracks;
        this.tracks = (tracks == null) ? new Track[0] : tracks;

       // Populate buildings table
        if (buildings != null) {
            for (Building building : buildings) {
                buildingTable.putIfAbsent(building.id(), building);
            }
        }
    }

    // Maximum number of passengers that can be transported from start to end
    int maxPassengers(BuildingID start, BuildingID end) {
        // input validation....
        if (start.equals(end) || !buildingTable.containsKey(start) || !buildingTable.containsKey(end)) {
            return 0;
        }

        // create the residual graph to store connected building and updated capacity
        HashMap<BuildingID, HashMap<BuildingID, Integer>> residualGraph = new HashMap<>();
        // {<key>: <value>
        //  b1: {b2: c1, b5: c2, b6: c3}
        //  b2: {b5: c4, b3: c5, b1: c6}
        //  b3: {b1: c7, b3: c8, b1: c9}
        //  .
        //  .
        //  .
        //  b6: {b3: c10, b2: c11, b1: c12}
        // }

        for (Track track : tracks) {
            if (track == null) continue;
            BuildingID u = track.startBuildingId();
            BuildingID v = track.endBuildingId();
            int capacity = getCapacity(track);

            // computeIfAbsent tries to get the value for key u in the hashmap.
            // if theres no value for key u, it adds the result of the lamda function to the hashmap for key u
            // it any case it returns the value of the hashmap for key u.
            residualGraph.computeIfAbsent(u, this::newHashMap) // here we get an entry like: {b2: c1, b5: c2, b6: c3}
                         .merge(v, capacity, Integer::sum); // incase theres 2 parralel edges in same direction

             // initialize the reverse edge with 0 if its not already there
            residualGraph.computeIfAbsent(v, this::newHashMap)
                         .putIfAbsent(u, 0);
        }


        int maxFlow = 0;
        // hold the path from bfs
        HashMap<BuildingID, BuildingID> parentMap = new HashMap<>();


        while (bfs(residualGraph, start, end, parentMap)) {

            int pathFlow = Integer.MAX_VALUE;
            BuildingID currID = end;
            while (!currID.equals(start)) {
                BuildingID prev = parentMap.get(currID);

                int availableCapacity = residualGraph.get(prev).get(currID);

                pathFlow = Math.min(pathFlow, availableCapacity);

                currID = prev;
            }


            currID = end;
            while (!currID.equals(start)) {
                BuildingID prev = parentMap.get(currID);

                // reduce forward path
                int oldFwd = residualGraph.get(prev).get(currID);
                residualGraph.get(prev).put(currID, oldFwd - pathFlow);

                // reduce reverse path
                int oldRev = residualGraph.get(currID).get(prev);
                residualGraph.get(currID).put(prev, oldRev + pathFlow);

                currID = prev;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }


    private boolean bfs(HashMap<BuildingID, HashMap<BuildingID, Integer>> resGraph, 
                        BuildingID s, BuildingID t, 
                        HashMap<BuildingID, BuildingID> parentMap) {
        parentMap.clear();
        HashSet<BuildingID> visited = new HashSet<>();
        Queue<BuildingID> queue = new LinkedList<>();

        // add source node
        queue.add(s);
        visited.add(s);

        while (!queue.isEmpty()) {
            BuildingID u = queue.poll();
            if (u.equals(t)) return true;

            HashMap<BuildingID, Integer> neighbors = resGraph.get(u);
            if (neighbors == null) continue;

            for (Map.Entry<BuildingID, Integer> entry : neighbors.entrySet()) {
                BuildingID v = entry.getKey();
                int cap = entry.getValue();

                if (!visited.contains(v) && cap > 0) {
                    parentMap.put(v, u);
                    visited.add(v);
                    queue.add(v);
                }
            }
        }
        return false;
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
        return this.passengerTrie.searchAll(firstLetters);
    }

    // Return how many ticket checkers will be hired
    static int hireTicketCheckers(int[][] schedule) {
        // TODO: your implementation here
        if (schedule.length == 0) return 0;
        
        Arrays.sort(schedule, (a, b) -> Integer.compare(a[1], b[1])); // O(nlogn)
        
        int numCheckers = 1;
        int[] prevShift = schedule[0];

        for (int i = 1; i < schedule.length; i++) {
            int[] currShift = schedule[i];

            if (currShift[0] < prevShift[1]) continue;
            numCheckers ++;
            prevShift = currShift;
        }
        return numCheckers;
    }

    private double getGoodness(Track track) {
        return (track.cost() == 0) ? Double.MAX_VALUE : Math.min(track.capacity(),
                                                        Math.min(
                                                            buildingTable.get(track.startBuildingId()).occupants(),
                                                            buildingTable.get(track.endBuildingId()).occupants()
                                                        )) / ((double) track.cost());
    }

    private int getCapacity(Track track) {
        // get buildings from track
        BuildingID u = track.startBuildingId();
        BuildingID v = track.endBuildingId();


        // compute each track's capacity
        int uOccupants = buildingTable.get(u).occupants();
        int vOccupants = buildingTable.get(v).occupants();
        int capacity = Math.min(track.capacity(), Math.min(uOccupants, vOccupants));
        return capacity;
    }

    private HashMap<BuildingID, Integer> newHashMap(BuildingID key) {
        return new HashMap<>();
    }
}


// REFERENCES:
// https://www.youtube.com/watch?v=VbeTl1gG4l4
// https://www.youtube.com/watch?v=rlftCrW8t9s
// 
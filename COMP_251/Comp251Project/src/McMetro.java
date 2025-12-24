import java.util.*;


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
            public TrieNode[] children = new TrieNode[26]; 
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
            if (lastNode == null) return out; 

            String prefix = (s.length() == 0)? "" : s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase(); 

            
            this.dfs(lastNode, prefix, out);
            return out;
        }

        private void dfs(TrieNode node, String prefix, ArrayList<String> out) {
            if (node.endNode) out.add(prefix);

            
            for (char letter = 'a'; letter <= 'z'; letter++) {
                if (node.children[letter-'a'] == null) continue;
                
                
                String newPrefix = prefix.length() < 1 ? (""+letter).toUpperCase() : prefix + letter;
                this.dfs(node.children[letter-'a'], newPrefix, out);
            }
        }
    }

    
    McMetro(Track[] tracks, Building[] buildings) {
        
        this.tracks = (tracks == null) ? new Track[0] : tracks;

       
        if (buildings != null) {
            for (Building building : buildings) {
                buildingTable.putIfAbsent(building.id(), building);
            }
        }
    }

    
    int maxPassengers(BuildingID start, BuildingID end) {
        
        if (start.equals(end) || !buildingTable.containsKey(start) || !buildingTable.containsKey(end)) {
            return 0;
        }

        
        HashMap<BuildingID, HashMap<BuildingID, Integer>> residualGraph = new HashMap<>();
        
        
        
        
        
        
        
        
        

        for (Track track : tracks) {
            if (track == null) continue;
            BuildingID u = track.startBuildingId();
            BuildingID v = track.endBuildingId();
            int capacity = getCapacity(track);

            
            
            
            residualGraph.computeIfAbsent(u, this::newHashMap) 
                         .merge(v, capacity, Integer::sum); 

             
            residualGraph.computeIfAbsent(v, this::newHashMap)
                         .putIfAbsent(u, 0);
        }


        int maxFlow = 0;
        
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

                
                int oldFwd = residualGraph.get(prev).get(currID);
                residualGraph.get(prev).put(currID, oldFwd - pathFlow);

                
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

    
    TrackID[] bestMetroSystem() {
        
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

    
    void addPassenger(String name) {
        
        this.passengerTrie.insert(name);
    }

    
    void addPassengers(String[] names) {
        for (String s : names) {
            addPassenger(s);
        }
    }

    
    ArrayList<String> searchForPassengers(String firstLetters) {
        
        return this.passengerTrie.searchAll(firstLetters);
    }

    
    static int hireTicketCheckers(int[][] schedule) {
        
        if (schedule.length == 0) return 0;
        
        Arrays.sort(schedule, (a, b) -> Integer.compare(a[1], b[1])); 
        
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
        
        BuildingID u = track.startBuildingId();
        BuildingID v = track.endBuildingId();


        
        int uOccupants = buildingTable.get(u).occupants();
        int vOccupants = buildingTable.get(v).occupants();
        int capacity = Math.min(track.capacity(), Math.min(uOccupants, vOccupants));
        return capacity;
    }

    private HashMap<BuildingID, Integer> newHashMap(BuildingID key) {
        return new HashMap<>();
    }
}






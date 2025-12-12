import java.util.*;

public class NaiveDisjointSet<T> {
    HashMap<T, T> parentMap = new HashMap<>();
    private HashMap<T, Integer> rankMap = new HashMap<>();

    void add(T element) {
        parentMap.put(element, element);
        rankMap.put(element, 0);
    }

    // TODO: Implement path compression
    T find(T a) {
        T node = parentMap.get(a);
        if (node.equals(a)) {
            return node;
        } else {
            T parent = find(parentMap.get(a));
            parentMap.put(a, parent); // put the parent here
            return parent;
        }
    }

    // TODO: Implement union by size or union by rank
    void union(T a, T b) {
        T rootA = find(a);
        T rootB = find(b);
        if (rootA.equals(rootB)) return;

        Integer rankA = rankMap.get(rootA);
        Integer rankB = rankMap.get(rootB);

        if (rankA.equals(rankB)) {
            parentMap.put(rootB, rootA);
            rankMap.put(rootA, rankMap.get(rootA) + 1);
        } else if (rankA > rankB) {
            parentMap.put(rootB, rootA);
        } else {
            parentMap.put(rootA, rootB);
        }
    }
}

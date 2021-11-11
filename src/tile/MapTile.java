package tile;

public class MapTile {

    private int id;
    private boolean[] validDirections;

    /**
     *   ID  
     *   0   Wall
     *   1   Dot
     *   2   Power Pellet
     *   7   Empty (No Dot / Dot Eaten)
     * 
     *   validDirections
     *   [w, a, s, d]
     *   true false representing wasd
     */

    public MapTile(int id, boolean[] validDirections) {
        this.id = id;
        this.validDirections = validDirections;
    }

    public int getId() {
        return this.id;
    }

    public boolean[] getValidDirections() {
        return this.validDirections;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIntersection() {
        boolean[] d = this.getValidDirections();
        if (d[0] && d[1]) return true;
        else if (d[1] && d[2]) return true;
        else if (d[2] && d[3]) return true;
        else if (d[3] && d[0]) return true;
        return false;
    }
    
}

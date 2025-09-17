/** (0meansblank) */
public final class Tile {
    private final int value;
    public Tile(int value) {
        this.value = value;
    }
    public int value() { return value; }
    public boolean isBlank() { return value == 0; }
}

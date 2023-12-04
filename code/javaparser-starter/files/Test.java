
public class Test {
    private int number;
    private String name;
    private boolean active;

    public Test(int number, String name, boolean active) {
        this.number = number;
        this.name = name;
        this.active = active;
    }

    public int getNumber() {
        return this.number;
    }

    public boolean hasName() {
        return this.name != null && !this.name.isEmpty();
    }
}
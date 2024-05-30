public class Card {
    
    private String color, value;

    public Card(String color, String value) {
        this.color = color; // red, blue, green, yellow
        this.value = value; // 0-9
        
    }

    public String getColor() {
        return color;
    }
    public String getValue() {
        return this.value;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return color + " " + value;
    }
}

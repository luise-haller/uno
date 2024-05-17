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
    public boolean matches(Card otherCard) {
        return this.color.equals(otherCard.getColor()) || this.value.equals(otherCard.getValue());
    }
    @Override
    public String toString() {
        return color + " " + value;
    }
}
class ActionCard extends Card {
    public ActionCard(String color, String value) { // value = "reverse," "skip," or "draw2"
        super(color, value);
    }
}
class WildCard extends Card {
    public WildCard(String value) { // value = "wild" or "wilddraw4"
        super("ANY", value); 
    }
}

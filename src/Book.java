public class Book {
    public int id;
    public int score;

    public Book(int id, int score) {
        this.id = id;
        this.score = score;
    }

    public String toString() {
        return "ID: " + id;
    }
}

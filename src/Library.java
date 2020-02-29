import java.util.ArrayList;

public class Library {
    public int nBooks;
    public int signUpTime;
    public int shippingRate;
    public int libraryID;
    public ArrayList<Book> books;

    public Library(int nBooks, int signUpTime, int shippingRate, int libraryID, ArrayList<Book> books) {
        this.nBooks = nBooks;
        this.signUpTime = signUpTime;
        this.shippingRate = shippingRate;
        this.books = books;
        this.libraryID = libraryID;
    }

    public String toString() {
        return "Library " + signUpTime;
    }

}

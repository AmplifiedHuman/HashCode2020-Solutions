import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Solution {
    private int nBooks;
    private int nLibraries;
    private int nDays;
    private TreeMap<Integer, Integer> bookMap;
    private ArrayList<Library> libraries;
    private ArrayList<ArrayList<Integer>> booksSent;
    private ArrayList<Library> usedLibraries;
    private HashSet<Integer> scannedBooks;


    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 6; i++) {
            Solution s = new Solution();
            String name = Character.toString((char) ('a' + i));
            s.inputParser("./input/" + name + ".txt");
            s.scannedBooks = new HashSet<>();
            s.usedLibraries = new ArrayList<>();
            s.solve();
            s.scorer();
            s.fileWriter(name);
        }
    }

    private void scorer() {
        HashSet<Integer> set = new HashSet<>();
        long sum = 0;
        for (ArrayList<Integer> a : booksSent) {
            for (int book : a) {
                if (!set.contains(book)) {
                    set.add(book);
                    sum += bookMap.get(book);
                }
            }
        }
        System.out.println("Total score: " + sum);
    }

    private void fileWriter(String name) throws IOException {
        FileWriter writer = new FileWriter("./output/" + name + "_output.txt");
        writer.write(booksSent.size() + "\n");
        int i = 0;
        for (ArrayList<Integer> b : booksSent) {
            writer.write(String.format("%d %d\n", usedLibraries.get(i).libraryID, b.size()));
            i++;
            StringBuilder sb = new StringBuilder();
            for (int j : b) {
                sb.append(j);
                sb.append(" ");
            }
            sb.append("\n");
            writer.write(sb.toString());
        }
        writer.close();
    }

    private void solve() {
        // initialise booksSent
        booksSent = new ArrayList<>();
        // Sort libraries according to sign up time
        libraries.sort(new Comparator<Library>() {
            @Override
            public int compare(Library o1, Library o2) {
                if (o1.signUpTime == o2.signUpTime) {
                    return Long.compare(o2.shippingRate, o1.shippingRate);
                }
                return o1.signUpTime - o2.signUpTime;
            }
        });
        // total setup time
        int currentTotalSetUp = 0;

        while (libraries.iterator().hasNext()) {
            // time spent by library
            int libraryTime = 0;
            Library temp = libraries.iterator().next();
            int librarySetUpTime = temp.signUpTime;
            int libraryRate = temp.shippingRate;
            if (currentTotalSetUp + librarySetUpTime > nDays) {
                break;
            }
            currentTotalSetUp += librarySetUpTime;
            libraryTime += librarySetUpTime;

            ArrayList<Book> libraryBooks = temp.books;
            libraryBooks.sort(new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o2.score - o1.score;
                }
            });

            ArrayList<Integer> tempBooks = new ArrayList<>();
            for (int j = 0; j < libraryBooks.size(); j++) {
                if ((tempBooks.size() / libraryRate) + libraryTime > nDays) {
                    break;
                }
                tempBooks.add(libraryBooks.get(j).id);
                scannedBooks.add(libraryBooks.get(j).id);
            }
            usedLibraries.add(temp);
            libraries.remove(temp);
            libraries.sort((o1, o2) -> (int) Math.round((Integer.compare(o1.signUpTime, o2.signUpTime) * 0.7 + Long.compare(getLibraryWorth(o2), getLibraryWorth(o1)) * 0.3) * 100));
            booksSent.add(tempBooks);
        }
    }

    private void inputParser(String name) throws FileNotFoundException {
        Scanner reader = new Scanner(new File(name));
        String[] firstLineInput = reader.nextLine().split(" ");
        nBooks = Integer.parseInt(firstLineInput[0]);
        nLibraries = Integer.parseInt(firstLineInput[1]);
        nDays = Integer.parseInt(firstLineInput[2]);
        String[] tbooks = reader.nextLine().split(" ");
        bookMap = new TreeMap<>();
        for (int i = 0; i < tbooks.length; i++) {
            bookMap.put(i, Integer.parseInt(tbooks[i]));
        }

        libraries = new ArrayList<>();
        for (int i = 0; i < nLibraries; i++) {
            String[] firstLine = reader.nextLine().split(" ");
            String[] secondLine = reader.nextLine().split(" ");
            Library l = new Library(Integer.parseInt(firstLine[0]), Integer.parseInt(firstLine[1]),
                    Integer.parseInt(firstLine[2]), i, new ArrayList<>());
            ArrayList<Book> bookList = l.books;
            for (String s : secondLine) {
                int id = Integer.parseInt(s);
                bookList.add(new Book(id, bookMap.get(id)));
            }
            libraries.add(l);
        }
        reader.close();
    }

    private long getLibraryWorth(Library l) {
        long total = 0;
        for (Book b : l.books) {
            if (!scannedBooks.contains(b.id)) {
                total += b.score;
            }
        }
        return total;
    }
}


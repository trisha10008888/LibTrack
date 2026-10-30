
package com.libtrack.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookDataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {

        // Prevent duplicate insertion when the application restarts
        if (bookRepository.count() > 0) {
            System.out.println("Books already exist. Skipping data insertion.");
            return;
        }

        String[][] bookData = {

            // Personal Development, Motivation & Psychology
            {"Atomic Habits", "James Clear", "Personal Development, Motivation & Psychology"},
            {"How to Win Friends and Influence People", "Dale Carnegie", "Personal Development, Motivation & Psychology"},
            {"The 7 Habits of Highly Effective People", "Stephen R. Covey", "Personal Development, Motivation & Psychology"},
            {"The Psychology of Money", "Morgan Housel", "Personal Development, Motivation & Psychology"},
            {"Mindset: The New Psychology of Success", "Carol S. Dweck", "Personal Development, Motivation & Psychology"},
            {"Man's Search for Meaning", "Viktor E. Frankl", "Personal Development, Motivation & Psychology"},
            {"The Let Them Theory", "Mel Robbins", "Personal Development, Motivation & Psychology"},
            {"The Courage to Be Disliked", "Ichiro Kishimi & Fumitake Koga", "Personal Development, Motivation & Psychology"},
            {"Emotional Intelligence", "Daniel Goleman", "Personal Development, Motivation & Psychology"},
            {"The Subtle Art of Not Giving a F*ck", "Mark Manson", "Personal Development, Motivation & Psychology"},

            // Fiction
            {"The Alchemist", "Paulo Coelho", "Fiction"},
            {"The Kite Runner", "Khaled Hosseini", "Fiction"},
            {"A Thousand Splendid Suns", "Khaled Hosseini", "Fiction"},
            {"The Book Thief", "Markus Zusak", "Fiction"},
            {"The Midnight Library", "Matt Haig", "Fiction"},
            {"The Seven Husbands of Evelyn Hugo", "Taylor Jenkins Reid", "Fiction"},
            {"The Silent Patient", "Alex Michaelides", "Fiction"},
            {"The Song of Achilles", "Madeline Miller", "Fiction"},
            {"The Nightingale", "Kristin Hannah", "Fiction"},
            {"Before the Coffee Gets Cold", "Toshikazu Kawaguchi", "Fiction"},

            // Mystery
            {"Gone Girl", "Gillian Flynn", "Mystery"},
            {"And Then There Were None", "Agatha Christie", "Mystery"},
            {"The Girl with the Dragon Tattoo", "Stieg Larsson", "Mystery"},
            {"The Da Vinci Code", "Dan Brown", "Mystery"},
            {"The Housemaid", "Freida McFadden", "Mystery"},
            {"The Thursday Murder Club", "Richard Osman", "Mystery"},
            {"The Murder of Roger Ackroyd", "Agatha Christie", "Mystery"},
            {"The Guest List", "Lucy Foley", "Mystery"},
            {"The Woman in the Window", "A.J. Finn", "Mystery"},

            // Horror & Thriller
            {"The Shining", "Stephen King", "Horror & Thriller"},
            {"It", "Stephen King", "Horror & Thriller"},
            {"Misery", "Stephen King", "Horror & Thriller"},
            {"The Haunting of Hill House", "Shirley Jackson", "Horror & Thriller"},
            {"Mexican Gothic", "Silvia Moreno-Garcia", "Horror & Thriller"},
            {"The Only Good Indians", "Stephen Graham Jones", "Horror & Thriller"},
            {"Verity", "Colleen Hoover", "Horror & Thriller"},
            {"Home Before Dark", "Riley Sager", "Horror & Thriller"},

            // Science Fiction
            {"Dune", "Frank Herbert", "Science Fiction"},
            {"1984", "George Orwell", "Science Fiction"},
            {"Project Hail Mary", "Andy Weir", "Science Fiction"},
            {"The Martian", "Andy Weir", "Science Fiction"},
            {"The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "Science Fiction"},
            {"Fahrenheit 451", "Ray Bradbury", "Science Fiction"},
            {"Brave New World", "Aldous Huxley", "Science Fiction"},
            {"Ender's Game", "Orson Scott Card", "Science Fiction"},
            {"The Three-Body Problem", "Cixin Liu", "Science Fiction"},
            {"Do Androids Dream of Electric Sheep?", "Philip K. Dick", "Science Fiction"},

            // Biography & Autobiography
            {"Becoming", "Michelle Obama", "Biography & Autobiography"},
            {"The Diary of a Young Girl", "Anne Frank", "Biography & Autobiography"},
            {"Long Walk to Freedom", "Nelson Mandela", "Biography & Autobiography"},
            {"Steve Jobs", "Walter Isaacson", "Biography & Autobiography"},
            {"Wings of Fire", "A.P.J. Abdul Kalam", "Biography & Autobiography"},
            {"Educated", "Tara Westover", "Biography & Autobiography"},
            {"I Am Malala", "Malala Yousafzai", "Biography & Autobiography"},
            {"The Story of My Experiments with Truth", "Mahatma Gandhi", "Biography & Autobiography"},
            {"Open", "Andre Agassi", "Biography & Autobiography"},
            {"When Breath Becomes Air", "Paul Kalanithi", "Biography & Autobiography"},

            // Business & Finance
            {"Rich Dad Poor Dad", "Robert T. Kiyosaki", "Business & Finance"},
            {"The Intelligent Investor", "Benjamin Graham", "Business & Finance"},
            {"Think and Grow Rich", "Napoleon Hill", "Business & Finance"},
            {"The Lean Startup", "Eric Ries", "Business & Finance"},
            {"Zero to One", "Peter Thiel", "Business & Finance"},
            {"Good to Great", "Jim Collins", "Business & Finance"},
            {"The Millionaire Next Door", "Thomas J. Stanley & William D. Danko", "Business & Finance"},
            {"Deep Work", "Cal Newport", "Business & Finance"},
            {"Shoe Dog", "Phil Knight", "Business & Finance"},

            // History
            {"Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "History"},
            {"Guns, Germs, and Steel", "Jared Diamond", "History"},
            {"The Silk Roads", "Peter Frankopan", "History"},
            {"The Wright Brothers", "David McCullough", "History"},
            {"India After Gandhi", "Ramachandra Guha", "History"},
            {"The Discovery of India", "Jawaharlal Nehru", "History"},
            {"The Rise and Fall of the Third Reich", "William L. Shirer", "History"},
            {"The Guns of August", "Barbara W. Tuchman", "History"},
            {"The Anarchy", "William Dalrymple", "History"}
        };

        List<Book> books = new ArrayList<>();

        for (String[] data : bookData) {
            Book book = new Book();

            book.setTitle(data[0]);
            book.setAuthor(data[1]);
            book.setCategory(data[2]);
            book.setAvailable(true);

            books.add(book);
        }

        bookRepository.saveAll(books);

        System.out.println("==================================");
        System.out.println("LIBTRACK BOOKS SUCCESSFULLY ADDED!");
        System.out.println("Total books inserted: " + books.size());
        System.out.println("==================================");
    }
}

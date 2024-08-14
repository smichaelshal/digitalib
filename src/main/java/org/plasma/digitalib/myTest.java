package org.plasma.digitalib;

import org.plasma.digitalib.dtos.Book;
import org.plasma.digitalib.storage.FilePersistenterStorage;
import org.plasma.digitalib.storage.Storage;

import java.nio.file.Path;
import java.util.LinkedList;


public class myTest {
    public static void main(String[] args) {
        Storage<Book> s1 = new FilePersistenterStorage<>(new LinkedList<Book>(), Path.of("C:\\Users\\Ori\\IdeaProjects\\digitalib\\src\\db"));
//        Book b1 = new Book("a", "b", new BookIdentifier("c", "d"));
//        Instant t1 = Instant.now().plus(3, ChronoUnit.SECONDS);
//        b1.getBorrowings().add(new Borrowing(new User("1234"), Instant.now(), t1));
//        s1.create(b1);
//

//        Book b2 = new Book("summary", "bla bla", new BookIdentifier("book2", "kaki"));
//        b2.getBorrowings().add(new Borrowing(new User("1234"), Instant.now(), Optional.empty(), t1));
//        s1.create(b2);

//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        Path path = Path.of("C:\\Users\\Ori\\IdeaProjects\\digitalib\\src\\db", "8b5b7bcd-0640-4c0f-84b0-0dee132df454");
//        File file = new File("C:\\Users\\Ori\\IdeaProjects\\digitalib\\src\\db\\8b5b7bcd-0640-4c0f-84b0-0dee132df454");

//        SimpleModule module = new SimpleModule();
//        module.addSerializer(Optional.class, new OptionalSerializer<Instant>());
//        module.addDeserializer(Optional.class, new OptionalDeserializer<Instant>());
//        objectMapper.registerModule(module);

//        objectMapper.registerModule(new Jdk8Module());



//        try {
////            objectMapper.writeValue(file, new Borrowing(new User("1234"), Instant.now(), Optional.of(Instant.now()), Instant.now()));
////            Borrowing borrowing = objectMapper.readValue(file, Borrowing.class);
////            System.out.println(borrowing.getBorrowingTime());
//        } catch (Exception e) {
//            System.out.println(e);
//        }

//        Book b3 = new Book("summary", "bla bla", new BookIdentifier("book2", "kaki"));
//        b3.getBorrowings().add(new Borrowing(new User("1234"), Instant.now(), Optional.empty(), Instant.now().plus(7, ChronoUnit.SECONDS)));
//        s1.create(b3);
    }
}

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

  final Path filePath = Paths.get("users.csv");

  @BeforeEach
  void setUp() throws IOException {
    Files.deleteIfExists(filePath);
    Files.createFile(filePath);
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "2,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.deleteIfExists(filePath);
  }

  @Test
  void create_shouldSucceed() throws IOException {
    List.of(
      new User(15, "Dawson", "Augusta"),
      new User(25, "Ford", "Joseph", "Nicholas")
    ).forEach(user -> Main.create(filePath.toString(), user));

    assertEquals(
        String.join("\n", List.of(
          "1,Murphy,Aileen,Deborah,38",
          "2,Norton,Robert,\"\",12",
          "3,Dawson,Augusta,\"\",15",
          "4,Ford,Joseph,Nicholas,25"
        )),
        String.join("\n", Files.readAllLines(filePath))
    );
  }

  @Test
  void read_shouldReturnUser() {
    assertEquals(
        new User(12, "Norton", "Robert"),
        Main.read(filePath.toString(), 2)
    );
  }

  @Test
  void read_shouldReturnNull() {
    assertNull(Main.read(filePath.toString(), 10));
  }

  @Test
  void update_shouldSucceed() throws IOException {
    Main.update(filePath.toString(), 2,
        new User(25, "Ford", "Joseph", "Nicholas"));

    assertEquals(
        String.join("\n", List.of(
          "1,Murphy,Aileen,Deborah,38",
          "2,Ford,Joseph,Nicholas,25"
        )),
        String.join("\n", Files.readAllLines(filePath))
    );
  }

  @Test
  void update_shouldThrow() {
    assertThrows(NoSuchElementException.class, () -> Main.update(filePath.toString(), 10,
        new User(12, "Norton", "Robert")));
  }

  @Test
  void delete_shouldSucceed() throws IOException {
    Main.delete(filePath.toString(), 1);
    assertEquals(
        "2,Norton,Robert,\"\",12",
        String.join("\n", Files.readAllLines(filePath))
    );
  }

  @Test
  void delete_shouldThrow() {
    assertThrows(NoSuchElementException.class, () -> Main.delete(filePath.toString(), 10));
  }
}
package com.htc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

  final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
  final ByteArrayOutputStream testErr = new ByteArrayOutputStream();
  final PrintStream originalOut = System.out;
  final PrintStream originalErr = System.err;

  final String fileName = "users.csv";
  final Path filePath = Paths.get(fileName);

  @BeforeEach
  void setUp() throws IOException {
    System.setOut(new PrintStream(testOut));
    System.setErr(new PrintStream(testErr));

    Files.deleteIfExists(filePath);
    Files.createFile(filePath);
  }

  @AfterEach
  void tearDown() throws IOException {
    System.setOut(originalOut);
    System.setErr(originalErr);

    Files.deleteIfExists(filePath);
  }

  @Test
  void anyCommand_fileNotExists_shouldDisplayFailureMessage() throws IOException {
    Files.deleteIfExists(filePath);

    List<String[]> commands = List.of(
        new String[]{fileName, "show"},
        new String[]{fileName, "create", "first_name=A", "second_name=B", "age=1"},
        new String[]{fileName, "read", "id=1"},
        new String[]{fileName, "update", "first_name=A", "second_name=B", "age=1", "id=1"},
        new String[]{fileName, "delete", "id=1"}
    );
    commands.forEach(Main::main);

    assertEquals(
        String.format(Messages.FILE_NOT_EXISTS, fileName).repeat(commands.size()),
        testErr.toString()
    );
  }

  @Test
  void anyCommand_invalidArguments_shouldDisplayFailureMessage() {
    List<String> commands = List.of("show", "create", "read", "update", "delete");
    commands.forEach(command -> Main.main(new String[]{fileName, command, "arg1", "arg2"}));

    assertEquals(
        commands.stream()
          .map(command -> String.format(Messages.INVALID_ARGUMENTS, command))
          .collect(Collectors.joining()),
        testErr.toString()
    );
  }

  @Test
  void anyCommand_commandNotExists_shouldDisplayFailureMessage() {
    String command = "command";

    Main.main(new String[]{fileName, command});

    assertEquals(
        String.format(Messages.COMMAND_NOT_EXISTS, command),
        testErr.toString()
    );
  }

  @Test
  void show_fileIsNotEmpty_shouldDisplayTable() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    Main.main(new String[]{fileName, "show"});

    assertEquals(
        Messages.TABLE_HEAD
        + String.format(Messages.TABLE_ROW, 1, 38, "Murphy", "Aileen", "Deborah")
        + String.format(Messages.TABLE_ROW, 4, 12, "Norton", "Robert", ""),
        testOut.toString()
    );
  }

  @Test
  void show_fileIsEmpty_shouldDisplayTableHead() {
    Main.main(new String[]{fileName, "show"});
    assertEquals(Messages.TABLE_HEAD, testOut.toString());
  }

  @Test
  void create_fileIsNotEmpty_shouldDisplayUser() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    List.of(
        Stream.of("first_name=Augusta", "second_name=Dawson", "age=15"),
        Stream.of("age=25", "first_name=Joseph", "middle_name=Nicholas", "second_name=Ford"),
        Stream.of("second_name=Lambert", "age=18", "first_name=Edward")
    ).forEach(args -> Main.main(Stream
        .concat(Stream.of(fileName, "create"), args)
        .toArray(String[]::new))
    );

    assertAll(
        () -> assertEquals(
          String.join("\n", List.of(
            "1,Murphy,Aileen,Deborah,38",
            "2,Dawson,Augusta,\"\",15",
            "3,Ford,Joseph,Nicholas,25",
            "4,Norton,Robert,\"\",12",
            "5,Lambert,Edward,\"\",18"
          )),
          String.join("\n", Files.readAllLines(filePath))
        ),
        () -> assertEquals(
          Messages.TABLE_HEAD
          + String.format(Messages.TABLE_ROW, 2, 15, "Dawson", "Augusta", "")
          + Messages.TABLE_HEAD
          + String.format(Messages.TABLE_ROW, 3, 25, "Ford", "Joseph", "Nicholas")
          + Messages.TABLE_HEAD
          + String.format(Messages.TABLE_ROW, 5, 18, "Lambert", "Edward", ""),
          testOut.toString()
        )
    );
  }

  @Test
  void create_fileIsEmpty_shouldDisplayUser() {
    List.of(
        Stream.of("first_name=Augusta", "second_name=Dawson", "age=15"),
        Stream.of("age=25", "first_name=Joseph", "middle_name=Nicholas", "second_name=Ford"),
        Stream.of("second_name=Lambert", "age=18", "first_name=Edward")
    ).forEach(args -> Main.main(Stream
        .concat(Stream.of(fileName, "create"), args)
        .toArray(String[]::new))
    );

    assertAll(
        () -> assertEquals(
          String.join("\n", List.of(
            "1,Dawson,Augusta,\"\",15",
            "2,Ford,Joseph,Nicholas,25",
            "3,Lambert,Edward,\"\",18"
          )),
          String.join("\n", Files.readAllLines(filePath))
        ),
        () -> assertEquals(
          Messages.TABLE_HEAD
          + String.format(Messages.TABLE_ROW, 1, 15, "Dawson", "Augusta", "")
          + Messages.TABLE_HEAD
          + String.format(Messages.TABLE_ROW, 2, 25, "Ford", "Joseph", "Nicholas")
          + Messages.TABLE_HEAD
          + String.format(Messages.TABLE_ROW, 3, 18, "Lambert", "Edward", ""),
          testOut.toString()
        )
    );
  }

  @Test
  void read_fileIsNotEmpty_userExists_shouldDisplayUser() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    Main.main(new String[]{fileName, "read", "id=4"});

    assertEquals(
        Messages.TABLE_HEAD
        + String.format(Messages.TABLE_ROW, 4, 12, "Norton", "Robert", ""),
        testOut.toString()
    );
  }

  @Test
  void read_fileIsNotEmpty_userNotExists_shouldDisplayFailureMessage() throws IOException {
    Files.write(filePath, List.of("1,Murphy,Aileen,Deborah,38"), StandardCharsets.UTF_8);

    Main.main(new String[]{fileName, "read", "id=10"});

    assertEquals(
        String.format(Messages.USER_NOT_FOUND, 10, fileName),
        testErr.toString()
    );
  }

  @Test
  void read_fileIsEmpty_shouldDisplayFailureMessage() {
    Main.main(new String[]{fileName, "read", "id=10"});

    assertEquals(
        String.format(Messages.USER_NOT_FOUND, 10, fileName),
        testErr.toString()
    );
  }

  @Test
  void update_fileIsNotEmpty_userExists_shouldDisplayUser() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    Main.main(new String[]{fileName, "update",
        "second_name=Ford", "id=4", "age=25", "middle_name=Nicholas", "first_name=Joseph"});

    assertEquals(
        Messages.TABLE_HEAD
        + String.format(Messages.TABLE_ROW, 4, 25, "Ford", "Joseph", "Nicholas"),
        testOut.toString()
    );
  }

  @Test
  void update_fileIsNotEmpty_userNotExists_shouldDisplayFailureMessage() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    Main.main(new String[]{fileName, "update",
        "first_name=Robert", "second_name=Norton", "age=12", "id=10"});

    assertEquals(
        String.format(Messages.USER_NOT_FOUND, 10, fileName),
        testErr.toString()
    );
  }

  @Test
  void update_fileIsEmpty_shouldDisplayFailureMessage() {
    Main.main(new String[]{fileName, "update",
        "first_name=Robert", "second_name=Norton", "id=10", "age=12"});

    assertEquals(
        String.format(Messages.USER_NOT_FOUND, 10, fileName),
        testErr.toString()
    );
  }

  @Test
  void delete_fileIsNotEmpty_userExists_shouldDisplaySuccessMessage() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    Main.main(new String[]{fileName, "delete", "id=4"});

    assertEquals(
        String.format(Messages.DELETE_SUCCESS, 4, fileName),
        testOut.toString()
    );
  }

  @Test
  void delete_fileIsNotEmpty_userNotExists_shouldDisplayFailureMessage() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    Main.main(new String[]{fileName, "delete", "id=10"});

    assertEquals(
        String.format(Messages.USER_NOT_FOUND, 10, fileName),
        testErr.toString()
    );
  }

  @Test
  void delete_fileIsEmpty_shouldDisplayFailureMessage() {
    Main.main(new String[]{fileName, "delete", "id=10"});

    assertEquals(
        String.format(Messages.USER_NOT_FOUND, 10, fileName),
        testErr.toString()
    );
  }
}
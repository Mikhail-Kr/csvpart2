package com.htc;

public class Messages {
  /**
   * Шаблон для строки таблицы.
   */
  public static final String tableRow = "| %-3s | %-3s | %-15s | %-15s | %-15s |";

  /**
   * Заголовок таблицы.
   */
  public static final String tableHead = String.format(tableRow + "\n" + tableRow,
      "Id", "Age", "Second name", "First name", "Middle name",
      "---", "---", "-".repeat(15), "-".repeat(15), "-".repeat(15));

  /**
   * Сообщение о том, что указанный файл не существует.
   */
  public static final String fileNotExists = "File \"%s\" does not exists";

  /**
   * Сообщение о том, что указанная команда не существует.
   */
  public static final String commandNotExists = "Command \"%s\" does not exists";

  /**
   * Сообщение о том, что команда используется неправильно.
   */
  public static final String invalidArguments = "Invalid usage of command \"%s\"";

  /**
   * Сообщение о том, что запись о пользователе с указанным идентификатором не была найдена.
   */
  public static final String userNotFound = "User with id=%d does not found in \"%s\"";

  /**
   * Сообщение о том, что запись о пользователе была успешно удалена.
   */
  public static final String deleteSuccess = "User with id=%d was successfully deleted from \"%s\"";
}

package com.htc;

public class Messages {
  /**
   * Шаблон для строки таблицы.
   */
  public static final String TABLE_ROW = "| %-3s | %-3s | %-15s | %-15s | %-15s |";

  /**
   * Заголовок таблицы.
   */
  public static final String TABLE_HEAD = String.format(TABLE_ROW + "\n" + TABLE_ROW,
      "Id", "Age", "Second name", "First name", "Middle name",
      "---", "---", "-".repeat(15), "-".repeat(15), "-".repeat(15));

  /**
   * Сообщение о том, что указанный файл не существует.
   */
  public static final String FILE_NOT_EXISTS = "File \"%s\" does not exists";

  /**
   * Сообщение о том, что указанная команда не существует.
   */
  public static final String COMMAND_NOT_EXISTS = "Command \"%s\" does not exists";

  /**
   * Сообщение о том, что команда используется неправильно.
   */
  public static final String INVALID_ARGUMENTS = "Invalid usage of command \"%s\"";

  /**
   * Сообщение о том, что запись о пользователе с указанным идентификатором не была найдена.
   */
  public static final String USER_NOT_FOUND = "User with id=%d does not found in \"%s\"";

  /**
   * Сообщение о том, что запись о пользователе была успешно удалена.
   */
  public static final String DELETE_SUCCESS =
      "User with id=%d was successfully deleted from \"%s\"";
}

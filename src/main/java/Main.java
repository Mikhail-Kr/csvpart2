import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main {
  /**
   * Создаёт новую запись о пользователе {@code user}
   * в файле {@code filePath} формата CSV.
   *
   * @param filePath путь к файлу.
   * @param user пользователь.
   */
  public static void create(@NotNull String filePath, @NotNull User user) {}

  /**
   * Считывает из файла {@code filePath} формата CSV запись о пользователе
   * с идентификатором {@code id}. Возвращает {@code null}, если записи
   * с таким идентификатором не существует.
   *
   * @param filePath путь к файлу.
   * @param id идентификатор записи о пользователе.
   * @return Пользователь или {@code null}.
   */
  public static @Nullable User read(@NotNull String filePath, int id) {
    return null;
  }

  /**
   * Обновляет в файле {@code filePath} формата CSV информацию в записи
   * о пользователе с идентификатором {@code id}.
   *
   * @param filePath путь к файлу.
   * @param id идентификатор записи о пользователе.
   * @param user пользователь.
   * @throws java.util.NoSuchElementException запись о пользователе
   *     с идентификатором {@code id} не существует.
   */
  public static void update(@NotNull String filePath, int id, @NotNull User user) {}

  /**
   * Удаляет в файле {@code filePath} формата CSV запись
   * о пользователе с идентификатором {@code id}.
   *
   * @param filePath путь к файлу.
   * @param id идентификатор записи о пользователе.
   * @throws java.util.NoSuchElementException запись о пользователе
   *     с идентификатором {@code id} не существует.
   */
  public static void delete(@NotNull String filePath, int id) {}
}

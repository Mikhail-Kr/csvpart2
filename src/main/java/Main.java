import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main {
  /**
   * Создаёт новую запись о пользователе {@code user}
   * в файле {@code fileName} формата CSV.
   *
   * @param fileName имя файла.
   * @param user пользователь.
   */
  public static void create(@NotNull String fileName, @NotNull User user) {}

  /**
   * Считывает из файла {@code fileName} формата CSV запись о пользователе
   * с идентификатором {@code id}. Возвращает {@code null}, если записи
   * с таким идентификатором не существует.
   *
   * @param fileName имя файла.
   * @param id идентификатор записи о пользователе.
   * @return Пользователь или {@code null}.
   */
  public static @Nullable User read(@NotNull String fileName, int id) {
    return null;
  }

  /**
   * Обновляет в файле {@code fileName} формата CSV информацию в записи
   * о пользователе с идентификатором {@code id}.
   *
   * @param fileName имя файла.
   * @param id идентификатор записи о пользователе.
   * @param user пользователь.
   * @throws java.util.NoSuchElementException запись о пользователе
   *     с идентификатором {@code id} не существует.
   */
  public static void update(@NotNull String fileName, int id, @NotNull User user) {}

  /**
   * Удаляет в файле {@code fileName} формата CSV запись
   * о пользователе с идентификатором {@code id}.
   *
   * @param fileName имя файла.
   * @param id идентификатор записи о пользователе.
   * @throws java.util.NoSuchElementException запись о пользователе
   *     с идентификатором {@code id} не существует.
   */
  public static void delete(@NotNull String fileName, int id) {}
}

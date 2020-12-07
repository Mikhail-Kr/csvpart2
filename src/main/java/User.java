import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Пользователь.
 */
public class User implements Comparable<User> {
  /**
   * Возраст.
   */
  public int age;

  /**
   * Имя.
   */
  public String firstName;

  /**
   * Фамилия.
   */
  public String secondName;

  /**
   * Отчество.
   */
  @Nullable
  public String middleName;

  /**
   * Создаёт новый экземпляр класса.
   *
   * @param age возраст.
   * @param firstName имя.
   * @param secondName фамилия.
   * @param middleName отчество.
   */
  public User(int age, String secondName, String firstName, @Nullable String middleName) {
    this.age = age;
    this.firstName = firstName;
    this.secondName = secondName;
    this.middleName = middleName;
  }

  public User(int age, String secondName, String firstName) {
    this(age, secondName, firstName, null);
  }

  @Override
  public int hashCode() {
    return Objects.hash(age, firstName, secondName, middleName);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (!(object instanceof User)) {
      return false;
    }

    User user = (User) object;
    return age == user.age
      && firstName.equals(user.firstName)
      && secondName.equals(user.secondName)
      && Objects.equals(middleName, user.middleName);
  }

  @Override
  public int compareTo(@NotNull User user) {
    return secondName.compareTo(user.secondName);
  }

  @Override
  public String toString() {
    return String.format("User{%s %s%s, %d}",
      secondName,
      firstName,
      middleName != null ? " " + middleName : "",
      age);
  }
}

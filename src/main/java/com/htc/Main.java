package com.htc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main {
  /**
   * Получает данные пользователей {@code String[] args}.
   * Выводит в консоль данные о пользователе: id, Возраст, Фамилия, Имя, Отчество.
   * * @param  {@code String[] args} имя файла, команда: show, create, read, update, delete;
   * данные о пользователе: id, Возраст, Фамилия, Имя, Отчество.
   */
  public static void main(String[] args) {
    switch (args[1]) {
      case ("show"):
        break;
      case ("create"):
        String[] firstName = new String[2];
        String[] secondName = new String[2];
        String[] middleName = new String[2];
        String[] age = new String[2];
        if (args[0].equals("users.csv")) {
          if (args.length == 6) {
            for (int i = 2; i < args.length; i++) {
              if (args[i].contains("first")) {
                firstName = args[i].split("=");
              } else {
                if (args[i].contains("second")) {
                  secondName = args[i].split("=");
                } else {
                  if (args[i].contains("middle")) {
                    middleName = args[i].split("=");
                  } else {
                    if (args[i].contains("age")) {
                      age = args[i].split("=");
                    }
                  }
                }
              }
            }
            User user = new User(Integer.parseInt(age[1]), secondName[1],
                    firstName[1], middleName[1]);
            create(args[0], user);
            Map<Integer, String> inputUsers = new HashMap<>();
            inputUsers.put(1, secondName[1] + "," + firstName[1] + ","
                    + middleName[1] + "," + age[1]);
            Map<Integer, String> readUsers;
            readUsers = read("users.csv");
            printResult(inputUsers, readUsers);
          } else {
            if (args.length == 5) {
              for (int i = 2; i < args.length; i++) {
                if (args[i].contains("first")) {
                  firstName = args[i].split("=");
                } else {
                  if (args[i].contains("second")) {
                    secondName = args[i].split("=");
                  } else {
                    if (args[i].contains("age")) {
                      age = args[i].split("=");
                    }
                  }
                }
              }
              User user = new User(Integer.parseInt(age[1]), secondName[1], firstName[1]);
              create(args[0], user);
              Map<Integer, String> inputUsers = new HashMap<>();
              inputUsers.put(1, secondName[1] + "," + firstName[1] + ","
                      + (middleName[1] == null ? "\"\"" : middleName[1]) + "," + age[1]);
              Map<Integer, String> readUsers;
              readUsers = read("users.csv");
              printResult(inputUsers, readUsers);
            } else {
              System.err.printf(Messages.INVALID_ARGUMENTS, args[1]);
            }
          }
        } else {
          System.err.printf(Messages.FILE_NOT_EXISTS, args[0]);
        }
        break;
      case ("read"):
        break;
      case ("update"):
        break;
      case ("delete"):
        break;
      default:
        System.err.printf(Messages.COMMAND_NOT_EXISTS, args[1]);
        break;
    }
  }

  /**
   * Создаёт новую запись о пользователе {@code user}
   * в файле {@code filePath} формата CSV.
   *
   * @param filePath путь к файлу.
   * @param user пользователь.
   */
  public static void create(@NotNull String filePath, @NotNull User user) {
    Map<Integer, String> usersInfo = new HashMap<>();
    int id = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] userArr = users.split(",", 2);
        usersInfo.put(Integer.parseInt(userArr[0]), userArr[1]);
        if (id == Integer.parseInt(userArr[0])) {
          id++;
        }
      }
      usersInfo.put(id,
              user.secondName + "," + user.firstName + ","
                      + (user.middleName != null ? user.middleName : "\"\"") + "," + user.age);
    } catch (IOException e) {
      System.err.printf(Messages.FILE_NOT_EXISTS, filePath);
    }
    try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
      for (Map.Entry<Integer, String> item : usersInfo.entrySet()) {
        wr.write(item.getKey() + "," + item.getValue() + "\n");
      }
    } catch (IOException e) {
      System.err.printf(Messages.FILE_NOT_EXISTS, filePath);
    }
  }

  /**
   * Считывает из файла {@code filePath} формата CSV запись о пользователе
   * с идентификатором {@code id}. Возвращает {@code null}, если записи
   * с таким идентификатором не существует.
   *
   * @param filePath путь к файлу.
   * @param id       идентификатор записи о пользователе.
   * @return Пользователь или {@code null}.
   */
  public static @Nullable User read(@NotNull String filePath, int id) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] usersArr = users.split(",");
        if (id == Integer.parseInt(usersArr[0])) {
          return new User(Integer.parseInt(usersArr[4]),
                  usersArr[1],
                  usersArr[2],
                  usersArr[3].equals("\"\"") ? null : usersArr[3]);
        }
      }
      return null;
    } catch (IOException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
      return null;
    }
  }

  /** * Считывает из файла {@code filePath} формата CSV запись о пользователе.
   * Возвращает {@code Map<Integer, String>}.
   *
   * @param filePath путь к файлу.
   *
   * @return карту {@code Map<Integer, String>} пользователей в файле.
   */
  public static Map<Integer, String> read(@NotNull String filePath) {
    Map<Integer, String> usersInfo = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] userArr = users.split(",", 2);
        usersInfo.put(Integer.parseInt(userArr[0]), userArr[1]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return usersInfo;
  }

  /**
   * * Считывает из карты {@code Map<Integer, String>} данные о пользователях.
   * Возвращает String[] - key пользователей.
   * * @param  {@code Map<Integer, String> maps}.
     *
   * @return {@code String[]} key пользователей в файле.
   */


  public static String[] getKey(Map<Integer, String> maps) {
    String[] key = new String[maps.size()];
    Set entries = maps.entrySet();
    Iterator entriesIterator = entries.iterator();
    int i = 0;
    while (entriesIterator.hasNext()) {
      Map.Entry mapping = (Map.Entry) entriesIterator.next();
      key[i] = mapping.getKey().toString();
      i++;
    }
    return key;
  }

  /**
     * Считывает из карты {@code Map<Integer, String>} данные о пользователях.
     * Возвращает String[] - value пользователей.
     * * @param  {@code Map<Integer, String>} maps путь к файлу.
     *
     * @return {@code String[] value} пользователей в файле.
     */
  public static String[] getValue(Map<Integer, String> maps) {
    String[] value = new String[maps.size()];
    Set entries = maps.entrySet();
    Iterator entriesIterator = entries.iterator();
    int i = 0;
    while (entriesIterator.hasNext()) {
      Map.Entry mapping = (Map.Entry) entriesIterator.next();
      value[i] = mapping.getValue().toString();
      i++;
    }
    return value;
  }

  /**
   * Считывает из карты {@code Map<Integer, String>} данные о пользователях.
   * Выводит на экран данные о новых пользователях.
   * * @param  {@code Map<Integer, String>}  данные о новых пользователях,
   * данные о записанных пользователях.
   */
  public static void printResult(Map<Integer, String> inputUsers, Map<Integer, String> readUsers) {
    String[] valueInputUsers = getValue(inputUsers);
    String[] keyReadUsers = getKey(readUsers);
    String[] valueReadUsers = getValue(readUsers);
    for (int i = 0; i < valueReadUsers.length; i++) {
      if (valueInputUsers[0].equals(valueReadUsers[i])) {
        String[] print = valueReadUsers[i].split(",");
        System.out.println(Messages.TABLE_HEAD);
        System.out.printf((Messages.TABLE_ROW) + "%n",
                keyReadUsers[i], print[3], print[0], print[1],
                (print[2].equals("\"\"") ? "" : print[2]));
      }
    }
  }
}


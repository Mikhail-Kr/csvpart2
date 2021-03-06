package com.htc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
        if (args.length == 2) {
          Map<Integer, String> usersInfo;
          usersInfo = reader(args[0]);
          String[] keys;
          String[] value;
          String[] usersKey = getKey(usersInfo);
          String[] usersValue = getValue(usersInfo);
          System.out.printf(Messages.TABLE_HEAD);
          for (int i = 0; i < usersKey.length; i++) {
            keys = usersKey[i].split(",");
            value = usersValue[i].split(",");
            System.out.printf(Messages.TABLE_ROW,
                    keys[0], value[3], value[0], value[1],
                    (value[2].equals("\"\"") ? "" : value[2]));
          }
        } else {
          System.err.printf(Messages.INVALID_ARGUMENTS, args[1]);
        }
        break;
      case ("create"):
        if (args.length >= 5) {
          User user = initParam(args);
          create(args[0], user);
          printNewUsers(user, args[0]);
        }  else {
          System.err.printf(Messages.INVALID_ARGUMENTS, args[1]);
        }
        break;
      case ("read"):
        if (args[2].contains("id")) {
          String[] id = args[2].split("=");
          @Nullable User user = read(args[0], Integer.parseInt(id[1]));
          File file = new File(args[0]);
          if (user == null) {
            if (file.exists()) {
              System.err.printf(String.format(Messages.USER_NOT_FOUND,
                        Integer.parseInt(id[1]), args[0]));
            }
          } else {
            printNewUsers(user, args[0]);
          }
        } else {
          System.err.printf(Messages.INVALID_ARGUMENTS, args[1]);
        }
        break;
      case ("update"):
        if (args.length >= 5) {
          for (int i = 2; i < args.length; i++) {
            if (args[i].contains("id=")) {
              String[] id = args[i].split("=");
              User inputUser = initParam(args);
              @Nullable User user = read(args[0], Integer.parseInt(id[1]));
              File file = new File(args[0]);
              if (user == null) {
                if (file.exists()) {
                  System.err.printf(String.format(Messages.USER_NOT_FOUND,
                          Integer.parseInt(id[1]), args[0]));
                }
              } else {
                update(args[0], Integer.parseInt(id[1]), inputUser);
                printNewUsers(inputUser, args[0]);
              }
            }
          }
        } else {
          System.err.printf(Messages.INVALID_ARGUMENTS, args[1]);
        }
        break;
      case ("delete"):
        if (args[2].contains("id=")) {
          String[] id;
          id = args[2].split("=");
          Map<Integer, String> usersInfo;
          usersInfo = reader(args[0]);
          File file = new File(args[0]);
          if (usersInfo.size() != 0) {
            String[] keys;
            String key = "";
            keys = getKey(usersInfo);
            for (String s : keys) {
              if (s.equals(id[1])) {
                key = id[1];
                delete(args[0], Integer.parseInt(id[1]));
              }
            }
            if (!key.equals(id[1])) {
              System.err.printf(String.format(Messages.USER_NOT_FOUND,
                      Integer.parseInt(id[1]), args[0]));
            } else {
              System.out.printf(Messages.DELETE_SUCCESS, Integer.parseInt(id[1]), args[0]);
            }
          } else {
            if (file.exists()) {
              System.err.printf(String.format(Messages.USER_NOT_FOUND,
                      Integer.parseInt(id[1]), args[0]));
            }
          }
        } else {
          System.err.printf(Messages.INVALID_ARGUMENTS, args[1]);
        }
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
   * @param user     пользователь.
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
      try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
        for (Map.Entry<Integer, String> item : usersInfo.entrySet()) {
          wr.write(item.getKey() + "," + item.getValue() + "\n");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
        { }
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
    } catch (IOException e) {
      System.err.printf(Messages.FILE_NOT_EXISTS, filePath);
    }
    return null;
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
      System.err.printf(Messages.FILE_NOT_EXISTS, filePath);
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
   * Создает коллекцию пользователей из файла {@code filePath} формата CSV
   * идентификатором {@code id}. Возвращает коллекцию {@code Map<Integer, String>} данных
   * о существующих пользователях.
   *
   * @param filePath путь к файлу.
   * @throws java.util.NoSuchElementException запись о пользователе
   *                                          с идентификатором {@code id} не существует.
   */
  public static Map<Integer, String> reader(String filePath) {
    Map<Integer, String> usersInfo = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] userArr = users.split(",", 2);
        usersInfo.put(Integer.parseInt(userArr[0]), userArr[1]);
      }
    } catch (IOException e) {
      System.err.printf(Messages.FILE_NOT_EXISTS, filePath);
    }
    return usersInfo;
  }

  /**
   * Выводит в консоль данные о пользователе {@code User}
   * с идентификатором, прочитанном из файла.
   *
   * @param user данные о пользователе.
   * @param filepath путь к файлу.
   */
  public static void printNewUsers(User user, String filepath) {
    Map<Integer, String> inputUsers = new HashMap<>();
    inputUsers.put(1, user.secondName + "," + user.firstName + ","
            + (user.middleName == null ? "\"\"" : user.middleName) + "," + user.age);
    String[] valueInputUsers = getValue(inputUsers);
    Map<Integer, String> readUsers;
    readUsers = read(filepath);
    String[] keyReadUsers = getKey(readUsers);
    String[] valueReadUsers = getValue(readUsers);
    for (int i = 0; i < valueReadUsers.length; i++) {
      if (valueInputUsers[0].equals(valueReadUsers[i])) {
        String[] print = valueReadUsers[i].split(",");
        System.out.printf(Messages.TABLE_HEAD);
        System.out.printf(Messages.TABLE_ROW,
                keyReadUsers[i], print[3], print[0], print[1],
                (print[2].equals("\"\"") ? "" : print[2]));
      }
    }
  }

  /**
   * Преобразует входные параметры {@code args} и возвращает
   * данные о  пользователе {@code User}
   * на информацию пользователя {@code user}.
   *
   * @param args данные о пользователе.
   */
  public static User initParam(String[] args) {
    String[] firstName = new String[2];
    String[] secondName = new String[2];
    String[] middleName = new String[2];
    String[] age = new String[2];
    for (int i = 2; i < args.length; i++) {
      if (args[i].contains("first")) {
        firstName = args[i].split("=");
      } else {
        if (args[i].contains("second")) {
          secondName = args[i].split("=");
        } else {
          if (args[i].contains("age")) {
            age = args[i].split("=");
          } else {
            if (args[i].contains("middle")) {
              middleName = args[i].split("=");
            }
          }
        }
      }
    }
    return new User(Integer.parseInt(age[1]), secondName[1], firstName[1], middleName[1]);
  }

  /**
   * Заменяет в файле {@code filePath} формата CSV информацию
   * в записи о пользователе с идентификатором {@code id}
   * на информацию пользователя {@code user}.
   *
   * @param filePath путь к файлу.
   * @param id       идентификатор записи о пользователе.
   * @param user     пользователь.
   * @throws java.util.NoSuchElementException запись о пользователе
   *                                          с идентификатором {@code id} не существует.
   */
  public static void update(@NotNull String filePath, int id, @NotNull User user) {
    Map<Integer, String> usersInfo = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] userArr = users.split(",", 2);
        usersInfo.put(Integer.parseInt(userArr[0]), userArr[1]);
      }
    } catch (IOException e) {
      System.err.printf(Messages.FILE_NOT_EXISTS, filePath);
    }
    usersInfo.put(id,
            user.secondName + "," + user.firstName + ","
                    + (user.middleName != null ? user.middleName : "\"\"") + "," + user.age);
    try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
      for (Map.Entry<Integer, String> item : usersInfo.entrySet()) {
        wr.write(item.getKey() + "," + item.getValue() + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Удаляет в файле {@code filePath} формата CSV запись
   * о пользователе с идентификатором {@code id}.
   *
   * @param filePath путь к файлу.
   * @param id       идентификатор записи о пользователе.
   * @throws java.util.NoSuchElementException запись о пользователе
   *                                          с идентификатором {@code id} не существует.
   */
  public static void delete(@NotNull String filePath, int id) {
    Map<Integer, String> usersInfo = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] userArr = users.split(",", 2);
        usersInfo.put(Integer.parseInt(userArr[0]), userArr[1]);
      }
    } catch (IOException e) {
      System.err.printf(Messages.FILE_NOT_EXISTS, filePath);
    }
    usersInfo.remove(id);
    try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
      for (Map.Entry<Integer, String> item : usersInfo.entrySet()) {
        wr.write(item.getKey() + "," + item.getValue() + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
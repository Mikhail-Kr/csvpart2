package com.htc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
  private static Integer key;
  public static void main(String[] args) {
    switch (args[1]) {
      case ("show"):
        break;
      case ("create"):
        String[] first_name = new String[2];
        String[] second_name = new String[2];
        String[] middle_name = new String[2];
        String[] age = new String[2];
        key = 0;
        if (args[0] == "users.csv") {
          if (args.length == 6) {
            for (int i = 2; i < args.length; i++) {
              if (args[i].contains("first")) {
                first_name = args[i].split("=");
              } else {
                if (args[i].contains("second")) {
                  second_name = args[i].split("=");
                } else {
                  if (args[i].contains("middle")) {
                    middle_name = args[i].split("=");
                  } else {
                    if (args[i].contains("age")) {
                      age = args[i].split("=");
                    }
                  }
                }
              }
            }
            User user = new User(Integer.parseInt(age[1]), second_name[1], first_name[1], middle_name[1]);
            create(args[0], user);
            System.out.println(Messages.TABLE_HEAD);
            System.out.println(String.format(Messages.TABLE_ROW, key, (age[1]), second_name[1], first_name[1],
                    (middle_name[1] == null ? "" : middle_name[1])));
            //key++;
            /*try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
              int i = 0;
              String users;
              int lineCount = (int) Files.lines(Path.of("users.csv")).count();
              String[] key = new String[lineCount];
              String[] secondName = new String[lineCount];
              String[] firstName = new String[lineCount];
              String[] middleName = new String[lineCount];
              String[] agePrint = new String[lineCount];
              while ((users = br.readLine()) != null) {
                String[] userArr = users.split(",", 5);
                key[i] = userArr[0];
                agePrint[i] = userArr[4];
                secondName[i] = userArr[1];
                firstName[i] = userArr[2];
                middleName[i] = userArr[3];
                i++;
              }
              for(int j = 0; j < lineCount; j++) {
                System.out.println(Messages.TABLE_HEAD);
                System.out.println(String.format(Messages.TABLE_ROW, key[j], agePrint[j], secondName[j], firstName[j],
                        (middleName[j] == "\"\"" ? middleName[j] : "")));
              }
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }*/
          } else {
            if (args.length == 5) {
              key = 0;
              for (int i = 2; i < args.length; i++) {
                if (args[i].contains("first")) {
                  first_name = args[i].split("=");
                } else {
                  if (args[i].contains("second")) {
                    second_name = args[i].split("=");
                  } else {
                    if (args[i].contains("age")) {
                      age = args[i].split("=");
                    }
                  }
                }
              }
              User user = new User(Integer.parseInt(age[1]), second_name[1], first_name[1]);
              create(args[0], user);
              System.out.println(Messages.TABLE_HEAD);
              System.out.println(String.format(Messages.TABLE_ROW, key, (age[1]), second_name[1], first_name[1],
                      (middle_name[1] == null ? "" : middle_name[1])));
              //key++;
              /*try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                int i =0;
                String users;
                int lineCount = (int) Files.lines(Path.of("users.csv")).count();
                String[] key = new String[lineCount];
                String[] secondName = new String[lineCount];
                String[] firstName = new String[lineCount];
                String[] middleName = new String[lineCount];
                String[] agePrint = new String[lineCount];
                while ((users = br.readLine()) != null) {
                  String[] userArr = users.split(",", 5);
                  key[i] = userArr[0];
                  agePrint[i] = userArr[4];
                  secondName[i] = userArr[1];
                  firstName[i] = userArr[2];
                  middleName[i] = userArr[3];
                  i++;
                  *//*System.out.println(Messages.TABLE_HEAD);
                  System.out.println(String.format(Messages.TABLE_ROW, userArr[0], userArr[4], userArr[1], userArr[2],
                          (userArr[3] == "\"\"" ? userArr[3] : "")));*//*
                }
                for(int j = 0; j < lineCount; j++) {
                  System.out.println(Messages.TABLE_HEAD);
                  System.out.println(String.format(Messages.TABLE_ROW, key[j], agePrint[j], secondName[j], firstName[j],
                          (middleName[j] == "\"\"" ? middleName[j] : "")));
                }
              } catch (FileNotFoundException e) {
                e.printStackTrace();
              } catch (IOException e) {
                e.printStackTrace();
              }*/
            } else {
              System.err.print(String.format(Messages.INVALID_ARGUMENTS, args[1]));
            }
          }
        } else {
          System.err.print(String.format(Messages.FILE_NOT_EXISTS, args[0]));
        }
        break;
      case ("read"):
        break;
      case ("update"):
        break;
      case ("delete"):
        break;
      default:
        System.err.print(String.format(Messages.COMMAND_NOT_EXISTS, args[1]));
        break;
    }
  }

  /**
   * Создаёт новую запись о пользователе {@code user}
   * в файле {@code filePath} формата CSV.
   *  @param filePath путь к файлу.
   * @param user     пользователь.
   * @return
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
      e.printStackTrace();
    }
    try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
      for (Map.Entry<Integer, String> item : usersInfo.entrySet()) {
        wr.write(item.getKey() + "," + item.getValue() + "\n");
        key++;
      }
    } catch (IOException e) {
      e.printStackTrace();
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
}
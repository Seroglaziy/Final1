package finalProjectCore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UserDAO implements DAO<User> {
    private static UserDAO userDAO;
    private List<User> usersBase = new ArrayList<>();



    private UserDAO() {
        // если пустая, то  будет сообщение - "База пуста!"
        try(BufferedReader br = new BufferedReader(new FileReader("src/finalProjectCore/userBase.txt"))) {
            if (br.readLine()==null)System.out.println("База пуста!");


            else {
                Stream<String> streamFromFiles = Files.lines(Paths.get("src/finalProjectCore/userBase.txt"));
                streamFromFiles.forEach(line -> {
                    String fields[] = line.split(" ");

                    if (fields.length != 3) throw new RuntimeException("База юзеров повреждена!");
                    usersBase.add(new User(Long.parseLong(fields[0]), fields[1], fields[2]));
                });}
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("База юзеров повреждена");

        }


    }
    private void userBaseWriter(User user){ // сделал приватным
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("src/finalProjectCore/userBase.txt",true), "utf-8"))){
            bufferedWriter.write(String.valueOf(user.getId())+" ");
            bufferedWriter.write(user.getName()+" ");
            bufferedWriter.write(user.getPassword()+System.lineSeparator());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void userBaseDeleter(User user){ // сделал приватным
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader((
                        new FileInputStream("src/finalProjectCore/userBase.txt"))));
             BufferedWriter bufferedWriter = new BufferedWriter(
                     new OutputStreamWriter(
                             new FileOutputStream("src/finalProjectCore/userBase.txt")))){
            for (int i = 0; i <usersBase.size(); i++){
                String line = bufferedReader.readLine();
                bufferedWriter.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static UserDAO getUserDAO (){
        if (userDAO == null) userDAO = new UserDAO();
        return userDAO;
    }
    //метод добавляет в список и в txt документ
    //проверить на одинаковый ID!!!!
    @Override
    public boolean add(User user) {
        if (usersBase.contains(user)) {
            System.out.println("Пользователь уже существует!");
            return false;
        } else {
            try {
                userBaseWriter(user); //внес метод добавление в базу
                usersBase.add(user);
            } catch (NullPointerException e) {
                System.out.println("Внесите корректную информацию о новом пользователе!");
                return false;
            }

            return true;
        }
    }

    @Override
    public boolean edit(User user) {
        return false;

        //использовать сетеры
    }


    // метод удаляет из списка и из txt документа
    @Override
    public boolean remove(User user) {
        if (usersBase.contains(user)) {
            try{
                usersBase.remove(user);
                userBaseDeleter(user);
                return true;

            } catch (NullPointerException e){
                System.out.println("База пуста!");
            }
        }
        else System.out.println("Такого пользователя нет в базе данных!");
        return false;
    }

    public List<User> getUsersBase() {
        return usersBase;
    }

}
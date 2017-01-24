package finalProjectCore;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {


    static UserDAO userDAO = UserDAO.getUserDAO();
    static Hotel hotel = new Hotel(100, "5454", "bn");
    static List<Hotel> list = new ArrayList<>();





    //Регистрация юзера - проверяем есть ли в системе юзер с таким id, как у переданного на регистрацию,
    // если нет - добавляем в базу

    //21-01-2017 немного изменил метод
    public static boolean registerUser(User userToRegister) {
        if (userDAO.getUsersBase().stream().anyMatch(userFromBase -> userFromBase.getId() == userToRegister.getId()))
            return true;
        else {

            System.out.println("Пожалуйста, зарегистрируйтесь в базе!");
            return false;
        }
    }

    public static Collection<Hotel> findHotelByName(String name) throws Exception {

        if ( name == null || name.isEmpty()) {
            throw new Exception("Имя отеля должно быть заполнено!");
        }
        return list.stream()
                .filter(hotel -> hotel.getName()==name)
                .collect(Collectors.toList());
    }

    public static Collection<Hotel> findHotelByCity(String city) throws Exception {

        if ( city == null || city.isEmpty()) {
            throw new Exception(" Поле город отеля должно быть заполнено!");
        }
        return list.stream()
                .filter(hotel -> hotel.getCity()==city)
                .collect(Collectors.toList());
    }

    public static void bookRoom(long roomId, long userId, long hotelId) {
        try {
            User userToRegister = userDAO.getUsersBase().stream()
                    .filter(user -> user.getId() == userId)
                    .findAny()
                    .get();

            try {
                Hotel hotel = list.stream()
                        .filter(hotel1 -> hotel1.getId() == hotelId)
                        .findAny()
                        .get();
                try {
                    Room roomToReservation = hotel.getRooms().stream()
                            .filter(room -> room.getId() == roomId)
                            .findAny()
                            .get();

                    if (roomToReservation.getUserReserved() == null) {
                        roomToReservation.setUserReserved(userToRegister);
                        System.out.printf("Комната %s забронирована пользователем %s.\n", roomToReservation.toString(), userToRegister.toString());

                    } else
                        System.out.println("Комната недоступна для бронирования!");

                } catch (NoSuchElementException | NullPointerException e){
                    System.out.printf("Комната с ID %d не найдена.\n", roomId);
                }

            } catch (NoSuchElementException | NullPointerException e){
                System.out.printf("Отеля с ID %d нет базе.\n", hotelId);
            }

        } catch (NoSuchElementException e) {
            System.out.printf("Пользователя с ID %d нет базе. Просим зарегистрироваться!\n", userId);


        }
    }

    public static void cancelReservation(long roomId, long userId, long hotelId) {
        try {
            User userToRegister = userDAO.getUsersBase().stream()
                    .filter(user -> user.getId() == userId)
                    .findAny()
                    .get();

            try {
                Hotel hotel = list.stream()
                        .filter(hotel1 -> hotel1.getId() == hotelId)
                        .findAny()
                        .get();
                try {
                    Room roomToReservation = hotel.getRooms().stream()
                            .filter(room -> room.getId() == roomId)
                            .findAny()
                            .get();

                    if (roomToReservation.getUserReserved() != null) {
                        roomToReservation.setUserReserved(null);
                        System.out.printf("Резерв пользователя %s с комнаты %s снят.\n", userToRegister.toString(),roomToReservation.toString());

                    } else
                        System.out.println("Комната доступна для бронирования.");

                } catch (NoSuchElementException | NullPointerException e){
                    System.out.printf("Комната с ID %d не найдена.\n", roomId);
                }

            } catch (NoSuchElementException | NullPointerException e){
                System.out.printf("Отеля с ID %d нет базе.\n", hotelId);
            }

        } catch (NoSuchElementException e) {
            System.out.printf("Пользователя с ID %d нет базе. Просим зарегистрироваться!\n", userId);


        }
    }
}
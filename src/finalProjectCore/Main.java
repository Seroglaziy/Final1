package finalProjectCore;

public class Main {

    public static void main(String[] args) {

        HotelDAO hotelDAO = HotelDAO.getHotelDAO();

        hotelDAO.add(new Hotel(1, "a", "a1"));
        hotelDAO.add(new Hotel(2, "b", "b2"));
        hotelDAO.add(new Hotel(3, "c", "c3"));

        System.out.println();
        hotelDAO.getHotelList().stream().forEach(System.out::println);
        System.out.println("-----------------------------");

        UserDAO userDAO = UserDAO.getUserDAO();
       userDAO.add(new User(10, "SL", "qwe"));
        System.out.println();
        userDAO.getUsersBase().stream().forEach(System.out::println);

        RoomDAO roomDAO = RoomDAO.getRoomDAO();

        roomDAO.getRoomList().stream().forEach(System.out::println);


    }
}

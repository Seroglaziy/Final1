package finalProjectCore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RoomDAO implements DAO<Room>{
    private static RoomDAO roomDAO;
    private List<Room> roomList = new ArrayList<>();

    private RoomDAO(){
        HotelDAO hotelDAO = HotelDAO.getHotelDAO();
        UserDAO userDAO = UserDAO.getUserDAO();
        try {
            Stream<String> streamFromFiles = Files.lines(Paths.get("src/finalProjectCore/roomBase.txt"));
            streamFromFiles.forEach(line -> {
                String fields[] = line.split("@");
                if (roomList.stream().anyMatch(room -> room.getId() == Long.parseLong(fields[0]))) throw new RuntimeException("База комнат повреждена");
                if (fields.length != 5 && fields.length != 4) throw new RuntimeException("База комнат повреждена");
                Hotel hotel = null;
                User user = null;
                for (int i = 0; i < hotelDAO.getHotelList().size(); i++) {
                    if (hotelDAO.getHotelList().get(i).getId() == Long.parseLong(fields[3]) ){
                        hotel = hotelDAO.getHotelList().get(i);
                        break;
                    }
                }
                if (fields.length == 5) {
                    for (int i = 0; i < userDAO.getUsersBase().size(); i++) {
                        if (userDAO.getUsersBase().get(i).getId() == Long.parseLong(fields[4]) ) {
                            user = userDAO.getUsersBase().get(i);
                            break;
                        }
                    }
                }

                if (hotel != null ) {
                    roomList.add(new Room(Long.parseLong(fields[0]), Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), hotel, user));
                }
            });
            streamFromFiles.close();
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("База юзеров повреждена");

        }
    }

    public static RoomDAO getRoomDAO (){
        if (roomDAO == null) roomDAO = new RoomDAO();
        return roomDAO;
    }

    @Override
    public boolean add(Room room) {
        return false;
    }

    @Override
    public boolean edit(Room room) {
        return false;
    }

    @Override
    public boolean remove(Room room) {
        return false;
    }

    public List<Room> getRoomList() {
        return roomList;
    }
}

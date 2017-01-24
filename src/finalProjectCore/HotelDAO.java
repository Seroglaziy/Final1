package finalProjectCore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HotelDAO implements DAO<Hotel> {

    private static HotelDAO hotelDAO;
    private List<Hotel> hotelList = new ArrayList<>();


    private HotelDAO() {
        try {
            Stream<String> streamFromFiles = Files.lines(Paths.get("src/finalProjectCore/hotelBase.txt"));
            streamFromFiles.forEach(line -> {
                String fields[] = line.split("@");
                if (fields.length != 3) throw new RuntimeException("База отелей повреждена");
                if (hotelList.stream().anyMatch(hotel -> hotel.getId() == Long.parseLong(fields[0]))) throw new RuntimeException("База отелей повреждена");
                hotelList.add(new Hotel(Long.parseLong(fields[0]), fields[1], fields[2]));

            });
            streamFromFiles.close();
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("База отелей повреждена");
        }

    }

    public static HotelDAO getHotelDAO (){
        if (hotelDAO == null) hotelDAO = new HotelDAO();
        return hotelDAO;
    }


    @Override
    public boolean add(Hotel hotel) {
        if (hotelList.stream().anyMatch(hotelFromBase -> hotelFromBase.getId() == hotel.getId())) return false;

        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("src/finalProjectCore/hotelBase.txt",true), "utf-8"))){
            bufferedWriter.write(String.valueOf(hotel.getId())+"@");
            bufferedWriter.write(hotel.getName()+"@");
            bufferedWriter.write(hotel.getCity()+System.lineSeparator());
            bufferedWriter.flush();
            bufferedWriter.close();
            hotelList.add(hotel);

        } catch (IOException e) {
            System.out.println("Ошибка при записи базы отелей");

            return false;
        }
        return true;
    }

    @Override
    public boolean edit(Hotel hotel) {
        Hotel oldHotel;
        if (!hotelList.contains(hotel)){
            System.out.println("Отель не найден");
            return false; }
        oldHotel = hotelDAO.getHotelList().stream().filter(hotel1 -> hotel1.getId() == hotel.getId()).findAny().get();
        oldHotel.setCity(hotel.getCity());
        oldHotel.setName(hotel.getName());
        writeHotelBase();

//        add(ho);
//        RoomDAO roomDAO = RoomDAO.getRoomDAO();
//        for (int i = 0; i < roomDAO.getRoomList().size(); i++) {
//            if (roomDAO.getRoomList().get(i).getHotel().equals(oldHotel)) {
//                roomDAO.edit(roomDAO.getRoomList().get(i), new Room(roomDAO.getRoomList().get(i).getId(),
//                        roomDAO.getRoomList().get(i).getPrice(), roomDAO.getRoomList().get(i).getPersons(), ho));
//            }
//        }
//        remove(oldHotel);
//
        return true;
    }

    @Override
    public boolean remove(Hotel hotel) {
        if (!hotelList.contains(hotel)){
            System.out.println("Отель не найден");
            return false; }

        RoomDAO roomDAO = RoomDAO.getRoomDAO();
        for (int i = 0; i < roomDAO.getRoomList().size(); i++) {
            if (roomDAO.getRoomList().get(i).getHotel().equals(hotel)) {
                roomDAO.remove(roomDAO.getRoomList().get(i));
            }
        }
        hotelList.remove(hotel);
        writeHotelBase();

//        StringBuilder stringBuilder = new StringBuilder();
//            hotelList.stream().forEach(hotelFromList -> stringBuilder.append(hotelFromList.getId() + "@"
//                    + hotelFromList.getName() + "@" + hotelFromList.getCity() + "\n"));
//
//            try {
//                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/finalProjectCore/hotelBase.txt"));
//                bufferedWriter.append(stringBuilder);
//                bufferedWriter.flush();
//                bufferedWriter.close();
//
//            } catch (IOException e) {
//                System.out.println("Не удалось сохранить базу отелей");
//                return false;
//            }
        return true;
    }

    public boolean writeHotelBase (){
        StringBuilder stringBuilder = new StringBuilder();
        hotelList.stream().forEach(hotelFromList -> stringBuilder.append(hotelFromList.getId() + "@"
                + hotelFromList.getName() + "@" + hotelFromList.getCity() + "\n"));

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/finalProjectCore/hotelBase.txt"));
            bufferedWriter.append(stringBuilder);
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            System.out.println("Не удалось сохранить базу отелей");
            return false;
        }
        return true;

    }

    public List<Hotel> getHotelList() {
        return hotelList;
    }


}

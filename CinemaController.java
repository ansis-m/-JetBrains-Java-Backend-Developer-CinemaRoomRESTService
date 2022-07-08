package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class CinemaController {

    Seats seats;
    List<String> booked;
    int current_income;
    int available;

    public CinemaController() {

        current_income = 0;
        seats = new Seats();
        booked = new ArrayList<String>();
        available = 81;

        for(int i = 1; i <= seats.total_rows; i++) {
            for(int j = 1; j <= seats.total_columns; j++) {
                seats.addSeat(i, j);
                booked.add("not booked");
            }
        }
    }

    @GetMapping("/seats")
    public Seats getSeats() {

        return seats;
    }

    @PostMapping ("/purchase")
    public ResponseEntity purchase(@RequestBody RowAndColumn rowAndColumn) {

        for(int i = 0; i < seats.available_seats.size(); i++){
            if (seats.available_seats.get(i).getColumn() == rowAndColumn.getColumn() &&
                    seats.available_seats.get(i).getRow() == rowAndColumn.getRow()) {
                if(booked.get(i).equals("not booked")) {
                    String uuid = UUID.randomUUID().toString();
                    booked.set(i, uuid);
                    current_income += rowAndColumn.getRow() > 4? 8 : 10;
                    available--;
                    return new ResponseEntity(Map.of("token", uuid, "ticket", seats.available_seats.get(i)), HttpStatus.OK);
                }
                else
                    return new ResponseEntity(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);

            }
        }

        return new ResponseEntity(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping ("/return")
    public ResponseEntity returnTicket(@RequestBody Map<String, String> token) {

        for(int i = 0; i < booked.size(); i++) {
            if(booked.get(i).equals(token.getOrDefault("token", "def"))) {
                booked.set(i, "not booked");
                current_income -= i > 36? 8 : 10;
                available++;
                return new ResponseEntity(Map.of("returned_ticket", seats.available_seats.get(i)), HttpStatus.OK);
            }
        }

        return new ResponseEntity(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping ("/stats")
    public ResponseEntity StatsNoPassword(@RequestParam(value = "password", required = false) String password) {

        if(password == null){
            return new ResponseEntity(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
        }

        if (password.equals("super_secret"))
            return new ResponseEntity(Map.of("current_income", current_income, "number_of_available_seats", available, "number_of_purchased_tickets", 81 - available ), HttpStatus.OK);


        return new ResponseEntity(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
    }

}

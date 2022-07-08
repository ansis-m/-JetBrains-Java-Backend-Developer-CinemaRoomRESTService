package cinema;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Seats {

    int total_rows;
    int total_columns;
    List<Seat> available_seats;

    Seats(){
        this.total_rows = 9;
        this.total_columns = 9;
        available_seats = new ArrayList<Seat>();
    }

    public void addSeat(int row, int column) {
        available_seats.add(new Seat(row, column));
    }


    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotal_columns() {
        return total_columns;
    }

    public void setTotal_columns(int total_columns) {
        this.total_columns = total_columns;
    }

    public List<Seat> getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(List<Seat> available_seats) {
        this.available_seats = available_seats;
    }
}

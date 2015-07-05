package hello;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Customer {
    private long id;
    private String firstName;
    private String lastName;
}

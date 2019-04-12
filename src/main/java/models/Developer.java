package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Developer {
    private int id;
    private String name;
    private int age;
    private double salary;
}

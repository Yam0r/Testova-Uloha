package user.testovauloha.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "obec")
public class Obec{
    @Id
    private Long id;

    private String name;
}

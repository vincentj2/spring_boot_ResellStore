package JPAstore.ResellStore.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter
@Setter
public class Shoes extends Item {

    private String brand;
    private String collaboration;
}

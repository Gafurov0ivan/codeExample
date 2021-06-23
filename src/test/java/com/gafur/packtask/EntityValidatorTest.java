package com.gafur.packtask;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gafur.packtask.entity.BackpackSample;
import com.gafur.packtask.entity.Item;
import com.gafur.packtask.util.EntityValidator;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EntityValidatorTest {

    @Autowired
    private EntityValidator validator;

    @Test
    public void negativeMaxWeight() {
        BackpackSample sample = new BackpackSample(-1, List.of());
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> validator.validate(sample));
        assertThat(
                ex.getMessage(),
                equalTo("maxWeight: must be greater than or equal to 0")
        );
    }

    @Test
    public void greater100MaxWeight() {
        BackpackSample sample = new BackpackSample(101, List.of());
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> validator.validate(sample));
        assertThat(
                ex.getMessage(),
                equalTo("maxWeight: must be less than or equal to 100")
        );
    }

    @Test
    public void negativeId() {
        Item item = new Item(-1, 1d, 1d);
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> validator.validate(item));
        assertThat(
                ex.getMessage(),
                equalTo("id: must be greater than or equal to 1")
        );
    }

    @Test
    public void negativeItemWeight() {
        Item item = new Item(1, -1d, 1d);
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> validator.validate(item));
        assertThat(
                ex.getMessage(),
                equalTo("weight: must be greater than or equal to 0")
        );
    }

    @Test
    public void greater100ItemWeight() {
        Item item = new Item(1, 100.01d, 1d);
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> validator.validate(item));
        assertThat(
                ex.getMessage(),
                equalTo("weight: must be less than or equal to 100")
        );
    }

    @Test
    public void negativeItemCost() {
        Item item = new Item(1, 1d, -1d);
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> validator.validate(item));
        assertThat(
                ex.getMessage(),
                equalTo("cost: must be greater than or equal to 0")
        );
    }

    @Test
    public void greater100ItemCost() {
        Item item = new Item(1, 1d, 101.01d);
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> validator.validate(item));
        assertThat(
                ex.getMessage(),
                equalTo("cost: must be less than or equal to 100")
        );
    }

    @Test
    public void multipleConstraintExceptions() {
        BackpackSample sample = new BackpackSample(1, List.of(new Item(-1, -1d, -1d)));
        Item item = new Item(-1, -1d, -1d);
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> validator.validate(item));
        assertThat(ex.getMessage(), containsString("id: must be greater than or equal to 1"));
        assertThat(ex.getMessage(), containsString("weight: must be greater than or equal to 0"));
        assertThat(ex.getMessage(), containsString("cost: must be greater than or equal to 0"));
    }
}

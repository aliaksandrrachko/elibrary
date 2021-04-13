package by.it.academy.grodno.elibrary.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AEntityDto<K extends Number> implements Serializable {

    protected K id;
}

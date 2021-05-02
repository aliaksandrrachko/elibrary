package by.it.academy.grodno.elibrary.api.dto.users.socialresponses;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Picture implements Serializable {

    private PictureData data;
}

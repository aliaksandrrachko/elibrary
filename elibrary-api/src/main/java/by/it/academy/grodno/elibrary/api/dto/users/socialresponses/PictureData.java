package by.it.academy.grodno.elibrary.api.dto.users.socialresponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PictureData implements Serializable {

    private int height;
    private int width;
    @JsonProperty("is_silhouette")
    private boolean isSilhouette;
    private String url;
}

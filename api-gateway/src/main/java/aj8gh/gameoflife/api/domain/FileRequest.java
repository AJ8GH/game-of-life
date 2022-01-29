package aj8gh.gameoflife.api.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class FileRequest {
    String fileName;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public FileRequest(@JsonProperty("fileName") String fileName) {
        this.fileName = fileName;
    }
}

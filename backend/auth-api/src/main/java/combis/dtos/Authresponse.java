package combis.dtos;

import lombok.Data;

@Data
public class Authresponse {
    private String token;
    private user user;
}

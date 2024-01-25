package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data

public class MemberResponse {
    private String username;
    private int age;

    public MemberResponse(String username, int age) {
        this.username = username;
        this.age = age;
    }


    @Data
    public static class MemberRequest{
        private String username;

        public MemberRequest(String username) {
            this.username = username;
        }
    }

}

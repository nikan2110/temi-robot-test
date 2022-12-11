package meuhedet.com.temitestappl.dto;

import androidx.annotation.NonNull;

public class ResponseCameraDto {

    boolean verified;
    String userName;
    String message;

    public boolean verified() {
        return verified;
    }

    public String getUserName() {
        return userName;
    }


    public String getMessage() {
        return message;
    }


    @NonNull
    @Override
    public String toString() {
        return "ResponseCameraDto{" +
                "verified=" + verified +
                ", userName='" + userName + '\'' +
                '}';
    }

    public ResponseCameraDto() {


    }
}

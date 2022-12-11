package meuhedet.com.temitestappl.dto;

import androidx.annotation.NonNull;

public class ResponseUploadDto {

    String message;

    public String getMessage() {
        return message;
    }

    public ResponseUploadDto() {
    }

    @NonNull
    @Override
    public String toString() {
        return "ResponseUploadDto{" +
                "message='" + message + '\'' +
                '}';
    }
}

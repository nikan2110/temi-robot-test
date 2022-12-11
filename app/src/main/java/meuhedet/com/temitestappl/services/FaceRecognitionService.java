package meuhedet.com.temitestappl.services;

import meuhedet.com.temitestappl.dto.ResponseCameraDto;
import meuhedet.com.temitestappl.dto.ResponseUploadDto;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FaceRecognitionService {

    @Multipart
    @POST("upload")
    Call<ResponseUploadDto> uploadPhotoToDataBase(@Part("user_id") RequestBody userId, @Part("user_name") RequestBody userName, @Part MultipartBody.Part file);


    @Multipart
    @POST("camera")
    Call<ResponseCameraDto> uploadPhotoToCameraFolder(@Part MultipartBody.Part file);
}

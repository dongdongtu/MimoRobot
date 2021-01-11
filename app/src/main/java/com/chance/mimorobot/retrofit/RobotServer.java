package com.chance.mimorobot.retrofit;

import com.chance.mimorobot.model.AndroidUpdate;
import com.chance.mimorobot.model.BaseResponseModel;
import com.chance.mimorobot.model.ExplainModel;
import com.chance.mimorobot.model.FaceListModel;
import com.chance.mimorobot.model.FaceRequestModel;
import com.chance.mimorobot.model.GetSemanticModel;
import com.chance.mimorobot.model.IDCardModel;
import com.chance.mimorobot.model.InitModel;
import com.chance.mimorobot.model.InitRequest;
import com.chance.mimorobot.model.LineUpRequestModel;
import com.chance.mimorobot.model.MapListResponse;
import com.chance.mimorobot.model.MapPointListResponse;
import com.chance.mimorobot.model.MapPointRequest;
import com.chance.mimorobot.model.MapPointResponse;
import com.chance.mimorobot.model.MapResponse;
import com.chance.mimorobot.model.SemanticModel;
import com.chance.mimorobot.model.TempRequestModel;
import com.chance.mimorobot.retrofit.model.AIUIResponse;
import com.chance.mimorobot.retrofit.model.BaseModel;
import com.chance.mimorobot.retrofit.model.GetActionListResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface RobotServer {
    /**
     * AIUI WebApi请求接口
     *
     * @param body
     * @return
     */
    @POST("https://openapi.xfyun.cn/v2/aiui")
    Observable<AIUIResponse> requestWebAIUI(@Body RequestBody body);


    /**
     * 语义分析借口
     */
    @POST("api/SemanticNnalysis/Post")
    Observable<SemanticModel> getSemantic(@Body GetSemanticModel getSemanticModel);

    /**
     * 初始化接口
     */
    @POST("api/RobotInit/Init")
    Observable<InitModel> initRobot(@Body InitRequest initRequest);

    /**
     * 上传身份证
     */
    @POST("api/RobotIDCardRecord/UploadRecord")
    Observable<BaseResponseModel> postIDcard(@Body IDCardModel idCardModel);

    /**
     * 获取人脸更新
     */
    @GET("api/RobotFace/GetFace")
    Observable<FaceListModel> getFace(@Query("robotno") String robotno);


    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


    /**
     * 更新人脸完成通知
     */
    @GET("api/RobotFace/UpDateAllFace")
    Observable<BaseResponseModel>  upDateAllFace(@Query("robotno")String robotno);
    /**
     * 获取全部人脸
     */
    @GET("api/RobotFace/GetAllFace")
    Observable<FaceListModel> getAllFace(@Query("robotno") String robotno);


    /**
     * Android 更新
     */
    @GET("api/RobotInit/GetNewAndroidVesion")
    Observable<AndroidUpdate> getNewAndroidVersion(@Query("RobotNo")String RobotNo,@Query("IsTest")int IsTest);


    /**
     * 获取讲解路径
     */
    @GET("api/RobotExplainPath/GetPathByRobotNo")
    Observable<ExplainModel> getExplainPath(@Query("RobotNo")String RobotNo);


    /**
     * 上传地图
     * @param
     * @return
     */
    @Multipart
    @POST("api/RobotMap/Upload")
    Observable<MapResponse> postMap(@Part("RobotNo") RequestBody RobotNo, @Part("MapName") RequestBody MapName
            , @Part("MapDes") RequestBody MapDes, @Part("MapHeight") RequestBody MapHeight, @Part("MapWidth") RequestBody MapWidth
            , @Part("MapOriginX") RequestBody MapOriginX, @Part("MapOriginY") RequestBody MapOriginY
            , @Part("ResolutionX") RequestBody ResolutionX, @Part("ResolutionY") RequestBody ResolutionY
            , @Part() List<MultipartBody.Part> parts);


    /**
     * 获取地图列表
     * @param RobotNo
     * @param Mapid
     * @return
     */
    @GET("api/RobotMap/GetMap")
    Observable<MapListResponse> getMapList(@Query("RobotNo")String RobotNo, @Query("Mapid")int Mapid);

    /**
     * 获取地图点列表
     * @param Mapid
     * @return
     */
    @GET("api/RobotMapPoint/GetPointByMap")
    Observable<MapPointListResponse> getMapPointList(@Query("Mapid")int Mapid);


    /**
     * 上传地图点
     * @param mapPointRequest
     * @return
     */
    @POST("api/RobotMapPoint/Upload")
    Observable<MapPointResponse> postMapPoint(@Body MapPointRequest mapPointRequest);

    /**
     * 删除地图点
     */
    @DELETE("api/RobotMapPoint/DeletePoint")
    Observable<BaseResponseModel> deletePoint(@Query("RobotNo")String RobotNo,@Query("Mapid")int Mapid,@Query("Pointid")int Pointid);


    /**
     * 删除地图
     * @param RobotNo
     * @param Mapid
     * @return
     */
    @DELETE("api/RobotMap/DeleteMap")
    Observable<BaseResponseModel> deleteMap(@Query("RobotNo")String RobotNo,@Query("Mapid")int Mapid);


    /**
     * 设置地图
     */
    @GET("api/RobotSet/SetMap")
    Observable<BaseResponseModel> setMap(@Query("RobotNo")String RobotNo,@Query("MapID")int MapID);


    /**
     * 上传温度
     */
    @POST("api/RobotTemperature/Upload")
    Observable<BaseResponseModel> uploadTemp(@Body TempRequestModel tempRequestModel);


    /**
     * 上传人脸识别
     */
    @POST("api/RobotClock/Upload")
    Observable<BaseResponseModel> upLoadFace(@Body FaceRequestModel faceRequestModel);


    /**
     * 上传叫号
     */
    @POST("api/RobotQueueRecord/Upload")
    Observable<BaseResponseModel> uploadLineUp(@Body LineUpRequestModel lineUpRequestModel);


    /**
     * 获取地图点动作列表
     * @param RobotNo
     * @return
     */
    @GET("api/RobotPad/GetBlindingListOnlyMap")
    Observable<GetActionListResponse> getActionList(@Query("RobotNo")String  RobotNo);

    /**
     * 执行动作
     * @param actionid
     * @param yituid
     * @param type
     * @param RobotNo
     * @return
     */
    @GET("api/RobotPad/ExcuteActions")
    Observable<BaseModel> doAction(@Query("actionid")int actionid, @Query("yituid")int yituid, @Query("type")int type, @Query("RobotNo")String RobotNo);


    /**
     * 获取户型列表
     */
    @GET("api/RobotPad/GetBlindingListOnlyHuXing")
    Observable<GetActionListResponse> getRoomList(@Query("RobotNo")String  RobotNo);

}

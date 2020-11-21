package com.yapp.picon.presentation.post

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yapp.picon.R
import com.yapp.picon.data.model.Address
import com.yapp.picon.data.model.Coordinate
import com.yapp.picon.domain.usecase.CreatePostUseCase
import com.yapp.picon.domain.usecase.LoadAccessTokenUseCase
import com.yapp.picon.domain.usecase.UploadImageUseCase
import com.yapp.picon.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class PostViewModel(
    private val loadAccessTokenUseCase: LoadAccessTokenUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val createPostUseCase: CreatePostUseCase
) : BaseViewModel() {

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> get() = _toastMsg

    private val _pictureUris = MutableLiveData<List<Uri>>()
    val pictureUris: LiveData<List<Uri>> get() = _pictureUris

    private val _emotions = MutableLiveData<List<Map<String, String>>>()
    val emotions: LiveData<List<Map<String, String>>> get() = _emotions

    private val _lat = MutableLiveData<Double>()
    private val _lng = MutableLiveData<Double>()
    private val _selectedEmotion = MutableLiveData<String>()
    private val _contentResolver = MutableLiveData<ContentResolver>()

    init {
        _pictureUris.value = mutableListOf()

        //todo emotions room database로 만들고 변경하기
        _emotions.value = listOf(
            mapOf(
                "color" to R.color.soft_blue.toString(),
                "text" to "새벽 3",
                "bg" to R.drawable.ic_custom_circle_soft_blue.toString()
            ),
            mapOf(
                "color" to R.color.cornflower.toString(),
                "text" to "구름없는 하늘",
                "bg" to R.drawable.ic_custom_circle_cornflower.toString(),
            ),
            mapOf(
                "color" to R.color.bluegrey.toString(),
                "text" to "아침 이",
                "bg" to R.drawable.ic_custom_circle_bluegrey.toString(),
            ),
            mapOf(
                "color" to R.color.very_light_brown.toString(),
                "text" to "창문 너머 노",
                "bg" to R.drawable.ic_custom_circle_very_light_brown.toString(),
            ),
            mapOf(
                "color" to R.color.warm_grey.toString(),
                "text" to "잔잔한 ",
                "bg" to R.drawable.ic_custom_circle_warm_grey.toString(),
            ),
        )
    }

    private fun showToast(msg: String) {
        _toastMsg.value = msg
    }

    fun setPictureUris(pictureUris: List<Uri>) {
        _pictureUris.value = pictureUris
    }

    fun setLatLng(lat: Double, lng: Double) {
        _lat.value = lat
        _lng.value = lng
    }

    fun setContentResolver(contentResolver: ContentResolver) {
        _contentResolver.value = contentResolver
    }

    fun deletePictureUri(uri: Uri) {
        _pictureUris.value = _pictureUris.value?.minus(uri)
    }

    fun setSelectedEmotion(emtion: String) {
        _selectedEmotion.value = emtion
    }

    private fun getPath(uri: Uri): String? {
        _contentResolver.value?.let {
            it.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)?.run {
                val columnIndex = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                moveToFirst()
                val str = getString(columnIndex)
                close()
                return str
            }
            return null
        }
        return null
    }

    fun uploadImage() {
        viewModelScope.launch {
            loadAccessTokenUseCase().collect { accessToken ->
                _pictureUris.value?.let {
                    val arrBody: MutableList<MultipartBody.Part> = ArrayList()

                    for (uri in it) {
                        getPath(uri)?.let { strUri ->
                            File(strUri).run {

                                val mapRequestBody = LinkedHashMap<String, RequestBody>()
                                val requestBody =
                                    this.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                                mapRequestBody["images\"; filename=\"$name"] = requestBody
                                val body = MultipartBody.Part.createFormData(
                                    "fileName",
                                    name,
                                    requestBody
                                )

                                arrBody.add(body)
                            }
                        }
                    }
                    Log.e("aa12", "이미지 전송시작")
                    uploadImageUseCase(accessToken, arrBody).let { response ->
                        Log.e("aa12", "이미지 전송완료")
                        Log.e("aa12", "${response.string()}")
//                    if (response.status == 200) {
                        createPost(
                            listOf(
                                "https://news.samsungdisplay.com/wp-content/uploads/2018/08/2.png"
                            )
                        )
//                    } else {
//                        showToast("이미지를 업로드하는 중 오류가 발생했습니다.")
//                    }
                    }
                    Log.e("aa12", "이미지 전송완료 2")
                }
            }
        }
    }

//    fun requestPost() {
//        viewModelScope.launch {
//            try {
//                NetworkModule.yappApi.requestPost("1").let {
//                    showToast("감정 : ${it.emotion} 메모 : ${it.memo}")
//                }
//            } catch (e: Exception) {
//                showToast("통신 오류")
//            }
//        }
//    }

    private fun createPost(imageUrls: List<String>?) {
        viewModelScope.launch {
            loadAccessTokenUseCase().collect { accessToken ->
                createPostUseCase(
                    accessToken,
                    Coordinate(
                        1.5,
                        1.5
                    ),
                    imageUrls,
                    Address(
                        "주소",
                        "시",
                        "도",
                        "구"
                    ),
                    memo = "memo",
                    emotion = "BLUE_GRAY"
                ).let {
                    Log.e("aa12", it.string())
                }
            }
        }
    }
}
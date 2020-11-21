package com.yapp.picon.presentation.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.yapp.picon.BR
import com.yapp.picon.R
import com.yapp.picon.databinding.MapActivityBinding
import com.yapp.picon.helper.LocationHelper
import com.yapp.picon.helper.RequestCodeSet
import com.yapp.picon.presentation.base.BaseMapActivity
import com.yapp.picon.presentation.login.LoginActivity
import com.yapp.picon.presentation.nav.NavActivity
import com.yapp.picon.presentation.nav.NavTypeStringSet
import com.yapp.picon.presentation.post.PostActivity
import com.yapp.picon.presentation.search.SearchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapActivity : BaseMapActivity<MapActivityBinding, MapViewModel>(
    R.layout.map_activity,
    R.id.map_frame
), NavigationView.OnNavigationItemSelectedListener {

    override val vm: MapViewModel by viewModel()

    private lateinit var naverMap: NaverMap

    private val currentLocationMarker = Marker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        todo
            1. 시작 시 지도 카메라 한반도 전체 보여주기
            2. 전체 포스트 조회 및 마커로 뿌려주기
            3. 해당 마커는 클러스터링 라이브러리를 이용한다.
            4. 마커 클릭 시 엑티비티 이동
            5. 다시 화면으로 돌아왔을 경우 전체 포스트 다시 조회
         */

        setToolBar()
        setOnListeners()

        vm.loadAccessToken()
    }

    override fun initViewModel() {
        binding.setVariable(BR.mapVM, vm)

        val toastMsgObserver = Observer<String> {
            showToast(it)
        }

        vm.toastMsg.observe(this, toastMsgObserver)
    }

    private fun setToolBar() {
        setSupportActionBar(binding.mapToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setOnListeners() {
        binding.navView.setNavigationItemSelectedListener(this)

        binding.mapIbMenu.setOnClickListener { binding.mapDrawerLayout.openDrawer(GravityCompat.START) }

        binding.mapIbSearch.setOnClickListener {
            vm.toggleButtonShown()
            startSearchActivity()
        }

        binding.mapIbAdd.setOnClickListener {
            vm.toggleShowPostForm()
        }

        binding.mapIbPostForm.setOnClickListener {
            vm.toggleButtonShown()
            vm.toggleShowPostFormBtn()
        }

        binding.mapTvPostFormTempSave.setOnClickListener {
            //todo 위도 경우 만 전송
            vm.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.mapTvPostFormAddPicture.setOnClickListener {
            checkStoragePermission()
        }

        binding.mapIbPinClose.setOnClickListener {
            vm.toggleShowPostFormBtn()
            vm.toggleShowPostForm()
            vm.toggleButtonShown()
        }

        binding.mapIbCurrentLocation.setOnClickListener {
            setCurrentLocation()
        }
    }

    private fun setCurrentLocation() {
        LocationHelper(this).requestLocationPermissions()?.let {
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(it.latitude, it.longitude))
            naverMap.moveCamera(cameraUpdate)

            currentLocationMarker.map = null
            currentLocationMarker.run {
                icon = OverlayImage.fromResource(R.drawable.ic_map_now)
                position = LatLng(it.latitude, it.longitude)
                this.map = naverMap
            }
        }
    }

    private fun checkStoragePermission() {
        val storagePermissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                startPostActivity()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                showToast("저장 권한을 거부하셨습니다. 해당 기능을 사용할 수 없습니다.")
            }
        }

        TedPermission.with(this)
            .setPermissionListener(storagePermissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }

    private fun startSearchActivity() {
        startActivityForResult(
            Intent(this, SearchActivity::class.java),
            RequestCodeSet.SEARCH_REQUEST_CODE.code
        )
    }

    private fun startPostActivity() {
        //todo reverse geolocaion 을 이용해서 위치 넘겨주기
        val intent = Intent(this@MapActivity, PostActivity::class.java)
        intent.putExtra("lat", naverMap.cameraPosition.target.latitude)
        intent.putExtra("lng", naverMap.cameraPosition.target.longitude)

        startActivityForResult(
            intent,
            RequestCodeSet.SEARCH_REQUEST_CODE.code
        )
    }

    private fun startNavActivity(type: String) {
        startActivity(
            Intent(this, NavActivity::class.java)
                .putExtra("type", type)
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map_nav_customize_emotion_name -> startNavActivity(NavTypeStringSet.CustomEmotion.type)
            R.id.map_nav_setting -> startNavActivity(NavTypeStringSet.Setting.type)
        }
        return true
    }

    private fun settingOptionToMap() {
        naverMap.apply {
            mapType = NaverMap.MapType.Navi
            isNightModeEnabled = true
            uiSettings.isZoomControlEnabled = false
            uiSettings.isZoomGesturesEnabled = true
            setLayerGroupEnabled("LAYER_GROUP_BUILDING", true)
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        this.naverMap.setOnMapClickListener { _, _ ->
            vm.isShownPostForm.value?.let { isShownPostForm ->
                vm.isShownPostFormBtn.value?.let { isShownPostFormBtn ->
                    if (!(isShownPostForm or isShownPostFormBtn)) {
                        vm.toggleButtonShown()
                    }
                }
            }
        }

        settingOptionToMap()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCodeSet.SEARCH_REQUEST_CODE.code -> {
                    vm.toggleButtonShown()
                    data?.let {
                        val mapX = it.getDoubleExtra("mapX", 0.0)
                        val mapY = it.getDoubleExtra("mapY", 0.0)
                        val tm128 = Tm128(mapX, mapY)
                        val latLng = tm128.toLatLng()
                        val cameraUpdate = CameraUpdate.scrollTo(latLng)
                        naverMap.moveCamera(cameraUpdate)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (binding.mapDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.mapDrawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        vm.isShownPostFormBtn.value?.let {
            if (it) {
                vm.toggleButtonShown()
                vm.toggleShowPostFormBtn()
                return@onBackPressed
            }
        }
        vm.isShownPostForm.value?.let {
            if (it) {
                vm.toggleShowPostForm()
                return@onBackPressed
            }
        }
        super.onBackPressed()
    }

}
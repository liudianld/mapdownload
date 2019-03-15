NProgress.start();

(function () {
    // document.getElementsByClassName('leaflet-control-measure .leaflet-bar .leaflet-control').style.visibility = "hidden";
    var geo = window.geo = {
        model: function (title, content, callback) {
            $("#myModalLabel").html(title);
            $("#myModalBody").html(content);

            $('#myModal').modal('show');
            if (callback) {
                $('#myModal .btn-primary').show();
                $('#myModal .btn-primary').off().click(function () {
                    callback();
                });
            } else
                $('#myModal .btn-primary').hide();
        },

        modelInfo: function (title, content, callback) {
            this.model(title, content, callback);
            $("#myModal .modal-header")
                .removeClass().addClass("modal-header modal-header-info");
        },

        modelSuccess: function (title, content, callback) {
            this.model(title, content, callback);
            $("#myModal .modal-header")
                .removeClass().addClass("modal-header modal-header-success");
        },

        modelWarning: function (title, content, callback) {
            this.model(title, content, callback);
            $("#myModal .modal-header").removeClass().addClass("modal-header modal-header-warning");
        },

        modelError: function (title, content, callback) {
            this.model(title, content, callback);
            $("#myModal .modal-header").removeClass().addClass("modal-header modal-header-danger");
        },

        ajax: {
            _defaultError: function (xhr, err, msg) {
                if (typeof window.console !== 'undefined') {
                    var log = window.console.error;

                    if (!log) {
                        log = window.console.log;
                    }
                    log("==========================================");
                    log("An error occurred with the request.");
                    log("- Status Code: " + xhr.status);
                    log("- Status Text: " + xhr.statusText);
                    log("- Response Text: " + xhr.responseText);
                    log("- Error: " + err);
                    log("- Message: " + msg);
                    log("==========================================");
                }
            },

            getData: function (url, data, successCallback, errorCallback) {
                if (!errorCallback) {
                    errorCallback = this._defaultError;
                }

                $.ajax({
                    type: "GET"
                    , url: url
                    , data: data
                    , contentType: "application/x-www-form-urlencoded; charset=utf-8"
                    , success: successCallback
                    , error: errorCallback
                });
            },

            postData: function (url, data, successCallback, errorCallback) {
                if (!errorCallback) {
                    errorCallback = this._defaultError;
                }

                $.ajax({
                    type: "POST"
                    , url: url
                    , data: data
                    , contentType: "application/json; charset=utf-8"
                    , success: successCallback
                    , error: errorCallback
                });
            },

            putData: function (url, data, successCallback, errorCallback) {
                if (!errorCallback) {
                    errorCallback = this._defaultError;
                }

                $.ajax({
                    type: "PUT"
                    , url: url
                    , data: data
                    , success: successCallback
                    , error: errorCallback
                });
            },

            deleteData: function (url, data, successCallback, errorCallback) {
                if (!errorCallback) {
                    errorCallback = this._defaultError;
                }

                $.ajax({
                    type: "DELETE"
                    , url: url
                    , data: data
                    , success: successCallback
                    , error: errorCallback
                });
            }
        }
    };

    // 根据网络获取当前的经纬度
    // var latlngFromLocation = [];
    // navigator.geolocation.getCurrentPosition(function(position) {
    //     var lat = position.coords.latitude;
    //     var lng = position.coords.longitude;
    //     latlngFromLocation.push(lat);
    //     latlngFromLocation.push(lng);
    // });

    var options = {
        // crs: L.CRS.EPSGB3857,
        center: [30.44285652149073, 114.46136561263256],
        zoom: 12,
        attributionControl: false,
        measureControl: true
    };

    var osm = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');

    var layersDef = {
        // GoogleChinaMap: new L.Google('ROADMAP')                //Google普通地图
        // , GoogleChinaSatelliteMap: new L.Google('SATELLITE')    //Google卫星地图
        // , GoogleChinaHybridMap: new L.Google('HYBRID')          //Google混合地图
        // , CacheGoogleChinaMap: new L.CacheLayer({ mapName: 'GoogleChinaMap' })                      //缓存
        // , CacheGoogleChinaSatelliteMap: new L.CacheLayer({ mapName: 'GoogleChinaSatelliteMap' })    //缓存
        // , CacheGoogleChinaHybridMap: new L.CacheLayer({ mapName: 'GoogleChinaHybridMap' })          //缓存       

        // , 
        OSM: osm                                              //osm

        , TencentMap: new L.TencentLayer("ROADMAP")                                                       //腾讯普通地图
        , TencentSatelliteMap: new L.TencentLayer("SATELLITE")                                   //腾讯卫星地图
        , TencentHybridMap: [new L.TencentLayer("SATELLITE"), new L.TencentLayer("RealROADMAP")]    //腾讯混合地图
        , CacheTencentMap: new L.CacheLayer({ mapName: 'TencentMap' })                       //缓存
        , CacheTencentSatelliteMap: new L.CacheLayer({ mapName: 'TencentSatelliteMap' })     //缓存
        , CacheTencentHybridMap: new L.CacheLayer({ mapName: 'TencentHybridMap' })           //缓存

        , AMapMap: new L.AMapLayer("ROADMAP")                                                 //高德普通地图
        , AMapSatelliteMap: new L.AMapLayer("SATELLITE")                             //高德卫星地图
        , AMapHybridMap: [new L.AMapLayer("SATELLITE"), new L.AMapLayer("HYBRID")]    //高德混合地图
        , CacheAMapMap: new L.CacheLayer({ mapName: 'AMapMap' })                       //缓存
        , CacheAMapSatelliteMap: new L.CacheLayer({ mapName: 'AMapSatelliteMap' })     //缓存
        , CacheAMapHybridMap: new L.CacheLayer({ mapName: 'AMapHybridMap' })           //缓存

        , BaiduMap: new L.TileLayer.BaiduLayer("ROADMAP"),
        BaiduSatelliteMap: new L.TileLayer.BaiduLayer("SATELLITE"),
        BaiduHybridMap: new L.TileLayer.BaiduLayer("HYBRID"),

    };

    // 定义画图元素
    var drawnItems = new L.FeatureGroup();
    // 定义画图获取的上下左右经纬度
    var latLngs = [];

    // 初始化地图为百度地图
    var layer = [layersDef.AMapMap];
    layer[0].on('load', function (e) {
        NProgress.done();
    });
    //Leaflet
    window.map = L.map('map', options);
    layer[0].addTo(map);
    mapEvent();

    // 添加地图监听事件
    function mapEvent() {
        map.addLayer(drawnItems);

        // 定义画图控制器
        drawControl();

        // 添加全屏控件
        map.addControl(new L.Control.Fullscreen({
            title: {
                'false': '全屏',
                'true': '退出全屏'
            }
        }));

        // 地图画图事件监听
        drawEvent();

        // 移除鼠标监听
        mouseMove();
    }

    // 定义画图控制器
    function drawControl() {
        var drawControl = new L.Control.Draw({
            draw: {
                position: 'topleft',
                polygon: {
                    allowIntersection: false,
                    shapeOptions: {
                        color: '#FF0000'
                    }
                },
                rectangle: {
                    shapeOptions: {
                        color: '#FF0000'
                    }
                },
                polyline: false,
                circle: {
                    shapeOptions: {
                        color: '#662d91'
                    }
                }
            },
            edit: {
                featureGroup: drawnItems
            }
        });
        map.addControl(drawControl);

        // 将经纬度展现在左下角的提示处
        latlngText();

    }

    // 将经纬度展现在左下角的提示处
    function latlngText() {
        $(".status-footer .zoom").text(map.getZoom())
        map.on("zoomend", function (e) {
            $(".status-footer .zoom").text(map.getZoom());
        });
    }

    // 地图画图事件监听
    function drawEvent() {
        map.on('draw:created', function (e) {
            latLngs = [];

            var type = e.layerType,
                layer = e.layer;
            // alert(type);
            drawnItems.addLayer(layer);

            if (type === 'circle') {
                //latLngs.push([ layer.getLatLng() ], layer.getRadius());
            } else {
                latLngs = layer.getLatLngs();
            }

            var northEast = layer.getBounds().getNorthEast();
            var southWest = layer.getBounds().getSouthWest();
            var northWest = {
                lat: "",
                lng: ""
            };
            var southEast = {
                lat: "",
                lng: ""
            };
            northWest.lat = northEast.lat;
            northWest.lng = southWest.lng;
            southEast.lat = southWest.lat;
            southEast.lng = northEast.lng;


            $("#lat-north").val(northWest.lat);
            $("#lon-east").val(northWest.lng);
            $("#lat-south").val(southEast.lat);
            $("#lon-west").val(southEast.lng);

            // layer.bindPopup('范围: -fff-');

            // window.geo.model("fff", JSON.stringify(latLngs), function(){alert(JSON.stringify(latLngs))})
        });
    }

    // 鼠标移出事件监听
    function mouseMove() {
        map.on('mousemove', function (e) {
            $(".status-footer .coord")
                .text(e.latlng.lng.toFixed(3) + ", " + e.latlng.lat.toFixed(3))
        });
    }

    /** 
     * 按钮事件 
     * 
     * */
    //把导航栏按钮关联到leaflet.draw控件按钮上.
    $("#draw_rect").click(function () {
        $(this).parents('.dropdown-menu').find('li').removeClass("active");
        $(this)
            .parent().addClass("active")
        $(".leaflet-draw-draw-rectangle")[0].click();
    });
    $("#draw_circle").click(function () {
        $(this).parents('.dropdown-menu').find('li').removeClass("active");
        $(this)
            .parent().addClass("active")
        $(".leaflet-draw-draw-circle")[0].click();
    });
    $("#draw_polygon").click(function () {
        $(this).parents('.dropdown-menu').find('li').removeClass("active");
        $(this)
            .parent().addClass("active")
        $(".leaflet-draw-draw-polygon")[0].click();
    });
    $("#draw_clear").click(function () {
        $(this).parents('.dropdown-menu').find('li').removeClass("active");
        drawnItems.clearLayers();
    });

    /**
     * 区域下载按钮点击事件
     */
    var geoLayer;
    var areaCardFlag = true;
    $("#areaDownload").click(function () {
        if (areaCardFlag) {
            document.getElementById("areacard").style.visibility = "visible";//显示
            areaCardFlag = false;
        } else {
            if (geoLayer) {
                geoLayer.remove();
            }
            document.getElementById("areacard").style.visibility = "hidden";//隐藏
            areaCardFlag = true;
        }
    });

    $("#province").click(function () {
        var obj = document.getElementById("province");
        search(obj);
    });
    $("#city").click(function () {
        var obj = document.getElementById("city");
        search(obj);
    });
    $("#district").click(function () {
        var obj = document.getElementById("district");
        search(obj);
    });
    $("#street").click(function () {
        var obj = document.getElementById("street");
        search(obj);
    });
    $("#areaSelectClose").click(function () {
        if (geoLayer) {
            geoLayer.remove();
        }
        document.getElementById("areacard").style.visibility = "hidden";//隐藏
        areaCardFlag = true;
    });

    var district, polygons = [], citycode;
    var citySelect = document.getElementById('city');
    var districtSelect = document.getElementById('district');
    var areaSelect = document.getElementById('street');

    //行政区划查询
    var opts = {
        subdistrict: 1,   //返回下一级行政区
        showbiz: false  //最后一级返回街道信息
    };
    district = new AMap.DistrictSearch(opts);//注意：需要使用插件同步下发功能才能这样直接使用
    district.search('中国', function (status, result) {
        if (status == 'complete') {
            getData(result.districtList[0]);
        }
    });
    function getData(data, level) {
        var threeArray = data.threeArray;
        if (threeArray) {
            var zoom = 7;
            if ("province" === level) {
                zoom = 6;
            } else if ("city" === level) {
                zoom = 8;
            } else if ("district" === level) {
                zoom = 10;
            } else if ("street" === level) {
                zoom = 10;
            }
            map.flyTo([data.center.lat, data.center.lng], zoom);
            var adcode = data.adcode;
            var name = data.name;
            var geojson =
            {
                "type": "Feature",
                "properties": {
                    "id": adcode,
                    "name": name,
                    "length": 0,
                    "area": 0
                },
                "geometry": {
                    "type": "Polygon",
                    "coordinates": threeArray
                }
            };
            if (geoLayer) {
                geoLayer.remove();
            }
            geoLayer = L.geoJson(geojson, {
                style: function (feature) {
                    return {
                        color: '#00FF00', // 边框颜色
                        fillColor: '#90EE90' //填充色
                    };
                }
            }).addTo(map);
        }

        //清空下一级别的下拉列表
        if (level === 'province') {
            citySelect.innerHTML = '';
            districtSelect.innerHTML = '';
            areaSelect.innerHTML = '';
        } else if (level === 'city') {
            districtSelect.innerHTML = '';
            areaSelect.innerHTML = '';
        } else if (level === 'district') {
            areaSelect.innerHTML = '';
        }

        var subList = data.districtList;
        if (subList) {
            var contentSub = new Option('--请选择--');
            var curlevel = subList[0].level;
            var curList = document.querySelector('#' + curlevel);
            curList.add(contentSub);
            for (var i = 0, l = subList.length; i < l; i++) {
                var name = subList[i].name;
                var levelSub = subList[i].level;
                var cityCode = subList[i].citycode;
                contentSub = new Option(name);
                contentSub.setAttribute("value", levelSub);
                contentSub.center = subList[i].center;
                contentSub.adcode = subList[i].adcode;
                curList.add(contentSub);
            }
        }

    }

    function search(obj) {
        //清除地图上所有覆盖物
        // map.setView([115.980367, 36.456013], 7);
        for (var i = 0, l = polygons.length; i < l; i++) {
            polygons[i].setMap(null);
        }
        var option = obj[obj.options.selectedIndex];
        var keyword = option.text; //关键字
        var adcode = option.adcode;
        district.setLevel(option.value); //行政区级别
        district.setExtensions('all');
        //行政区查询
        //按照adcode进行查询可以保证数据返回的唯一性
        district.search(adcode, function (status, result) {
            if (status === 'complete') {
                // console.log(result.districtList[0].boundaries);
                var boundaries = result.districtList[0].boundaries;
                var threeArray = new Array();
                var maxLat = 0;
                var minLat = 999;
                var maxLng = 0;
                var minLng = 999;
                for (var i = 0; i < boundaries.length; i++) {
                    //第一层外层循环
                    var firstArr = boundaries[i];
                    var sceondArr = new Array();
                    for (var j = 0; j < firstArr.length; j++) {
                        // 第二层循环
                        var thirdArr = new Array();
                        // 获取经度维度的最大值跟最小值
                        var lng = firstArr[j].lng;//经度
                        var lat = firstArr[j].lat;//维度
                        if (parseFloat(lng) > maxLng) {
                            maxLng
                        }
                        maxLng = (parseFloat(lng) - maxLng > 0) ? lng : maxLng;
                        minLng = (minLng - parseFloat(lng) > 0) ? lng : minLng;
                        maxLat = (parseFloat(lat) - maxLat > 0) ? lat : maxLat;
                        minLat = (minLat - parseFloat(lat) > 0) ? lat : minLat;

                        thirdArr[0] = lng;
                        thirdArr[1] = lat;
                        sceondArr.push(thirdArr);
                    }
                    threeArray.push(sceondArr);
                }
                $("#lat-north").val(maxLat);
                $("#lon-east").val(maxLng);
                $("#lat-south").val(minLat);
                $("#lon-west").val(minLng);
                // console.log(threeArray);
                result.districtList[0].threeArray = threeArray;
                getData(result.districtList[0], obj.id);
            }
        });
    }

    //点击下载按钮时触发
    var downloadData;
    var downloadUrl;
    $("#downloadBtn").click(function () {
        var n = $("#lat-north").val();
        var e = $("#lon-east").val();
        var s = $("#lat-south").val();
        var w = $("#lon-west").val();

        var ll = [];
        if (n && e && s && w) {
            ll.push(
                { "lng": w, "lat": n },
                { "lng": e, "lat": n },
                { "lng": e, "lat": s },
                { "lng": w, "lat": s }
            );
        }

        if (latLngs.length == 0 && ll.length == 0) {
            geo.modelError("警告", "<h5>请先使用导航中的<code>画图工具</code>框选区域 ,或在左侧<code>选取范围</code>内填写 !</h5>", false);
            return;
        }

        ll = ll.length > 0 ? ll : latLngs;

        var fromZoom = $("#from-zoom").val();
        var toZoom = $("#to-zoom").val();
        if (!fromZoom || !toZoom) {
            geo.modelError("警告", "<h5>请在左侧任务栏<code>缩放级别</code>中填写范围 !</h5>", false);
            return;
        }

        $(this).text("下载中 ...");
        $(this).prop('disabled', true);
        $(".status-footer .down").show();

        downloadData = {
            downType: 'all',
            latLngs: ll,
            fromZoom: fromZoom,
            toZoom: toZoom,
            providerName: mapProvider,
            countType: 'count'
        };
        // postData : function (url, data, successCallback, errorCallback) {
        downloadUrl = "http://192.168.101.5:8086/downloadTile";

        geo.ajax.postData(downloadUrl, JSON.stringify(downloadData), function(data){
            let tileSum = data.titleSum;
            let tileSize = bytesToSize(tileSum * 20 * 1024);

            geo.modelInfo("提示", "本次下载" + tileSum + "张瓦片，预计使用空间" + tileSize, downloadTitle);

            $("#downloadBtn").text("点击下载");
            $("#downloadBtn").prop('disabled', false);
            $(".status-footer .down").hide();
        }, errorData);

    });

    function bytesToSize(bytes) {
        if (bytes === 0) return '0 B';
        var k = 1024, // or 1024
            sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            i = Math.floor(Math.log(bytes) / Math.log(k));
     
       return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
    }

    function downloadTitle() {
        
        // 将model的确定改成点击下载
        document.getElementById("modelPrimary").innerHTML = "下载中...";
        $("#modelPrimary").attr('disabled',true);

        document.getElementsByClassName
        // geo.postData(url, data);
        downloadData.countType = "download";
        geo.ajax.postData(downloadUrl, JSON.stringify(downloadData), successData, errorData);
    }

    function successData(data) {
        document.getElementById("modelPrimary").innerHTML = "确定";
        $("#modelPrimary").attr('disabled',false);
        if (data.isError) {
            geo.modelError(data.title || "错误", data.content, false);
            $("#downloadBtn").text("点击下载");
            $("#downloadBtn").prop('disabled', false);
            $(".status-footer .down").hide();
            return;
        } else {
            if (data.isDone) {
                $("#downloadBtn").text("点击下载");
                $("#downloadBtn").prop('disabled', false);

                var ds = data.status;
                var repeat = data.repeat || 0;
                if (ds.failCount != 0) {
                    geo.modelWarning(
                        "警告：有未完成的下载, 重试次数:" + repeat,
                        "本次下载,总共:" + ds.tileCount + ", 成功:" + ds.downCount + ", 失败:" + ds.failCount +
                        " <br />点击确定将尝试再次下载失败的瓦片,或者可以稍候重新下载！",
                        function () {
                            var data = {
                                downType: 'fail',
                                failRepeat: repeat,
                                providerName: mapProvider
                            };
                            geo.ajax.postData(url, JSON.stringify(data), successData, errorData);
                        }
                    );
                } else
                    if (data.jumpCount > 0) {
                        var tileSum = ds.tileCount + data.jumpCount;
                        geo.modelSuccess("通知", "全部瓦片下载完成! 总共:" + tileSum + ", 成功:" + ds.downCount + ", 跳过已存在的瓦片" + data.jumpCount + ", 失败:" + ds.failCount);

                    } else {
                        geo.modelSuccess("通知", "全部瓦片下载完成! 总共:" + ds.tileCount + ", 成功:" + ds.downCount + ", 失败:" + ds.failCount);
                    }
                return;
            } else {
                $(".status-footer .down-zoom").text(data.currentZoom);
                $(".status-footer .down-count").text(data.tileNum + "/" + data.tileCount);
            }
        }
    }

    function errorData(data) {
        document.getElementById("modelPrimary").innerHTML = "确定";
        $("#modelPrimary").attr('disabled',false);
        // console.log('------------>errorData' + data);
        geo.modelError(data.title || "错误", data.content || "下载失败，请重试！", false);
        $("#downloadBtn").text("点击下载");
        $("#downloadBtn").prop('disabled', false);
        $(".status-footer .down").hide();
        return;
    }

    var mapProvider = "AMapMap", providerPrefix = "AMap", providerSuffix = "";
    //地图切换按钮事件
    $("#mapPrefix a").click(function () {
        var prefix = $(this).attr("prefix");

        // 如果是百度地图，重新加载地图，因为百度地图使用的坐标系不同，因此要单独拎出来设置options
        if ("" != prefix && "Baidu" == prefix) {
            options = {
                crs: L.CRS.EPSGB3857,
                center: [30.44285652149073, 114.46136561263256],
                zoom: 13,
                attributionControl: false,
                measureControl: true
            };
        }

        // 如果不是百度地图
        if ("" != prefix && "Baidu" != prefix) {
            options = {
                center: [30.44285652149073, 114.46136561263256],
                zoom: 12,
                attributionControl: false,
                measureControl: true
            };
        }
        // map = L.map('map', options);
        // 清除地图以及地图的监听
        map.remove();
        map = L.map('map', options);

        // 地图的监听事件
        mapEvent();

        var accessType = $("#accessType .active");
        if (accessType.length != 0 && accessType.find('a').hasClass("offline")) {
            prefix = "Cache" + prefix;
        }

        mapProvider = prefix + providerSuffix + "Map";

        if (switchLayer(mapProvider)) {
            providerPrefix = prefix;
            var selText = $(this).html();
            $(this).parents('.dropdown-menu').find('li').removeClass("active");
            $(this)
                .parent().addClass("active")
                .parents('.dropdown').find('.dropdown-toggle')
                .html(selText + '<span class="caret"></span>');
        }
    });

    //切换类型按钮事件
    $("#mapSuffix a").click(function () {
        var suffix = $(this).attr("suffix");
        mapProvider = providerPrefix + suffix + "Map";

        if (switchLayer(mapProvider)) {
            providerSuffix = suffix;
            var selText = $(this).html();
            $(this).parents('.dropdown-menu').find('li').removeClass("active");
            $(this).parent().addClass("active")
                .parents('.dropdown').find('.dropdown-toggle')
                .html(selText + '<span class="caret"></span>');
        }
    });

    //访问方式切换
    $("#accessType a").click(function () {
        var type = $(this).prop("class");

        if (type == "online" && mapProvider.indexOf("Cache") != -1) {
            mapProvider = mapProvider.substring(5);
            providerPrefix = providerPrefix.substring(5);
            if (layersDef[mapProvider])
                $("#downloadBtn").prop('disabled', false);
        } else if (type == "offline" && mapProvider.indexOf("Cache") == -1) {
            mapProvider = "Cache" + mapProvider;
            providerPrefix = "Cache" + providerPrefix;
            if (layersDef[mapProvider])
                $("#downloadBtn").prop('disabled', true);
        }

        if (switchLayer(mapProvider)) {
            var selText = $(this).html();
            $(this).parents('.dropdown-menu').find('li').removeClass("active");
            $(this)
                .parent().addClass("active")
                .parents('.dropdown').find('.dropdown-toggle')
                .html(selText + '<span class="caret"></span>');
        }
    });

    function switchLayer(layerName) {
        if (layersDef[layerName]) {

            for (var i in layer) {
                map.removeLayer(layer[i]);
                layer[i].off('loading', loadingHandler);
                layer[i].off('load', loadHandler);
            }

            if (layersDef[layerName] instanceof Array) {
                var layers = layersDef[layerName];
                for (var i in layers) {
                    layer.push(layers[i]);
                    layers[i].on('loading', loadingHandler);
                    layers[i].on('load', loadHandler);
                    map.addLayer(layers[i]);
                }
            } else {
                var _layer = layersDef[layerName];

                layer.push(_layer);
                _layer.on('loading', loadingHandler);
                _layer.on('load', loadHandler);
                map.addLayer(_layer);
            }

            return true;
        } else {
            geo.modelWarning("警告", "层定义中未发现对应层 : " + layerName, false);
            return false;
        }
    };

    function loadingHandler(event) {
        NProgress.start();
    };

    function loadHandler(event) {
        NProgress.done();
    };

    /**
     *  测距用到的方法
     
    var celiangFlag = false;
    $("#celiang").click(function () {
        $(this).parents('.dropdown-menu').find('li').removeClass("active");
        $(this).parent().addClass("active")
        // $(".leaflet-draw-draw-rectangle")[0].click();
        if (!celiangFlag) {
            document.getElementById('toggleCeliang').style.display = "";
            celiangFlag = true;
        }
        else {
            document.getElementById('toggleCeliang').style.display = "none";
            celiangFlag = false;
        }

        // document.getElementById("toggleCeliang").style.display = "";
        // getstyle("leaflet-control-measure").display = "";
    });

    document.getElementById('toggleCeliang').style.display = "none";
    */

    // 定位
    //获取用户所在城市信息
    var localteFlag = false;
    var locateMarker = L.marker();
    $("#locatePostion").click(function () {
        let locateId = 'locateOk';
        if (!localteFlag) {
            // <span class="glyphicon glyphicon-ok"></span>
            showCityInfo();
            localteFlag = true;
            // popContent = '<span id="locateOk" class="glyphicon glyphicon-ok" style="margin-left: 70px"></span>';
            // $('#locatePostion').append(popContent);
            addOkIcon(locateId, '#locatePostion');
        } else {
            locateMarker.remove();
            localteFlag = false;
            // $('#locateOk').remove();
            removeOkIcon(locateId);
        }
    });

    function addOkIcon(id, fromId) {
        var popContent = '<span id="' + id + '" class="glyphicon glyphicon-ok" style="margin-left: 70px"></span>';
        $(fromId).append(popContent);
    }

    function removeOkIcon(id) {
        $('#' + id).remove();
    }

    function showCityInfo() {
        //实例化城市查询类
        var citysearch = new AMap.CitySearch();
        //自动获取用户IP，返回当前城市
        citysearch.getLocalCity(function (status, result) {
            if (status === 'complete' && result.info === 'OK') {
                if (result && result.city && result.bounds) {
                    let cityinfo = result.province + " " + result.city;
                    // document.getElementById('info').innerHTML = '您当前所在城市：' + cityinfo;
                    //地图显示当前城市
                    // map.setBounds(citybounds);
                    let locateLat = (result.bounds.ac.lat + result.bounds.oc.lat) / 2;
                    let locateLng = (result.bounds.ac.lng + result.bounds.oc.lng) / 2;
                    map.flyTo([locateLat, locateLng], 13);
                    locateMarker.setLatLng([locateLat, locateLng]);
                    locateMarker.bindTooltip(cityinfo);
                    // locateMarker.
                    // locateMarker = L.marker(,{
                    //     title: cityinfo
                    // });

                    locateMarker.addTo(map);
                    // locateMarker.bindPopup();
                }
            } else {
                // document.getElementById('info').innerHTML = result.info;
            }
        });
    };

    var searchFlag = false;
    $("#searchAuto").click(function () {
        var searchOk = 'searchOk';
        if (!searchFlag) {
            $("#map").append('<div id="searchContainer"></div>');
            // 加入poi搜索
            var options = {
                geojsonServiceAddress: "http://localhost:8086/search"
            };
            $("#searchContainer").GeoJsonAutocomplete(options);
            searchFlag = true;
            addOkIcon(searchOk, '#searchAuto')
        } else {
            $("#searchContainer").remove();
            searchFlag = false;
            removeOkIcon(searchOk);
        }
    });
})()

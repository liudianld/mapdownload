NProgress.start();

(function () {
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

    //Leaflet
    var map = L.map('map', {
        center: [30.44285652149073, 114.46136561263256],
        zoom: 12
    });
    var myMap = map;

    var osm = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');

    var ROADMAP = new L.TencentLayer();
    var RealROADMAP = new L.TencentLayer("RealROADMAP");
    var SATELLITE = new L.TencentLayer("SATELLITE");
    var TERRAIN = new L.TencentLayer("TERRAIN");

    var layersDef = {
        GoogleChinaMap: new L.Google('ROADMAP')                //Google普通地图
        , GoogleChinaSatelliteMap: new L.Google('SATELLITE')    //Google卫星地图
        , GoogleChinaHybridMap: new L.Google('HYBRID')          //Google混合地图
        , CacheGoogleChinaMap: new L.CacheLayer({ mapName: 'GoogleChinaMap' })                      //缓存
        , CacheGoogleChinaSatelliteMap: new L.CacheLayer({ mapName: 'GoogleChinaSatelliteMap' })    //缓存
        , CacheGoogleChinaHybridMap: new L.CacheLayer({ mapName: 'GoogleChinaHybridMap' })          //缓存       

        , OSM: osm                                              //osm

        , TencentMap: new L.TencentLayer()                                                       //腾讯普通地图
        , TencentSatelliteMap: new L.TencentLayer("SATELLITE")                                   //腾讯卫星地图
        , TencentHybridMap: [new L.TencentLayer("SATELLITE"), new L.TencentLayer("RealROADMAP")]    //腾讯混合地图
        , CacheTencentMap: new L.CacheLayer({ mapName: 'TencentMap' })                       //缓存
        , CacheTencentSatelliteMap: new L.CacheLayer({ mapName: 'TencentSatelliteMap' })     //缓存
        , CacheTencentHybridMap: new L.CacheLayer({ mapName: 'TencentHybridMap' })           //缓存

        , AMapMap: new L.AMapLayer()                                                 //高德普通地图
        , AMapSatelliteMap: new L.AMapLayer("SATELLITE")                             //高德卫星地图
        , AMapHybridMap: [new L.AMapLayer("SATELLITE"), new L.AMapLayer("HYBRID")]    //高德混合地图
        , CacheAMapMap: new L.CacheLayer({ mapName: 'AMapMap' })                       //缓存
        , CacheAMapSatelliteMap: new L.CacheLayer({ mapName: 'AMapSatelliteMap' })     //缓存
        , CacheAMapHybridMap: new L.CacheLayer({ mapName: 'AMapHybridMap' })           //缓存

        , BaiduMap: new L.BaiduLayer()                                                 //百度普通地图
        , BaiduSatelliteMap: new L.BaiduLayer("SATELLITE")                             //百度卫星地图
        , BaiduHybridMap: [new L.BaiduLayer("SATELLITE"), new L.BaiduLayer("HYBRID")]  //百度混合地图
    };

    // 初始化地图为高德地图
    var layer = [layersDef.AMapMap];
    layer[0].on('load', function (e) {
        NProgress.done();
    });
    layer[0].addTo(map);


    var drawnItems = new L.FeatureGroup();
    map.addLayer(drawnItems);

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

    /**
     * 地图事件监听
     */
    var latLngs = [];
    map.on('draw:created', function (e) {
        latLngs = [];

        var type = e.layerType,
            layer = e.layer;
        alert(type);
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

    $(".status-footer .zoom").text(myMap.getZoom())
    map.on("zoomend", function (e) {
        $(".status-footer .zoom").text(myMap.getZoom());
    });

    map.on('mousemove', function (e) {
        $(".status-footer .coord")
            .text(e.latlng.lng.toFixed(3) + ", " + e.latlng.lat.toFixed(3))
    });

    /** 
     * 按钮事件 
     * 
     * */
    //把导航栏按钮关联到leaflet.draw控件按钮上.
    $("#draw_rect").click(function () {
        alert(1);
        $(this).parents('.dropdown-menu').find('li').removeClass("active");
        $(this)
            .parent().addClass("active")
        $(".leaflet-draw-draw-rectangle")[0].click();
    });
    $("#draw_circle").click(function () {
        alert(2);
        $(this).parents('.dropdown-menu').find('li').removeClass("active");
        $(this)
            .parent().addClass("active")
        $(".leaflet-draw-draw-circle")[0].click();
    });
    $("#draw_polygon").click(function () {
        alert(3);
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
            if (geoLayer){
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
        if (geoLayer){
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
            if ("province" === level){
                zoom = 7;
            }else if ("city" === level){
                zoom = 9;
            } else if ("district" === level){
                zoom = 11;
            } else if ("street" === level){
                zoom = 11;
            }
            map.setView([data.center.lat, data.center.lng], zoom);
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
            if (geoLayer){
                geoLayer.remove();
            }
            geoLayer = L.geoJson(geojson).addTo(map);
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
                        if (parseFloat(lng) > maxLng){
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

    //区域选择器
    function areaSelect() {
        var strs = new Array();
        var polyline = new Array();
        var url = "https://restapi.amap.com/v3/config/district?key=86c8686ee6d8b5db26ac753231d7206b&keywords=长清区&subdistrict=2&extensions=all";
        $.ajaxSettings.async = false;
        $.getJSON(url, function (json) {
            strs = json.districts[0].center.split(",");
            polyline = json.districts[0].polyline.split("|");
            name = json.districts[0].name;
            adcode = json.districts[0].adcode;
        });
        //生成以|分割的二维数组
        var len1 = polyline.length;
        var n = 1;
        var lineNum = len1 % n === 0 ? len1 / n : Math.floor((len1 / n) + 1);
        var towArray = new Array();
        for (var i = 0; i < lineNum; i++) {
            // slice() 方法返回一个从开始到结束（不包括结束）选择的数组的一部分浅拷贝到一个新数组对象。且原始数组不会被修改。
            var temp = polyline.slice(i * n, i * n + n);
            towArray.push(temp);
        }
        // console.log(towArray);

        //生成三维数组
        var threeArray = new Array();
        for (var i = 0; i < towArray.length; i++) {
            var temp = towArray[i].toString().replaceAll(";", ",").split(",");
            var len2 = temp.length;
            // console.log(len2);
            var n = 2;
            var lineNum = len2 % n === 0 ? len2 / n : Math.floor((len2 / n) + 1);
            var towArray2 = new Array();
            for (var j = 0; j < lineNum; j++) {
                // slice() 方法返回一个从开始到结束（不包括结束）选择的数组的一部分浅拷贝到一个新数组对象。且原始数组不会被修改。
                var temp2 = temp.slice(j * n, j * n + n);
                towArray2.push(temp2);
            }
            // console.log(towArray2);
            threeArray.push(towArray2);
            // console.log(b);
        }
        // console.log(threeArray);
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
        var geoLayer = L.geoJson(geojson).addTo(map);
        // geoList = new L.Control.GeoJSONSelector(geoLayer, {
        //     zoomToLayer: true,
        //     listDisabled: false,
        //     activeListFromLayer: false,
        //     activeLayerFromList: false,
        //     listOnlyVisibleLayers: false
        // }).addTo(map);

        map.setView([strs[1], strs[0]], 7);
    }

    //点击下载按钮时触发
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

        $(this).text("下载中 ...");
        $(this).prop('disabled', true);
        $(".status-footer .down").show();

        var data = {
            downType: 'all',
            latLngs: ll,
            fromZoom: fromZoom,
            toZoom: toZoom,
            providerName: mapProvider
        };
        // postData : function (url, data, successCallback, errorCallback) {
        var url = "http://192.168.101.5:8086/downloadTile";
        // geo.postData(url, data);
        geo.ajax.postData(url, JSON.stringify(data), success);
        // geo.sendSocket(JSON.stringify(data));
    });

    function success(data) {
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
                            geo.sendSocket(JSON.stringify(data));
                        }
                    );
                } else
                    geo.modelSuccess("通知", "全部瓦片下载完成! 总共:" + ds.tileCount + ", 成功:" + ds.downCount + ", 失败:" + ds.failCount);
                return;
            } else {
                $(".status-footer .down-zoom").text(data.currentZoom);
                $(".status-footer .down-count").text(data.tileNum + "/" + data.tileCount);
            }
        }
    }

    var mapProvider = "AMapMap", providerPrefix = "AMap", providerSuffix = "";
    //地图切换按钮事件
    $("#mapPrefix a").click(function () {
        var prefix = $(this).attr("prefix");

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
})()

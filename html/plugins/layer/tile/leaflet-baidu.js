/**
 * Projection class for Baidu Spherical Mercator
 *
 * @class BaiduSphericalMercator
 */
L.Projection.BaiduSphericalMercator = {
    /**
     * Project latLng to point coordinate
     *
     * @method project
     * @param {Object} latLng coordinate for a point on earth
     * @return {Object} leafletPoint point coordinate of L.Point
     */
    project: function(latLng) {
        var projection = new BMap.MercatorProjection();
        var point = projection.lngLatToPoint(
            new BMap.Point(latLng.lng, latLng.lat)
        );
        var leafletPoint = new L.Point(point.x, point.y);
        return leafletPoint;
    },

    /**
     * unproject point coordinate to latLng
     *
     * @method unproject
     * @param {Object} bpoint baidu point coordinate
     * @return {Object} latitude and longitude
     */
    unproject: function (bpoint) {
        var projection= new BMap.MercatorProjection();
        var point = projection.pointToLngLat(
            new BMap.Pixel(bpoint.x, bpoint.y)
        );
        var latLng = new L.LatLng(point.lat, point.lng);
        return latLng;
    },

    /**
     * Don't know how it used currently.
     *
     * However, I guess this is the range of coordinate.
     * Range of pixel coordinate is gotten from
     * BMap.MercatorProjection.lngLatToPoint(180, -90) and (180, 90)
     * After getting max min value of pixel coordinate, use
     * pointToLngLat() get the max lat and Lng.
     */
    bounds: (function () {
        var MAX_X= 20037726.37;
        var MIN_Y= -11708041.66;
        var MAX_Y= 12474104.17;
        var bounds = L.bounds(
            [-MAX_X, MIN_Y], //-180, -71.988531
            [MAX_X, MAX_Y]  //180, 74.000022
        );
        var MAX = 33554432;
        bounds = new L.Bounds(
            [-MAX, -MAX],
            [MAX, MAX]
        );
        return bounds;
    })()
};

/**
 * Coordinate system for Baidu EPSG3857
 *
 * @class BEPSG3857
 */
L.CRS.BEPSG3857 = L.extend({}, L.CRS, {
    code: 'EPSG:3857',
    projection: L.Projection.BaiduSphericalMercator,

    transformation: (function () {
        var z = -18 - 8;
        var scale = Math.pow(2, z);
        return new L.Transformation(scale, 0.5, -scale, 0.5);
    }())
});


L.BaiduLayer = L.TileLayer.extend({
    options: {
        subdomains: [1, 2, 3],
        minZoom: 3,
        maxZoom: 19,
        attribution: '© 2014 Baidu - GS(2012)6003;- Data © <a target="_blank" href="http://www.navinfo.com/">NavInfo</a> & <a target="_blank" href="http://www.cennavi.com.cn/">CenNavi</a> & <a target="_blank" href="http://www.365ditu.com/">DaoDaoTong</a>'
    },
    
    //type: ROADMAP(普通地图), SATELLITE(卫星), HYBRID(混合)
    initialize: function(type, options) {
        L.Util.setOptions(this, options);
        
        this._type = type || 'ROADMAP';
    },
    
    getTileUrl: function (tilePoint) {
        
        this._url = 
            "http://online{s}.map.bdimg.com/tile/?qt=tile&x={x}&y={y}&z={z}&styles=pl&udt=20150213";
        
        var zoom = tilePoint.z - 1;
        var offsetX = Math.pow(2, zoom);
        var offsetY = offsetX - 1;

        var numX = tilePoint.x - offsetX;
        var numY = -tilePoint.y + offsetY;

        zoom = zoom + 1;
        var x = (numX+"").replace("-", "M");
        var y = (numY+"").replace("-", "M");
        
        var urlArgs = {
            z: zoom,
            x: x,
            y: y
        };
        
        switch(this._type){
        case 'ROADMAP':
            break;
        case 'SATELLITE':
            this._url = "http://shangetu{s}.map.bdimg.com/it/u=x={x};y={y};z={z};v=009;type=sate&fm=46&udt=20150601";
            break;
        case 'HYBRID':
            this._url = "http://online{s}.map.bdimg.com/tile/?qt=tile&x={x}&y={y}&z={z}&styles=sl&v=039&udt=20140314";
            break;
        }
        
        return L.Util.template(this._url, L.extend(urlArgs, this.options, {s: this._getSubdomain(tilePoint)}));
    }
});

L.baiduLayer = function (key, options) {
    return new L.BaiduLayer(key, options);
};

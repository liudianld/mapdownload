/**
 * 本地缓存层
 */
L.CacheLayer = L.TileLayer.extend({
    mapName: '',
    
    options: {
        subdomains: [1, 2, 3, 4],
        attribution: '© Map4J Cache Layer Data: localhsot ©',
        errorTileUrl: 'images/tile_404.png'
    },
    
    initialize: function(options) {
        L.Util.setOptions(this, options);
        
        this.mapName = options.mapName;
    },
    
    getTileUrl: function (tilePoint) {
        this._url = 'http://'+window.location.host+'/geo/'+this.mapName+'/L{z}/R{y}/C{x}.png';
        var urlArgs = {
            z: tilePoint.z,
            x: tilePoint.x,
            y: tilePoint.y
        };

        return L.Util.template(this._url, L.extend(urlArgs, this.options));
    }
});

L.cacheLayer = function (key, options) {
    return new L.CacheLayer(key, options);
};

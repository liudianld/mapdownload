package com.shenlandt.wh.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.shenlandt.wh.utils.SystemPath;
import com.shenlandt.wh.utils.URLUtils;
import com.shenlandt.wh.utils.YamlLoader;

import ch.qos.logback.classic.Level;

/**
 * 实用工具类，帮助处理map4j配置文件。
 */
public class Configuration {
    /**
     * map4j 版本是从 MANIFEST.MF文件中获取的.是由Maven构建时自动生成的
     */
    public static final String MAP4J_VERSION = Configuration.class.getPackage().getImplementationVersion();

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private final boolean httpListener;
    private final int httpPort;
    private final String httpHost;

    private final boolean ajpListener;
    private final int ajpPort;
    private final String ajpHost;

    private final String logFilePath;
    private final Level logLevel;
    private final boolean logToConsole;
    private final boolean logToFile;
    
    private final int ioThreads;
    private final int workerThreads;
    private final int bufferSize;
    private final int buffersPerRegion;
    private final boolean directBuffers;
    
    private final String tilePath;
    
    private final int requestsLimit;
    
    private final List<Map<String, Object>> staticResourcesMounts;
    
    private final List<Map<String, Object>> applicationLogicMounts;
    
    private final List<Map<String, Object>> webSocketMounts;
    
    public static final String DEFAULT_AJP_HOST = "0.0.0.0";

    public static final int DEFAULT_AJP_PORT = 8009;

    public static final String DEFAULT_HTTP_HOST = "0.0.0.0";

    public static final int DEFAULT_HTTP_PORT = 8085;
    
    public static final String DEFAULT_WHERE = "geo";
    
    public static final String LOG_LEVEL_KEY = "log-level";

    public static final String LOG_FILE_PATH_KEY = "log-file-path";
    
    public static final String ENABLE_LOG_FILE_KEY = "enable-log-file";

    public static final String ENABLE_LOG_CONSOLE_KEY = "enable-log-console";

    public static final String AJP_HOST_KEY = "ajp-host";

    public static final String AJP_PORT_KEY = "ajp-port";

    public static final String AJP_LISTENER_KEY = "ajp-listener";

    public static final String HTTP_HOST_KEY = "http-host";

    public static final String HTTP_PORT_KEY = "http-port";

    public static final String HTTP_LISTENER_KEY = "http-listener";
    
    public static final String DIRECT_BUFFERS_KEY = "direct-buffers";

    public static final String BUFFERS_PER_REGION_KEY = "buffers-per-region";
    
    public static final String APPLICATION_LOGIC_MOUNTS_KEY = "application-logic-mounts";
    
    public static final String APPLICATION_LOGIC_MOUNT_ARGS_KEY = "args";

    public static final String APPLICATION_LOGIC_MOUNT_WHAT_KEY = "what";

    public static final String APPLICATION_LOGIC_MOUNT_WHERE_KEY = "where";
    
    public static final String WEBSOCKET_MOUNT_KEY = "websocket-mounts";
    
    public static final String WEBSOCKET_MOUNT_WHAT_KEY = "what";

    public static final String WEBSOCKET_MOUNT_WHERE_KEY = "where";
    
    public static final String STATIC_RESOURCES_MOUNTS_KEY = "static-resources-mounts";

    public static final String STATIC_RESOURCES_MOUNT_WHAT_KEY = "what";

    public static final String STATIC_RESOURCES_MOUNT_WHERE_KEY = "where";

    public static final String STATIC_RESOURCES_MOUNT_WELCOME_FILE_KEY = "welcome-file";

    public static final String BUFFER_SIZE_KEY = "buffer-size";

    public static final String WORKER_THREADS_KEY = "worker-threads";

    public static final String IO_THREADS_KEY = "io-threads";
    
    public static final String TILE_PATH = "tile-path";
    
    public static final String REQUESTS_LIMIT_KEY = "requests-limit";
    
    public static final String WHERE_KEY = "where";
    
    public static final String JS_SOURCE_KEY = "source-js";
    
    public static final String CSS_SOURCE_KEY = "source-css";
    
    public static final String JS_BUILD_KEY = "build-js";
    
    public static final String CSS_BUILD_KEY = "build-css";
    
    public static final String JS_COMP_KEY = "comp-js";
    
    public static final String CSS_COMP_KEY = "comp-css";
    
    
    public static String JS_TINY_SOURCE_FILE;
    public static String JS_TINY_COMPRESS_FILE;
    public static String CSS_TINY_SOURCE_FILE;
    public static String CSS_TINY_COMPRESS_FILE;

    /**
     * Creates a new instance of Configuration with defaults values.
     */
    public Configuration() {
        httpListener = true;
        httpPort = DEFAULT_HTTP_PORT;
        httpHost = DEFAULT_HTTP_HOST;

        ajpListener = false;
        ajpPort = DEFAULT_AJP_PORT;
        ajpHost = DEFAULT_AJP_HOST;

        ioThreads = 2;
        workerThreads = 32;
        bufferSize = 16384;
        buffersPerRegion = 20;
        directBuffers = true;
        
        requestsLimit = 100;

        logFilePath = URLUtils.removeTrailingSlashes(System.getProperty("java.io.tmpdir"))
                .concat(File.separator + "map4j.log");
        logToConsole = true;
        logToFile = true;
        logLevel = Level.INFO;
        
        tilePath = URLUtils.removeTrailingSlashes(System.getProperty("java.io.tmpdir"))
                .concat(File.separator + "mapTile");
        
        staticResourcesMounts = new ArrayList<>();
        HashMap<String, Object> browserStaticResourcesMountArgs = new HashMap<>();
        browserStaticResourcesMountArgs.put(STATIC_RESOURCES_MOUNT_WHAT_KEY, tilePath);
        browserStaticResourcesMountArgs.put(STATIC_RESOURCES_MOUNT_WHERE_KEY, "/geo");
        browserStaticResourcesMountArgs.put(STATIC_RESOURCES_MOUNT_WELCOME_FILE_KEY, "geo.html");
        staticResourcesMounts.add(browserStaticResourcesMountArgs);
        
        applicationLogicMounts = new ArrayList<>();
        HashMap<String, Object> browserApplicationLogicMountArgs = new HashMap<>();
        browserApplicationLogicMountArgs.put(APPLICATION_LOGIC_MOUNT_WHAT_KEY, "org.cunframework.sc.map4j.support.handle.applicationlogic.PingHandler");
        browserApplicationLogicMountArgs.put(APPLICATION_LOGIC_MOUNT_WHERE_KEY, "/ping");
        applicationLogicMounts.add(browserApplicationLogicMountArgs);
        
        webSocketMounts = new ArrayList<>();
        HashMap<String, Object> browserWebSocketMountArgs = new HashMap<>();
        browserWebSocketMountArgs.put(WEBSOCKET_MOUNT_WHAT_KEY, "org.cunframework.sc.map4j.support.handle.map.MapDownloadsHandler");
        browserWebSocketMountArgs.put(WEBSOCKET_MOUNT_WHERE_KEY, "/mapDownload");
        webSocketMounts.add(browserWebSocketMountArgs);
        
        loadScript();
    }
    
    /**
     * 使用配置文件路径创建新实例
     *
     * @param confFilePath 配置文件路径
     */
    public Configuration(final Path confFilePath) throws Exception {
        this(YamlLoader.getConfiguration(confFilePath));
    }

    /**
     * 使用配置文件路径创建新实例
     */
    public Configuration(Map<String, Object> conf) throws Exception {

        httpListener = YamlLoader.getAsBooleanOrDefault(conf, HTTP_LISTENER_KEY, false);
        httpPort = YamlLoader.getAsIntegerOrDefault(conf, HTTP_PORT_KEY, DEFAULT_HTTP_PORT);
        httpHost = YamlLoader.getAsStringOrDefault(conf, HTTP_HOST_KEY, DEFAULT_HTTP_HOST);

        ajpListener = YamlLoader.getAsBooleanOrDefault(conf, AJP_LISTENER_KEY, false);
        ajpPort = YamlLoader.getAsIntegerOrDefault(conf, AJP_PORT_KEY, DEFAULT_AJP_PORT);
        ajpHost = YamlLoader.getAsStringOrDefault(conf, AJP_HOST_KEY, DEFAULT_AJP_HOST);

        logFilePath = YamlLoader.getAsStringOrDefault(conf, LOG_FILE_PATH_KEY,
                URLUtils.removeTrailingSlashes(System.getProperty("java.io.tmpdir"))
                .concat(File.separator + "map4j.log"));
        
        tilePath = YamlLoader.getAsStringOrDefault(conf, TILE_PATH,
                URLUtils.removeTrailingSlashes(System.getProperty("java.io.tmpdir"))
                .concat(File.separator + "mapTile"));
        
        String _logLevel = YamlLoader.getAsStringOrDefault(conf, LOG_LEVEL_KEY, "INFO");
        logToConsole = YamlLoader.getAsBooleanOrDefault(conf, ENABLE_LOG_CONSOLE_KEY, true);
        logToFile = YamlLoader.getAsBooleanOrDefault(conf, ENABLE_LOG_FILE_KEY, true);
        
        ioThreads = YamlLoader.getAsIntegerOrDefault(conf, IO_THREADS_KEY, 2);
        workerThreads = YamlLoader.getAsIntegerOrDefault(conf, WORKER_THREADS_KEY, 32);
        bufferSize = YamlLoader.getAsIntegerOrDefault(conf, BUFFER_SIZE_KEY, 16384);
        buffersPerRegion = YamlLoader.getAsIntegerOrDefault(conf, BUFFERS_PER_REGION_KEY, 20);
        directBuffers = YamlLoader.getAsBooleanOrDefault(conf, DIRECT_BUFFERS_KEY, true);
        
        requestsLimit = YamlLoader.getAsIntegerOrDefault(conf, REQUESTS_LIMIT_KEY, 100);
        
        staticResourcesMounts = YamlLoader.getAsListOfMaps(conf, STATIC_RESOURCES_MOUNTS_KEY, new ArrayList());
        
        applicationLogicMounts = YamlLoader.getAsListOfMaps(conf, APPLICATION_LOGIC_MOUNTS_KEY, new ArrayList());
        
        webSocketMounts = YamlLoader.getAsListOfMaps(conf, WEBSOCKET_MOUNT_KEY, new ArrayList());
        
        Level level;

        try {
            level = Level.valueOf(_logLevel);
        } catch (Exception e) {
            LOGGER.info("参数值错误 {}: {}. 使用它的默认值 {}", "log-level", _logLevel, "INFO");
            level = Level.INFO;
        }

        logLevel = level;
        
        loadScript();
    }

    @SuppressWarnings("unchecked")
    private void loadScript(){
        InputStream io = SystemPath.getStream("scriptcss.yml");
        LOGGER.info("从 {} 载入脚本文件 ", "scriptcss.yml");
        Map<String, Object> scriptConf = null;
        try {
            scriptConf = YamlLoader.getConfiguration(io);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        
        Map jsSource = YamlLoader.getAsMap(scriptConf, JS_SOURCE_KEY);
        Map cssSource = YamlLoader.getAsMap(scriptConf, CSS_SOURCE_KEY);
        Map jsBuild = YamlLoader.getAsMap(scriptConf, JS_BUILD_KEY);
        Map cssBuild = YamlLoader.getAsMap(scriptConf, CSS_BUILD_KEY);
        Map jsComp = YamlLoader.getAsMap(scriptConf, JS_COMP_KEY);
        Map cssComp = YamlLoader.getAsMap(scriptConf, CSS_COMP_KEY);
        
        List<String> jsSourceList = Lists.newArrayList(jsSource.values());
        List<String> cssSourceList = Lists.newArrayList(cssSource.values());
        List<String> jsBuildList = Lists.newArrayList(jsBuild.values());
        List<String> cssBuildList = Lists.newArrayList(cssBuild.values());
        List<String> jsCompList = Lists.newArrayList(jsComp.values());
        List<String> cssCompList = Lists.newArrayList(cssComp.values());
        
        try { //先尝试加载压缩版及合并版
            LOGGER.info("尝试加载发布版脚本及样式表.");
            JS_TINY_SOURCE_FILE = this.appendScript(jsBuildList);
            CSS_TINY_SOURCE_FILE = this.appendScript(cssBuildList);
            JS_TINY_COMPRESS_FILE = this.appendScript(jsCompList);
            CSS_TINY_SOURCE_FILE = this.appendScript(cssCompList);
            LOGGER.info("加载发布版脚本及样式表完毕.");
        } catch (Exception e) { //否则尝试加载源文件版
            try {
                LOGGER.info("发布版加载失败,由于{},尝试加载开发版脚本及样式表.",e.getMessage());
                JS_TINY_SOURCE_FILE = this.appendScript(jsSourceList);
                CSS_TINY_SOURCE_FILE = this.appendScript(cssSourceList);
                LOGGER.info("加载开发版脚本及样式表完毕.");
            } catch (IOException e1) {
                LOGGER.error("脚本及样式表加载失败",e1);
            }
        }
    }
    
    /**
     * @return the httpListener
     */
    public final boolean isHttpListener() {
        return httpListener;
    }

    /**
     * @return the httpPort
     */
    public final int getHttpPort() {
        return httpPort;
    }

    /**
     * @return the httpHost
     */
    public final String getHttpHost() {
        return httpHost;
    }

    /**
     * @return the ajpListener
     */
    public final boolean isAjpListener() {
        return ajpListener;
    }

    /**
     * @return the ajpPort
     */
    public final int getAjpPort() {
        return ajpPort;
    }

    /**
     * @return the ajpHost
     */
    public final String getAjpHost() {
        return ajpHost;
    }

    /**
     * @return the logFilePath
     */
    public final String getLogFilePath() {
        return logFilePath;
    }

    /**
     * @return the logLevel
     */
    public final Level getLogLevel() {
        return logLevel;
    }

    /**
     * @return the logToConsole
     */
    public final boolean isLogToConsole() {
        return logToConsole;
    }

    /**
     * @return the logToFile
     */
    public final boolean isLogToFile() {
        return logToFile;
    }
    
    /**
     * @return the ioThreads
     */
    public final int getIoThreads() {
        return ioThreads;
    }

    /**
     * @return the workerThreads
     */
    public final int getWorkerThreads() {
        return workerThreads;
    }

    /**
     * @return the bufferSize
     */
    public final int getBufferSize() {
        return bufferSize;
    }

    /**
     * @return the buffersPerRegion
     */
    public final int getBuffersPerRegion() {
        return buffersPerRegion;
    }

    /**
     * @return the directBuffers
     */
    public final boolean isDirectBuffers() {
        return directBuffers;
    }

    public String getTilePath() {
        return tilePath;
    }

    public int getRequestsLimit() {
        return requestsLimit;
    }

    public List<Map<String, Object>> getApplicationLogicMounts() {
        return applicationLogicMounts;
    }

    public List<Map<String, Object>> getStaticResourcesMounts() {
        return staticResourcesMounts;
    }

    public List<Map<String, Object>> getWebSocketMounts() {
        return webSocketMounts;
    }
    
    private String appendScript(List<String> files) throws IOException{
        StringBuffer sb = new StringBuffer();
        for(String file : files){
            LOGGER.info("从 {} 位置导入文件...", file);
            InputStream io = SystemPath.getStream(file);
            List<String> lists = IOUtils.readLines(io);
            for(String str : lists){
                sb.append(str);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}

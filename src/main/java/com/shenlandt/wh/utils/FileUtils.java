package com.shenlandt.wh.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shenlandt.wh.config.Configuration;

/**
 *
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static final Path TMP_DIR = new File(System.getProperty("java.io.tmpdir")).toPath();

    public static Path getFileAbsoultePath(String path) {
        if (path == null) {
            return null;
        }

        return FileSystems.getDefault().getPath(path).toAbsolutePath();
    }

    public static int getFileAbsoultePathHash(Path path) {
        if (path == null) {
            return 0;
        }

        return Objects.hash(path.toString());
    }
    
    public static Configuration getConfiguration(String[] args) throws Exception {
        return getConfiguration(getConfigurationFilePath(args), false);
    }

    public static Configuration getConfiguration(String[] args, boolean silent) throws Exception {
        return getConfiguration(getConfigurationFilePath(args), silent);
    }

    public static Configuration getConfiguration(Path configurationFilePath, boolean silent) throws Exception {
        if (configurationFilePath != null) {
            LOGGER.info("从{}中抽取配置信息**",configurationFilePath.toString());
            return new Configuration(configurationFilePath);
        } else {
            LOGGER.info("载入默认配置设置 *******************************************");
            return new Configuration();
        }
    }

    public static Path getConfigurationFilePath(String[] args) {
        if (args != null) {
            for (String arg : args) {
                if (!arg.equals("--fork")) {
                    return getFileAbsoultePath(arg);
                }
            }
        }

        return null;
    }

    public static Path getTmpDirPath() {
        return TMP_DIR;
    }
    
    public static void logDownFail(String filePath,String tilePath,String failUrl){
        try {
            File file = new File(filePath);
            org.apache.commons.io.FileUtils.write(file, tilePath+";"+failUrl+"\r\n",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> getFailList(String filePath){
        try {
            File file = new File(filePath);
            if(file.exists())
                return org.apache.commons.io.FileUtils.readLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<String> readAndDeleteFailList(String filePath){
        try {
            File file = new File(filePath);
            if(file.exists()){
                List<String> list = org.apache.commons.io.FileUtils.readLines(file);
                org.apache.commons.io.FileUtils.write(file, "");
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FileUtils() {
    }
}
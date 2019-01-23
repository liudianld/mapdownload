package com.shenlandt.wh.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作工具类
 * @author Administrator
 *
 */
public class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	private static final String[] IMAGES_SUFFIXES = {
		"bmp", "jpg", "jpeg", "gif", "png", "tiff"
	};
	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtend(String filename) {
		return getExtend(filename, "");
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtend(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > 0) && (i < (filename.length() - 1))) {
				return (filename.substring(i+1)).toLowerCase();
			}
		}
		return defExt.toLowerCase();
	}

	/**
	 * 获取文件名称[不含后缀名]
	 * 
	 * @param
	 * @return String
	 */
	public static String getFilePrefix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
	}
	
	/**
	 * 获取文件名称[不含后缀名]
	 * 不去掉文件目录的空格
	 * @param
	 * @return String
	 */
	public static String getFilePrefix2(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex);
	}
	
	/**
	 * 反回指定目录下的所有文件.到一个数组里
	 * @param directoryName 要扫描的目录
	 * @param files 扫描后保存在这里
	 */
	public static void listFile(String directoryName, List<File> files) {
		File directory = new File(directoryName);

		File[] fList = directory.listFiles();
		if (fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					files.add(file);
				} else if (file.isDirectory()) {
					listFile(file.getAbsolutePath(), files);
				}
			}
		}
	}
	
	/**
	 * 反回指定目录下的所有文件.到一个数组里
	 * @param directoryName 要扫描的目录
	 * @param files 扫描后保存在这里
	 */
	public static void listFileName(String directoryName, List files) {
		File directory = new File(directoryName);

		File[] fList = directory.listFiles();
		if (fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					System.out.println(file.getName());
					System.out.println(file.getPath());
					files.add(file.getName());
				} else if (file.isDirectory()) {
					listFileName(file.getAbsolutePath(), files);
				}
			}
		}
	}
	
	/**
	 * 反回指定目录下的所有文件.到一个数组里
	 * @param projectPath 工程路径如"d:/tomcat/toto/"
	 * @param relativePath 相对工程的路径""
	 * @param files
	 */
	public static void listRFileName(String projectPath, String relativePath,
			List files) {
		File directory = new File(projectPath + relativePath);

		File[] fList = directory.listFiles();
		if (fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					files.add(relativePath + "/" + file.getName());
				} else if (file.isDirectory()) {
					String temp = file.getAbsolutePath().substring(
							projectPath.length(),
							file.getAbsolutePath().length());
					listRFileName(projectPath, temp, files);
				}
			}
		}
	}
	
	/**
	 * 文件复制
	 *方法摘要：这里一句话描述方法的用途
	 *@param
	 *@return void
	 */
	public static void copyFile(String inputFile,String outputFile) throws FileNotFoundException{
		File sFile = new File(inputFile);
		File tFile = new File(outputFile);
		FileInputStream fis = new FileInputStream(sFile);
		FileOutputStream fos = new FileOutputStream(tFile);
		int temp = 0;  
		byte[] buf = new byte[10240];
        try {  
        	while((temp = fis.read(buf))!=-1){   
        		fos.write(buf, 0, temp);   
            }   
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally{
            try {
            	fis.close();
            	fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } 
	}
	/**
	 * 判断文件是否为图片<br>
	 * <br>
	 * 
	 * @param filename
	 *            文件名<br>
	 *            判断具体文件类型<br>
	 * @return 检查后的结果<br>
	 * @throws Exception
	 */
	public static boolean isPicture(String filename) {
		// 文件名称为空的场合
		if (StringUtils.isBlank(filename)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = getExtend(filename);
		//String tmpName = filename;
		// 声明图片后缀名数组
		String imgeArray[][] = { { "bmp", "0" }, { "dib", "1" },
				{ "gif", "2" }, { "jfif", "3" }, { "jpe", "4" },
				{ "jpeg", "5" }, { "jpg", "6" }, { "png", "7" },
				{ "tif", "8" }, { "tiff", "9" }, { "ico", "10" } };
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (imgeArray[i][0].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否是图片附件
	 *
	 * @param filename
	 * @return
	 */
	public static boolean isImage(String filename) {
	    if (filename == null || filename.trim().length() == 0) return false;
	    return ArrayUtils.contains(IMAGES_SUFFIXES, FilenameUtils.getExtension(filename).toLowerCase());
	}
	
	/**
	 * 检查文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean isExist(String path){
	    return new File(path).exists();
	}

	/**
	 * 判断文件是否为DWG<br>
	 * <br>
	 * 
	 * @param filename
	 *            文件名<br>
	 *            判断具体文件类型<br>
	 * @return 检查后的结果<br>
	 * @throws Exception
	 */
	public static boolean isDwg(String filename) {
		// 文件名称为空的场合
		if (StringUtils.isBlank(filename)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = getExtend(filename);
		// 声明图片后缀名数组
		if (tmpName.equals("dwg")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 删除指定的文件
	 * 
	 * @param strFileName
	 *            指定绝对路径的文件名
	 * @return 如果删除成功true否则false
	 */
	public static boolean delete(String strFileName) {
		File fileDelete = new File(strFileName);

		if (!fileDelete.exists() || !fileDelete.isFile()) {
			System.out.println("错误: " + strFileName + "不存在!");
			return false;
		}

		System.out.println("--------成功删除文件---------"+strFileName);
		return fileDelete.delete();
	}
	
	/**
	 * 图片格式化为base64字符串
	 * @param img
	 * @param formatName
	 * @return
	 */
	public static String imageToBase64(final BufferedImage img, final String formatName) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, formatName, Base64.getEncoder().wrap(os));
            return os.toString(StandardCharsets.ISO_8859_1.name());
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

	/**
	 * base64字符串转换为图片
	 * @param base64String
	 * @return
	 */
    public static BufferedImage base64ToImage(final String base64String) {
        try {
            return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64String)));
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
    
    public static void main(String[] args) {
        String BLANK_IMG_BASE64 = "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";
        base64ToImage(BLANK_IMG_BASE64);
    }
}

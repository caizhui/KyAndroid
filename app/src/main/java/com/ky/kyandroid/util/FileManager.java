package com.ky.kyandroid.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Blob;

/**
 * 类名称：文件管理工具类
 * JIRA：
 * 类描述：
 * 		文件操作工具类&lt;br/&gt;实现文件的创建、删除、复制、压缩、解压以及目录的创建、删除、复制等功能
 *
 *
 * @author msi
 * @date 2017年6月17日 下午1:05:22
 * @jira JIRA:http://192.168.0.6:88/browse/
 * @updateRemark 修改备注：
 *     
 */
public class FileManager {

	/**
	 * 获取文件名，去掉后缀<Br>
	 * add by 李清泉 2012-6-5 下午3:25:14<br>
	 */
	public static String getPrefix(String name) {
		int index = name.lastIndexOf(".");
		return name.substring(0, index);
	}

	/**
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static void copyFolder(String oldPath, String newPath) {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					input = new FileInputStream(temp);
					output = new FileOutputStream(newPath + "/ "
							+ (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/ " + file[i], newPath + "/ "
							+ file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错 ");
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
		}
	}

	public static void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		// removeFile(oldPath);
	}

	public static void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		// removeFolder(oldPath);
	}

	/**
	 * 
	 * 功能描述：复制单个文件，如果目标文件存在，则不覆盖
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @return 返回： 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String descFileName) {
		return copyFileCover(srcFileName, descFileName, false);

	}

	/**
	 * 
	 * 功能描述：复制单个文件
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @param coverlay
	 *            如果目标文件已存在，是否覆盖
	 * @return 返回： 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFileCover(String srcFileName,
			String descFileName, boolean coverlay) {
		File srcFile = new File(srcFileName);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			System.out.println("复制文件失败，源文件" + srcFileName + "不存在!");
			return false;
		}
		// 判断源文件是否是合法的文件
		else if (!srcFile.isFile()) {
			System.out.println("复制文件失败，" + srcFileName + "不是一个文件!");
			return false;
		}
		File descFile = new File(descFileName);
		// 判断目标文件是否存在
		if (descFile.exists()) {
			// 如果目标文件存在，并且允许覆盖
			if (coverlay) {
				System.out.println("目标文件已存在，准备删除!");
				if (!delFile(descFileName)) {
					System.out.println("删除目标文件" + descFileName + "失败!");
					return false;
				}
			} else {
				System.out.println("复制文件失败，目标文件" + descFileName + "已存在!");
				return false;
			}
		} else {
			if (!descFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				System.out.println("目标文件所在的目录不存在，创建目录!");
				// 创建目标文件所在的目录
				if (!descFile.getParentFile().mkdirs()) {
					System.out.println("创建目标文件所在的目录失败!");
					return false;
				}
			}
		}

		// 准备复制文件
		// 读取的位数
		int readByte = 0;
		InputStream ins = null;
		OutputStream outs = null;
		try {
			// 打开源文件
			ins = new FileInputStream(srcFile);
			// 打开目标文件的输出流
			outs = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = ins.read(buf)) != -1) {
				// 将读取的字节流写入到输出流
				outs.write(buf, 0, readByte);
			}
			System.out.println("复制单个文件" + srcFileName + "到" + descFileName
					+ "成功!");
			return true;
		} catch (Exception e) {
			System.out.println("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，首先关闭输出流，然后再关闭输入流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException ine) {
					ine.printStackTrace();
				}
			}
		}
	}

	/**
	 * 生产二进制文件
	 * 
	 * @param bytes
	 * @param path
	 * @return
	 */
	public static boolean writeByteFile(byte[] bytes, String path)
			throws Exception {
		OutputStream outs = null;
		// 打开目标文件的输出流
		try {
			outs = new BufferedOutputStream(new FileOutputStream(path));
			outs.write(bytes);
			outs.flush();
		} finally {
			// 关闭输出流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;

	}

	/**
	 * 
	 * 功能描述：复制整个目录的内容，如果目标目录存在，则不覆盖
	 * 
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @return 返回： 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String descDirName) {
		return copyDirectoryCover(srcDirName, descDirName, false);
	}

	/**
	 * 
	 * 功能描述：复制整个目录的内容
	 * 
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @param coverlay
	 *            如果目标目录存在，是否覆盖
	 * @return 返回： 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectoryCover(String srcDirName,
			String descDirName, boolean coverlay) {
		File srcDir = new File(srcDirName);
		// 判断源目录是否存在
		if (!srcDir.exists()) {
			System.out.println("复制目录失败，源目录" + srcDirName + "不存在!");
			return false;
		}
		// 判断源目录是否是目录
		else if (!srcDir.isDirectory()) {
			System.out.println("复制目录失败，" + srcDirName + "不是一个目录!");
			return false;
		}
		// 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
		if (!descDirName.endsWith(File.separator)) {
			descDirName = descDirName + File.separator;
		}
		File descDir = new File(descDirName);
		// 如果目标文件夹存在
		if (descDir.exists()) {
			if (coverlay) {
				// 允许覆盖目标目录
				System.out.println("目标目录已存在，准备删除!");
				if (!delFile(descDirName)) {
					System.out.println("删除目录" + descDirName + "失败!");
					return false;
				}
			} else {
				System.out.println("目标目录复制失败，目标目录" + descDirName + "已存在!");
				return false;
			}
		} else {
			// 创建目标目录
			System.out.println("目标目录不存在，准备创建!");
			if (!descDir.mkdirs()) {
				System.out.println("创建目标目录失败!");
				return false;
			}

		}

		boolean flag = true;
		// 列出源目录下的所有文件名和子目录名
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 如果是一个单个文件，则直接复制
			if (files[i].isFile()) {
				flag = copyFile(files[i].getAbsolutePath(), descDirName
						+ files[i].getName());
				// 如果拷贝文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 如果是子目录，则继续复制目录
			if (files[i].isDirectory()) {
				flag = copyDirectory(files[i].getAbsolutePath(), descDirName
						+ files[i].getName());
				// 如果拷贝目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}
		if (!flag) {
			System.out.println("复制目录" + srcDirName + "到" + descDirName + "失败!");
			return false;
		}
		System.out.println("复制目录" + srcDirName + "到" + descDirName + "成功!");
		return true;

	}

	/**
	 * 
	 * 功能描述：删除文件，可以删除单个文件或文件夹
	 * 
	 * @param
	 *
	 * @return 返回： 如果删除成功，则返回true，否是返回false
	 */
	public static boolean createTempFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			System.out.println("文件存在，无法创建!");
			return false;
		}
		try {
			String suffix = getFileSuffix(filePath);
			String newFilePath = filePath.replaceAll(suffix, "");
			String fileName = newFilePath.substring(
					newFilePath.lastIndexOf(File.separator) + 1,
					newFilePath.length());
			File.createTempFile(fileName, suffix, new File(file.getParent()));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建临时文件出错" + e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * 
	 * 功能描述：删除文件，可以删除单个文件或文件夹
	 * 
	 * @param
	 *
	 * @return 返回： 如果删除成功，则返回true，否是返回false
	 */
	private static String getFileSuffix(String filePath) {
		return filePath.substring(filePath.lastIndexOf("."), filePath.length());
	}

	/**
	 * 
	 * 功能描述：删除文件，可以删除单个文件或文件夹
	 * 
	 * @param fileName
	 *            被删除的文件名
	 * @return 返回： 如果删除成功，则返回true，否是返回false
	 */
	public static boolean delFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("删除文件失败，" + fileName + "文件不存在!");
			return false;
		} else {
			if (file.isFile()) {
				return deleteFile(fileName);
			} else {
				return deleteDirectory(fileName);
			}
		}
	}

	/**
	 * 功能描述：删除单个文件
	 * 
	 * @param fileName
	 *            被删除的文件名
	 * @return 返回： 如果删除成功，则返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				// System.out.println("删除单个文件" + fileName + "成功!");
				return true;
			} else {
				System.out.println("删除单个文件" + fileName + "失败!");
				return false;
			}
		} else {
			System.out.println("删除单个文件失败，" + fileName + "文件不存在!");
			return false;
		}
	}

	/**
	 * 
	 * 功能描述：删除目录及目录下的文件
	 * 
	 * @param dirName
	 *            被删除的目录所在的文件路径
	 * @return 返回： 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName) {
		if (!dirName.endsWith(File.separator)) {
			dirName = dirName + File.separator;
		}
		File dirFile = new File(dirName);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			System.out.println("删除目录失败" + dirName + "目录不存在!");
			return false;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子目录
			if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i].getAbsolutePath());
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			} else if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}

		}

		if (!flag) {
			System.out.println("删除目录失败!");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			// System.out.println("删除目录" + dirName + "成功!");
			return true;
		} else {
			System.out.println("删除目录" + dirName + "失败!");
			return false;
		}

	}

	/**
	 * 
	 * 功能描述：创建单个文件
	 * 
	 * @param descFileName
	 *            文件名，包含路径
	 * @return 返回： 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createFile(String descFileName) {
		File file = new File(descFileName);
		if (file.exists()) {
			System.out.println("文件" + descFileName + "已存在!");
			return false;
		}
		if (descFileName.endsWith(File.separator)) {
			System.out.println(descFileName + "为目录，不能创建目录!");
			return false;
		}
		if (!file.getParentFile().exists()) {
			// 如果文件所在的目录不存在，则创建目录
			if (!file.getParentFile().mkdirs()) {
				System.out.println("创建文件所在的目录失败!");
				return false;
			}
		}

		// 创建文件
		try {
			if (file.createNewFile()) {
				// System.out.println(descFileName + "文件创建成功!");
				return true;
			} else {
				System.out.println(descFileName + "文件创建失败!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(descFileName + "文件创建失败!");
			return false;
		}

	}

	/**
	 * 
	 * 功能描述：创建目录
	 * 
	 * @param descDirName
	 *            目录名,包含路径
	 * @return 返回： 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createDirectory(String descDirName) {
		if (!descDirName.endsWith(File.separator)) {
			descDirName = descDirName + File.separator;
		}
		File descDir = new File(descDirName);
		if (descDir.exists()) {
			System.out.println("目录" + descDirName + "已存在!");
			return false;
		}
		// 创建目录
		if (descDir.mkdirs()) {
			// System.out.println("目录" + descDirName + "创建成功!");
			return true;
		} else {
			System.out.println("目录" + descDirName + "创建失败!");
			return false;
		}

	}

	/**
	 * 将字符串写入txt文件中，并返回是否写入成功
	 * 
	 * @param filePath
	 *            写入的文件路径
	 * @param content
	 *            被写入的 文件内容
	 * @return boolean 是否写入成功
	 * @throws IOException
	 */
	public static boolean writeTextInFile(String filePath, String content) {
		Writer fw = null;
		FileOutputStream fo = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				FileManager.createFile(filePath);
			}
			fo = new FileOutputStream(file);
			fw = new OutputStreamWriter(fo, "UTF-8");
			fw.write(content, 0, content.length());
			fw.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fo != null) {
					fo.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 调用common-io包，写入String。<br>
	 * add by 李清泉 2012-5-28 上午09:33:34<br>
	 */
	/*
	 * public static void writeTextToFile(String filePath, String content)
	 * throws Exception { FileUtils.writeStringToFile(new File(filePath),
	 * content, "UTF-8"); }
	 */

	/**
	 * 从txt文件中读取文件并以字符串方式返回
	 * 
	 * @param filePath
	 *            读取的文件路径
	 * @return String 读取的文件内容
	 */
	public static String readTextFromFile(String filePath) {
		FileInputStream fls = null;
		InputStreamReader ins = null;
		BufferedReader br = null;
		try {
			fls = new FileInputStream(filePath);
			ins = new InputStreamReader(fls, "UTF-8");
			br = new BufferedReader(ins);
			StringBuffer tempSB = new StringBuffer();
			String temp;
			while ((temp = br.readLine()) != null) {
				tempSB.append(temp);
			}

			return tempSB.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (br != null) {
					br.close();
					br = null;
				}
				if (ins != null) {
					ins.close();
					ins = null;
				}
				if (fls != null) {
					fls.close();
					fls = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 支持直接传File对象<Br>
	 * add by 李清泉 2012-6-6 上午8:52:38<br>
	 */
	public static String readTextFromFile(File file) {
		return readTextFromFile(file.getAbsolutePath());
	}

	/**
	 * 清空文件夹下面对应的所有文件
	 * 
	 * @param path
	 */
	public static void clearFolder(String path) {
		try {
			File file = new File(path);
			File[] files = file.listFiles();
			for (File fileItem : files) {
				deleteFile(path + File.separator + fileItem.getName());
			}
		} catch (Exception e) {
			System.out.println("删除整个文件夹内容操作出错 ");
			e.printStackTrace();
		}
	}

	/**
	 * 从txt文件中读取文件并以字符串方式返回
	 * 
	 * @param
	 *
	 * @return String 读取的文件内容
	 */
	public static byte[] readBytesFromFile(InputStream fin) {
		try {
			// 读取的位数
			@SuppressWarnings("unused")
			int readByte = 0;
			byte[] buff = new byte[fin.available()];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = fin.read(buff)) != -1) {

			}
			return buff;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将BLOB对象转换为byte[]数组
	 * 
	 * @param blob
	 * @return byte[]
	 */
	public static byte[] blobToBytes(Blob blob) {
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;
			while (offset < len
					&& (read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}
			return bytes;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				return null;
			}

		}
	}

	/**
	 * 将BLOB对象转换为byte[]数组
	 * 
	 * @param blob
	 * @return byte[]
	 */
	public static byte[] blobToOutputStream(Blob blob) {
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;
			while (offset < len
					&& (read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}
			return bytes;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				return null;
			}
		}
	}

	public static void main(String[] args) throws IOException {
	}
}

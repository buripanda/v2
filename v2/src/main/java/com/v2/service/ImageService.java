package com.v2.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.v2.bean.PropartyConfig;
import com.v2.bean.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageService {
	
	@Autowired
	PropartyConfig propartyConfig;
	
	/**
	 * 画像ファイルを保存する
	 * @param user
	 * @param imageFile
	 * @throws Exception
	 */
	public void saveImageFile(User user, MultipartFile imageFile) throws Exception {
		
		File originalFile = null;
		
		// 格納フォルダを作成
		Path p = Paths.get(propartyConfig.pathroot + "users" + File.separator + user.id);
		if (!Files.exists(p))
			Files.createDirectory(p);

		// 画像ファイルがない場合
		if (imageFile.isEmpty()) {
			originalFile = new File(propartyConfig.pathroot + "default.png");
		} else { 
			originalFile = new File(propartyConfig.pathroot + "users" + File.separator + user.id + File.separator + imageFile.getOriginalFilename());
			imageFile.transferTo(originalFile);
		}

		// 画像を保存（メイン）
		BufferedImage originalImage = ImageIO.read(originalFile);
		BufferedImage mainImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 500, 500);
		File mainFile = new File(propartyConfig.pathroot + "users" + File.separator + user.id + File.separator + originalFile.getName());
		ImageWriter imageWriter = ImageIO
				.getImageWritersBySuffix(FilenameUtils.getExtension(originalFile.getName().toLowerCase()))
				.next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(new FileOutputStream(mainFile));
		imageWriter.setOutput(ios);
		imageWriter.write(mainImage);
		ios.flush();
		ios.close();
		imageWriter.dispose();

		// 画像を保存（ミニ）
		BufferedImage miniImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 100, 100);
		File miniFile = new File(propartyConfig.pathroot + "users" + File.separator + user.id + File.separator + "mini_" + originalFile.getName());
		ImageWriter imageWriter2 = ImageIO
				.getImageWritersBySuffix(FilenameUtils.getExtension(originalFile.getName().toLowerCase()))
				.next();
		ImageOutputStream ios2 = ImageIO.createImageOutputStream(new FileOutputStream(miniFile));
		imageWriter2.setOutput(ios2);
		imageWriter2.write(miniImage);
		ios2.flush();
		ios2.close();
		imageWriter2.dispose();

		user.imageFile = originalFile.getName();

	}
	/**
	 * 画像ファイルを保存する（デフォルト画像）
	 * @param user
	 * @param imageFile
	 * @throws Exception
	 */
	public void saveImageFileDefault(int id) throws Exception {
		
		File	originalFile = new File(propartyConfig.pathroot + "default.png");
		
		// 格納フォルダを作成
		Path p = Paths.get(propartyConfig.pathroot + "users" + File.separator + id);
		if (!Files.exists(p))
			Files.createDirectory(p);

		// 画像を保存（メイン）
		BufferedImage originalImage = ImageIO.read(originalFile);
		BufferedImage mainImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 300, 300);
		File mainFile = new File(propartyConfig.pathroot + "users" + File.separator + id + File.separator + originalFile.getName());
		ImageWriter imageWriter = ImageIO
				.getImageWritersBySuffix(FilenameUtils.getExtension(originalFile.getName().toLowerCase()))
				.next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(new FileOutputStream(mainFile));
		imageWriter.setOutput(ios);
		imageWriter.write(mainImage);
		ios.flush();
		ios.close();
		imageWriter.dispose();

		// 画像を保存（ミニ）
		BufferedImage miniImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 100, 100);
		File miniFile = new File(propartyConfig.pathroot + "users" + File.separator + id + File.separator + "mini_" + originalFile.getName());
		ImageWriter imageWriter2 = ImageIO
				.getImageWritersBySuffix(FilenameUtils.getExtension(originalFile.getName().toLowerCase()))
				.next();
		ImageOutputStream ios2 = ImageIO.createImageOutputStream(new FileOutputStream(miniFile));
		imageWriter2.setOutput(ios2);
		imageWriter2.write(miniImage);
		ios2.flush();
		ios2.close();
		imageWriter2.dispose();

	}
	
	/**
	 * 通常画像データを取得
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public byte[] getFileBytes(int id, String fileName) throws Exception {
		
		//通常画像ファイル
		File fileImg = new File(propartyConfig.pathroot + "users" + File.separator + id +  File.separator + fileName);

		//バイト列に変換
		return Files.readAllBytes(fileImg.toPath());

	}
	
	/**
	 * Mini画像データを取得
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public byte[] getFileBytesMini(int id, String fileName) throws Exception {
		
		//通常画像ファイル
		File fileImg = new File(propartyConfig.pathroot + "users" + File.separator + id +  File.separator + "mini_" + fileName);

		//バイト列に変換
		return Files.readAllBytes(fileImg.toPath());

	}
	
	/**
	 * 画像データを取得（共通）
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public byte[] getFileBytesCommon(String fileName) throws Exception {
		
		//通常画像ファイル
		File fileImg = new File(propartyConfig.pathroot + fileName);

		//バイト列に変換
		return Files.readAllBytes(fileImg.toPath());

	}


}

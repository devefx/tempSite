package com.devefx.gamedata.parser;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class WASFile {
	
	private RandomAccessFile file;
	private FileChannel channel;
	private ByteBuffer buffer;
	
	private short	flag;
	private short 	headerSize;
	private short 	group;
	private short 	frame;
	private short 	width;
	private short 	height;
	private short	centerX;
	private short	centerY;
	private short[] palette;
	private int[][] offset;
	
	public WASFile() {
		palette = new short[256];
	}
	
	public boolean open(String filename) throws IOException {
		try {
			file = new RandomAccessFile(filename, "rw");
			channel = file.getChannel();
			return open(channel.map(MapMode.READ_WRITE, 0, file.length()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean open(byte[] data) throws IOException {
		return open(ByteBuffer.wrap(data));
	}
	
	public boolean open(ByteBuffer buffer) throws IOException {
		this.buffer = buffer;
		return process();
	}
	
	public int[][] getTGA(int group, int frame) {
		if (flag == 0x5053) {
			int pos = offset[group][frame] + headerSize + 4;
			buffer.position(pos);
			
			int centerX = readSwappedInt();
			int centerY = readSwappedInt();
			int width = readSwappedInt();
			int height = readSwappedInt();
			int[] lineOffset = new int[height];
			for (int i = 0; i < height; i++) {
				lineOffset[i] = readSwappedInt();
			}
			
			int[][] pixels = new int[height][width];
			int repeat;
			for (int y = 0; y < height; y++) {
				int x = 0;
				buffer.position(pos + lineOffset[y]);
				while (x < width) {
					byte b = buffer.get();
					switch (b & 0xC0) {
					case 0:
						if ((b & 0x20) > 0) {
							int c = palette[buffer.get() & 0xff];
							pixels[y][x++] = (c + ((b & 0x1F) << 16));
						} else if (b != 0) {
							repeat = b & 0x1F;
							b = buffer.get();
							int c = palette[buffer.get() & 0xff];
							for (int i = 0; i < repeat; i++) {
								pixels[y][x++] = (c + ((b & 0x1F) << 16));
							}
						} else {
							if (x > width) {
								continue;
							} else if (x != 0) {
								x = width;
							}
						}
						break;
					case 64:
						repeat = b & 0x3F;
						for (int i = 0; i < repeat; i++) {
							int n = buffer.get() & 0xff;
							pixels[y][x++] = palette[n] + 2031616;
						}
						break;
					case 128:
						repeat = b & 0x3F;
						int index = buffer.get() & 0xff;
						for (int i = 0; i < repeat; i++) {
							pixels[y][x++] = palette[index] + 2031616;
						}
						break;
					case 192:
						x += (b & 0x3F);
						break;
					}
				}
			}
			output("F:\\0.png", pixels, this.centerX - centerX, this.centerY - centerY, width, height);
			return pixels;
		}
		return null;
	}
	
	public void output(String filename, int[][] pixels, int x, int y, int frameWidth, int frameHeight) {
		ImageWriter imgWriter = null;
		Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("png");
		if (it.hasNext()) {
			imgWriter = it.next();
		}
		if (imgWriter != null) {
			BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
			WritableRaster raster = image.getRaster();
			
			int minX = raster.getMinX();
			int minY = raster.getMinY();
			int width = raster.getWidth();
			int height = raster.getHeight();
			int[] color = new int[4];
			for (int y1 = 0; y1 < frameHeight; y1++) {
				if (y1 + y >= minY) {
					if (y1 + y >= minY + height) {
						break;
					}
					for (int x1 = 0; x1 < frameWidth; x1++) {
						if (x1 + x >= minX) {
							if (x1 + x >= minX + width) {
								break;
							}
							if (pixels[y1][x1] >>> 16 != 0) {
								color[0] = (100 * (pixels[y1][x1] >>> 11 & 0x1F) / 31 * 255 / 100);
								color[1] = (100 * (pixels[y1][x1] >> 5 & 0x3F) / 63 * 255 / 100);
								color[2] = (100 * (pixels[y1][x1] & 0x1F) / 31 * 255 / 100);
								color[3] = (100 * (pixels[y1][x1] >>> 16 & 0x1F) / 31 * 255 / 100);
							} else {
								color[3] = 0;
							}
							raster.setPixel(x1 + x, y1 + y, color);
						}
					}
				}
			}
			
			File outputFile = new File(filename);
			try {
				ImageOutputStream imgOut = ImageIO.createImageOutputStream(outputFile);
				imgWriter.setOutput(imgOut);
		        IIOImage iioImage = new IIOImage(image, null, null);
		        imgWriter.write(iioImage);
		        imgOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close() throws IOException {
		channel.close();
		file.close();
	}
	
	int readSwappedInt() {
		return (buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 8) 
				+ ((buffer.get() & 0xFF) << 16) + ((buffer.get() & 0xFF) << 24);
	}
	
	int readSwappedUnsignedInt() {
		int low = (buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 8) 
				+ ((buffer.get() & 0xFF) << 16);
		return ((buffer.get() & 0xFF) << 24) + (low & 0xFFFFFFFF);
	}
	
	short readSwappedShort() {
		return (short) ((buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 8));
	}
	
	boolean process() throws IOException {
		flag = readSwappedShort();
		headerSize = readSwappedShort();
		if (flag != 0x5053 || headerSize < 12) {
			close();
			return false;
		}
		group = readSwappedShort();
		frame = readSwappedShort();
		width = readSwappedShort();
		height = readSwappedShort();
		centerX = readSwappedShort();
		centerY = readSwappedShort();
		
		if (headerSize > 12) {
			byte[] delayLine = new byte[headerSize - 12];
			buffer.get(delayLine);
		}
		buffer.position(headerSize + 4);
		for (int i = 0; i < 256; i++) {
			palette[i] = readSwappedShort();
		}
		
		buffer.position(headerSize + 4 + 512);
		offset = new int[group][frame];
		for (int i = 0; i < group; i++) {
			for (int j = 0; j < frame; j++) {
				offset[i][j] = readSwappedInt();
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException {

		WASFile file = new WASFile();
		if (file.open("F:\\005944E2.was")) {
			file.getTGA(0, 0);
		}
		
	}
	
	
}

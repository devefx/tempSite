package com.devefx.gamedata.parser;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.devefx.gamedata.parser.struct.SequenceFrame;
import com.devefx.gamedata.parser.struct.WASFrame;
import com.devefx.gamedata.parser.struct.WASInfo;

public class WASFile extends WASInfo {
	
	private RandomAccessFile file;
	private FileChannel channel;
	private ByteBuffer buffer;
	
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
	
	public WASFrame get(int group, int frame) {
		int position = this.offset[group][frame] + this.headerLen + 4;
		buffer.position(position);
		
		WASFrame wasFrame = new WASFrame();
		wasFrame.centerX = readSwappedInt();
		wasFrame.centerY = readSwappedInt();
		wasFrame.width = readSwappedInt();
		wasFrame.height = readSwappedInt();
		
		int[] lineOffset = new int[wasFrame.height];
		for (int i = 0; i < wasFrame.height; i++) {
			lineOffset[i] = readSwappedInt();
		}
		
		wasFrame.pixels = new int[wasFrame.height][wasFrame.width];
		int repeat;
		for (int y = 0; y < wasFrame.height; y++) {
			int x = 0;
			buffer.position(position + lineOffset[y]);
			while (x < wasFrame.width) {
				byte b = buffer.get();
				switch (b & 0xC0) {
				case 0:
					if ((b & 0x20) > 0) {
						int alpha = b & 0x1F;
						int c = this.palette[buffer.get() & 0xff];
						//wasFrame.pixels[y][x++] = (c + (alpha << 16));
						wasFrame.pixels[y][x++] = RGB565to888(Alpha565(c, alpha), alpha * 8);
					} else if (b != 0) {
						repeat = b & 0x1F;
						int alpha = buffer.get() & 0x1F;
						int c = this.palette[buffer.get() & 0xff];
						for (int i = 0; i < repeat; i++) {
							//wasFrame.pixels[y][x++] = (c + (alpha << 16));
							wasFrame.pixels[y][x++] = RGB565to888(Alpha565(c, alpha), alpha * 8);
						}
					} else {
						if (x > wasFrame.width) {
							continue;
						} else if (x != 0) {
							x = wasFrame.width;
						}
					}
					break;
				case 64:
					repeat = b & 0x3F;
					for (int i = 0; i < repeat; i++) {
						int index = buffer.get() & 0xff;
						//wasFrame.pixels[y][x++] = this.palette[index] + 0x1F0000;
						wasFrame.pixels[y][x++] = RGB565to888(this.palette[index], 0xff);
					}
					break;
				case 128:
					repeat = b & 0x3F;
					int index = buffer.get() & 0xff;
					for (int i = 0; i < repeat; i++) {
						//wasFrame.pixels[y][x++] = this.palette[index] + 0x1F0000;
						wasFrame.pixels[y][x++] = RGB565to888(this.palette[index], 0xff);
					}
					break;
				case 192:
					x += (b & 0x3F);
					break;
				}
			}
		}
	//	output(String.format("F:\\%s-%s.png", group, frame), wasFrame.pixels, this.centerX - wasFrame.centerX,
	//			this.centerY - wasFrame.centerY, wasFrame.width, wasFrame.height);
		return wasFrame;
	}
	
	public SequenceFrame getSequenceFrame() {
		int maxWidth = 0;
		int maxHeight = 0;
		WASFrame[][] frames = new WASFrame[group][frame];
		for (int i = 0; i < group; i++) {
			for (int j = 0; j < frame; j++) {
				WASFrame frame = get(i, j);
				if (frame.width > maxWidth) {
					maxWidth = frame.width;
				}
				if (frame.height > maxHeight) {
					maxHeight = frame.height;
				}
				frames[i][j] = frame;
			}
		}
		SequenceFrame sequenceFrame = new SequenceFrame(maxWidth, maxHeight, group, frame);
		for (int i = 0; i < group; i++) {
			for (int j = 0; j < frame; j++) {
				WASFrame was = frames[i][j];
				sequenceFrame.centerX[i][j] = this.centerX - was.centerX;
				sequenceFrame.centerY[i][j] = this.centerY - was.centerY;
				for (int y = 0; y < was.height; y++) {
					for (int x = 0; x < was.width; x++) {
						int top = (was.height - y - 1) + (group - i - 1) * maxHeight;
						sequenceFrame.pixels[top][x + j * maxWidth] = was.pixels[y][x];
					}
				}
			}
		}
		return sequenceFrame;
	}
	
	
	public static int Alpha565(int src, int alpha) {
		
		int r = (src & 0xF800) >> 11;
		int g = (src & 0x07E0) >> 5;
		int b = (src & 0x001F) >> 0;
		
		r = (r * alpha) >> 5;
		g = (g * alpha) >> 5;
		b = (b * alpha) >> 5;
		
		return r << 11 | g << 5 | b;
	}
	
	public static int RGB565to888(int color, int alpha) {
		
		int r = (color >> 11) & 0x1f;
		int g = (color >>  5) & 0x3f;
		int b = (color      ) & 0x1f;
		
		r = (r << 3) | (r >> 2);
		g = (g << 2) | (g >> 4);
		b = (b << 3) | (b >> 2);
		
		return ((alpha & 0xff) << 24) + ((r & 0xff) << 16) + ((g & 0xff) << 8) + ((b & 0xff) << 0);
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
		this.flag = readSwappedShort();
		this.headerLen = readSwappedShort();
		if (this.flag != 0x5053 || this.headerLen < 12) {
			close();
			return false;
		}
		this.group = readSwappedShort();
		this.frame = readSwappedShort();
		this.width = readSwappedShort();
		this.height = readSwappedShort();
		this.centerX = readSwappedShort();
		this.centerY = readSwappedShort();
		if (this.headerLen > 12) {
			byte[] delayLine = new byte[this.headerLen - 12];
			buffer.get(delayLine);
		}
		for (int i = 0; i < 256; i++) {
			this.palette[i] = readSwappedShort();
		}
		this.offset = new int[this.group][this.frame];
		for (int i = 0; i < this.group; i++) {
			for (int j = 0; j < this.frame; j++) {
				this.offset[i][j] = readSwappedInt();
			}
		}
		return true;
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
								/*color[0] = (100 * (pixels[y1][x1] >>> 11 & 0x1F) / 31 * 255 / 100);
								color[1] = (100 * (pixels[y1][x1] >> 5 & 0x3F) / 63 * 255 / 100);
								color[2] = (100 * (pixels[y1][x1] & 0x1F) / 31 * 255 / 100);
								color[3] = (100 * (pixels[y1][x1] >>> 16 & 0x1F) / 31 * 255 / 100);*/
								int c = pixels[y1][x1];
								color[0] = c >> 16;
								color[1] = c >> 8;
								color[2] = c >> 0;
								color[3] = c >> 24;
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
	
	public static void main(String[] args) throws IOException {

		WASFile file = new WASFile();
		if (file.open("F:\\t.was")) {
			
			SequenceFrame frame = file.getSequenceFrame();
			byte[] b = TGAUtils.toTGA(frame.pixels, frame.width * frame.frame, frame.height * frame.group);
			
			OutputStream out = new FileOutputStream("f:\\005944E2_1.tga");
			out.write(b);
			out.flush();
			out.close();
		}
		
	}
	
	
}

package com.devefx.gamedata.common;

public class TGAUtils {
	
	public static byte[] toTGA(int[][] pixels, int width, int height) {
		if (pixels != null) {
			byte[] buffer = new byte[18 + width * height * 4];
			System.out.println(buffer.length);
			byte[] header = {0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 8};
			for (int i = 0; i < header.length; i++) {
				buffer[i] = header[i];
			}
			buffer[12] = (byte)(width & 0xFF);
			buffer[13] = (byte)((width >> 8) & 0xFF);
			buffer[14] = (byte)(height & 0xFF);
			buffer[15] = (byte)((height >> 8) & 0xFF);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int color = pixels[y][x];
					int index = (y * width + x) * 4 + 18;
					
					/*buffer[index] = (byte)(100 * (color >>> 11 & 0x1F) / 31 * 255 / 100);
					buffer[index + 1] = (byte)(100 * (color >> 5 & 0x3F) / 63 * 255 / 100);
					buffer[index + 2] = (byte)(100 * (color & 0x1F) / 31 * 255 / 100);
					buffer[index + 3] = (byte)(100 * (color >>> 16 & 0x1F) / 31 * 255 / 100);
					*/
					buffer[index] = (byte)(color & 0xFF);
					buffer[index + 1] = (byte)((color >> 8) & 0xFF);
					buffer[index + 2] = (byte)((color >> 16) & 0xFF);
					buffer[index + 3] = (byte)((color >> 24) & 0xFF);
				}
			}
			return buffer;
		}
		return null;
	}
}

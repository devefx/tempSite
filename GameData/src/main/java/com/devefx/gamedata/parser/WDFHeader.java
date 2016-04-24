package com.devefx.gamedata.parser;

import java.io.DataInput;
import java.io.IOException;

public class WDFHeader {
	public int count;
	public int offset;
	
	public WDFHeader() {
	}
	public WDFHeader(DataInput input) {
		try {
			this.count = input.readInt();
			this.offset = input.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

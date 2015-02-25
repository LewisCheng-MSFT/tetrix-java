package org.tetrix.v2;

class Shape {
	private ShapeKind kind;
	private ShapeRotation rot;
	private int row;
	private int col;
	private int edgeLen;
	private int[][] map;
	
	public Shape() {
		kind = ShapeKind.I;
		rot = ShapeRotation.R0;
	}
	
	public void setKind(ShapeKind kind) {
		this.kind = kind;
		map = Maps.maps[kind.ordinal()][rot.ordinal()];
		edgeLen = map.length;
	}
	
	public ShapeKind getKind() {
		return kind;
	}
	
	public ShapeRotation getRotation() {
		return rot;
	}
	
	public void setRotation(ShapeRotation rot) {
		this.rot = rot;
		map = Maps.maps[kind.ordinal()][rot.ordinal()];
	}
	
	public void rotate() {
		int curr = rot.ordinal();
		curr = (curr + 1) % ShapeRotation.COUNT.ordinal();
		setRotation(ShapeRotation.values()[curr]);
	}
	
	public void setPosition(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public void moveLeft() {
		--this.col;
	}
	
	public void moveRight() {
		++this.col;
	}
	
	public void moveDown() {
		++this.row;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getRow(int i) {
		return i + row - 1;
	}
	
	public int getCol(int j) {
		return j + col - 1;
	}
	
	public int getEdgeLength() {
		return edgeLen;
	}
	
	public int[][] getMap() {
		return map;
	}
	
	public void copyFrom(Shape shape) {
		kind = shape.kind;
		rot = shape.rot;
		row = shape.row;
		col = shape.col;
		edgeLen = shape.edgeLen;
		map = shape.map;
	}
}

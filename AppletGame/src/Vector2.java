
public class Vector2 {
	
	public double x;
	public double y;
	
	public Vector2(double _x, double _y) {
		x = _x;
		y = _y;
	}
	public Vector2(double _value) {
		x = _value;
		y = _value;
	}
	
	public Vector2 add(Vector2 operand) {
		return new Vector2(x+operand.x,y+operand.y);
	}
	public Vector2 sub(Vector2 operand) {
		return new Vector2(x-operand.x,y-operand.y);
	}
	public Vector2 mult(double operand) {
		return new Vector2(x*operand,y*operand);
	}
	public Vector2 div(double operand) {
		return new Vector2(x/operand,y/operand);
	}

}

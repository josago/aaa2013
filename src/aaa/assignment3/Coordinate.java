package aaa.assignment3;

@SuppressWarnings("rawtypes")
public class Coordinate implements Comparable
{
	public final int x, y;
	
	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Object o)
	{
		Coordinate other = (Coordinate) o;
		
		if (this.x != other.x)
		{
			return Integer.compare(this.x, other.x);
		}
		else
		{
			return Integer.compare(this.y, other.y);
		}
	}
}

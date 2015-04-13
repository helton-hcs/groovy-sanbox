import groovy.transform.Immutable

@Immutable
class Point {
	double x
	double y
}

//Point p = new Point(x:3, y:4)
Point p = new Point(3, 4)
println p.dump()
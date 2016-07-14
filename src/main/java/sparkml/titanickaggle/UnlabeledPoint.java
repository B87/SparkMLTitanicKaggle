package sparkml.titanickaggle;

import java.io.Serializable;

import org.apache.spark.mllib.linalg.Vector;

public class UnlabeledPoint implements Serializable {
	private static final long serialVersionUID = 9124880550374563946L;
	
	private Vector vector;

	public UnlabeledPoint(Vector dense) {
		this.vector = dense;
	}

	public Vector getVector() {
		return vector;
	}

	public void setVector(Vector vector) {
		this.vector = vector;
	}

}

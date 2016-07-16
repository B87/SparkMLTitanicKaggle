package sparkml.titanickaggle;

import java.io.Serializable;

import org.apache.spark.mllib.linalg.Vector;

public class UnlabeledPoint implements Serializable {
	private static final long serialVersionUID = 9124880550374563946L;
	
	private Vector features;

	public UnlabeledPoint(Vector dense) {
		this.features = dense;
	}

	public Vector getVector() {
		return features;
	}

	public void setVector(Vector vector) {
		this.features = vector;
	}

}

package sparkml.titanickaggle;

import java.io.Serializable;

import org.apache.spark.mllib.linalg.Vectors;

public class UnlabeledPoint implements Serializable {
	private static final long serialVersionUID = 9124880550374563946L;
	
	private Vectors vector;

	public Vectors getVector() {
		return vector;
	}

	public void setVector(Vectors vector) {
		this.vector = vector;
	}

}

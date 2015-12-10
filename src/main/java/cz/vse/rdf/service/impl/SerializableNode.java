package cz.vse.rdf.service.impl;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SerializableNode implements Serializable {
	
	public final String value;
	public final String type;
	
	public SerializableNode(String value, String type) {
		super();
		this.value = value;
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		SerializableNode other = (SerializableNode) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}

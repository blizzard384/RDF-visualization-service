package cz.vse.rdf.service;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SerializableNode implements Serializable {
	
	public final String value;
	public final String type;
	public final ResourceType resourceType;
	private int connections;
	
	public SerializableNode(String value, String type, ResourceType resourceType) {
		super();
		this.value = value;
		this.type = type;
		this.resourceType = resourceType;
		this.connections = 1;
	}
	
	public SerializableNode(String value, String type, ResourceType resourceType, int connections) {
		super();
		this.value = value;
		this.type = type;
		this.resourceType = resourceType;
		this.connections = connections;
	}
	
	public void setConnections(int connections) {
		this.connections = connections;
	}
	
	public static enum ResourceType {
		LITERAL,
		RESOURCE;
	}
	
	public int getConnections() {
		return connections;
	}
	
	public void addConnection() {
		connections++;
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

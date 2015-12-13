package cz.vse.rdf.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
public class SerializableNode implements Serializable {
	
	private final String value;
	private String type;
	private Collection<Serializable> literals;
	private int connections;
	private final String label;
	
	public SerializableNode(String value, String type, int connections) {
		this(value, type, connections, new ArrayList<Serializable>());
		
	}
	
	public SerializableNode(String value, String type, int connections, Collection<Serializable> literals) {
		super();
		this.value = value;
		this.type = type;
		this.connections = connections;
		this.literals = literals;
		
		String[] resourceArray = value.split("/");
		String[] bookmarkArray = resourceArray[resourceArray.length-1].split("#");
		String label = bookmarkArray[bookmarkArray.length-1];
		this.label = value != null && !value.equals(label) ? "... " + label : label; 
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public void setLiterals(Collection<Serializable> literals) {
		this.literals = literals;
	}

	public void addLiteral(Serializable lit) {
		literals.add(lit);
	}
	
	public Collection<Serializable> getLiterals() {
		return new ArrayList<Serializable>(literals);
	}
	
	public void setConnections(int connections) {
		this.connections = connections;
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

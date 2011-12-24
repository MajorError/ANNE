package uk.ac.ic.doc.neuralnets.tests.persistence;

import uk.ac.ic.doc.neuralnets.graph.Saveable;

public class TestString implements Saveable{
	private static final long serialVersionUID = 6448778295106930377L;
	
	private int payload;
	
	public TestString(int number){
		this.payload = number;
	}
	
	public String toString(){
		return "" + payload;
	}
	
	public int getPayload(){
		return payload;
	}
	
}
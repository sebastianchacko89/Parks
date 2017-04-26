package com.codelab.parks.vo;

import java.util.List;

public class ParkServiceResponse{
	
	private List<Place> placesList;
	private boolean isError;
	private String errorMessage;
	
	public List<Place> getPlacesList() {
		return this.placesList;	
	}
	
	public void setPlacesList(List<Place> placesList) {
		this.placesList = placesList;	
	}
	
	public boolean getIsError() {
		return this.isError;	
	}
	
	public void setIsError(boolean isError) {
		this.isError = isError;	
	}
	
	public String getErrorMessage() {
		return this.errorMessage;	
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;	
	}
}
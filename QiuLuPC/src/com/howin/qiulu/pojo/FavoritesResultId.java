package com.howin.qiulu.pojo;

/**
 * FavoritesResultId entity. @author MyEclipse Persistence Tools
 */

public class FavoritesResultId implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer skuId;
	private Integer userId;
	private String image;
	private String sellPoint;
	private Double price;
	private Integer repertory;
	// Constructors

	/** default constructor */
	public FavoritesResultId() {
	}

	/** minimal constructor */
	public FavoritesResultId(Integer id, Integer skuId, Integer userId) {
		this.id = id;
		this.skuId = skuId;
		this.userId = userId;
	}

	/** full constructor */
	public FavoritesResultId(Integer id, Integer skuId, Integer userId,
			String image, String sellPoint, Double price,Integer repertory) {
		this.id = id;
		this.skuId = skuId;
		this.userId = userId;
		this.image = image;
		this.sellPoint = sellPoint;
		this.price = price;
		this.repertory=repertory;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSkuId() {
		return this.skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSellPoint() {
		return this.sellPoint;
	}

	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	
	public Integer getRepertory() {
		return repertory;
	}

	public void setRepertory(Integer repertory) {
		this.repertory = repertory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((repertory == null) ? 0 : repertory.hashCode());
		result = prime * result + ((sellPoint == null) ? 0 : sellPoint.hashCode());
		result = prime * result + ((skuId == null) ? 0 : skuId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		FavoritesResultId other = (FavoritesResultId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (repertory == null) {
			if (other.repertory != null)
				return false;
		} else if (!repertory.equals(other.repertory))
			return false;
		if (sellPoint == null) {
			if (other.sellPoint != null)
				return false;
		} else if (!sellPoint.equals(other.sellPoint))
			return false;
		if (skuId == null) {
			if (other.skuId != null)
				return false;
		} else if (!skuId.equals(other.skuId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	

}
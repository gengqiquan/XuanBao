package com.aibaide.xuanbao.bean;

import java.io.Serializable;

public class AddressBean  implements Serializable{
	private Long id;

	private String receiver;

	private String postAddress;

	private Long defaultAddress;

	private Long memberId;

	private Long receiverPhone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getPostAddress() {
		return postAddress;
	}

	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	public Long getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(Long defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(Long receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
}

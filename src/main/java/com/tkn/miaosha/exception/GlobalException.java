package com.tkn.miaosha.exception;

import com.tkn.miaosha.result.CodeMsg;

/**
 *  自定义全局异常类
 */
public class GlobalException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private CodeMsg cm;
	
	public GlobalException(CodeMsg cm) {
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMsg getCm() {
		return cm;
	}

}

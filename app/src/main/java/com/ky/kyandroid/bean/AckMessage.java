package com.ky.kyandroid.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 类名称：请求响应message<br/>
 * 类描述：<br/>
 *
 * 创建人： Cz <br/>
 * 创建时间：2016年9月20日 下午4:54:23 <br/>
 * @updateRemark 修改备注：
 *
 */
public class AckMessage implements Serializable{

		/** String */
		public static final String TAG = "ACK_MESSAGE";

		/** long */
		private static final long serialVersionUID = 1L;
		/** 成功标识 */
		public final static String  SUCCESS = "SUCCESS";
		/** 失败标识 */
		public final static String  FAILURE = "FAILURE";
		/** 参数实体 */
		private Object entity;
		/** 响应代码  ACK_SUCCESS or ACK_FAILURE */
		private String ackCode;
		/** 错误代码 枚举AckEnum */
		private String errorCode;
		/** 错误内容 枚举AckEnum */
		private String errorMsg;
		/** 令牌 */
		private String accessToken;
		/** 令牌有效时间（秒数） */
		private String expiresIn;
		/** List<Object>对象列表 */
		private List<?> data;
		/** 分页对象 */
		private PageBean pageBean;

		public void setErrorMessage(AckEnum ack){
			this.ackCode = AckMessage.FAILURE;
			this.errorCode = ack.name();
			this.errorMsg = ack.getMsg();
		}

		public void setSuccessMessage(List<?> data){
			this.ackCode = AckMessage.SUCCESS;
			this.data = data;
		}

		public void setSuccessMessage(Object entity){
			this.ackCode = AckMessage.SUCCESS;
			this.entity = entity;
		}

		public void setSuccessMessage(List<?> data,PageBean pageBean){
			this.ackCode = AckMessage.SUCCESS;
			this.data = data;
			this.pageBean = pageBean;
		}

		public void setSuccessMessage(Object entity,PageBean pageBean){
			this.ackCode = AckMessage.SUCCESS;
			this.entity = entity;
			this.pageBean = pageBean;
		}

		public void setSuccessMessage(List<?> data,Object entity,PageBean pageBean){
			this.ackCode = AckMessage.SUCCESS;
			this.data = data;
			this.entity = entity;
			this.pageBean = pageBean;
		}

		public String getAckCode() {
			return ackCode;
		}
		public void setAckCode(String ackCode) {
			this.ackCode = ackCode;
		}
		public String getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		public String getErrorMsg() {
			return errorMsg;
		}
		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public String getExpiresIn() {
			return expiresIn;
		}
		public void setExpiresIn(String expiresIn) {
			this.expiresIn = expiresIn;
		}
		public List<?> getData() {
			return data;
		}
		public void setData(List<?> data) {
			this.data = data;
		}
		public PageBean getPageBean() {
			return pageBean;
		}
		public void setPageBean(PageBean pageBean) {
			this.pageBean = pageBean;
		}

		public Object getEntity() {
			return entity;
		}

		public void setEntity(Object entity) {
			this.entity = entity;
		}

}

package com.kongfu.backend.entity;
import com.kongfu.backend.util.BlogConstant;
import java.io.Serializable;

/**
 * 封装返回信息
 * @param <T>
 */
public class ObjectRespBean<T> implements Serializable {

        private static final long serialVersionUID = 1L;
        private T data;
        private Integer retCode;
        private String retMsg;

        private ObjectRespBean(Integer retCode, String retMsg) {
            this.retCode = retCode;
            this.retMsg = retMsg;
        }

        private ObjectRespBean(T data) {
            this.data = data;
            this.retCode = BlogConstant.RETURN_SUCCESS;
            this.retMsg = BlogConstant.RETURN_SUCCESS_DESC;
        }


        private ObjectRespBean(T data,Integer retCode,String retMsg) {
            this.data = data;
            this.retCode = retCode;
            this.retMsg = retMsg;
        }

        public ObjectRespBean() {
            this.data = null;
            this.retCode = BlogConstant.RETURN_SUCCESS;
            this.retMsg = BlogConstant.RETURN_SUCCESS_DESC;
        }

        public static <T> ObjectRespBean<T> returnSuccess() {
            return new ObjectRespBean<>();
        }

        public static <T> ObjectRespBean<T> returnSuccess(T data) {
            return new ObjectRespBean<>(data);
        }

        public static <T> ObjectRespBean<T> returnSuccess(T data,Integer retCode,String retMsg) {
            return new ObjectRespBean<>(data,retCode,retMsg);
        }

        public static <T> ObjectRespBean<T> returnFail(Integer retCode, String retMsg) {
            return new ObjectRespBean<>(retCode, retMsg);
        }

        public static <T> ObjectRespBean<T> returnFail(String retMsg) {
            return new ObjectRespBean<>(BlogConstant.RETURN_FAILURE, retMsg);
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Integer getRetCode() {
            return retCode;
        }

        public void setRetCode(Integer retCode) {
            this.retCode = retCode;
        }

        public String getRetMsg() {
            return retMsg;
        }

        public void setRetMsg(String retMsg) {
            this.retMsg = retMsg;
        }
}

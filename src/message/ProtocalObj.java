
//用于把java类转换为xml文件和json串，各个信息存储类的父类，是程序信息传输的核心

package message;


import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

public class ProtocalObj {
	public String toXml() {
		XStream x = new XStream();
		x.alias(this.getClass().getSimpleName(), this.getClass());
		return x.toXML(this);
	}
	public Object fromXml(String xml) {
		XStream x = new XStream();
		x.alias(this.getClass().getSimpleName(), this.getClass());
		return x.fromXML(xml);
	}
	public String toJson() {
		Gson g = new Gson();
		return g.toJson(this);
	}
	public Object fromJson(String json) {
		Gson g = new Gson();
		return g.fromJson(json, this.getClass());
	}
}

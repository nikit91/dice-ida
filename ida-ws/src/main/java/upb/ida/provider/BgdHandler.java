package upb.ida.provider;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rivescript.macro.Subroutine;

import upb.ida.bean.ResponseBean;
import upb.ida.constant.IDALiteral;
import upb.ida.temp.DemoMain;
@Component
public class BgdHandler implements Subroutine {
	@Autowired
	DemoMain DemoMain;
	@Autowired
	ResponseBean responseBean;
	public String call (com.rivescript.RiveScript rs, String[] args) {
		
		//		String user = rs.currentUser();
		try {
			Map<String, Object> dataMap = responseBean.getPayload();
			dataMap.put("label", "Bar Graph");
//			dataMap.put("dsName", message);
			dataMap.put("dataset", DemoMain.getJsonData("city\\citydistance.csv",args[0].toLowerCase(),args[1].toLowerCase()));
			responseBean.setPayload(dataMap);
			responseBean.setActnCode(IDALiteral.UIA_BG);
			return "pass";
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		return "fail";
	
	}
}
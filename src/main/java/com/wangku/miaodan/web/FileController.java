package com.wangku.miaodan.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileController {
	
	@RequestMapping("/upload")
	@ResponseBody
	public Map<String, Object> upload(MultipartFile file, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("user-info");
		Map<String, Object> result = new HashMap<String, Object>();
		if (file != null) {
			String[] split = file.getOriginalFilename().split("\\.");
			String name = split[split.length - 1];
			name = new StringBuffer(40).append(UUID.randomUUID().toString()).append(".").append(name).toString();
			File temp = new File(path + File.separator + name);
			try {
				temp.mkdirs();
				file.transferTo(temp);
				result.put("code", 200);
				result.put("url", "/user-info/" + name);
			} catch (IOException e) {
				e.printStackTrace();
				result.put("code", 500);
			}
		}
		return result;
	}

}

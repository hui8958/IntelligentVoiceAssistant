package edu.hui.vassistant.supports;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;

public class Translator {
	String text;
	String result;
	String languagecode;
	
	public Translator(String text,  String languagecode) {
		super();
		this.text = text;
		
		this.languagecode = languagecode;
	}



	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getLanguagecode() {
		return languagecode;
	}

	public void setLanguagecode(String languagecode) {
		this.languagecode = languagecode;
	}
	public String getTranslateResult() {
		String result = "null";
			// 先转成url可以用的编码
			String content = "";
		try {
			content = URLEncoder.encode(text, "UTF-8");

			String appId = "78280AF4DFA1CE1676AFE86340C690023A5AC139";
			String url = "http://api.microsofttranslator.com/V2/Ajax.svc/Translate?appId="
					+ appId + "&from=en&to=" + languagecode + "&text=" + content;
			URL u;

			u = new URL(url);

			Reader reader = null;
			reader = new InputStreamReader(new BufferedInputStream(
					u.openStream()));
			int c;
			StringBuilder sb = new StringBuilder();

			while ((c = reader.read()) != -1) {
				sb.append((char) c);
			}

			reader.close();
			result = sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
}

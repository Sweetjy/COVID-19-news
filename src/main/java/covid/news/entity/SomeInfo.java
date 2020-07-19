package covid.news.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class SomeInfo {
	@Value("${webname}")
	public String source;
}

package cn.qp.sso.server;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 自己实现的简单的字符串模板渲染器。
 *  因为 SpelView 类是包私有的，无法调用，所以复制一份
 * @author Baozi
 *
 */
public class SsoSpelView implements View {
	
private final String template;
	
	private final String prefix;

	private final SpelExpressionParser parser = new SpelExpressionParser();

	private final StandardEvaluationContext context = new StandardEvaluationContext();

	private PlaceholderResolver resolver;

	SsoSpelView(String template) {
		this.template = template;
		this.prefix = new RandomValueStringGenerator().generate() + "{";
		this.context.addPropertyAccessor(new MapAccessor());
		this.resolver = (String name) -> {
            Expression expression = parser.parseExpression(name);
            Object value = expression.getValue(context);
            return value == null ? null : value.toString();
        };
	}

	@Override
    public String getContentType() {
		return "text/html";
	}

	@Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<>(model);
		String path = ServletUriComponentsBuilder.fromContextPath(request).build()
				.getPath();
		map.put("path", path ==null ? "" : path);
		context.setRootObject(map);
		String maskedTemplate = template.replace("${", prefix);
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(prefix, "}");
		String result = helper.replacePlaceholders(maskedTemplate, resolver);
		result = result.replace(prefix, "${");
		response.setContentType(getContentType());
		response.getWriter().append(result);
	}

}

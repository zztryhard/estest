package es.test.base;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

/**
 * Created by mty02 on 2017/3/14.
 */
public abstract class AbstractCustomController {

    protected static final String JSON = "application/json;charset=UTF-8";
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected Validator validator;
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    protected AjaxResultVo result = new AjaxResultVo();

    public AbstractCustomController() {
    }

    protected void validBindingResult(final BindingResult result) {
        if (result.hasErrors()) {
            final List<String> msg = Lists.newArrayList();
            for (final ObjectError error : result.getAllErrors()) {
                msg.add(error.getDefaultMessage());
            }
            throw new FieldErrorException(msg.isEmpty() ? "" : StringUtils.join(msg, ","));
        }
    }

    protected boolean beanValidator(final Model model, final Object object, final Class... groups) {
        try {
            BeanValidators.validateWithException(this.validator, object, groups);
            return true;
        } catch (final ConstraintViolationException var6) {
            final List<String> list = BeanValidators.extractPropertyAndMessageAsList(var6, ": ");
            list.add(0, "数据验证失败：");
            this.addMessage(model, (String[]) list.toArray(new String[0]));
            return false;
        }
    }

    protected boolean beanValidator(final RedirectAttributes redirectAttributes, final Object object,
        final Class... groups) {
        try {
            BeanValidators.validateWithException(this.validator, object, groups);
            return true;
        } catch (final ConstraintViolationException var6) {
            final List<String> list = BeanValidators.extractPropertyAndMessageAsList(var6, ": ");
            list.add(0, "数据验证失败：");
            this.addMessage(redirectAttributes, (String[]) list.toArray(new String[0]));
            return false;
        }
    }

    protected void beanValidator(final Object object, final Class... groups) {
        BeanValidators.validateWithException(this.validator, object, groups);
    }

    protected void addMessage(final Model model, final String... messages) {
        final StringBuilder sb = new StringBuilder();
        final String[] var7 = messages;
        final int var6 = messages.length;

        for (int var5 = 0; var5 < var6; ++var5) {
            final String message = var7[var5];
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }

        model.addAttribute("message", sb.toString());
    }

    protected void addMessage(final RedirectAttributes redirectAttributes, final String... messages) {
        final StringBuilder sb = new StringBuilder();
        final String[] var7 = messages;
        final int var6 = messages.length;

        for (int var5 = 0; var5 < var6; ++var5) {
            final String message = var7[var5];
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }

        redirectAttributes.addFlashAttribute("message", sb.toString());
    }

    protected void sendSuccessMessage(final HttpServletResponse response, final String message) {
        this.sendMessage(response, Boolean.valueOf(true), (String) null, message);
    }

    protected void sendSuccessMessage(final HttpServletResponse response, final String json, final String message) {
        this.sendMessage(response, Boolean.valueOf(true), json, message);
    }

    protected void sendMessage(final HttpServletResponse response, final Boolean isSuccess, final String message) {
        this.sendMessage(response, isSuccess, (String) null, message);
    }

    protected void sendMessage(final HttpServletResponse response, final Boolean isSuccess, final String json,
        final String message) {
        final HashMap<String, java.io.Serializable> responseMap = new HashMap<String, Serializable>();
        responseMap.put("success", isSuccess);
        responseMap.put("data", json);
        responseMap.put("message", message);
        this.renderString(response, (new Gson()).toJson(responseMap), "application/json");
    }

    protected void renderString(final HttpServletResponse response, final Object object) {
        this.renderString(response, (new Gson()).toJson(object), "application/json");
    }

    protected void renderString(final HttpServletResponse response, final String string) {
        this.renderString(response, string, "application/json");
    }

    protected void renderString(final HttpServletResponse response, final String string, final String type) {
        try {
            response.reset();
            response.setContentType(type);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (final IOException var5) {
            new IOException("send data exception!");
        }

    }

    protected void renderString(final String string) {
        this.renderString(this.response, string);
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                final Object value = this.getValue();
                return value != null ? value.toString() : null;
            }

            @Override
            public void setAsText(final String text) {
                this.setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }
        });
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(final String text) {
                if (StringUtils.isNumeric(text)) {
                    this.setValue(new Date(Long.parseLong(text)));
                    return;
                }
                this.setValue(DateUtils.stringToDate(text, "yyyy-MM-dd HH:mm:ss"));
            }
        });
    }
}

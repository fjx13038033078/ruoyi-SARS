package com.ruoyi.agricultural.aspect;

import com.ruoyi.agricultural.service.IAgriErrorLogService;
import com.ruoyi.common.utils.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * AOP Aspect for agricultural controllers.
 * Intercepts exceptions from agricultural controllers and records them into agri_error_log.
 */
@Aspect
@Component
public class AgriErrorLogAspect {

    @Autowired
    private IAgriErrorLogService errorLogService;

    @Around("execution(* com.ruoyi.web.controller.agricultural..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Exception e) {
            String errorType = classifyError(e);
            String errorMsg = buildFriendlyMsg(e);
            String stackTrace = getStackTrace(e);
            String requestUrl = "";
            String requestParam = Arrays.toString(pjp.getArgs());

            try {
                ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attrs != null) {
                    HttpServletRequest req = attrs.getRequest();
                    requestUrl = req.getRequestURI();
                }
            } catch (Exception ignored) {}

            Long userId = null;
            String userName = "unknown";
            try {
                userId = SecurityUtils.getUserId();
                userName = SecurityUtils.getUsername();
            } catch (Exception ignored) {}

            errorLogService.recordError(errorType, errorMsg, stackTrace, requestUrl, requestParam, userId, userName);
            throw e;
        }
    }

    private String classifyError(Exception e) {
        String msg = e.getMessage() == null ? "" : e.getMessage();
        if (msg.contains("format") || msg.contains("parse") || msg.contains("JSON")) {
            return "DATA_FORMAT";
        }
        if (msg.contains("algorithm") || msg.contains("optimize") || msg.contains("cluster")) {
            return "ALGORITHM_FAIL";
        }
        if (msg.contains("upload") || msg.contains("MultipartFile") || msg.contains("file")) {
            return "UPLOAD_FAIL";
        }
        return "OTHER";
    }

    private String buildFriendlyMsg(Exception e) {
        String msg = e.getMessage();
        if (msg == null) return "Unknown system error, please contact administrator";
        if (msg.contains("format") || msg.contains("parse")) return "Data format error: " + msg;
        if (msg.contains("CSV") || msg.contains("Excel")) return "File import error, please check CSV/Excel format";
        return msg.length() > 200 ? msg.substring(0, 200) : msg;
    }

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}

package com.demo.memshell.whole.unload;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Whoopsunix
 *
 * 卸载 Thread 获取上下文注入 Tomcat Listener 型内存马
 * Tomcat 7 8 9
 */
public class UnloadTomcatListenerThreadMS {
    private static String CLASSNAME = "TomcatListenerJMXMS";
    private static HttpServletRequest request;

    static {
        try {
            /**
             * 获取 request、response 对象
             */
            Thread[] threads = (Thread[]) getFieldValue(Thread.currentThread().getThreadGroup(), "threads");
            boolean isFind = false;
            for (int i = 0; i < threads.length; i++) {
                if (isFind) break;
                Thread thread = threads[i];
                // Thread 筛选
                if (thread == null)
                    continue;
                String threadName = thread.getName();
                if (!(
                        ((threadName.contains("http-nio") || threadName.contains("http-apr")) && threadName.contains("Poller"))
                                || (threadName.contains("http-bio") && threadName.contains("AsyncTimeout"))
                                || (threadName.contains("http-") && threadName.contains("Acceptor"))
                ))
                    continue;
                Object target = getFieldValue(thread, "target");
                Object this0 = getFieldValue(target, "this$0");
                Object handler = getFieldValue(this0, "handler");
                Object global = getFieldValue(handler, "global");
                java.util.List processors = (java.util.List) getFieldValue(global, "processors");

                for (int j = 0; j < processors.size(); j++) {
                    Object processor = processors.get(j);
                    Object req = getFieldValue(processor, "req");
                    request = (HttpServletRequest) req.getClass().getDeclaredMethod("getNote", Integer.TYPE).invoke(req, new Integer(1));
                    isFind = true;
                    break;
                }
            }

            Object standardContext;
            try {
                Object servletContext = request.getClass().getDeclaredMethod("getServletContext").invoke(request);
                Object applicationContext = getFieldValue(servletContext, "context");
                standardContext = getFieldValue(applicationContext, "context");
            } catch (NoSuchMethodException e) {
                standardContext = getFieldValue(request, "context");
            }

            /**
             * 卸载 Listener
             */
            List<Object> applicationEventListeners = (List<Object>) getFieldValue(standardContext, "applicationEventListenersList");
            for (Object applicationEventListener : applicationEventListeners) {
                if (applicationEventListener.getClass().getName().contains(CLASSNAME)) {
                    applicationEventListeners.remove(applicationEventListener);
                    break;
                }
            }

        } catch (Exception e) {
        }
    }

    public static Object getFieldValue(final Object obj, final String fieldName) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        return field.get(obj);
    }

    public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null)
                field = getField(clazz.getSuperclass(), fieldName);
        }
        return field;
    }
}
